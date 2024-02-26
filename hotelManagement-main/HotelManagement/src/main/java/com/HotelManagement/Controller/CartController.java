package com.HotelManagement.Controller;

import com.HotelManagement.Dto.CartDto;
import com.HotelManagement.Repository.CartProductRepo;
import com.HotelManagement.Repository.CartRepo;
import com.HotelManagement.Repository.ProductRepo;
import com.HotelManagement.Repository.UserRepo;
import com.HotelManagement.model.Cart;
import com.HotelManagement.model.CartProduct;
import com.HotelManagement.model.Product;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
public class CartController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartProductRepo cartProductRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private  CartRepo cartRepo;

    @GetMapping("/cart")
    public List<Cart> getCartProduct(){
        List<Cart> allProduct=cartRepo.findAll();
        return allProduct;
    }

    @GetMapping("/cart/{cartId}")
    public ArrayList<CartDto> getCartProduct(@PathVariable("cartId") int cartId,Product product){
        Cart cart= cartRepo.findById(cartId).orElse(null);
        List <CartProduct> cartProducts=cartProductRepo.findByCartId(cartId);
        ArrayList<CartDto> cartD= new ArrayList<CartDto>();
        cart.getProduct().forEach(p->{
            cartProducts.forEach(cp->{
                if(p.getProductId()==cp.getProductId()){
                    cartD.add(new CartDto(p.getProductId(),p.getProductName(), p.getPrice(), cp.getProductQuantity()));
                }
            });
        });

        return cartD;
    }


    @GetMapping("/carts/{cartId}")
    public Cart getCartProducts(@PathVariable("cartId") int cartId,Product product){
        Cart Cartproduct=cartRepo.findById(cartId).orElse(null);
        Cartproduct.getProduct();
        cartRepo.save(Cartproduct);
        return Cartproduct;
    }


    @PostMapping("/{cartId}/product/{productId}")
    public String addToCart(@PathVariable("cartId") int cartId, @PathVariable("productId") int productId){
        double totalPrice=0;
        Cart cart= cartRepo.findById(cartId).orElse(null);
        if(cart==null){
            return "Nothing is there in Cart";
        }

        Product product=productRepo.findById(productId).orElse(null);
        if(product==null){
            return "Product not Found";
        }


        CartProduct existingProduct=cartProductRepo.findByCartIdAndProductId(cartId,productId);

        if(existingProduct!=null){
            existingProduct.setProductQuantity(existingProduct.getProductQuantity() +1);
            cartProductRepo.save(existingProduct);

        }
        else {
            CartProduct cp = new CartProduct();
            cp.setProductId(product.getProductId());
            cp.setCartId(cartId);
            cp.setProductQuantity(1);
            cartProductRepo.save(cp);
        }

        List<CartProduct> cartItems=cartProductRepo.findByCartId(cartId);

        for(CartProduct item:cartItems){
            Product p=productRepo.findById(item.getProductId()).orElse(null);
            totalPrice+=item.getProductQuantity()*p.getPrice();
        }


        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
        return "Successfully added to cart ";
    }




    @DeleteMapping("{cartId}/product/{productId}")
    public String  deleteFromCart(@PathVariable("productId") int productId,@PathVariable("cartId") int cartId){
        double totalPrice = 0;
        Cart cart =cartRepo.findById(cartId).orElse(null);
        Product product=productRepo.findById(productId).orElse(null);
        CartProduct cp=new CartProduct();

        CartProduct existingProduct=cartProductRepo.findByCartIdAndProductId(cartId,productId);
//
        if(existingProduct.getProductQuantity() >1){
            existingProduct.setProductQuantity(existingProduct.getProductQuantity()-1);
            cartProductRepo.save(existingProduct);
        }
        else{
            cartProductRepo.deleteById(existingProduct.getId());
        }
        List<CartProduct> cartItems=cartProductRepo.findByCartId(cartId);

        for(CartProduct item:cartItems){
            Product p=productRepo.findById(item.getProductId()).orElse(null);
            totalPrice+=item.getProductQuantity()*p.getPrice();
        }

        cart.setTotalPrice(cart.getTotalPrice() - product.getPrice());

        if (cart.getTotalPrice() < 0) {
            cart.setTotalPrice(0);
        }
        cartRepo.save(cart);

        return "product Removed From Cart";
    }



    @DeleteMapping("/{cartId}/products/{productId}")
    public String removeProduct(@PathVariable("cartId") int cartId,@PathVariable("productId") int productId){
        Cart cart=cartRepo.findById(cartId).orElse(null);

        CartProduct selectedProduct=cartProductRepo.findByCartIdAndProductId(cartId,productId);
        Product p = productRepo.findById(selectedProduct.getProductId()).orElse(null);
        cart.setTotalPrice(cart.getTotalPrice()-(selectedProduct.getProductQuantity()*p.getPrice()));
        cartRepo.save(cart);
        cartProductRepo.deleteById((selectedProduct.getId()));

        return "Full product removed";
    }
}