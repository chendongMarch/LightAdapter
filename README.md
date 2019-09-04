


![](https://cdn1.showjoy.com/shop/images/20190904/9HTXBS92UUT4GOTN2PM21567587709442.jpeg)

# LxAdapter


`LxAdapter` **轻量** 、 **面向业务** 为主要目的，一方面希望可以快速、简单的的完成数据的适配工作，另一方面针对业务中经常出现的场景能提供统一、简单的解决方案。

> LxAdapter 是我做通用适配器的第三次重构版本，尝试了很多种方案，这次摒弃了很多复杂的东西，回归简单，希望会越来越好；

> [本文博客地址](http://zfyx.coding.me/article/1632666977/)

> [GitHub - LxAdapter](https://github.com/chendongMarch/LxAdapter)

> com.zfy:lxadapter:2.0.0

<!--more-->

<div style="width:100%;display:flex;height:30px;">

<img style="margin-right:20px;"  src="https://img.shields.io/github/stars/chendongMarch/LxAdapter.svg"/>

</div>

## Feature

- 使用`LxAdapter` 完成单类型、多类型数据适配；
- 使用 `LxVH` 作为 `ViewHolder` 进行数据绑定；
- 使用 `LxModelList` 作为数据源，基于 `DiffUtil` 并自动完成数据比对和更新；
- 使用 `LxItemBind` 完成每种类型的数据绑定和事件处理；
- 使用 `LxComponent` 完成分离、易于扩展的扩展功能，如果加载更多等；
- 支持针对每种数据类型，进行细粒度的配置侧滑、拖拽、顶部悬停、跨越多列、动画等效果；
- 支持单击事件、双击事件、长按事件；
- 支持自动检测数据更新的线程，避免出现在子线程更新数据的情况；
- 支持使用 `payloads` 实现有效更新；
- 支持自定义类型，可扩展实现 `Header/Fooer/Loading/Empty/Fake` 等场景效果；
- 支持列表顶部、列表底部，预加载更多数据；
- 支持快速实现选择器效果，单选、多选、滑动选中。
- 支持 `ItemAnimator` / `BindAnimator` 两种方式实现添加布局动画。
- 支持借助 `SnapHelper` 快速实现 `ViewPager` 效果；

## 设计分析

1. 数据源统一使用 `LxModelList`，内部借助 `DiffUtil` 实现数据的自动更新，当需要更改数据时，只需要使用它的内部方法即可；
2. 每种类型时完全分离的，使用 `LxItemBind` 来描述如何对该类型进行数据的绑定，事件的响应，以此来保证每种类型数据绑定的可复用性，已经类型之间的独立性；
3. 拖拽、侧滑、`Snap` 使用、动画、选择器、加载更多，这些功能都分离出来，每个功能由单独的 `component` 负责，这样职责更加分离，需要时注入指定的 `component` 即可，也保证了良好的扩展性；


## 内置的数据类型

### TypeOpts

他用来标记一种类型及其附加的相关属性，具体可以看下面的注释说明；

```java
public class TypeOpts {

    public            int viewType = Lx.VIEW_TYPE_DEFAULT; // 数据类型
    @LayoutRes public int layoutId; // 布局资源
    public            int spanSize = Lx.SPAN_NONE; // 跨越行数

    public boolean enableClick     = true; // 是否允许点击事件
    public boolean enableLongPress = false; // 是否允许长按事件
    public boolean enableDbClick   = false; // 是否允许双击事件

    public            boolean enableDrag  = false; // 是否允许拖动
    public            boolean enableSwipe = false; // 是否允许滑动
    public            boolean enableFixed = false; // 钉住，支持悬停效果

    public BindAnimator bindAnimator;
}
```

## 基础：LxGlobal

设置图片加载全局控制：

```java
LxGlobal.setImgUrlLoader((view, url, extra) -> {
    Glide.with(view).load(url).into(view);
});
```

设置全局事件拦截，这部份详细的会在下面 **事件发布** 一节说明：

```java
LxGlobal.addOnAdapterEventInterceptor((event, adapter, extra) -> {
    // 定义全局公共事件，用来清空数据
    if ("CLEAR_ALL_DATA".equals(event)) {
        adapter.getData().updateClear();
    }
    // 返回 true, 事件将不会往下分发
    return true;
});
```


## 基础：LxAdapter

一般适配器的使用会有单类型和多类型的区分，不过单类型也是多类型的一种，`LxAdapter` 是面向类型的，因此不需要过多的关注单类型和多类型，或者说这里只有多类型；

```java
LxModelList models = new LxModelDiffList();
LxAdapter.of(models)
        // 这里指定了两个类型的数据绑定
        .bindItem(new StudentItemBind(), new TeacherItemBind())
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));

// 以下是为 Adapter 更新数据，应该在下面讲解，放在这里是为了让整个流程看起来更加完整
// 假设我们获取到了数据
List<Student> students = ListX.range(count, index -> new Student(index + " " + System.currentTimeMillis()));
// 数据打包成 LxModel 类型
List<LxModel> tempList = LxTransformations.pack(TYPE_STUDENT, students);
// 发布更新
models.update(tempList);
```

## 基础：LxItemBind

既然是面向类型的，那么每种类型的数据绑定会单独处理，这些由 `LxItemBind` 负责；

```java
// 自增的数据类型，不需要自己去定义 1、2、3
public static final int TYPE_STUDENT = Lx.incrementViewType();

class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        // 指定类型和布局文件
        super(TypeOpts.make(TYPE_STUDENT, R.layout.item_squire1));
    }
    // 在这里完成数据的绑定
    @Override
    public void onBindView(LxVh holder, Student data, LxModel model, int position, @NonNull List<String> payloads) {
        holder.setText(R.id.title_tv, "学：" + data.name);
    }
    // 在这里完成事件的分发
    @Override
    public void onEvent(LxContext context, Student data, LxModel model, int eventType) {

    }
}
```

## 基础：LxList

`LxList` 内部基于 `DiffUtil` 实现，辅助完成数据的自动比对和更新，彻底告别 `notify`；

基于 `LxList` 封装了 `LxModelList` 专门给 `LxAdapter` 使用，`LxModelList` 是 `LxAdapter` 的数据源，本质上是 `List` 对象，继承关系如下：

```bash
LxList -> LxModelList -> LxModelDiffList
```

以下是 `LxList` 内置的各种方法 `updateXXX()`，基本能满足开发需求，另外也可以使用 `snapshot` 获取快照，然后自定义扩展操作；

```java
LxList list = new LxModelDiffList();
// 内部使用异步实现，避免阻塞主线程
// LxList<LxModel> list = new LxAsyncDiffList<>();
List<LxModel> newList = new ArrayList<>();
LxModel item = new LxModel(new Student("name"));
// 添加元素
list.updateAdd(item);
list.updateAdd(0, item);
// 添加列表
list.updateAddAll(newList);
list.updateAddAll(0, newList);
// 更改第 1 个元素
list.updateSet(0, data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});
// 遍历列表，找到符合规则的元素，执行 set 操作
list.updateSet(data -> {
    Student stu = data.unpack();
    return stu.id > 10;
}, data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});
// 遍历列表，执行 set 操作
list.updateSet(data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});
// 清空列表
list.updateClear();
// 删除元素
list.updateRemove(item);
list.updateRemove(0);
// 删除符合规则的元素
list.updateRemove(data -> {
    Student stu = data.unpack();
    return stu.id > 0;
});
list.updateRemove(10, true, data -> {
    Student stu = data.unpack();
    return stu.id > 10;
});
// 获取列表快照, 删除第一个元素, 发布更新
List<LxModel> snapshot = list.snapshot();
snapshot.remove(0);
list.update(newList);
```

同时，`LxModelList` 专门给 `LxAdapter` 使用，扩展了获取类型数据的方法，这部分可以结合后面多类型的介绍来看：

```java
LxModelList list = new LxModelDiffList();
// 获取内容类型的数据
list.getContentTypeData();
// 获取指定类型的数据
list.getCustomTypeData(Lx.VIEW_TYPE_HEADER);
```

## 基础：LxVH

为了支持同时对多个控件进行一样的绑定操作，可以使用 `Ids` 来包含多个 `id`

```java
holder.setText(Ids.all(R.id.test_tv, R.id.tv_count), "new text");
```

为了更优雅的绑定数据显示，扩展了 `ViewHolder` 的功能，现在支持如下绑定方法

```java
holder
        // 设置 visibility
        .setVisibility(R.id.tv, View.VISIBLE)
        // 同时对多个控件设置 visibility
        .setVisibility(Ids.all(R.id.tv, R.id.tv_count), View.GONE)
        // 对多个控件设置某种显示状态
        .setVisible(R.id.tv, R.id.tv_count)
        .setGone(R.id.tv, R.id.tv_count)
        .setInVisible(R.id.tv, R.id.tv_count)
        // 通过 bool 值切换两种显示状态
        .setVisibleGone(R.id.test_tv, true)
        .setVisibleInVisible(R.id.test_tv, false)
        // 设置 select
        .setSelect(R.id.tv, true)
        .setSelectYes(R.id.tv_count, R.id.test_tv)
        .setSelectNo(R.id.tv_count, R.id.test_tv)
        // 设置 checked
        .setChecked(R.id.tv, true)
        .setCheckedNo(R.id.tv_count, R.id.test_tv)
        .setCheckedYes(R.id.tv_count, R.id.test_tv)
        // 设置背景
        .setBgColor(R.id.test_tv, Color.RED)
        .setBgColorRes(R.id.test_tv, R.color.colorPrimary)
        .setBgDrawable(R.id.test_tv, new ColorDrawable(Color.RED))
        .setBgRes(R.id.test_tv, R.drawable.wx_logo)
        // 设置文字颜色
        .setTextColor(R.id.test_tv, Color.RED)
        .setTextColorRes(R.id.test_tv, R.color.colorPrimary)
        // 设置文字
        .setText(R.id.test_tv, "test", true)
        .setTextRes(R.id.test_tv, R.string.app_name)
        // 设置图片
        .setImage(R.id.test_tv, R.drawable.wx_logo)
        .setImage(R.id.test_tv, new ColorDrawable(Color.RED))
        .setImage(R.id.test_tv, BitmapFactory.decodeFile("test"))
        .setImage(R.id.test_tv, "http://www.te.com/1.jpg")
        // 给 itemView 设置 LayoutParams
        .setLayoutParams(100, 100)
        // 给指定控件设置 LayoutParams
        .setLayoutParams(R.id.test_tv, 100, 100)
        // 点击事件，会发送到 Adapter#ChildViewClickEvent
        .setClick(R.id.test_tv)
        // 点击事件，直接设置 listener
        .setClick(R.id.test_tv, view -> {
            ToastX.show("点击事件");
        })
        // 长按事件，会发送到 Adapter#ChildViewLongPressEvent
        .setLongClick(R.id.test_tv)
        // 长按事件，直接设置 listener
        .setLongClick(R.id.test_tv, view -> {
            ToastX.show("长按事件");
            return true;
        })
        // 设置长按触发拖拽事件
        .dragOnLongPress(R.id.tv)
        // 设置触摸触发拖拽事件
        .dragOnTouch(R.id.tv)
        // 设置长按触发侧滑事件
        .swipeOnLongPress(R.id.tv)
        // 设置触摸触发侧滑事件
        .swipeOnTouch(R.id.tv);
```


## 基础：点击事件

点击事件需要在 `TypeOpts` 手动开启，单击事件默认是开启的；
重写 `onEvent` 方法，根据 `eventType` 的不同，对不同事件进行处理；


```java
class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        // 指定类型和布局文件
        super(TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;
            opts.enableClick = true; // 开启单击，默认开启
            opts.enableLongPress = true; // 开启长按，默认不开启
            opts.enableDbClick = true; // 开启双击，默认不开启
        }));
    }
    // 在这里完成数据的绑定
    @Override
    public void onBindView(LxVh holder, Student data, LxModel model, int position, @NonNull List<String> payloads) {
        holder.setText(R.id.title_tv, "学：" + data.name)
                // 给控件加点击事件
                .setClick(R.id.title_tv, v -> {
                });
    }
    // 在这里完成事件的分发
    @Override
    public void onEvent(LxContext context, Student data, LxModel model, int eventType) {
        switch (eventType) {
            case Lx.EVENT_CLICK:
                // 单击
                break;
            case Lx.EVENT_LONG_PRESS:
                // 长按
                break;
            case Lx.EVENT_DOUBLE_CLICK:
                // 双击
                break;
        }
    }
}
```

## 基础：扩展自定义类型

开发过程中，我们通常会有一些特殊类型的数据，比如:

- `Header`
- `Footer`
- `空载页`
- `骨架屏`
- `Loading`

作为一个框架来说，无法完全覆盖这些业务场景，而且比较常见的使用 `inflate view` 添加的方式并不友好，容易出错，也丧失了一些特性；

所以我们把数据分为两种，一种称为内容类型数据，一种称为扩展类型数据，它们大概以如下方式排列：

- Header1
- Header2
- 学生（内容类型1）
- 老师（内容类型2）
- 学生（内容类型1）
- 老师（内容类型2）
- Footer1
- Footer2
- Loading

主要问题在于添加到数据列表中的其他类型数据污染了我们的内容列表，我们无法分离出真正的业务数据列表，这样就使得数据的更新、处理变得很困难，为了解决这个问题我们需要明确，哪些是内容，那些是其他；

```java
public static final int TYPE_STUDENT = Lx.incrementViewType();
public static final int TYPE_TEACHER = Lx.incrementViewType();

LxModelList models = new LxModelDiffList();
LxAdapter.of(models)
        // 指定老师、学生类型，是我们的业务类型，其他的是扩展类型
        .contentType(TYPE_STUDENT, TYPE_TEACHER)
        // 这里指定了 5 种类型的数据绑定
        .bindItem(new StudentItemBind(), new TeacherItemBind(),
                new HeaderItemBind(),new FooterItemBind(),new EmptyItemBind())
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

添加数据

```java
List<LxModel> snapshot = models.snapshot();
// 添加两个 header
snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header1")));
snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header2")));
// 交替添加 10 个学生和老师
List<Student> students = ListX.range(10, index -> new Student());
List<Teacher> teachers = ListX.range(10, index -> new Teacher());
for (int i = 0; i < 10; i++) {
    snapshot.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
    snapshot.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
}
// 添加两个 footer
snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer1")));
snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer2")));
// 发布数据更新
models.update(snapshot);
```

更新数据

```java
class StudentItemBind extends LxItemBind<Student> {

    // ... 省略部分代码

    @Override
    public void onEvent(LxContext context, Student data, LxModel model, int eventType) {
        switch (eventType) {
            case Lx.EVENT_CLICK:
                // 获取内容类型，这里面只包括了学生和老师的数据
                // 这样我们就可以愉快的操作业务类型数据了，不用管什么 Header/Footer
                LxList contentTypeData = getData().getContentTypeData();
                // 删除第一个吧
                contentTypeData.updateRemove(0);
                break;
            case Lx.EVENT_LONG_PRESS:
                // 获取 header，会把顶部的两个 header 单独获取出来
                LxList headerData = getData().getCustomTypeData(Lx.VIEW_TYPE_HEADER);
                // 更新 header
                headerData.updateSet(0, d -> {
                    CustomTypeData firstData = d.unpack();
                    firstData.desc = "新设置的";
                });

                // 获取 footer，会把底部的两个 footer 单独获取出来
                LxList footerData = getData().getCustomTypeData(Lx.VIEW_TYPE_FOOTER);
                // 清空 footer
                footerData.updateClear();
                break;
            case Lx.EVENT_DOUBLE_CLICK:
                // 更新当前这一个数据
                LxList list = getData();
                list.updateSet(context.position, d -> {
                    Student unpack = d.unpack();
                    unpack.name = String.valueOf(System.currentTimeMillis());
                });
                break;
        }
    }
}
```

我们发现，添加和更改每种特殊的类型，是非常方便的，没有针对性的去做 `Header` `Footer` 这些固定的功能，其实它们只是数据的一种类型，可以按照自己的需要做任意的扩展，这样会灵活很多，其他的比如骨架屏、空载页、加载中效果都可以基于这个实现；


## 功能：事件发布

比如如下场景，`Presenter` 层持有数据源，网络加载数据，数据加载完成发现没有更多数据了，列表底部此时要展示一条 ‘没有更多数据～’ 的文案，而我们的 `Adapter` 在 `UI` 层，这个更新变得非常困难；

开发过程中，我们通常会对业务分层，数据层和视图层通常是分离的，数据层改变从而影响视图层变化，并且这部分逻辑最好是可以抽离的，但实际场景下不是所有的操作都能完美的映射到数据变化上面，因此需要事件发布机制；

使用事件，这个过程将会变得很方便而且容易提取为单独的业务逻辑：

```java
// 定义事件拦截器
OnAdapterEventInterceptor interceptor = (event, adapter, extra) -> {
    LxModelList lxModels = adapter.getData();
    // 隐藏 loading 条目
    if ("HIDE_LOADING".equals(event)) {
        LxList customTypeData = lxModels.getCustomTypeData(Lx.VIEW_TYPE_LOADING);
        customTypeData.updateClear();
    }
    // 返回 true 表示停止事件的传递
    return true;
};

// 全局注入，会对所有 Adapter 生效
LxGlobal.addOnAdapterEventInterceptor(interceptor);
// 对 Adapter 注入，仅对当前 Adapter 生效
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        .onEvent(interceptor)
        .attachTo(mRecyclerView, new LinearLayoutManager(getContext()));
// 直接在数据层注入，会对该数据作为数据源的 Adapter 生效
models.addInterceptor(interceptor);
```

当我们数据更新完成时，需要发布事件：

```java
// 数据源
LxModelList models = new LxModelDiffList();

// 发布隐藏 Loading 事件
models.publishEvent("HIDE_LOADING");
```

内置了两个事件，可以直接使用以后看需求扩展：

```java
// 结束底部加载更多
models.publishEvent(Lx.EVENT_FINISH_END_EDGE_LOAD_MORE)
// 结束顶部加载更多
models.publishEvent(Lx.EVENT_FINISH_START_EDGE_LOAD_MORE)
```

## 功能：跨越多列（Span）

当使用 `GridLayoutManager` 布局时，可能某种类型需要跨越多列，需要针对每种类型进行指定；

```java
class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        // 指定类型和布局文件
        super(TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;
            // 使用内置参数，跨越所有列
            opts.spanSize = Lx.SPAN_SIZE_ALL;
            // 使用内置参数，跨越总数的一半
            opts.spanSize = Lx.SPAN_SIZE_HALF;
            // 使用固定数字，跨越 3 列
            opts.spanSize = 3;
        }));
    }
    // ...
}
```

## 功能：加载更多（LoadMore）

加载更多功能由 `LxStartEdgeLoadMoreComponent` 和 `LxEndEdgeLoadMoreComponent` 承担，可以选择性的使用它们；

```java
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 顶部加载更多，提前 10 个预加载
        .component(new LxStartEdgeLoadMoreComponent(10, comp -> {
            // 在这里做网络请求，完成后调用 finish 接口
            comp.finishLoadMore();
        }))
        // 底部加载更多，提前 6 个预加载
        .component(new LxEndEdgeLoadMoreComponent(6, comp -> {
            // 在这里做网络请求，完成后调用 finish 接口
            comp.finishLoadMore();
        }))
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

