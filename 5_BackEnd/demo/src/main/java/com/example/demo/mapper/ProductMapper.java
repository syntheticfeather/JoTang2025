package com.example.demo.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.Product;

@Mapper
public interface ProductMapper {

    @Insert("insert into product(title, description, type, price, status, publisher_id, publish_time, update_time) "
            + "values(#{title}, #{description}, #{type}, #{price}, #{status}, #{publisherId}, #{publishTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Product product);

    @Delete("delete from product where id = #{id}")
    int delete(long id);

    @Update("update product set title = #{title}, description = #{description}, type = #{type}, price = #{price}, status = #{status}, "
            + "publisher_id = #{publisherId}, update_time = #{updateTime} where id = #{id}")
    int update(Product product);

    @Select("SELECT * FROM product")
    ArrayList<Product> getList();

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product get(long id);

    // 在ProductMapper中添加筛选方法
    @Select("<script>"
            + "SELECT * FROM product WHERE 1=1"
            + "<if test='type != null'> AND type = #{type} </if>"
            + "<if test='minPrice != null'> AND price &gt;= #{minPrice} </if>"
            + "<if test='maxPrice != null'> AND price &lt;= #{maxPrice} </if>"
            + "<if test='hours != null'> AND publish_time &gt;= DATE_SUB(NOW(), INTERVAL #{hours} HOUR) </if>"
            + "<if test='status != null'> AND status = #{status} </if>"
            + "<if test='sortField != null'>"
            + " ORDER BY ${sortField} "
            + " <if test='sortDirection != null'>${sortDirection}</if>"
            + "</if>"
            + "<if test='sortField == null'> ORDER BY publish_time DESC </if>"
            + "</script>")
    List<Product> selectByFilters(@Param("type") String type,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("hours") Integer hours,
            @Param("status") String status,
            @Param("sortField") String sortField,
            @Param("sortDirection") String sortDirection
    );
}
