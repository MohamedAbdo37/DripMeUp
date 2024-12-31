package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.dto.OrderDTO;
import edu.alexu.cse.dripmeup.dto.OrdersListDTO;
import edu.alexu.cse.dripmeup.entity.Order;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.enumeration.orderStatus;
import edu.alexu.cse.dripmeup.exception.AuthorizationException;
import edu.alexu.cse.dripmeup.exception.BadInputException;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import edu.alexu.cse.dripmeup.repository.OrderRepository;
import edu.alexu.cse.dripmeup.service.OrderService;
import edu.alexu.cse.dripmeup.service.notifications.OrderManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderManagement orderManagement;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConfirmOrder_Success() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.DELIVERY);
        order.setUserEntity(new UserEntity());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = orderService.confirmOrder(1L);

        assertEquals("Order is received", result);
        assertEquals(orderStatus.CONFIRMED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(orderManagement, times(1)).ReceiveOrder();
    }

    @Test
    public void testConfirmOrder_OrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.confirmOrder(1L);
        });

        assertEquals("Order does not exist", exception.getMessage());
    }

    @Test
    public void testConfirmOrder_OrderNotInDelivery() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.CONFIRMED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.confirmOrder(1L);
        });

        assertEquals("Order is not in delivery", exception.getMessage());
    }

    @Test
    public void testConfirmOrder_FailedToSendMail() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.DELIVERY);
        order.setUserEntity(new UserEntity());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doThrow(new FailedToSendMailException("Failed to send email, email might not be valid")).when(orderManagement).ReceiveOrder();

        Exception exception = assertThrows(FailedToSendMailException.class, () -> {
            orderService.confirmOrder(1L);
        });

        assertEquals("Failed to send email, email might not be valid", exception.getMessage());
    }

    @Test
    public void testApproveOrder_Success() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.PENDING);
        order.setUserEntity(new UserEntity());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = orderService.approveOrder(1L);

        assertEquals("Order Approved", result);
        assertEquals(orderStatus.APPROVED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(orderManagement, times(1)).ConfirmOrder();
    }

    @Test
    public void testApproveOrder_OrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.approveOrder(1L);
        });

        assertEquals("Order does not exist", exception.getMessage());
    }

    @Test
    public void testApproveOrder_OrderNotPending() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.APPROVED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.approveOrder(1L);
        });

        assertEquals("Order is not Pending", exception.getMessage());
    }

    @Test
    public void testApproveOrder_FailedToSendMail() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.PENDING);
        order.setUserEntity(new UserEntity());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doThrow(new FailedToSendMailException("Failed to send email, email might not be valid")).when(orderManagement).ConfirmOrder();

        Exception exception = assertThrows(FailedToSendMailException.class, () -> {
            orderService.approveOrder(1L);
        });

        assertEquals("Failed to send email, email might not be valid", exception.getMessage());
    }

    @Test
    public void testDeliverOrder_Success() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.APPROVED);
        order.setUserEntity(new UserEntity());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = orderService.deliverOrder(1L);

        assertEquals("Order in delivery", result);
        assertEquals(orderStatus.DELIVERY, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(orderManagement, times(1)).ShipOrder();
    }

    @Test
    public void testDeliverOrder_OrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.deliverOrder(1L);
        });

        assertEquals("Order does not exist", exception.getMessage());
    }

    @Test
    public void testDeliverOrder_OrderNotApproved() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.deliverOrder(1L);
        });

        assertEquals("Order is not approved", exception.getMessage());
    }

    @Test
    public void testDeliverOrder_FailedToSendMail() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(orderStatus.APPROVED);
        order.setUserEntity(new UserEntity());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doThrow(new FailedToSendMailException("Failed to send email, email might not be valid")).when(orderManagement).ShipOrder();

        Exception exception = assertThrows(FailedToSendMailException.class, () -> {
            orderService.deliverOrder(1L);
        });

        assertEquals("Failed to send email, email might not be valid", exception.getMessage());
    }
    @Test
    public void testGetOrders_WithUserId() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setUserEntity(new UserEntity());
        orders.add(order);
        Page<Order> orderPage = new PageImpl<>(orders);
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(orderPage);

        OrdersListDTO result = orderService.getOrders(1L, 0, 10, orderStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
    }

    @Test
    public void testGetOrders_WithoutUserId() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setUserEntity(new UserEntity());
        orders.add(order);
        Page<Order> orderPage = new PageImpl<>(orders);
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(orderPage);

        OrdersListDTO result = orderService.getOrders(0, 10, orderStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
    }

    @Test
    public void testGetOrderDetails_WithUserId_Success() throws Exception {
        Order order = new Order();
        order.setUserEntity(new UserEntity());
        order.getUserEntity().setUserID(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderDetails(1L, 1L);

        assertNotNull(result);
    }

    @Test
    public void testGetOrderDetails_WithUserId_Unauthorized() {
        Order order = new Order();
        order.setUserEntity(new UserEntity());
        order.getUserEntity().setUserID(2L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(AuthorizationException.class, () -> {
            orderService.getOrderDetails(1L, 1L);
        });

        assertEquals("Unauthorized to Access this Resource", exception.getMessage());
    }

    @Test
    public void testGetOrderDetails_WithoutUserId_Success() throws Exception {
        Order order = new Order();
        order.setUserEntity(new UserEntity());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderDetails(1L);

        assertNotNull(result);
    }

    @Test
    public void testGetOrderDetails_OrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadInputException.class, () -> {
            orderService.getOrderDetails(1L);
        });

        assertEquals("", exception.getMessage());
    }
}