## 功能：选择器（Selector）

主要用于在列表中实现选择器的需求，单选、多选、状态变化等业务场景;

这部分功能交给 `LxSelectComponent`


```java
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 多选
        .component(new LxSelectComponent(Lx.SELECT_MULTI))
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
// 获取选择后的结果
List<Student> result = models.filterTo(LxModel::isSelected, LxModel::unpack);
```

滑动选中：使用 `LxSlidingSelectLayout` 包裹 `RecyclerView` 会自动和 `LxSelectComponent` 联动实现滑动选中功能；

```xml
<com.zfy.adapter.decoration.LxSlidingSelectLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:id="@+id/content_rv"
        android:layout_height="match_parent"/>
</com.zfy.adapter.decoration.LxSlidingSelectLayout>
```

自定义手动选中：主要用于当点击某个按钮自定义触发选中时使用：

```java
class StudentItemBind extends LxItemBind<Student> {
    // ...
    @Override
    public void onBindView(LxVh holder, Student data, LxModel model, int position, @NonNull List<String> payloads)
        // 点击标题, 触发选择器选中
        holder.setClick(R.id.title_tv, v -> {
            LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
            if (component != null) {
                component.select(model);
            }
        });
    }
    // ...
}
```

## 功能：列表动画（Animator）

动画分为了两种:

