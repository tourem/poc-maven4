package com.larbotech.maven4.service;

import com.larbotech.maven4.common.dto.OrderDto;
import com.larbotech.maven4.common.entity.Order;
import com.larbotech.maven4.common.entity.User;
import com.larbotech.maven4.common.exception.ResourceNotFoundException;
import com.larbotech.maven4.common.exception.ValidationException;
import com.larbotech.maven4.common.mapper.OrderMapper;
import com.larbotech.maven4.service.repository.OrderRepository;
import com.larbotech.maven4.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests pour OrderService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;
    private Order order;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .status(User.UserStatus.ACTIVE)
            .build();

        orderDto = OrderDto.builder()
            .id(1L)
            .userId(1L)
            .productName("Test Product")
            .quantity(2)
            .price(BigDecimal.valueOf(50.00))
            .status(Order.OrderStatus.PENDING)
            .build();

        order = Order.builder()
            .id(1L)
            .user(user)
            .productName("Test Product")
            .quantity(2)
            .price(BigDecimal.valueOf(50.00))
            .totalAmount(BigDecimal.valueOf(100.00))
            .status(Order.OrderStatus.PENDING)
            .build();
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // When
        OrderDto result = orderService.createOrder(orderDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo(orderDto.getProductName());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.createOrder(orderDto))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should get order by id successfully")
    void shouldGetOrderByIdSuccessfully() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // When
        OrderDto result = orderService.getOrderById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should get all orders successfully")
    void shouldGetAllOrdersSuccessfully() {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        // When
        List<OrderDto> result = orderService.getAllOrders();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should get orders by user id successfully")
    void shouldGetOrdersByUserIdSuccessfully() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByUserId(1L)).thenReturn(Arrays.asList(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        // When
        List<OrderDto> result = orderService.getOrdersByUserId(1L);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should cancel order successfully")
    void shouldCancelOrderSuccessfully() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // When
        OrderDto result = orderService.cancelOrder(1L);

        // Then
        assertThat(result).isNotNull();
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should throw exception when cancelling delivered order")
    void shouldThrowExceptionWhenCancellingDeliveredOrder() {
        // Given
        order.setStatus(Order.OrderStatus.DELIVERED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When & Then
        assertThatThrownBy(() -> orderService.cancelOrder(1L))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Cannot cancel a delivered order");
    }

    @Test
    @DisplayName("Should delete order successfully")
    void shouldDeleteOrderSuccessfully() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(true);

        // When
        orderService.deleteOrder(1L);

        // Then
        verify(orderRepository).deleteById(1L);
    }
}
