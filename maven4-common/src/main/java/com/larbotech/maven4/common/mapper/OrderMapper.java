package com.larbotech.maven4.common.mapper;

import com.larbotech.maven4.common.dto.OrderDto;
import com.larbotech.maven4.common.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper MapStruct pour Order
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "userEmail")
    OrderDto toDto(Order order);

    @Mapping(source = "userId", target = "user.id")
    Order toEntity(OrderDto orderDto);

    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(OrderDto orderDto, @MappingTarget Order order);
}
