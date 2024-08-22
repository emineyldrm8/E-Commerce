package com.haratres.ecommerce.dto;

import com.haratres.ecommerce.model.CartEntry;
import com.haratres.ecommerce.model.User;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private List<CartEntryDto> cartEntries;
}
