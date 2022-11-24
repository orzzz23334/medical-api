# 环保项目后端

### 技术
+ spring boot
+ maven
+ spring security
+ neo4j
+ mybatis

### 项目模块划分
+ environmental-kg 知识图谱相关模块
+ environmental-main 项目整体配置模块
+ environmental-web 权限管理模块

### 项目启动
项目启动类 environmental-main/src/main/java/com/bupt/EnvironmentalApplication

### 项目打包
进入environmental-main模块，先 `clean` 一下，再 `package` 。
打包完成会在target目录下生成一个 `environmental-main.jar` 的文件。

### 项目部署
将打好的jar包上传到服务器(h5, /data/sdb1/yxy/environmental)。使用 `java -jar environmental-main.jar` 指令运行jar包。  
推荐使用 `supervisor` 运行后端，`supervisor` 用 `root` 账户部署好了。配置文件在 `/etc/supervisor/conf.d` 下。  

####常用指令
+ 使用 `sudo supervisorctl restart environmental-api` 重启进程。
+ 使用 `sudo supervisorctl status` 查看进程状态。
+ 使用 `sudo supervisorctl start environmental-api` 启动进程。
+ 使用 `sudo supervisorctl stop environmental-api` 关闭进程。


> 注：每次项目修改以后需要重新打包，上传服务器，部署。否则不生效。