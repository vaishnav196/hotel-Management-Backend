package com.HotelManagement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders_products")
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

@Column(name = "orders_order_id")
private int orderId;


@Column(name = "products_product_id")
private int productId;

@Column(nullable = true)
private int productQuantity;

}
