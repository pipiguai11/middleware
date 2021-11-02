### 搭建中间件实现demo
此项目主要目的是熟悉常见的主流消息队列的框架，如RocketMQ、RabbitMQ、Kafka等等

### RocketMQ 注意事项
1、默认未开启sql过滤
    需要修改broker的配置，添加enablePropertyFilter=true