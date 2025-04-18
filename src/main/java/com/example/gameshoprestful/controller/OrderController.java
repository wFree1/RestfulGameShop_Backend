package com.example.gameshoprestful.controller;

import com.example.gameshoprestful.DTO.OrderDetailsDTO;
import com.example.gameshoprestful.DTO.OrderRequestDTO;
import com.example.gameshoprestful.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 功能二：创建订单
     * 请求体：{ userId, name, phone, email, payType }
     */
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO request) {
        orderService.createOrder(request);
        return ResponseEntity.ok("{\"message\":\"订单创建成功\"}");
    }

    /**
     * 功能三：根据 userId 查询该用户的所有订单详情
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDetailsDTO>> listOrdersByUser(@PathVariable Integer userId) {
        List<OrderDetailsDTO> list = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(list);
    }

    /**
     * 功能四：确认支付，将订单状态置为已支付
     */
    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Integer orderId) {
        orderService.confirmPayment(orderId);
        return ResponseEntity.ok("{\"message\":\"支付成功\"}");
    }

}
