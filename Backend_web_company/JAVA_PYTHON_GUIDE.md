# 用 Python 视角理解这份 Java Spring Boot 项目

这份文档把当前项目里的几个 Java 文件，翻译成更接近 Python 的思路来讲。

## 1. 整体结构

这个项目可以分成三层来理解：

- 数据模型：`ServiceCard`
- 数据访问：`ServiceCardRepository`
- 接口控制：`ServiceCardController`

再加上一个启动入口：`DemoApplication`

你可以把它类比成 Python Web 项目里的：

- 一个数据类
- 一个数据库操作对象
- 一个路由函数
- 一个启动程序

## 2. `ServiceCard`：数据模型

文件：[src/main/java/com/example/demo/ServiceCard.java](src/main/java/com/example/demo/ServiceCard.java)

这个类表示一张“服务卡片”，里面保存了数据库中的一条记录。

如果用 Python 表达，大概像这样：

```python
class ServiceCard:
    def __init__(self):
        self.id = None
        self.iconUrl = None
        self.title = None
        self.description = None
        self.targetLink = None
```

### 关键注解

- `@Entity`：告诉 Spring，这是一个数据库实体类
- `@Table(name = "company_website")`：这个类对应数据库表 `company_website`
- `@Id`：标记主键字段
- `@GeneratedValue(strategy = GenerationType.IDENTITY)`：主键由数据库自动生成

### 字段含义

- `id`：主键
- `iconUrl`：图标地址
- `title`：标题
- `description`：描述
- `targetLink`：跳转链接

### Getter / Setter

Java 里经常不用直接公开属性，而是通过 getter/setter 访问。

在 Python 里，你可能会直接写：

```python
card.title = "主页设计"
print(card.title)
```

在 Java 里，通常写成：

```java
card.setTitle("主页设计");
System.out.println(card.getTitle());
```

## 3. `ServiceCardRepository`：数据库访问层

文件：[src/main/java/com/example/demo/ServiceCardRepository.java](src/main/java/com/example/demo/ServiceCardRepository.java)

这个接口负责和数据库打交道。

你可以把它理解成 Python 里一个现成的数据访问工具：

```python
def get_all_cards():
    return session.query(ServiceCard).all()
```

### 代码含义

```java
public interface ServiceCardRepository extends JpaRepository<ServiceCard, Long> {
}
```

意思是：

- 这个 Repository 专门操作 `ServiceCard`
- 主键类型是 `Long`
- Spring Data JPA 会自动提供很多常用方法，比如：
  - `findAll()`：查全部
  - `findById()`：按 id 查询
  - `save()`：保存或更新
  - `delete()`：删除

这里虽然接口体是空的，但它已经“继承”了很多数据库操作能力。

## 4. `ServiceCardController`：HTTP 接口层

文件：[src/main/java/com/example/demo/ServiceCardController.java](src/main/java/com/example/demo/ServiceCardController.java)

这个类负责接收浏览器请求，并把数据库结果返回出去。

你可以把它类比成 Python Flask / FastAPI 的路由：

```python
@app.get("/api/cards")
def get_all_cards():
    return repository.find_all()
```

### 关键注解

- `@RestController`：这是一个 REST 接口控制器，返回 JSON
- `@RequestMapping("/api/cards")`：这个类里的接口统一以 `/api/cards` 开头
- `@GetMapping`：只处理 GET 请求

### 依赖注入

构造函数：

```java
public ServiceCardController(ServiceCardRepository serviceCardRepository) {
    this.serviceCardRepository = serviceCardRepository;
}
```

这表示 Spring 会自动把 `ServiceCardRepository` 传进来。

你可以理解成：框架帮你创建好数据库对象，然后“塞”给控制器使用，不需要你手动 `new`。

### 接口方法

```java
@GetMapping
public List<ServiceCard> getAllCards() {
    return serviceCardRepository.findAll();
}
```

意思是：

- 当访问 `GET /api/cards`
- 就执行 `getAllCards()`
- 返回数据库中所有 `ServiceCard`

返回值 `List<ServiceCard>` 可以理解成 Python 的列表：

```python
[
    {"id": 1, "title": "A"},
    {"id": 2, "title": "B"}
]
```

Spring Boot 会自动把它转成 JSON。

## 5. `DemoApplication`：程序入口

文件：[src/main/java/com/example/demo/DemoApplication.java](src/main/java/com/example/demo/DemoApplication.java)

这个文件是整个程序的启动点。

Python 里你可能会写：

```python
if __name__ == "__main__":
    app.run()
```

Java 里对应的是：

```java
public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
}
```

### `@SpringBootApplication`

这个注解表示：

- 这是 Spring Boot 主程序
- 会自动扫描当前包及子包下的类
- 会自动把 `Controller`、`Repository`、`Entity` 组合起来

## 6. 请求是怎么流动的

当你访问：

```text
GET http://localhost:8080/api/cards
```

流程大概是：

1. 浏览器发起 HTTP GET 请求
2. `ServiceCardController` 接收到请求
3. 控制器调用 `ServiceCardRepository.findAll()`
4. Repository 去数据库表 `company_website` 查询数据
5. 查询结果返回给控制器
6. Spring Boot 自动转换成 JSON 响应给浏览器

## 7. 你可以怎么用 Python 思维记忆

- `Entity` = 数据类 / ORM 模型
- `Repository` = 数据库操作层
- `Controller` = 路由层 / 接口层
- `main()` = 程序入口

## 8. 一句话总结

这套代码的作用就是：

“定义一个卡片对象，去数据库里查出所有卡片，再通过 `/api/cards` 这个接口返回给前端。”

## 9. 继续学习建议

如果你想继续学，我建议下一步重点看这几个概念：

- Java 类和 Python 类的区别
- `interface` 是什么
- Spring 的依赖注入是什么
- JPA 是怎么把对象映射到数据库表的
- JSON 是怎么自动返回的