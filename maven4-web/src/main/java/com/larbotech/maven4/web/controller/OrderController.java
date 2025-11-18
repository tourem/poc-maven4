package com.larbotech.maven4.web.controller;

import com.larbotech.maven4.common.dto.OrderDto;
import com.larbotech.maven4.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller pour la gestion des commandes
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "API de gestion des commandes")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Créer une commande", description = "Crée une nouvelle commande")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("REST request to create order for user: {}", orderDto.getUserId());
        OrderDto created = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une commande", description = "Récupère une commande par son ID")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        log.info("REST request to get order: {}", id);
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @Operation(summary = "Lister toutes les commandes", description = "Récupère la liste de toutes les commandes")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        log.info("REST request to get all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lister les commandes d'un utilisateur", description = "Récupère toutes les commandes d'un utilisateur")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        log.info("REST request to get orders for user: {}", userId);
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une commande", description = "Met à jour les informations d'une commande")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        log.info("REST request to update order: {}", id);
        OrderDto updated = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Annuler une commande", description = "Annule une commande")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id) {
        log.info("REST request to cancel order: {}", id);
        OrderDto cancelled = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelled);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une commande", description = "Supprime une commande du système")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("REST request to delete order: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
