package com.example.demo.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.Entity.Product;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product getProduct(long id);

    @Insert("insert into product(title, description, type, price, status, publisher_id, publish_time, update_time) "
            + "values(#{title}, #{description}, #{type}, #{price}, #{status}, #{publisherId}, #{publishTime}, #{updateTime})")
    void addProduct(Product product);

    @Delete("delete from product where id = #{id}")
    void deleteProduct(long id);

    @Update("update product set title = #{title}, description = #{description}, type = #{type}, price = #{price}, status = #{status}, "
            + "publisher_id = #{publisherId}, update_time = #{updateTime} where id = #{id}")
    void updateProduct(Product product);
}