1. 一种是 `BindAnimator`，在 `onBindViewHolder` 里面执行；
2. 一种是 `ItemAnimator`, 是 `RecyclerView` 官方的支持方案；

这部分功能由 `LxBindAnimatorComponent` 和 `LxItemAnimatorComponent` 完成；

### BindAnimator

内置了以下几种，还可以再自定义扩展：

- BindAlphaAnimator
- BindScaleAnimator
- BindSlideAnimator

```java
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 缩放动画
        .component(new LxBindAnimatorComponent(new BindScaleAnimator()))
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

也可以分类型指定动画，每种类型给予不同的动画效果

```java
class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        super(TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;
            // 这种类型单独的动画效果
            opts.bindAnimator = new BindAlphaAnimator();
        }));
    }
    // ...
}
```

### ItemAnimator

这部分参考 [wasabeef-recyclerview-animators](https://github.com/wasabeef/recyclerview-animators) 实现，它可以提供更多动画类型的实现。

```java
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 缩放动画
        .component(new LxItemAnimatorComponent(new ScaleInAnimator()))
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

## 功能：悬挂效果（Fixed）

针对每种类型悬挂效果，可以支持所有类型所有布局文件的顶部悬挂效果，需要使用 `LxFixedComponent` 实现，支持两种实现方式：

