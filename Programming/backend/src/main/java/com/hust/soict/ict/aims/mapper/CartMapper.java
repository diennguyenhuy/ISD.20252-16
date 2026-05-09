package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.dto.response.CartItemResponse;
import com.hust.soict.ict.aims.models.dto.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = ProductMapper.class
)
public interface CartMapper {

    CartResponse toCartResponse(Cart cart);

    CartItemResponse toCartItemResponse(CartItem cartItem);
}
