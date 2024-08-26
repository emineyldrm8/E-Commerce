package com.haratres.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Address", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "user_id"})
})
@Getter
@Setter
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "county_id", nullable = false)
    @NotNull(message = "County cannot be blank")
    private County county;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @NotNull(message = "City cannot be blank")
    private City city;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @NotNull(message = "District cannot be blank")
    private District district;

    @Column(name = "text", nullable = false)
    @NotBlank(message = "Text cannot be blank")
    private String text;
}