- 采用绘制的方式，优点是悬挂的视图有挤压效果，效率上也更好，但是因为是绘制的所以不支持点击事件，可以采用覆盖一层 `View` 来解决这个问题；
- 采用生成 `View` 的方式，优点是实实在在的 `View`，点击事件什么的自然都支持，缺点是你需要提供一个容器，而且视图之间没有挤压的效果；

```java
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 悬挂效果
        .component(new LxFixedComponent())
        // 悬挂效果
        .component(new LxFixedComponent(mMyViewGroup))
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

同时在 `TypeOpts` 中说明哪些类型需要支持悬挂

```java
class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        super(TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;
            // 这种类型单独的动画效果
            opts.enableFixed = true;
        }));
    }
    // ...
}
```

## 功能：拖拽和侧滑(drag/swipe)

针对每种类型支持拖拽和侧滑功能，由 `LxDragSwipeComponent` 完成该功能；

- 关注配置项，配置项决定了该类型的响应行为；
- 支持长按、触摸触发相应的响应；
- 支持全局自动触发和手动触发两种方式；

首先定义拖拽、侧滑得一些配置参数：

```java
public static class DragSwipeOptions {
    public int     dragFlags; // 拖动方向，在哪个方向上允许拖动，默认4个方向都可以
    public int     swipeFlags; // 滑动方向，在哪个方向上允许侧滑，默认水平
    public boolean longPressItemView4Drag = true; // 长按自动触发拖拽
    public boolean touchItemView4Swipe    = true; // 触摸自动触发滑动
    public float   moveThreshold          = .5f; // 超过 0.5 触发 onMoved
    public float   swipeThreshold         = .5f; // 超过 0.5 触发 onSwipe
}
```

然后使用 `LxDragSwipeComponent` 完成拖拽、侧滑功能：

```java
LxDragSwipeComponent.DragSwipeOptions options = new LxDragSwipeComponent.DragSwipeOptions();
// 在上下方向上拖拽
options.dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
// 关闭触摸自动触发侧滑
options.touchItemView4Swipe = false;
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 当侧滑和拖拽发生时触发的时机，可以响应的做高亮效果
        .component(new LxDragSwipeComponent(options, (state, holder, context) -> {
            switch (state) {
                case Lx.DRAG_SWIPE_STATE_NONE:
                    break;
                case Lx.DRAG_STATE_ACTIVE:
                    // 触发拖拽
                    break;
                case Lx.DRAG_STATE_RELEASE:
                    // 释放拖拽
                    break;
                case Lx.SWIPE_STATE_ACTIVE:
                    // 触发侧滑
                    break;
                case Lx.SWIPE_STATE_RELEASE:
                    // 释放侧滑
                    break;
            }
        }))
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

