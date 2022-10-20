# 在线学习平台系统
![https://img.shields.io/badge/license-Apache%202.0-blue.svg?longCache=true&style=flat-square](https://img.shields.io/badge/license-Apache%202.0-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/springboot-2.1.7-yellow.svg?longCache=true&style=flat-square](https://img.shields.io/badge/springboot-2.1.7-yellow.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/shiro-1.4.1-orange.svg?longCache=true&style=flat-square](https://img.shields.io/badge/shiro-1.4.1-orange.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/vue-2.6.10-brightgreen.svg?longCache=true&style=flat-square](https://img.shields.io/badge/vue-2.6.10-brightgreen.svg?longCache=true&style=flat-square)


基于Vue+SpringBoot+Mybatis Plus+MySql+UniApp的前后端分离在线学习平台系统，包含PC端与移动端


### 更多项目展示内容详见用户手册。
##  :sparkling_heart:[B站关注：嘉然今天吃什么](https://space.bilibili.com/672328094?from=search&seid=959638900625341813&spm_id_from=333.337.0.0) :sparkling_heart:
##  :sparkling_heart:[关注嘉然，炖炖解馋](https://space.bilibili.com/672328094?from=search&seid=959638900625341813&spm_id_from=333.337.0.0) :sparkling_heart:



### 1. 快速启动

#### 后端

1. IDEA安装lombok插件
2. 新建MySQL数据库，导入[SQL](/sql/lpf.sql)文件
3. 导入[后端项目](/platform_sys)
4. 修改数据库配置，redis配置，下载Maven依赖
5. 启动项目

#### 前端

1. 安装node.js
2. 切换到前端项目文件夹下
```
# 安装依赖
npm install 
# 启动
npm run serve
```
#### 移动端
```
推荐使用Hbuilder启动
```
### 2. 项目结构
> Learning_platform
>> platform PC端前端

>> platform_app 移动端前端

>> platform_sys 后端

>> sql 数据库文件

### 3. 项目特点
* 支持验证注册，包括邮箱验证与手机号验证（手机号验证需购买阿里云服务）
* 有学生(student)、教师(teacher)、管理员(admin)三种角色
* 推荐算法，根据用户行为（浏览秒数等）拟合数据来推荐用户感兴趣的课程
* 支持文件上传，视频播放等功能

### 4. 项目图示
* 登录注册

![image](https://user-images.githubusercontent.com/41564919/196928617-9e64d306-0550-4e2f-93db-9b205f994b5f.png)

* 完善个人信息

![image](https://user-images.githubusercontent.com/41564919/196928832-f88d8646-061b-4924-bdea-e62f06513730.png)

* 系统首页

![image](https://user-images.githubusercontent.com/41564919/196929106-66d4dfec-1172-409e-96f6-dc8e65aed6cd.png)

![image](https://user-images.githubusercontent.com/41564919/196929131-504530bd-449a-4a30-bbc3-d573753ad8a6.png)

* 分类与搜索

![image](https://user-images.githubusercontent.com/41564919/196929256-0c7abd33-d430-416f-ab30-9ae29bd76ce0.png)

* 修改个人信息

![image](https://user-images.githubusercontent.com/41564919/196929337-c500e167-435b-454c-b76f-f5c366521cec.png)

* 具体分类课程

![image](https://user-images.githubusercontent.com/41564919/196929437-f29f4317-d151-43c2-91ed-377f7ab6da59.png)

* 课程详情

![image](https://user-images.githubusercontent.com/41564919/196929536-a3c32727-ac5a-4c7b-927b-7a7d97d11307.png)

* 移动端登录

![image](https://user-images.githubusercontent.com/41564919/196929786-4947224e-3919-4f2e-9352-cfb0aedfed7e.png)

* 移动端首页

![image](https://user-images.githubusercontent.com/41564919/196929888-1a9dfcff-63d0-4a97-ad3f-11b92152bc62.png)

### 鸣谢

之前课设的项目，现在把文档了完善一下。如有帮助，万分荣幸。






