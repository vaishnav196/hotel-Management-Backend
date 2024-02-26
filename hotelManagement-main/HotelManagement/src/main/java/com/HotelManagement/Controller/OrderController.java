package com.HotelManagement.Controller;

import com.HotelManagement.Repository.*;
import com.HotelManagement.model.*;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
public class OrderController {

@Autowired
private CartRepo cartRepo;


@Autowired
private OrderRepo orderRepo;

@Autowired
private UserRepo userRepo;

@Autowired
private ProductRepo productRepo;

@Autowired
private OrderProductRepo orderProductRepo;


@Autowired
private CartProductRepo cartProductRepo;
//
//
@DeleteMapping("/orders/{orderId}")
    public String deleteOrderFromCart(@PathVariable("orderId") int orderId){
         Orders order=orderRepo.findById(orderId).orElse(null);
         orderRepo.delete(order);
         return "Order removed ";
}


    @GetMapping("/orders/{userId}")
    public List<Orders> getallorders(@PathVariable("userId") int userId){
        User user = userRepo.findById(userId).get();
       List<Orders> order=orderRepo.findByUser(user);
      return order;

    }

    @PostMapping("/{cartId}/orders")
    public String placeOrder(@PathVariable("cartId") int cartId){

        User user = userRepo.findById(cartId).get();
        Cart cart=cartRepo.findById(user.getCart().getCartId()).orElse(null);
        Orders o = new Orders();
        o.setUser(user);
        o.setTotalPrice(cart.getTotalPrice());
        Orders placedOrder= orderRepo.save(o);

        for(Product p:cart.getProduct()){
            CartProduct cartItem=cartProductRepo.findByCartIdAndProductId(cart.getCartId(), p.getProductId());
            OrderProduct op = new OrderProduct();
            op.setProductId(p.getProductId());
            op.setProductQuantity(cartItem.getProductQuantity());
            op.setOrderId(placedOrder.getOrderId());
            orderProductRepo.save(op);
        }



//for clearing product from cart table when order is placed
        cart.getProduct().clear();
        cart.setTotalPrice(0);
        cartRepo.save(cart);

        return "order placeed";

    }

}
