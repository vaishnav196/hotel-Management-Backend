package com.HotelManagement.Repository;

import com.HotelManagement.model.OrderProduct;
import com.HotelManagement.model.Orders;
import com.HotelManagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepo extends JpaRepository <OrderProduct,Integer>{

// OrderProduct findByOrderAndProduct(Orders o, Product p);


}
