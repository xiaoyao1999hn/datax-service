# datax-service  111
针对datax进行2次开发，实现data 以rpc的方式传递json配置调用推数服务，同时修复datax多处bug。项目中也引入nacos作为服务的配置中心和注册中心； 同时项目内扩展了kafkawriter，rabbitmqwriter，esreader，hivereader。增强了hdfs插件，支持分区表推送，支持动态参数传递（例如时间实现自增式抽取）。具体使用方式可以参照example模块。目前该服务已经稳定服务某上市公司半年，累计总任务数100+ ，日推送数据过10亿。具体如何使用，如何做插件开发以及datax底层原理，请关注https://blog.csdn.net/xiaoyao1999hn
