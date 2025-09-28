在Java中，与工厂相关的设计模式主要有以下几种，它们各自解决不同层面的对象创建问题：

## 1. 简单工厂模式（Simple Factory）

**特点**：一个工厂类，根据参数创建不同对象

```java
public class CarFactory {
    public static Car createCar(String type) {
        switch(type) {
            case "tesla": return new Tesla();
            case "porsche": return new Porsche();
            case "byd": return new Byd();
            default: throw new IllegalArgumentException("Unknown car type");
        }
    }
}

// 使用
Car car = CarFactory.createCar("tesla");
```

**适用场景**：对象创建逻辑简单，产品类型有限

## 2. 工厂方法模式（Factory Method）

**特点**：定义一个创建对象的接口，让子类决定实例化哪个类

```java
public interface CarFactory {
    Car createCar();
}

public class TeslaFactory implements CarFactory {
    public Car createCar() {
        return new Tesla();
    }
}

public class PorscheFactory implements CarFactory {
    public Car createCar() {
        return new Porsche();
    }
}
```

**适用场景**：需要扩展新产品，符合开闭原则

## 3. 抽象工厂模式（Abstract Factory）

**特点**：创建相关或依赖对象的家族，而不需要指定具体类

```java
// 抽象工厂
public interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
    Dialog createDialog();
}

// 具体工厂 - Windows风格
public class WindowsFactory implements GUIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
    public Dialog createDialog() { return new WindowsDialog(); }
}

// 具体工厂 - Mac风格  
public class MacFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
    public Dialog createDialog() { return new MacDialog(); }
}

// 使用
GUIFactory factory = new WindowsFactory();
Button button = factory.createButton();
Checkbox checkbox = factory.createCheckbox();
```

**适用场景**：需要创建一系列相关或依赖的对象

## 4. 静态工厂方法（Static Factory Method）

**特点**：在类中提供静态方法来创建对象

```java
public class Car {
    private String brand;
    private String model;
    
    private Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }
    
    // 静态工厂方法
    public static Car createSportsCar(String model) {
        return new Car("Porsche", model);
    }
    
    public static Car createElectricCar(String model) {
        return new Car("Tesla", model);
    }
    
    public static Car createLuxuryCar(String model) {
        return new Car("Mercedes", model);
    }
}

// 使用
Car sportsCar = Car.createSportsCar("911");
Car electricCar = Car.createElectricCar("Model S");
```

**适用场景**：替代构造函数，提供更有意义的创建方法

## 5. 建造者模式（Builder Pattern）

**特点**：分步构建复杂对象

```java
public class Computer {
    private String CPU;
    private String RAM;
    private String storage;
    private String graphicsCard;
    
    // 私有构造器
    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.RAM = builder.RAM;
        this.storage = builder.storage;
        this.graphicsCard = builder.graphicsCard;
    }
    
    public static class Builder {
        private String CPU;
        private String RAM;
        private String storage;
        private String graphicsCard;
        
        public Builder setCPU(String CPU) {
            this.CPU = CPU;
            return this;
        }
        
        public Builder setRAM(String RAM) {
            this.RAM = RAM;
            return this;
        }
        
        public Builder setStorage(String storage) {
            this.storage = storage;
            return this;
        }
        
        public Builder setGraphicsCard(String graphicsCard) {
            this.graphicsCard = graphicsCard;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
}

// 使用
Computer computer = new Computer.Builder()
    .setCPU("Intel i7")
    .setRAM("16GB")
    .setStorage("1TB SSD")
    .setGraphicsCard("RTX 3080")
    .build();
```

**适用场景**：创建复杂对象，需要多个步骤或配置

## 6. 原型模式（Prototype Pattern）

**特点**：通过复制现有对象来创建新对象

```java
public abstract class Shape implements Cloneable {
    private String id;
    protected String type;
    
    abstract void draw();
    
    public String getType() { return type; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    // 关键：实现clone方法
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

public class Rectangle extends Shape {
    public Rectangle() {
        type = "Rectangle";
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

// 原型注册表（工厂角色）
public class ShapeCache {
    private static Map<String, Shape> shapeMap = new HashMap<>();
    
    public static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return (Shape) cachedShape.clone(); // 返回克隆对象
    }
    
    // 初始化原型对象
    public static void loadCache() {
        Rectangle rectangle = new Rectangle();
        rectangle.setId("1");
        shapeMap.put(rectangle.getId(), rectangle);
        
        Circle circle = new Circle();
        circle.setId("2");
        shapeMap.put(circle.getId(), circle);
    }
}

// 使用
ShapeCache.loadCache();
Shape clonedShape = ShapeCache.getShape("1");
```

**适用场景**：创建对象成本较高，或需要基于现有对象创建新对象

## 7. 单例模式（Singleton Pattern）

**特点**：确保一个类只有一个实例，并提供全局访问点

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    private DatabaseConnection() {
        // 私有构造器
    }
    
    // 工厂方法：获取单例实例
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    // 线程安全版本
    public static synchronized DatabaseConnection getThreadSafeInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}

// 使用
DatabaseConnection db1 = DatabaseConnection.getInstance();
DatabaseConnection db2 = DatabaseConnection.getInstance();
// db1 == db2 为 true
```

**适用场景**：需要全局唯一实例的场景

## 总结对比


在实际项目中，这些模式经常结合使用。比如，抽象工厂中的具体工厂可能使用建造者模式来构建复杂对象，或者使用原型模式来复制预配置的对象。