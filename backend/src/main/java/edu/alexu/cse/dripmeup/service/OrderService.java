package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.dto.*;
import edu.alexu.cse.dripmeup.entity.Item;
import edu.alexu.cse.dripmeup.entity.Order;
import edu.alexu.cse.dripmeup.enumeration.Status;
import edu.alexu.cse.dripmeup.exception.AuthorizationException;
import edu.alexu.cse.dripmeup.exception.BadInputException;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import edu.alexu.cse.dripmeup.repository.OrderRepository;
import edu.alexu.cse.dripmeup.service.notifications.OrderManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.alexu.cse.dripmeup.specification.OrderSpecification.status;
import static edu.alexu.cse.dripmeup.specification.OrderSpecification.user;

@Service
public class OrderService {
    @Autowired
    private OrderManagement orderManagement;
    @Autowired
    private OrderRepository orderRepository;
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
        for(Item item : order.getItemList()){
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemTotalPrice(item.getProductVariantPrice() * item.getProductVariantQuantity());
            itemDTO.setProductName(item.getProductName());
            itemDTO.setProductVariantColor(item.getProductVariantColor());
            itemDTO.setProductVariantSize(item.getProductVariantSize());
            itemDTO.setProductVariantPrice(item.getProductVariantPrice());
            itemDTO.setProductVariantQuantity(item.getProductVariantQuantity());
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
    public OrdersListDTO getOrders(Long userId, Integer pageNumber, Integer pageSize, Status status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timeStamp");
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNumber).orElse(0),
                Optional.ofNullable(pageSize).orElse(10), sort);
        Specification<Order> filters = Specification.where(status==null ? null : status(status))
                .and(userId==null ? null :user(userId));
        return mapToOrdersListDTO(orderRepository.findAll(filters, pageable));

    }
    public OrdersListDTO getOrders(Integer pageNumber, Integer pageSize, Status status) {
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


    public String approveOrder(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("Order does not exist");
        if(order.getStatus() != Status.PENDING) throw new BadInputException("Order is not Pending");
        order.setStatus(Status.APPROVED);
        try{
            orderManagement.setEmail(order.getUserEntity().getEmail());
            orderManagement.setUsername(order.getUserEntity().getUserName());
            orderManagement.setOrderId(order.getId().intValue());
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
        if(order.getStatus() != Status.APPROVED) throw new BadInputException("Order is not approved");
        order.setStatus(Status.DELIVERY);
        try{
            orderManagement.setEmail(order.getUserEntity().getEmail());
            orderManagement.setUsername(order.getUserEntity().getUserName());
            orderManagement.setOrderId(order.getId().intValue());
            orderManagement.setOrderDTO(mapToOrderDTO(order));
            orderManagement.ShipOrder();
        }
        catch (Exception e){
            throw new FailedToSendMailException("Failed to send email, email might not be valid");
        }
        orderRepository.save(order);
        return "Order in delivery";
    }
    public String confirmOrder(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new BadInputException("Order does not exist");
        if(order.getStatus() != Status.DELIVERY) throw new BadInputException("Order is not in delivery");
        order.setStatus(Status.CONFIRMED);
        try{
            orderManagement.setEmail(order.getUserEntity().getEmail());
            orderManagement.setUsername(order.getUserEntity().getUserName());
            orderManagement.setOrderId(order.getId().intValue());
            orderManagement.setOrderDTO(mapToOrderDTO(order));
            orderManagement.ReceiveOrder();
        }
        catch (Exception e){
            throw new FailedToSendMailException("Failed to send email, email might not be valid");
        }
        orderRepository.save(order);
        return "Order is received";
    }
}
