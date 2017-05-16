#UCenter论坛 
主要用于论坛的登录验证、同步登录和同步登出操作。  
##指南
###添加依赖包
在pom.xml文件中加入依赖的jar包  

    <dependency>
    <groupId>com.huotu.ucphp</groupId> 
    <artifactId>ucphp</artifactId> 
    <version>1.0-snapshot</version>
    </dependency>
###项目配置
在ServiceConfig文件中载入UCenterConfig
###业务逻辑实现
在LoginServiceImpl实现类中增加对UCUserService接口的实现，实现  
* validUser(String username, String password)  
* userCheck(String username)  

分别对接口中定义的登录验证和用户名检验的方法进行扩展。
###密钥获取
密钥在环境中获取，通过  

     environment.getProperty("uCenterKey")
     
获取键为uCenterKey的值即为密钥

