<h1 style="text-align: center"> 显施RFID系统</h1>



#### 项目简介
一个基于 Spring Boot 2.1.0 、 Spring Boot Jpa、 JWT、Spring Security、Redis、Vue的前后端分离的RFID系统


#### 项目结构
项目采用按功能分模块开发方式，将通用的配置放在公共模块，```system```模块为系统核心模块也是项目入口模块，```logging``` 模块为系统的日志模块，```generator``` 为系统的代码生成模块

- xs-common 公共模块
    - annotation 为系统自定义注解
    - aspect 自定义注解的切面
    - base 提供了Entity、DTO基类和mapstruct的通用mapper
    - config 自定义权限实现、redis配置、swagger配置
    - exception 项目统一异常的处理
    - utils 系统通用工具类
- xs-system 系统核心模块（系统启动入口）
	- config 配置跨域与静态资源，与数据权限
	    - thread 线程池相关
	- modules 系统相关模块()
	    - store  RFID 功能模块（nf仓项目）
- xs-logging 系统日志模块
- xs-generator 系统代码生成模块


#### 历史记录
- 3/8 后台框架初步搭建完成
- 0309 完成任务，资产，仓库，设备，库门的业务功能
- 0310 新增JNA 模块，和java 直接调用串口模块（初步已证可行，待验证读写速度）
- 0313 数据库新增接收器表，和激励器表，便以扩展，提高系统的可扩展性；后台新增接收器，激励器，接收器配置的业务处理



