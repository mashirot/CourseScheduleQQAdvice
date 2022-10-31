# CourseScheduleQQAdvice
CourseSchedule的配套QQ通知插件，基于Mirai2.0

需要配合 Mirai 插件 [chat-command](https://github.com/project-mirai/chat-command) 使用
需要搭配 [ClassSchedule-Rebuild](https://github.com/mashirot/ClassSchedule-Rebuild) 使用

## 指令
- /user bind 学号 apitoken
- /user unbind 学号
- /course all 所有课程
- /course eff 所有有效课程
- /course today 今日有效课程
- /course next 下一节课
- /admin add 增加白名单
- /admin del 删除白名单
- /admin reload 重载配置文件

## 注意事项
需要修改Config中的Bot和Owner为机器人QQ和自己的QQ

只支持一个QQ绑定一个User
