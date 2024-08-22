package com.haratres.ecommerce.dto;

import com.haratres.ecommerce.model.CartEntry;
import com.haratres.ecommerce.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CartDto {
    private User user;
    private List<CartEntry> cartEntries;
}
