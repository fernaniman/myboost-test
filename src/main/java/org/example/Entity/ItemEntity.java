package org.example.Entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ITEMS")
@Data
public class ItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "Name should not be null")
    @Column(name = "NAME", length = 500)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @NotNull(message = "Price should not be null")
    @Column(name = "PRICE")
    private Integer price;

    @NotNull(message = "Cost should not be null")
    @Column(name = "COST")
    private Integer cost;
}
