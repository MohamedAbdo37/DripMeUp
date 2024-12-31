package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.controller.OrderRequestBody;
import edu.alexu.cse.dripmeup.dto.*;
import edu.alexu.cse.dripmeup.entity.*;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.enumeration.PaymentMethod;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import edu.alexu.cse.dripmeup.enumeration.orderStatus;
import edu.alexu.cse.dripmeup.exception.AuthorizationException;
import edu.alexu.cse.dripmeup.exception.BadInputException;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import edu.alexu.cse.dripmeup.repository.*;
import edu.alexu.cse.dripmeup.service.notifications.OrderManagement;
//import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;
import static edu.alexu.cse.dripmeup.specification.OrderSpecification.status;
import static edu.alexu.cse.dripmeup.specification.OrderSpecification.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderManagement orderManagement;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private CartRepository cartRepository ;
    @Autowired
    private VariantRepository variantRepository ;

    @Autowired
    private ItemRepository itemRepository ;

    @Autowired
    private ProductRepository productRepository ;

    @Autowired
    private ItemImageRepository itemImageRepository ;

    public String convertCartToOrder(long userId , OrderRequestBody orderBody , LinkedList<Long> outOfStock)
            throws BadInputException {

        // validate user
        UserEntity user = userRepository.findByUserID(userId) ;
        if(user == null)
            throw new BadInputException("User does not exist");


        // validate cart
        List<CartEntity> cart = cartRepository.findAllByUser(user) ;
        if(cart.isEmpty())
            throw new BadInputException("Cart is empty");


        Order order = new Order() ;
        this.setOrderInformation(order , orderBody);

        for(CartEntity cartElement : cart){
            VariantEntity variant = cartElement.getVariant() ;
            // validate variant
            if(variantRepository.findByVariantID(variant.getVariantID())==null)
                throw new BadInputException("Variant does not exist");

            // validate product
            if(productRepository.findByProductID(variant.getProduct().getProductID()) == null)
                throw new BadInputException("Product does not exist");

            // validate amount
            if(cartElement.getAmount() > variant.getStock())
                outOfStock.add(variant.getVariantID());
        }

        if(!outOfStock.isEmpty())
            return "Stock is not enough";

        order.setStatus(orderStatus.PENDING);
        order.setUserEntity(user);
        this.setOrderItems(cart , order);

        // saving order
        orderRepository.save(order) ;
        // clear cart
        cartRepository.deleteAllByUser(user) ;

        return "Order was added successfully" ;
    }

    private void setOrderItems(List<CartEntity> cart , Order order ){

        double totalPrice = 0.0 ;

        List<OrderItem> orderItems = new LinkedList<>() ;

        for(CartEntity cartElement : cart){
            VariantEntity variant = cartElement.getVariant() ;
            // copying item
            OrderItem item = new OrderItem() ;
            this.createItem(variant , cartElement.getAmount() , item , order );
            orderItems.add(item) ;

            // decreasing stock
            variant.setStock(variant.getStock()-cartElement.getAmount());
            // increasing sold
            variant.setSold(variant.getSold() + cartElement.getAmount());
            // changing product state
            if(variant.getStock()==0)
                variant.setState(ProductState.OUT_OF_STOCK);

            // saving variant
            variantRepository.save(variant) ;

            // update total price
            totalPrice+= variant.getPrice()*cartElement.getAmount() ;
        }

        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);
    }

    public void createItem(VariantEntity variant , int amount , OrderItem item , Order order){

        item.setVariantId(variant.getVariantID()) ;
        item.setProductId(variant.getProduct().getProductID()) ;
        item.setProductVariantSize(variant.getSize()) ;
        item.setProductVariantColor(variant.getColor()) ;
        item.setProductVariantWeight(variant.getWeight()) ;
        item.setProductVariantLength(variant.getLength()) ;
        item.setProductVariantPrice(variant.getPrice()) ;
        item.setProductDescription(variant.getProduct().getDescription()) ;
        item.setProductVariantQuantity(amount) ;
        itemRepository.save(item) ;

        for(VariantImageEntity image : variant.getVariantImages()) {
            ItemImage itemImage = new ItemImage() ;
            itemImage.setItem(item);
            itemImage.setImagePath(image.getImagePath());
            itemImageRepository.save(itemImage) ;
        }

    }

    private void setOrderInformation(Order order , OrderRequestBody orderBody) throws BadInputException {

        if(this.notValid(orderBody.getAddress()))
            throw new BadInputException("Variant does not exist");
        order.setAddress(orderBody.getAddress());

        if(orderBody.getPaymentMethod() !=  PaymentMethod.CASH && orderBody.getPaymentMethod() !=  PaymentMethod.VISA)
            throw new BadInputException("Variant does not exist");
        order.setPaymentMethod(orderBody.getPaymentMethod());

        if(orderBody.getPaymentMethod() ==  PaymentMethod.CASH){
            order.setCardNumber(null);
            order.setCardHolder(null);
            order.setExpirationDate(null);
            order.setCVV(null);
        }

        else {
            if(this.notValid(orderBody.getCardNumber()) | this.notValid(orderBody.getCardHolder()) |
                    this.notValid(orderBody.getExpirationDate()) | this.notValid(orderBody.getCvv()))
                throw new BadInputException("Variant does not exist");

            order.setCardNumber(orderBody.getCardNumber());
            order.setCardHolder(orderBody.getCardHolder());
            order.setExpirationDate(orderBody.getExpirationDate());
            order.setCVV(orderBody.getCvv());
        }
    }

    private boolean notValid(String x){
        return x == null || x.equals("") ;
    }


    public String approveOrder(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("Order does not exist");
        if(order.getStatus() != orderStatus.PENDING) throw new BadInputException("Order is not Pending");
        order.setStatus(orderStatus.APPROVED);
        order.onUpdate();
        try{
            orderManagement.setEmail(order.getUserEntity().getEmail());
            orderManagement.setUsername(order.getUserEntity().getUserName());
            orderManagement.setOrderId(order.getId());
            orderManagement.setOrderDTO(mapToOrderDTO(order));
            orderManagement.ConfirmOrder();
        }
        catch (Exception e){
            throw new FailedToSendMailException("Failed to send email, email might not be valid");
        }
        orderRepository.save(order);
        return "Order Approved";
    }
    public String deliverOrder(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("Order does not exist");
        if(order.getStatus() != orderStatus.APPROVED) throw new BadInputException("Order is not approved");
        order.setStatus(orderStatus.DELIVERY);
        order.onUpdate();
//        try{
//            orderManagement.setEmail(order.getUserEntity().getEmail());
//            orderManagement.setUsername(order.getUserEntity().getUserName());
//            orderManagement.setOrderId(order.getId().intValue());
//            orderManagement.setOrderDTO(mapToOrderDTO(order));
//            orderManagement.ShipOrder();
//        }
//        catch (Exception e){
//            throw new FailedToSendMailException("Failed to send email, email might not be valid");
//        }
        orderRepository.save(order);
        return "Order in delivery";
    }
    public String confirmOrder(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("Order does not exist");
        if(order.getStatus() != orderStatus.DELIVERY) throw new BadInputException("Order is not in delivery");
        order.setStatus(orderStatus.CONFIRMED);
        order.onUpdate();
//        try{
//            orderManagement.setEmail(order.getUserEntity().getEmail());
//            orderManagement.setUsername(order.getUserEntity().getUserName());
//            orderManagement.setOrderId(order.getId().intValue());
//            orderManagement.setOrderDTO(mapToOrderDTO(order));
//            orderManagement.ReceiveOrder();
//        }
//        catch (Exception e){
//            throw new FailedToSendMailException("Failed to send email, email might not be valid");
//        }
        orderRepository.save(order);
        return "Order is received";
    }

    private static OrdersListDTO mapToOrdersListDTO(Page<Order> orders) {
        OrdersListDTO ordersListDTO = new OrdersListDTO();
        List<OrderMetaDTO> ordersList = new ArrayList<OrderMetaDTO>();
        PageMetaDTO pageMetaDTO = new PageMetaDTO();
        pageMetaDTO.setPageNumber(orders.getNumber());
        pageMetaDTO.setPageSize(orders.getSize());
        pageMetaDTO.setTotalPages(orders.getTotalPages());
        for (Order order : orders.getContent()) {
            OrderMetaDTO orderMetaDTO = new OrderMetaDTO();
            orderMetaDTO.setId(order.getId());
            orderMetaDTO.setAddress(order.getAddress());
            orderMetaDTO.setCustomerName(order.getUserEntity().getUserName());
            orderMetaDTO.setStatus(order.getStatus());
            orderMetaDTO.setTimeStamp(order.getTimeStamp());
            orderMetaDTO.setTotalPrice(order.getTotalPrice());
            ordersList.add(orderMetaDTO);
        }
        ordersListDTO.setMeta(pageMetaDTO);
        ordersListDTO.setData(ordersList);
        return ordersListDTO;
    }
    private static OrderDTO mapToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        OrderMetaDTO orderMetaDTO = new OrderMetaDTO();
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for(OrderItem orderItem : order.getOrderItemList()){
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemTotalPrice(orderItem.getProductVariantPrice() * orderItem.getProductVariantQuantity());
            itemDTO.setProductName(orderItem.getProductDescription());
            itemDTO.setProductVariantColor(orderItem.getProductVariantColor());
            itemDTO.setProductVariantSize(orderItem.getProductVariantSize());
            itemDTO.setProductVariantPrice(orderItem.getProductVariantPrice());
            itemDTO.setProductVariantQuantity(orderItem.getProductVariantQuantity());
            itemDTOS.add(itemDTO);
        }
        orderMetaDTO.setId(order.getId());
        orderMetaDTO.setAddress(order.getAddress());
        orderMetaDTO.setStatus(order.getStatus());
        orderMetaDTO.setTotalPrice(order.getTotalPrice());
        orderMetaDTO.setTimeStamp(order.getTimeStamp());
        orderMetaDTO.setCustomerName(order.getUserEntity().getUserName());

        orderDTO.setMeta(orderMetaDTO);
        orderDTO.setItems(itemDTOS);
        return orderDTO;
    }
    public OrdersListDTO getOrders(Long userId, Integer pageNumber, Integer pageSize, orderStatus status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timeStamp");
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNumber).orElse(0),
                Optional.ofNullable(pageSize).orElse(10), sort);
        Specification<Order> filters = Specification.where(status==null ? null : status(status))
                .and(userId==null ? null :user(userId));
        return mapToOrdersListDTO(orderRepository.findAll(filters, pageable));

    }
    public OrdersListDTO getOrders(Integer pageNumber, Integer pageSize, orderStatus status) {
        Sort sort = Sort.by(Sort.Direction.ASC, "timeStamp");
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNumber).orElse(0),
                Optional.ofNullable(pageSize).orElse(10), sort);
        Specification<Order> filters = Specification.where(status==null ? null : status(status));
        return mapToOrdersListDTO(orderRepository.findAll(filters, pageable));

    }

    public OrderDTO getOrderDetails(Long userId, Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("");
        if(order.getUserEntity().getUserID().equals(userId)) return mapToOrderDTO(order);
        throw new AuthorizationException("Unauthorized to Access this Resource");
    }
    public OrderDTO getOrderDetails(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("");
        return mapToOrderDTO(order);
    }


}
