package org.example.Entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PO_DETAIL")
@Data
public class PoDetailEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PO_HEADER_ID")
    private Long poHeaderId;

    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "COST")
    private Integer cost;
}
