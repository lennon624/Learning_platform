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


### 2. 项目结构
> Learning_platform
>> platform PC端前端

>> platform_app 移动端前端

>> platform_sys 后端

>> sql 数据库文件

### 3. 项目特点
* 支持验证注册，包括邮箱验证与手机号验证（手机号验证需购买阿里云服务）
* 有学生(student)、教师(teacher)、管理员(admin)三种角色
* 推荐算法，根据用户行为拟合数据来推荐用户感兴趣的课程
* 支持文件上传，视频播放等功能







