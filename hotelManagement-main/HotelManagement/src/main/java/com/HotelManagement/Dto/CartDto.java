package com.HotelManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {


 private int productId;

    private String productName;

    private int productPrice;

    private int productQuantity;



}
