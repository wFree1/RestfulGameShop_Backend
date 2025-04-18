package com.example.gameshoprestful.service;

import com.example.gameshoprestful.DTO.OrderDetailsDTO;
import com.example.gameshoprestful.DTO.OrderItemDTO;
import com.example.gameshoprestful.DTO.OrderRequestDTO;
import com.example.gameshoprestful.DTO.CartWithPriceDTO;
import com.example.gameshoprestful.entity.Order;
import com.example.gameshoprestful.entity.OrderItem;
import com.example.gameshoprestful.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ItemMapper itemMapper;
    private final ExistingGameMapper existingGameMapper;

    @Autowired
    public OrderService(CartMapper cartMapper,
                        OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper, ItemMapper itemMapper, ExistingGameMapper existingGameMapper) {
        this.cartMapper       = cartMapper;
        this.orderMapper      = orderMapper;
        this.orderItemMapper  = orderItemMapper;
        this.itemMapper = itemMapper;
        this.existingGameMapper = existingGameMapper;
    }

    @Transactional
    public Integer createOrder(OrderRequestDTO request) {
        // 1. 查询所有已选购物车项及价格
        List<CartWithPriceDTO> carts = cartMapper.findSelectedCartsWithPrice(request.getUserId());
        if (carts.isEmpty()) {
            throw new IllegalStateException("购物车中没有已选商品");
        }

        // 2. 计算总价
        BigDecimal total = carts.stream()
                .map(c -> c.getEditionPrice() != null ? c.getEditionPrice() : c.getItemPrice())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        Integer maxId = orderMapper.selectMaxId();     // 可能为 null
        int nextId = (maxId == null ? 0 : maxId) + 1;

        // 3. 插入 orders 表
        Order order = new Order();
        order.setId((long) nextId);
        order.setUser_id(request.getUserId());
        order.setName(request.getName());
        order.setPhone(request.getPhone());
        order.setEmail(request.getEmail());
        order.setPaytype(request.getPayType());
        order.setTotal(total);
        order.setStatus(1);  // 未支付
        order.setDatetime(LocalDateTime.now());
        order.setState(String.valueOf(0));   // 未发货
        orderMapper.insert(order);

        // 4. 插入 order_item 表
        carts.forEach(c -> {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setItemId(c.getItemId());
            item.setEdition_id(c.getEditionId());
            item.setPrice(c.getEditionPrice() != null ? c.getEditionPrice() : c.getItemPrice());
            orderItemMapper.insert(item);
        });

        // 5. 删除购物车中已选项
        cartMapper.deleteSelectedByUserId(request.getUserId());


        return nextId;
    }

    /**
     * 功能三：查询某用户的所有订单及其明细
     */
    public List<OrderDetailsDTO> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderMapper.findByUserId(userId);
        return orders.stream().map(o -> {
            OrderDetailsDTO dto = new OrderDetailsDTO();
            dto.setOrderId(o.getId().intValue());
            if (o.getUser_id() != null) {
                dto.setUserId(o.getUser_id());
            } else {
                dto.setUserId(0L); // 或者你想设置的默认值
            }

            dto.setTotal(o.getTotal());
            dto.setName(o.getName());
            dto.setPhone(o.getPhone());
            dto.setEmail(o.getEmail());
            dto.setPaytype(o.getPaytype());
            dto.setStatus(o.getStatus());
            dto.setDatetime(o.getDatetime());
            dto.setState(o.getState());
            // 查询该订单下的所有商品
            List<OrderItemDTO> items = orderItemMapper.findByOrderId(o.getId().intValue());
            dto.setItems(items);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 功能四：确认支付，将订单状态置为已支付（2）
     */
    @Transactional
    public void confirmPayment(Integer orderId) {
        // 简单校验
        Order o = orderMapper.selectById(orderId);
        if (o == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        orderMapper.updateStatus(orderId, 2);

        // 3. 查询该订单的所有游戏项
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(orderId);

        for (OrderItem item : orderItems) {
            if (item.getItemId() == null) {
                throw new IllegalArgumentException("订单项中的商品ID为空");
            }
            Long itemId = item.getItemId().longValue();

            String itemName = itemMapper.selectItemNameById(Math.toIntExact(itemId));

            // 4. 插入 existing_games，避免重复
            existingGameMapper.insertIgnore(Math.toIntExact(o.getUser_id()), Math.toIntExact(itemId), itemName);
        }
    }

}