最后在 `TypeOpts` 里面配置该类型是否支持侧滑和拖拽，这样可以灵活的控制每种类型数据的行为：

```java
class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        super(TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;
            opts.enableDrag = true; // 支持拖拽
            opts.enableSwipe = true; // 支持侧滑
        }));
    }
    // ...
}
```

手动触发：使用以上方法会为整个 `item` 设置拖拽和侧滑响应，你可以指定某个控件触发这些操作，为了避免冲突我们现在配置项中关闭自动触发逻辑：

```java
LxDragSwipeComponent.DragSwipeOptions options = new LxDragSwipeComponent.DragSwipeOptions();
// 关闭触摸自动触发侧滑
options.touchItemView4Swipe = false;
// 关闭长按自动触发拖拽
options.longPressItemView4Drag = false;
```

然后在 `onBindView` 时，手动关联触发操作：

```java
class StudentItemBind extends LxItemBind<Student> {
    StudentItemBind() {
        super(TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;
            // 当使用 holder 手动设置时，以下属性会被自动更改，可以不用设置
            // opts.enableDrag = true;
            // opts.enableSwipe = true;
        }));
    }

    @Override
    public void onBindView(LxVh holder, Student data, LxModel model, int position, @NonNull List
        holder
                // 长按标题控件触发拖拽
                .dragOnLongPress(adapter, R.id.title_tv)
                // 触摸标题控件触发拖拽
                .dragOnTouch(adapter, R.id.title_tv)
                // 长按标题控件触发侧滑
                .swipeOnLongPress(adapter, R.id.title_tv)
                // 触摸标题控件触发侧滑
                .swipeOnTouch(adapter, R.id.title_tv);
    }
    // ...
}
```

