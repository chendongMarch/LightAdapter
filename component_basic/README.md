# 组件化 - 架构层

## Thanks

- [@ButterKnife](https://github.com/JakeWharton/butterknife)
- [@EventBus](https://github.com/greenrobot/EventBus)
- [@ARouter](https://github.com/alibaba/ARouter)
- [@binding-collection-adapter](https://github.com/evant/binding-collection-adapter)
- [@Glide](https://github.com/bumptech/glide)
- [@OkHttp3](https://github.com/square/okhttp)
- [@PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar)
- [@Retrofit](https://github.com/square/retrofit)
- [@Gson](https://github.com/google/gson)
- [@RxJava](https://github.com/ReactiveX/RxJava)
- [@DataBinding](https://developer.android.com/topic/libraries/data-binding/)
- [@Architecture Components](https://developer.android.com/topic/libraries/architecture/)

## Attentions

1. 支持使用 `Kotlin` 编码，但是 `ARouter` 解析使用的是 `annotationProcessor`，所以使用 `Route` 注解的类必须是 `Java` 类

## Directory Structure

- app/
    - build.gradle
- component_biz/
    - build.gradle
- component_mixin/
    - build.gradle
- component_basic/
    - build.gradle
- component_user/
    - build.gradle
- scripts/
    - app.gradle
    - library.gradle
    - project.gradle
    - utils.gradle
- products/
- publish/



## Component Design

![](http://cdn1.showjoy.com/shop/images/20180913/XMEX8EITHWRAROVVSOCM1536818047487.png)

## 架构层(component_basic)

`component_basic` 提供统一的依赖管理、基础服务建设、组件化、`MVVM/MVP` 基础模型搭建等，是完全与业务解耦的模块。

- `Common` 工具类依赖
- `Resource` 业务无关公共资源打包
- `Mvvm` 基础模型
- `ARouter` 依赖注入、路由管理
- `Glide` 图片加载
- `RxJava2 + Retrofit + OkHttp3 + Gson` 网络请求
- `EventBus` 组件通信
- `DataBinding` 数据绑定及扩展
- `Architecture` 组件和生命周期同步


## 基础业务层(component_biz)

`component_biz` 作为项目业务的底层支持，下沉服务和数据结构，统一业务公共资源，与业务层轻度耦合，完成架构层和业务层的分离。

- `Service` 服务接口下沉
- `Model` 服务相关数据结构暴露
- `BizResource` 业务耦合公共资源

## 业务混合层(component_mixin)

`component_mixin` 主要用来解决组件过多的问题，可以承载大多数简单的小服务，比如接入 `SDK` 等。

## 业务层实现层(component_xxx)

具体的业务组件实现


## MVVM Design

![](http://cdn1.showjoy.com/shop/images/20180913/BJVRKONATC6TI33OWI981536817416210.png)
