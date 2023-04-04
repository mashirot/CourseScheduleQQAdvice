# CourseScheduleQQAdvice
CourseSchedule的配套QQ通知插件，基于Mirai2.0

需要配合 Mirai 插件 [chat-command](https://github.com/project-mirai/chat-command) 使用

需要搭配 [ClassSchedule](https://github.com/mashirot/CourseSchedule) 使用

## 指令
- /user bind [学号] apitoken
- /u unbind [学号]
- /course all 所有课程
- /c eff 所有有效课程
- /c t 今日有效课程
- /c nd 明日有效课程
- /c n 下一节课
- /c rt 刷新课程队列
- /admin add [QQ] 增加白名单
- /admin del [QQ] 删除白名单
- /admin setLeadTime [time] 设置提前提醒时间
- /admin setUrl [url] 设置接口地址
- /admin reload 重载配置文件

## 注意事项
需要修改 config.yml 中的 bot 和 owner 为机器人QQ和自己的QQ

只支持一个QQ绑定一个User