## 功能：实现 ViewPager (Snap)

内部使用 `SnapHelper` 实现，很简单，只是要把他封装成 `LxComponent` 的形式，统一起来，由 `LxSnapComponent` 实现；

```java
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        // 实现 ViewPager 效果
        .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER))
        // 实现 ViewPager 效果，但是可以一次划多个 item
        .component(new LxSnapComponent(Lx.SNAP_MODE_LINEAR))
        .attachTo(mRecyclerView, new LinearLayoutManager(getContext()));
```

## 进阶：使用 Idable 优化 change

使用 `DiffUtil` 比对数据时，类库不知道它们是不是同一个对象，会使用一个自增的 `ID` 作为唯一标示，以此来触发 `notifyDataSetChange`，所以当你更改列表中的一个数据时，只会执行一次绑定，这是内部做的优化；

这也意味着每次创建对象这个 `ID` 都将改变，也就是说学生A 和 学生A，并不是同一个学生，因为这关系到使用者具体的业务逻辑，不过你可以通过实现 `Idable` 接口来返回你自己的业务 `ID`，当然这不是必须的。

```java
static class Student implements Idable  {

    int    id;
    String name;

    Student(String name) {
        this.name = name;
    }

    @Override
    public Object getObjId() {
        return id;
    }
}
```

