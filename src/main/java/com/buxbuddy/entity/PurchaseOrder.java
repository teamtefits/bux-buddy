package com.buxbuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_orders")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String status;
    // PENDING, CONFIRMED, DELIVERED

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderItem> items;

    // getters and setters
}