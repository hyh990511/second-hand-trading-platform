# 校园二手交易平台

## 运行前准备

### 数据库准备
* 导入目录下的Second-hand Trading Platform.sql文件到数据库中

* 打开backend/src/main/resources文件夹下的application.yml文件进行数据库配置(默认数据库端口为3307, username为root，密码为123456)

### 后端运行要求
* 安装spring boot所需环境及maven,在IDE中运行backend/src/main/java/com/backend/BackendApplication该文件

### 管理端前端运行要求
* 下载安装node.js后控制台进入frontend\Administrator文件夹

* 运行 `npm install` 命令安装模块（第一次操作后可忽略此操作）

* 运行 `cnpm i element-ui -S` 命令安装 `element-ui` 框架（第一次操作后可忽略此操作）

* 运行 `vue add axios` 安装axios（第一次操作后可忽略此操作）

* 运行 `npm run serve` 命令启动服务

### 用户端前端运行要求
* 下载安装node.js后控制台进入frontend\CommonUser文件夹

* 运行 `npm install` 命令安装模块（第一次可忽略此操作）

* 运行 `cnpm i element-ui -S` 命令安装 `element-ui` 框架（第一次操作后可忽略此操作）

* 运行 `vue add axios` 安装axios（第一次操作后可忽略此操作）

* 运行 `npm run serve` 命令启动服务

## 项目登录账号
* 管理端预置超管账号：1999，密码为：1999，其余管理员账号自行申请

* 用户端账号可在项目运行后通过注册页面进行注册(目前仅支持@fzu.edu.cn邮箱进行注册，使用其它邮箱请自行修改项目代码) 。目前有一个预留用户账号：22170226 密码：suoa2536

* 验证码的发送使用的是我的QQ邮箱，建议在application.yml配置其他邮箱

## 项目后端部分接口秘钥部分
* 项目使用七牛云图片上传接口，相关对象存储空间将于毕业设计答辩结束后关闭，在/service/Impl/UploadPictureServiceimpl文件里修改ak与sk秘钥、对象空间名bucket、外链域名qiniuUrl
* 项目使用百度云文本审核API，相关百度云应用将于毕业设计答辩结束后关闭，在/service/BaiduAI/AuthService文件修改ak与sk秘钥

*系统所有解释权归黄于晗本人所有，有发现问题请联系我的QQ邮箱（1061919896@qq.com）
