# Springboot整合Mybatis

---

## 基本项目结构

>  src下分为main和test目录，test目录为测试目录，main目录结构如下
>
>  - scr
>     - main
>       - java.com.xxx.xxx
>         - SpringdemoApplication
>         - entity
>         - mapper/DAO
>         - service
>         - controller
>       - resources
>         - application.properties
>     - test
>  - pom.xml



- java [com.xxx.xxx]

  > java代码，下面的`com.xxx.xxx`为包空间

  - SpringdemoApplication

    > 启动类，是在`com.xxx.xxx`下而不是`java/`下

  - entity

    - User

      > 属性private、`getter`、`setter`、`toString`，idea快捷键：`alt`+`Insert`

  - mapper/DAO

    - UserMapper

      > `@Repository`，接口，实现基本的增删改查（sql语句）
      >
      > 名字要和映射的xml同名；
      >
      > 为什么用接口：可以为DAO进行多实现；编写上层代码不需要实现；
      >
      > 在`XxxApplication`启动类中用`@MapperScan("")`来扫描mapper；

  - service

    - UserService

      > 接口
      >
      > 为什么用接口：AOP和事务管理的实现基于动态代理,而动态代理的实现依赖于接口；编写上层代码不需要实现

    - Impl

      - UserServiceImpl

        > `@Service`，实现类，业务的实现
        >
        > `@Autowired`装配`mapper/DAO`

  - controller

    - UserController

      > `@RestController`Rest风格，`@RequestMapping("/xxx")`请求路径前缀
      >
      > `@Autowired`装配service接口
      >
      > `@RequestMapping("getUser/{id}")`具体的请求路由，参数中用`@PathVariable`获取路由的参数

- resources

  > 静态资源

  - mapping

    > mapper映射的xml文件,与mapper类要同名；
    >
    > 在`application.properties`中配置扫描xml：
    >
    > ```xml
    > mybatis.mapper-locations=classpath:mapping/*Mapper.xml
    > ```
  
    - UserMapper.xml
  
  - application.properties
  
    > 简单的spring的配置



---

## 引入相关依赖



- 基本模版

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  
  <!--    项目信息-->
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.checker</groupId>
      <artifactId>springdemo</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <name>springdemo</name>
      <description>Demo project for Spring Boot</description>
  
  <!--    全局配置-->
      <properties>
          <java.version>1.8</java.version>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
          <spring-boot.version>2.3.7.RELEASE</spring-boot.version>
      </properties>
  
  <!--    父依赖-->
      <dependencyManagement>
          <dependencies>
              <dependency>
              </dependency>
          </dependencies>
      </dependencyManagement>
  
  <!--    依赖-->
      <dependencies>
          <dependency>
          </dependency>
      </dependencies>
  
  
  <!--    指定使用的插件-->
      <build>
          <plugins>
              <plugin>
              </plugin>
          </plugins>
      </build>
  
  </project>
  
  ```

  

### dependencies

> 依赖

- web

  - spring-boot-starter-web

  ```xml
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
  ```

- mysql

  - mysql-connector-java

  ```xml
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <scope>runtime</scope>
          </dependency>
  ```

  > scope：依赖关系
  >
  > runntime表示被依赖项目无需参与项目的编译，不过后期的测试和运行周期需要其参与。

- mybatis

  - mybatis-spring-boot-starter

  ```xml
          <dependency>
              <groupId>org.mybatis.spring.boot</groupId>
              <artifactId>mybatis-spring-boot-starter</artifactId>
              <version>2.1.0</version>
          </dependency>
  ```

### build

> 插件

- plugins > plugin

  - spring-boot-maven-plugin

  ```xml
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
              </plugin>
  ```

### dependencyManagement

> 配置父依赖，设置版本，子依赖在使用的时候就无需设置版本

- dependencies > dependency
  - spring-boot-dependencies
    
    ```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ```



---

## 对应类的编写

### entity

> 实体类，对应数据库

```java
public class User {
    private Integer id;

    // getter and setter
	// toString
}
```

### mapper/DAO

> 操作数据库

```java
@Repository
public interface UserMapper {
    User selectByID(int id);
}
```

### mapper.xml

> 映射sql
>
> `namespace`对应Mapper的位置，`id`对应方法名，`resultType`对应返回类型，`parameterType`对应参数类型，`#{id}`取同名参数

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.checker.springdemo.mapper.UserMapper">
    <select id="selectByID" resultType="com.checker.springdemo.entity.User" parameterType="Integer">
        SELECT * FROM user where id=#{id}
    </select>

</mapper>
```

### service

> 具体业务

```java
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User selectByID(int id){
        return userMapper.selectByID(id);
    }
}
```

### controller

> 控制层

```java
@RestController
@RequestMapping("/testBoot")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUser/{id}")
    public String GetUserByID(@PathVariable int id){
        User user = userService.selectByID(id);
        if (user != null){
            return user.toString();
        }else{
            return "无记录";
        }
    }
}
```



---

## 配置文件

### application.properties

> `classpath:`对应`resource`目录

- 设置开发环境

  ```properties
  # 设置为开发环境
  spring.profiles.active=dev
  ```

  > dev: 开发、生产: pro、测试: test

- 应用服务 WEB 访问端口
  
```properties
  # 应用服务 WEB 访问端口
  server.port=8080 
  ```
  
- 数据库源配置

  ```properties
  # 数据库源配置
  spring.datasource.username=root
  spring.datasource.password=123456
  spring.datasource.url=jdbc:mysql://localhost:3306/boottest?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  ```

  > `com.mysql.cj.jdbc.Driver`需要时区，`com.mysql.jdbc.Driver`不需要时区

- mybatis配置

  ```properties
  # mybatis配置
  	# 配置Mapper.xml的位置
  mybatis.mapper-locations=classpath:mapping/*Mapper.xml
  	# 配置返回类型所在的包（一般为entity）
  mybatis.type-aliases-package=com.checker.springdemo.entity
  ```

### XxxApplication

- 扫描Mapper接口

  ```java
  @MapperScan("com.checker.springdemo.mapper")//使用MapperScan批量扫描所有的Mapper接口；
  ```

