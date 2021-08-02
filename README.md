# community
牛客论坛首次提交

## 项目简介
待完善……


## 项目准备
主要是工具准备以及环境准备
* IDE ：IntelliJ IDEA
* JDK : openjdk version "1.8.0_292"
* Maven : Apache Maven 3.8.1
* elasticsearch-6.4.3 && elasticsearch-analysis-ik-6.4.3(分词工具)
* kafka_2.8.0
* Redis 
* apache-jmeter-5.4.1压力测试工具


## 总体架构
待完善……

## 数据库设计

### 用户表
表名：user

| 字段名称      | 字段代码 | 数据类型     |字段说明     |
| :----:      |    :----:   |       :----:  |       :----:  |
| 主键      | id       | int   | 作为主键|
| 用户名     | username        | varchar(50)      |用户登录名|
| 密码     | password        | varchar(50)      |用户密码|
| 盐     | salt        | varchar(50)      |盐用来MD5加密|
| 邮箱     | email        | varchar(50)      |用户邮箱用来注册用户，验证码等|    
| 类型     | type        | int      |0-普通用户; 1-超级管理员; 2-版主;|
| 状态     | status        | int    |0-未激活; 1-已激活;|
| 激活码     | activation_code        | varchar(100)      |   |
| 头像地址     | header_url        | varchar(50)      |头像图片存放的地址|
| 注册时间     | create_time        | timestamp      |用户注册时间|


### 登录凭证表
表名：login_ticket

| 字段名称      | 字段代码 | 数据类型     |字段说明     |
| :----:      |    :----:   |       :----:  |       :----:  |
| 主键      | id       | int   | 作为主键|
| 用户id     | user_id        | int      |代表一个用户实体|
| 凭证？    |ticket        | varchar(50)      |用户密码|
| 状态     | status        | int      |0-有效; 1-无效;|    
| 过期     | expired        | timestamp      | |

### 评论表
表名：comment

| 字段名称      | 字段代码 | 数据类型     |字段说明     |
| :----:      |    :----:   |       :----:  |       :----:  |
| 主键      | id       | int   | 作为主键|
| 用户id     | user_id        | int      |代表一个用户实体|
| 评论类型   | entity_type      |int     |评论的对象、帖子/评论|
| 评论的id     | entity_id        | int      |0-有效; 1-无效;|    
|      | target_id        | int      | 评论的对象（人）|
|      | content        | text      | 评论内容|
|      | status        | timestamp      | |
|      | create_time        | timestamp      | |

### 私信表
表名：message

| 字段名称      | 字段代码 | 数据类型     |字段说明     |
| :----:      |    :----:   |       :----:  |       :----:  |
| 主键      | id       | int   | 作为主键|
|      | from_id        | int      ||
|    | to_id      |int     ||
|      | conversation_id        | varchar(45)      |会话id|    
|      | content        | text      | 私信内容|
|      | status        | int      | 0-未读;1-已读;2-删除;|
|      | create_time        | timestamp      | |

### 帖子/文章表
表名：discuss_post

| 字段名称      | 字段代码 | 数据类型     |字段说明     |
| :----:      |    :----:   |       :----:  |       :----:  |
| 主键      | id       | int   | 作为主键|
|      | user_id        | varchar(45)      |发布文章的用户|
|    | title      |varchar(100)     |文章的标题|
|      | content        | text      |文章的内容|    
|      | type        | int      | 0-普通; 1-置顶;|
|      | status        | int      | 0-正常; 1-精华; 2-拉黑;|
|      | create_time        | timestamp      | |
|      | comment_count        | int      | |
|      | score        | double      | |


## 论坛系统实现

### 注册与登录功能的实现

### 发布帖子与敏感词过滤

### 发表评论与私信

### 使用Redis实现点赞关注

### Kafka实现异步消息系统

### ElasticSearch实现网站的搜索功能

### Spring Quartz实现定时热帖排行

### 使用本地缓存Caffeine优化网站性能

#### JMeter压力测试