## 进阶：使用 payloads

某些场景我们只更改了一部分数据，但是会触发 `notifyDataSetChanged` 重新执行整个条目的绑定，这样会造成性能的损耗，有时图片要重新加载，很不友好，因此我们需要 `payploads` 更新的方式；

`payloads` 可以被称为有效载荷，它记录了哪些数据是需要被更新的， 我们只更新需要的那部分就可以了，既然称为有效载荷那么他肯定是需要比对和计算的，为了实现它需要自定义这个比对规则，我们看下以下比对方法的简单介绍：


- `areItemsTheSame`

> 当返回 true 的时候表示是相同的元素，调用 `areContentsTheSame`，推荐使用 `id` 比对
> 当返回 false 的时候表示是一个完全的新元素，此时会调用 `insert` 和 `remove` 方法来达到数据更新的目的

- `areContentsTheSame`

> 用来比较两项内容是否相同，只有在 `areItemsTheSame` 返回 `true` 时才会调用
> 返回 `true` 表示内容完全相同不需要更新
> 返回 `false` 表示虽然是同个元素但是内容改变了，此时会调用 `changed` 方法来更新数据

- `getChangePayload`

> 只有在 `areItemsTheSame` 返回 `true` 时才会调用，`areContentsTheSame` 返回 `false` 时调用
> 返回更新事件列表，会触发 `payload` 更新

