org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `long` from Object value (token `JsonToken.START_OBJECT`)


token过期很快？

# 怎么第一个的id不是1呢？

![](imgs/sql.png)

    只要对sql进行一次请求，就会为该请求分配一次id。

    但是我有很多次请求，因为publish_id的外键约束导致了sql创建行出错，再加上测试时删除了一些行。所以该id并没有存进sql或者直接被删除了。

    因此该id在sql中已经使用过了。

    下次分配id的时候就不会分配这个id了。


# 关于json不能首字母大写传参

```json
{
  "adminRegister" : "654321"
}
```

![](imgs/JsonNameSuc.png)

#### **可用**

```json
{
  "AdminRegister" : "654321"
}
```

![](imgs/JsonNameFail.png)

#### **不可用**

# Redis序列化异常

org.springframework.data.redis.serializer.SerializationException: Could not write JSON: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (or disable `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`) (through reference chain: java.util.ArrayList[0]->com.example.demo.entity.Product["publishTime"])

# 虚拟机崩了

    平静的下午，正在写我的markdown文档，突然显示markdown文档保存出错，ssh连接断开，神秘的bug出现了，之后再也连不上了。

很显然没法直接git拉一个新的库，因为我还没push到远程。

于是:

    先是来三遍重启，虚拟机重启，vmware重启，机子重启，

    发现好像不是重启能解决问题的大问题

然后我查阅了资料，说分为配置文件出错，磁盘出错:

    配置文件出错的话，我新建的虚拟机，然后把磁盘添加过去，就可以了

    但是发现磁盘添加过去后，相同的出现了无法打开的问题。好家伙是磁盘出问题了，

然后只能搜索磁盘回复的方法:

    发现vmware有自己的修复工具

`"C:\Program Files (x86)\VMware\VMware Workstation\vmware-vdiskmanager" -R "D:\VMware Machines\Windows 10\Windows 10.vmdk"`

    前面是vmware-vdiskmanager的路径，后面是要修复磁盘的路径。

啊但是，仍然没用，我只能想办法把磁盘里的东西拿出来

**于是找到了米奇妙妙工具:**

### [DiskGenius]("https://www.diskgenius.cn/help/images/open-virtual-disk.png")

直接把磁盘的东西拷贝了出来，好在东西没损失多少，部分图片丢失了，大东西倒是没什么丢失，嗯还有git仓库崩了，得重新建库


