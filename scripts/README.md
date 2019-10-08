# 构建脚本

## 介绍

主要关注三个主要脚本，其他子脚本作为依赖被这三个脚本管理，分别是

- `app.gradle` 完成 `app` 模块配置
- `module.gradle` 完成 `module` 模块配置
- `project.gradle` 完成 `project` 配置

主要功能

- 简化 `module` 编译配置，把通用的配置提出，只做个性化配置；
- 简化依赖管理，依赖、版本统一管理，升级、版本控制都变得更简单；
- 版本自动化，使用 `git` 自动生成 `versionName` 和 `versionCode`，助力可持续集成；
- 组件化管理，借助脚本实现更可控的组件化配置；
- 代码规范检测，禁止不符合规则的代码提交到远端；


## 构建脚本的使用

详细可以参考 `project/` 中的例子

> `app` 使用

```gradle
ext.params = [
        applicationId : "com.zfy.mantis.sample",
        resourcePrefix: "index_"
]
apply from: '../scripts/app.gradle'
```

> `module` 使用

```gradle
apply from: '../scripts/module.gradle'
```

> `project` 使用

```gradle
apply from: "scripts/project.gradle"
```