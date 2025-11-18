package com.larbotech.maven4.service;

import com.larbotech.maven4.common.dto.OrderDto;
import com.larbotech.maven4.common.entity.Order;
import com.larbotech.maven4.common.entity.User;
import com.larbotech.maven4.common.exception.ResourceNotFoundException;
import com.larbotech.maven4.common.exception.ValidationException;
import com.larbotech.maven4.common.mapper.OrderMapper;
import com.larbotech.maven4.service.repository.OrderRepository;
import com.larbotech.maven4.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des commandes
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    /**
     * Créer une nouvelle commande
     */
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating order for user: {}", orderDto.getUserId());

        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(orderDto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User", orderDto.getUserId()));

        // Validation métier
        if (orderDto.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be positive");
        }

        if (orderDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be positive");
        }

        Order order = orderMapper.toEntity(orderDto);
        order.setUser(user);

        // Calculer le montant total
        BigDecimal totalAmount = orderDto.getPrice().multiply(BigDecimal.valueOf(orderDto.getQuantity()));
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully with id: {}", savedOrder.getId());
        return orderMapper.toDto(savedOrder);
    }

    /**
     * Récupérer une commande par ID
     */
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        log.debug("Fetching order with id: {}", id);

        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        return orderMapper.toDto(order);
    }

    /**
     * Récupérer toutes les commandes
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        log.debug("Fetching all orders");

        return orderRepository.findAll().stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Récupérer les commandes d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(Long userId) {
        log.debug("Fetching orders for user: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        return orderRepository.findByUserId(userId).stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Mettre à jour une commande
     */
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        log.info("Updating order with id: {}", id);

        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        orderMapper.updateEntityFromDto(orderDto, existingOrder);

        // Recalculer le montant total si nécessaire
        if (orderDto.getPrice() != null && orderDto.getQuantity() != null) {
            BigDecimal totalAmount = orderDto.getPrice().multiply(BigDecimal.valueOf(orderDto.getQuantity()));
            existingOrder.setTotalAmount(totalAmount);
        }

        Order updatedOrder = orderRepository.save(existingOrder);

        log.info("Order updated successfully: {}", id);
        return orderMapper.toDto(updatedOrder);
    }

    /**
     * Annuler une commande
     */
    public OrderDto cancelOrder(Long id) {
        log.info("Cancelling order with id: {}", id);

        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new ValidationException("Cannot cancel a delivered order");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new ValidationException("Order is already cancelled");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        log.info("Order cancelled successfully: {}", id);
        return orderMapper.toDto(cancelledOrder);
    }

    /**
     * Supprimer une commande
     */
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);

        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", id);
        }

        orderRepository.deleteById(id);
        log.info("Order deleted successfully: {}", id);
    }
}
