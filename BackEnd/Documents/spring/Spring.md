# 项目创建
```bash
sudo apt install openjdk-17-jdk
sudo apt install maven
```

项目包是从 https://start.spring.io/ 直接建立。然后复制到linux上，  

不过也可以直接Linux指令创建:
`curl https://start.spring.io/starter.tgz -d dependencies=web -d type=maven-project -d groupId=com.example -d artifactId=demo -d name=demo | tar -xzvf -`不过起始依赖挺难设置的，记不住。


# sql库和javaEntity类的相互转换

sql库习惯下划线命名，java实体类习惯驼峰命名。

sql库中表的字段和java实体类的属性名不一致，需要进行转换。

### 1.使用@Result进行转换

```java
@Select("SELECT id, product_id, buyer_id, status, create_time, update_time FROM order WHERE id = #{id}")
@Results({
    @Result(property = "id", column = "id"),
    @Result(property = "productId", column = "product_id"),
    @Result(property = "buyerId", column = "buyer_id"),
    @Result(property = "status", column = "status"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "updateTime", column = "update_time")
})
// property是java实体类的属性名，column是sql库中表的字段名
Order selectBasicById(Long id);
```
当然这样写十分麻烦

### 2.采用mybatis提供的设置

```yml
mybatis:
  configuration:
    map-underscore-to-camel-case: true
```
写入application.yml文件中，mybatis会自动将下划线命名的字段映射到驼峰命名的属性上。  

例如: 
    create_time -> createTime

# 分类查询

这个倒是没有什么特别新的，只有动态的sql语句

## 1.书写controller，service，mapper三个层的方法

## 2.mapper层中写入动态的sql语句

```java
// 我写了通过type查询， 
// 范围价格查询， 
// 发布时间查询， 
// 状态查询， 
// 依照某种字段升/降序排序
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
```
可以写在xml文件中，也可以写在java文件中。  
但是我就这么一个地方要用到动态的sql语句，就直接写在这了