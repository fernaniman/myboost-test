package org.example.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PO_HEADER")
@Data
public class PoHeaderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE_TIME")
    private Date dateTime;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "TOTAL_PRICE")
    private Integer totalPrice;

    @Column(name = "TOTAL_COST")
    private Integer totalCost;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "PO_HEADER_ID",referencedColumnName = "ID")
    private List<PoDetailEntity> poDetailEntityList;
}
