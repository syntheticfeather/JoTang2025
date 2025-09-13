package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.Order;

@Mapper
public interface OrderMapper {

    // 插入订单
    @Insert("INSERT INTO product_order (product_id, buyer_id, status, create_time, update_time) "
            + "VALUES (#{productId}, #{buyerId}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addOrder(Order order);

    // 删除订单
    @Delete("DELETE FROM product_order WHERE id = #{id}")
    int deleteById(Long id);

    // 更新订单状态
    @Update("UPDATE product_order SET status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    int updateStatus(Long id, String status, LocalDateTime updateTime);

    // 插入订单
    @Insert("INSERT INTO product_order (product_id, buyer_id, status, create_time, update_time) "
            + "VALUES (#{productId}, #{buyerId}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT * FROM product_order WHERE id = #{id}")
    Order selectById(Long id);

    // 根据买家ID查询订单列表
    @Select("SELECT * FROM product_order WHERE buyer_id = #{buyerId}")
    List<Order> selectByBuyerId(Long buyerId);

    // 根据商品ID查询订单
    @Select("SELECT * FROM product_order WHERE product_id = #{productId}")
    List<Order> selectByProductId(Long productId);

}
