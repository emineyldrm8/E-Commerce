package com.haratres.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Table(name = "Price")
@Getter
@Setter
@Document(indexName = "prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value", nullable = false)
    private Double value;

    @OneToOne(mappedBy = "price")
    @JsonIgnore
    @JsonBackReference
    private Product product;
}