为了实现它，需要对数据对象进行一些更改:

- 实现 `Diffable` 接口，声明比对规则
- 实现 `Copyable` 接口，实现对象的拷贝，如果对象有嵌套，可能需要嵌套拷贝；
- 实现 `Parcelable` 接口，作用同 `Copyable`，写起来简单，但是性能会差一些，二选一即可；

```java
class Student implements Diffable<Student>, Copyable<Student> {
    int    id;
    String name;

    Student(String name) {
        this.name = name;
    }

    @Override
    public Student copyNewOne() {
        Student student = new Student(name);
        student.id = id;
        return student;
    }

    @Override
    public boolean areContentsTheSame(Student newItem) {
        return name.equals(newItem.name);
    }

    @Override
    public Set<String> getChangePayload(Student newItem) {
        Set<String> payloads = new HashSet<>();
        if (!name.equals(newItem.name)) {
            payloads("name_change");
        }
        return payloads;
    }

}
```

这样我们就通过比对拿到了 `payloads`, 那我们如何使用这些有效载荷呢？

```java
class StudentItemBind extends LxItemBind<Student> {
    //...
    @Override
    public void onBindView(LxVh holder, Student data, LxModel model, int position, @NonNull List<String> payloads) {
        if (payloads.isEmpty()) {
            // 没有 payloads 正常绑定数据
            holder.setText(R.id.title_tv, "学：" + data.name);
        } else {
            // 有 payloads 通过 payloads 绑定数据
            for (String payload : payloads) {
                if (payload.equals("name_change")) {
                    holder.setText(R.id.title_tv, "payloads：" + data.name);
                }
            }
        }
    }
    // ...
}
```

