![](https://cdn1.showjoy.com/shop/images/20190904/9HTXBS92UUT4GOTN2PM21567587709442.jpeg)


# LxAdapter


`LxAdapter` **轻量** 、 **面向业务** 为主要目的，一方面希望可以快速、简单的的完成数据的适配工作，另一方面针对业务中经常出现的场景能提供统一、简单的解决方案。

> LxAdapter 是我做通用适配器的第三次重构版本，尝试了很多种方案，这次摒弃了很多复杂的东西，回归简单，希望会越来越好；

> [GitHub - LxAdapter](https://github.com/chendongMarch/LxAdapter)

> com.zfy:lxadapter:2.0.11

<!--more-->

<div style="width:100%;display:flex;height:30px;">

<img style="margin-right:20px;"  src="https://img.shields.io/github/stars/chendongMarch/LxAdapter.svg"/>

</div>

<span id="top"> </span>



||||||
|:--|:--|:--|:--|:--|
|<img width="130px" src="http://s3.hixd.com/218363.gif"/>|<img width="130px" src="http://s3.hixd.com/218364.gif"/>|<img width="130px" src="http://s3.hixd.com/218365.gif"/>|<img width="130px" src="http://s3.hixd.com/218366.gif"/>|<img width="130px" src="http://s3.hixd.com/218367.gif"/>|
|LxDragSwipeComponent|LxSnapComponent|LxPicker|LxSpaceComponent|LxSelectComponent|
|拖拽，侧滑|SnapHelper效果|滚轮选择器|多类型等距间隔|选择器效果|

---

||||||
|:--|:--|:--|:--|:--|
|<img width="130px" src="http://s3.hixd.com/218368.gif"/>|<img width="130px" src="http://s3.hixd.com/218370.gif"/>|<img width="130px" src="http://s3.hixd.com/218371.gif"/>|<img width="130px" src="http://s3.hixd.com/218372.gif"/>|<img width="130px" src="http://s3.hixd.com/218374.gif"/>|
|LxAnimComponent|LxExpandable|LxFixedComponent|LxLoadMoreComponent|LxNesting|
|动画|分组|悬停|加载更多|垂直嵌套水平滑动，记录位置|


## 目录

- [联系我](#contact)
- [特性](#feature)
- [设计分析](#design)
- [内置的数据类型](#data)
  - [TypeOpts ～ 配置化类型](#typeopts)
  - [LxModel ～ 数据包装](#lxmodel)
  - [LxContext ～ 上下文对象](#lxcontext)
- 基础
  - [基础：LxGlobal ～ 全局配置](#lxglobal)
  - [基础：LxAdapter ～ 适配器](#lxadapter)
  - [基础：数据源 ～ 适配器的数据来源](#data)
  - [基础：LxQuery～ 针对类型的数据更新](#query)
  - [基础：LxItemBind ～ 类型绑定](#itembind)
  - [基础：LxList ～ 数据源，自动更新，告别 notify](#lxlist)
  - [基础：LxViewHolder ～ 扩展 ViewHolder](#LxViewHolder)
  - [基础：点击事件 ～ 单击、双击、长按、焦点](#event)
  - [基础：扩展自定义类型 ～ 灵活扩展](#multitype)
- 功能
  - [功能：事件发布 ～ 将数据更新抽象成事件](#publishevent)
  - [功能：跨越多列（Span）～ 灵活布局](#span)
  - [功能：间隔（Space）～ 多类型布局等距间隔](#space)
  - [功能：加载更多（LoadMore）～ 赋能分页加载](#loadmore)
  - [功能：选择器（Selector）～ 面向选择器业务场景](#selector)
  - [功能：列表动画（Animator）](#animator)
  - [功能：悬挂效果（Fixed）](#fixed)
  - [功能：拖拽和侧滑（Drag/Swipe）](#dragswipe)
  - [功能：实现 ViewPager (Snap) ](#snap)
  - [功能：实现分组列表 (Expandable) ～ 按组划分，展开收起](#expandable)
  - [功能：实现 RecyclerView 嵌套 (Nesting) ～ 嵌套滑动，恢复滑动位置](#nesting)
  - [功能：实现滚轮选择器效果 (Picker) ～ 多级级联滚动，数据异步获取](#picker)
- 进阶
  - [进阶：使用缓存优化绑定性能](#cache)
  - [进阶：使用 Extra 扩展数据](#extra)
  - [进阶：使用条件更新](#condition)
  - [进阶：使用 Idable 优化 change](#idable)
  - [进阶：使用 Typeable 内置类型](#typeable)
  - [进阶：使用有效载荷（payloads）更新 ](#payloads)

<span id="feature"></span>

## 特性

- 使用 `LxAdapter` 构建单类型、多类型数据适配器；
- 使用 `LxItemBinder` 完成每种类型的数据绑定和事件处理；
- 使用 `LxViewHolder` 作为 `ViewHolder` 进行数据绑定；
- 使用 `LxList` 作为数据源，基于 `DiffUtil` 并自动完成数据比对和更新；
- 使用 `LxComponent` 完成分离、易于扩展的扩展功能，如果加载更多等；
- 使用 `TypeOpts` 针对每种数据类型，进行细粒度的配置侧滑、拖拽、顶部悬停、跨越多列、动画等效果；
- 支持单击事件、双击事件、长按事件；
- 支持自动检测数据更新的线程，避免出现在子线程更新数据的情况；
- 支持自定义类型，可灵活扩展实现 `Header/Fooer/Loading/Empty` 等场景效果；
- 支持列表顶部、列表底部，预加载更多数据；
- 支持快速实现选择器效果，单选、多选、滑动选中等。
- 支持 `ItemAnimator` / `BindAnimator` 两种方式实现添加布局动画。
- 支持借助 `SnapHelper` 快速实现 `ViewPager` 效果；
- 支持发布订阅模式的事件抽离，更容易分离公共逻辑；
- 支持使用 `payloads` 实现有效更新；
- 支持使用 `condition` 实现条件更新，按照指定条件更新数据，拒绝无脑刷新；
- 使用 `LxExpandable` 快速实现分组列表；
- 使用 `LxNesting` 快速实现 `RecyclerView` 的嵌套滑动；
- 使用 `LxPicker` 快速实现滚轮选择器效果；

<span id="design"></span>

## 设计分析

1. 数据源统一使用 `LxList`，内部借助 `DiffUtil` 实现数据的自动更新，当需要更改数据时，只需要使用它的内部方法即可；
2. 每种类型是完全分离的，`LxAdapter` 作为一个适配器的容器，实际上使用 `LxItemBinder` 来描述如何对该类型进行数据的绑定，事件的响应，以此来保证每种类型数据绑定的可复用性，以及类型之间的独立性；
3. 拖拽、侧滑、`Snap` 使用、动画、选择器、加载更多，这些功能都分离出来，每个功能由单独的 `component` 负责，这样职责更加分离，需要时注入指定的 `component` 即可，也保证了良好的扩展性；
4. 将类型分为了 **内容类型** 和 **扩展类型** 两种，内容类型一般指的是业务数据类型，扩展类型一般是其他的类型，比如 `Header/Footer` 这种，需要注意的是每种类型、内容类型都需要是连续的。
5. 区块的概念，整个列表被分为多个区块，可以按照区块去更新数据，这样在多种类型的列表中可以灵活的更新某种类型的数据，注意，内容类型归属于一个区块，成为内容区块，扩展类型，每种类型属于一个区块，区块里面的数据必须是连续的；

<span id="data"></span>

## 内置的数据类型

<span id="typeopts"></span>

### TypeOpts

他用来标记一种类型及其附加的相关属性，具体可以看下面的注释说明；

```java
public class TypeOpts {

    public            int viewType = Lx.ViewType.DEFAULT; // 数据类型
    @LayoutRes public int layoutId; // 布局资源
    public            int spanSize = Lx.SpanSize.NONE; // 跨越行数

    public boolean enableClick     = true; // 是否允许点击事件
    public boolean enableLongPress = false; // 是否允许长按事件
    public boolean enableDbClick   = false; // 是否允许双击事件
    public boolean enableFocusChange   = false; // 是否焦点变化事件

    public            boolean enableDrag  = false; // 是否允许拖动
    public            boolean enableSwipe = false; // 是否允许滑动
    public            boolean enableFixed = false; // 钉住，支持悬停效果

    public BindAnimator bindAnimator; // 每种类型可以支持不同的动画效果
}
```

<span id="lxmodel"></span>

### LxModel

`LxAdapter` 的数据类型是 `LxModel`，业务类型需要被包装成 `LxModel` 才能被 `LxAdapter` 使用，获取其中真正的业务数据可以使用 `model.unpack()` 方法；


```java
public class LxModel implements Diffable<LxModel>, Typeable, Selectable, Idable, Copyable<LxModel> {

    private int     incrementId; // 自增ID
    private Object  data; // 内置数据
    private int     type = Lx.ViewType.DEFAULT; // 类型
    private int     moduleId; // 模块ID
    private boolean selected; // 选中

    private Bundle extra; // 数据扩展

    @NonNull
    public Bundle getExtra() {
        if (extra == null) {
            extra = new Bundle();
        }
        return extra;
    }
}
```

<span id="lxcontext"></span>

### LxContext

`LxContext` 是数据绑定过程中的上下文对象，承载了一些附加的数据，易于扩展；

```java
public class LxContext {

    public int          layoutPosition; // 布局中的位置
    public int          dataPosition; // 数据位置
    public int          viewType; // 类型
    public int          bindMode; // 绑定类型
    @NonNull
    public List<String> payloads; // payloads 更新数据
    public String conditionKey; // 条件更新的 key
    @NonNull
    public Bundle conditionValue; // 条件更新的数据
}
```

<span id="lxglobal"></span>

## 基础：LxGlobal

设置图片加载全局控制：

```java
LxGlobal.setImgUrlLoader((view, url, extra) -> {
    Glide.with(view).load(url).into(view);
});
```

设置全局事件处理，这部份详细的会在下面 **事件发布** 一节说明：

```java
public static final String CLEAR_ALL_DATA = "CLEAR_ALL_DATA";

LxGlobal.subscribe(CLEAR_ALL_DATA, (event, adapter, extra) -> {
    adapter.getData().updateClear();
});
```
<span id="lxadapter"></span>

## 基础：LxAdapter

一般适配器的使用会有单类型和多类型的区分，不过单类型也是多类型的一种，数据的绑定使用 `LxItemBinder` 来做，所以 `LxAdapter` 就只作为一个容器， 不再考虑单类型和多类型的问题；

```java
// 构造数据源
LxList list = new LxTypedList();
// Builder 模式
LxAdapter.of(list)
        // 这里指定了两个类型的数据绑定
        .bindItem(new StudentItemBind(), new TeacherItemBind())
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));

// 为 Adapter 更新数据
List<Student> students = ListX.range(count, index -> new Student());
// 数据打包成 LxModel 类型

LxSource source = LxSource.just(TYPE_STUDENT, students);
// 发布更新
list.update(source);
```

<span id="data"></span>

## 基础：数据源

- 数据类型分为两种，内容类型 和 扩展类型；
- 数据源被分为多个区块，内容类型同属于一个区块，扩展类型每种类型属于一个区块；
- 每个区块需要是连续的不能被分隔开；

如下用来理解，区块、类型之间的对应差别：

```
列表流开始
------------------ A-HEADER 区块 开始
- TYPE: A-HEADER
- TYPE: A-HEADER
------------------ A-HEADER 区块 结束
------------------ B-HEADER 区块 开始
- TYPE: B-HEADER
- TYPE: B-HEADER
------------------ B-HEADER 区块 结束
------------------ 内容区块 开始
- 学生（内容类型1）
- 老师（内容类型2）
- 时间隔断（内容类型3）
- 学生（内容类型1）
- 老师（内容类型2）
------------------ 内容区块 结束
------------------ FOOTER 区块 开始
- TYPE: FOOTER
- TYPE: FOOTER
------------------ FOOTER 区块 结束
------------------ LOADING 区块 开始
- TYPE: LOADING
- TYPE: LOADING
------------------ LOADING 区块 结束
列表流结束
```

当声明类型时，同时也决定了两件事情：

- 这个类型是内容类型还是扩展类型
- 类型在列表中相对的排列顺序

```java
// 内容类型1，属于内容区块
public static final int TYPE_TEACHER  = Lx.contentTypeOf();
// 内容类型2，属于内容区块
public static final int TYPE_STUDENT = Lx.contentTypeOf();

// 扩展类型，属于单独的区块，这个类型位置在内容类型后面
public static final int FOOTER = Lx.extTypeAfterContentOf();
// 扩展类型，属于单独的区块，这个类型位置在内容类型后面
// LOADING 在列表中的位置要比 FOOTER 更靠后
// 声明的顺序决定了他们的排序
public static final int LOADING = Lx.extTypeAfterContentOf();

// 扩展类型，属于单独的区块，这个类型位置在内容类型前面
public static final int HEADER  = Lx.extTypeBeforeContentOf();
```

如果只有一种类型，我们建议使用 `LxList`，如果是多类型的需要使用  `LxTypedList`，区别就在于在扩展类型的单独更新上；

```java
// 单类型建议使用 LxList
// 可以直接发布更新，效率也相对更高
LxList list = new LxList();

// 多类型建议使用 LxTypedList
// 数据的更新需要获取指定区块后才能发布更新
LxList list = new LxTypedList();
// 获取扩展类型的数据区块
LxList l1 = list.getExtTypeData(HEADER);
// 获取内容类型的数据区块
LxList l2 = list.getContentTypeData();
```

如何更新数据？

```java
LxList list = new LxTypedList();
// 获取扩展类型的数据区块
LxList l = list.getExtTypeData(HEADER);
// 清空数据
l.updateClear();
```


<span id="itembind"></span>

## 基础：LxItemBinder


`LxAdapter` 是完全面向类型的，每种类型的数据绑定会单独处理，这些由 `LxItemBinder` 负责，这样可以使所有类型绑定更容易复用：

```java
// 自增的数据类型，不需要自己去定义 1、2、3
public static final int TYPE_STUDENT = Lx.contentTypeOf();

// 实现类型绑定
static class StudentItemBind extends LxItemBinder<Student> {

    @Override
    protected TypeOpts newTypeOpts() {
        return TypeOpts.make(TYPE_STUDENT, R.layout.item_squire1);
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, Student data) {

    }

    @Override
    public void onBindEvent(LxContext context, Student listItem, int eventType) {

    }
}
```

也支持使用构建者模式快速创建新的类型绑定：

```java
TypeOpts opts = TypeOpts.make(R.layout.order_item);
LxItemBinder<PayMethod> binder = LxItemBinder.of(PayMethod.class, opts)
        .onViewBind((itemBinder, context, holder, data) -> {

        })
        .onEventBind((itemBinder, context, data, eventType) -> {

        })
        .build();
```

<span id="lxlist"></span>

## 基础：LxList

`LxList` 作为 `LxAdapter` 的数据来源，内部基于 `DiffUtil` 实现，辅助完成数据的自动比对和更新，彻底告别 `notify` 更新数据的方式，继承关系如下：

```bash
AbstractList -> DiffableList -> LxList -> LxTypedList
```

获取区块数据，可以对指定区块发布更新：

```java
LxList list = new LxTypedList();

// 获取内容类型的数据
LxList list1 = list.getContentTypeData();

// 获取指定类型的数据
LxList list2 = list.getExtTypeData(TYPE_HEADER);
```

以下是 `LxList` 内置 **增删改查** 方法，基本能满足开发需求，另外也可以使用 `snapshot` 获取快照，然后自定义扩展操作：

```java
// 内部使用 DiffUtil 实现，同步更新
LxList list = new LxList();

// 内部使用 DiffUtil + 异步 实现，避免阻塞主线程
LxList list = new LxList(true);

// 用来测试的数据
List<LxModel> newList = new ArrayList<>();
LxModel item = new LxModel(new Student("name"));
```

增：

```java
// 添加元素
list.updateAdd(item);
list.updateAdd(0, item);

// 在末尾添加
list.updateAddLast(item);

// 添加列表
list.updateAddAll(newList);
list.updateAddAll(0, newList);
```


删：

```java
// 清空列表
list.updateClear();

// 删除元素
list.updateRemove(item);
list.updateRemove(0);

// 删除符合条件的元素
list.updateRemove(model -> model.getItemType() == TYPE_STUDENT);
// 使用增强循环，删除符合条件的元素
list.updateRemoveX(model -> {
    if (model.getItemType() == TYPE_STUDENT) {
        return Lx.Loop.TRUE_BREAK;
    }
    return Lx.Loop.FALSE_NOT_BREAK;
});

// 从末尾开始，删除符合条件的元素
list.updateRemoveLast(model -> model.getItemType() == TYPE_STUDENT);
// 从末尾开始，使用增强循环，删除符合条件的元素
list.updateRemoveLastX(model -> {
    if (model.getItemType() == TYPE_STUDENT) {
        return Lx.Loop.TRUE_BREAK;
    }
    return Lx.Loop.FALSE_NOT_BREAK;
});
```


改：

```java
// 使用索引更新某一项
list.updateSet(0, data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});

// 指定更改某一项
list.updateSet(model, data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});

// 遍历列表，找到符合规则的元素，并做更改操作
list.updateSet(data -> {
    Student stu = data.unpack();
    return stu.id > 10;
}, data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});

// 遍历列表，无差别做更改操作
list.updateSet(data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});

// 使用增强循环，更改指定的元素
list.updateSetX(data -> {
    Student stu = data.unpack();
    // id > 10 就更改，发现一个后停止循环
    if (stu.id > 10) {
        return Lx.Loop.TRUE_BREAK;
    }
    // 其他情况，不更改，继续循环
    return Lx.Loop.FALSE_NOT_BREAK;
}, data -> {
    Student stu = data.unpack();
    stu.name = "new name";
});

```

查：

```java
List<Student> students = list.find(data -> data.getItemType() == TYPE_STUDENT, LxModel::unpack);
```

快照更新：

```java
// 获取列表快照, 删除第一个元素, 发布更新
List<LxModel> snapshot = list.snapshot();
snapshot.remove(0);
list.update(newList);
```

<span id="query"></span>

## 数据更新

由于 `LxList` 列表是基于 `LxModel` 的，在实际使用过程中，会有些不方便，为了解决这个问题，引入 `LxSource` 和 `LxQuery` 来对数据做自动的包装和解包装：

```java
// 初始化测试数据
Student student = new Student("Job");
List<Student> studentList = new ArrayList<>();
studentList.add(student);
```

使用 `LxSource` 构建数据源:

```java
LxSource source = null;

// 多种方式创建 LxSource
source = LxSource.just(student);
source = LxSource.just(TYPE_STUDENT, student);
source = LxSource.just(studentList);
source = LxSource.just(TYPE_STUDENT, studentList);
source = LxSource.empty();
source = LxSource.snapshot(list);

// 添加一个
source.add(student);
// 添加一个，指定类型
source.add(TYPE_STUDENT, student);
// 添加一个，指定类型，并可以重写相关属性
source.add(TYPE_STUDENT, student, model -> model.setModuleId(100));
// 指定下标，添加一个
source.addOnIndex(10, student);
// 指定下标，添加一个，指定类型
source.addOnIndex(10, TYPE_STUDENT, student);
// 指定下标，添加一个，并可以重写相关属性
source.addOnIndex(10, TYPE_STUDENT, student, model -> model.setModuleId(100));

// 添加多个
source.addAll(studentList);
// 添加多个，指定类型
source.addAll(TYPE_STUDENT, studentList);
// 添加多个，指定类型，并可以重写相关属性
source.addAll(TYPE_STUDENT, studentList, model -> model.setModuleId(100));
// 指定下标，添加多个
source.addAllOnIndex(10, studentList);
// 指定下标，添加多个，指定类型
source.addAllOnIndex(10, TYPE_STUDENT, studentList);
// 指定下标，添加多个，指定类型，并可以重写相关属性
source.addAllOnIndex(10, TYPE_STUDENT, studentList, model -> model.setModuleId(100));

// 使用 source 更新数据
list.update(source);
```

使用 `LxQuery` 更方便的完成数据的更新：


```java
// 数据更新辅助类
LxQuery query = list.query();
```

增:

```java
// 增加元素基于 LxSource 实现
query.add(LxSource.just(student));
```

删：

```java
// 按条件删除元素
query.remove(Student.class, TYPE_STUDENT, stu -> stu.id > 10);
// 删除类型为 TYPE_STUDENT 所有元素
query.remove(TYPE_STUDENT);
// 按条件删除，增强循环删除
query.removeX(Student.class, stu -> {
    if (stu.id == 10) {
        return Lx.Loop.TRUE_BREAK;
    }
    return Lx.Loop.FALSE_NOT_BREAK;
});
```

改：

```java
int index = 10;
// 按条件更改元素
query.set(Student.class, TYPE_STUDENT, stu -> stu.id == 10, stu -> stu.name = "NEW_NAME");

// 更改指定下标的元素
query.set(Student.class, TYPE_STUDENT, index, stu -> stu.name = "NEW_NAME");

// 更改指定类型的元素
query.set(Student.class, TYPE_STUDENT, stu -> {
    stu.name = "NEW_NAME";
});

// 增强循环指定条件更新
query.setX(Student.class, TYPE_STUDENT, stu -> {
    if (stu.id == 10) {
        // 返回 true，停止循环
        return Lx.Loop.TRUE_BREAK;
    }
    return Lx.Loop.FALSE_NOT_BREAK;
}, data -> data.name = "NEW_NAME");
```

查：

```java
// 按条件查找元素
List<Student> students1 = query.find(Student.class, TYPE_STUDENT, stu -> stu.id > 10);
// 按类型查找元素
List<Student> students2 = query.find(Student.class, TYPE_STUDENT);

// 按条件查找元素 一个
Student one = query.findOne(Student.class, TYPE_STUDENT, stu -> stu.id > 10);

// 使用 ID 查找，类需实现 Idable 接口返回 ID
Student oneById = query.findOneById(Student.class, 100);
```

<span id="LxViewHolder"></span>

## 基础：LxViewHolder

为了支持同时对多个控件进行一样的绑定操作，可以使用 `Ids` 来包含多个 `id`:

```java
// 为多个 TextView 设置相同的文字
holder.setText(Ids.all(R.id.test_tv, R.id.tv_count), "new text");
```

使用 ID `R.id.item_view` 来标记 `holder` 的 `itemView`:

```java
holder.setClick(R.id.item_view, v -> {

});
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
        // 点击事件
        .setClick(view -> {
            ToastX.show("点击事件");
        })
        // 将某个控件的点击事件绑定到另一个上面
        // 针对需要触发点击效果的场景
        .linkClick(R.id.cover_iv,R.id.item_view);
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
<span id="event"></span>

## 基础：点击事件

点击事件需要在 `TypeOpts` 设置，单击事件默认是开启的，双击、长按事件需要手动开启；
重写 `onBindEvent` 方法，根据 `eventType` 的不同，对不同事件进行处理；


```java
class StudentItemBind extends LxItemBinder<Student> {

    @Override
    protected TypeOpts newTypeOpts() {
      return TypeOpts.make(opts -> {
          opts.viewType = TYPE_STUDENT;
          opts.layoutId = R.layout.item_squire1;
          opts.enableLongPress = true; // 开启长按
          opts.enableDbClick = true; // 开启双击
          opts.enableClick = true; // 开启单击
          opts.enableFocusChange = true; // 开启焦点变化事件
      });
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, Student data) {
        holder.setText(R.id.title_tv, "学：" + data.name)
                // 给控件加点击事件
                .setClick(R.id.title_tv, v -> {
                });
    }

    @Override
    public void onBindEvent(LxContext context, Student data, int eventType) {
        // 如果只有点击事件，那可以不做区分，因为别的根本不会触发
        switch (eventType) {
            case Lx.ViewEvent.CLICK:
                // 单击
                break;
            case Lx.ViewEvent.LONG_PRESS:
                // 长按
                break;
            case Lx.ViewEvent.DOUBLE_CLICK:
                // 双击
                break;
            case Lx.ViewEvent.FOCUS_CHANGE:
                // 焦点变化，可以通过 context.holder.itemView.hasFocus() 判断有没有焦点
                break;
            case Lx.ViewEvent.FOCUS_ATTACH:
                // 焦点变化，获得焦点
                break;
            case Lx.ViewEvent.FOCUS_DETACH:
                // 焦点变化，失去焦点
                break;

        }
    }
}
```

<span id="multitype"></span>

## 基础：扩展自定义类型

首先声明类型


```java
public static final int TYPE_TEACHER  = Lx.contentTypeOf();
public static final int TYPE_STUDENT = Lx.contentTypeOf();

public static final int FOOTER = Lx.extTypeAfterContentOf();

public static final int HEADER  = Lx.extTypeBeforeContentOf();
```

构建 `LxAdapter` 和平常一样使用，这里我们使用了 4 种类型：

```java
LxList list = new LxTypedList();
LxAdapter.of(list)
        // 这里指定了 5 种类型的数据绑定
        .bindItem(new StudentItemBind(), new TeacherItemBind(),
                new HeaderItemBind(),new FooterItemBind())
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

添加数据，它们被添加到一个数据源列表中：

```java
LxList list = new LxList();
LxSource snapshot = LxSource.snapshot(list);
// 添加两个 header
snapshot.add(TYPE_HEADER, new NoNameData("header1"));
snapshot.add(TYPE_HEADER, new NoNameData("header2"));
// 交替添加 10 个学生和老师
List<Student> students = ListX.range(10, index -> new Student("1"));
List<Teacher> teachers = ListX.range(10, index -> new Teacher("2"));
for (int i = 0; i < 10; i++) {
    snapshot.add(TYPE_STUDENT, students.get(i));
    snapshot.add(TYPE_TEACHER, teachers.get(i));
}
// 添加两个 footer
snapshot.add(TYPE_FOOTER, new NoNameData("footer1"));
snapshot.add(TYPE_FOOTER, new NoNameData("footer2"));
// 发布数据更新
list.update(snapshot);
```

从源数据中获取区块数据，对他们做独立的修改操作：

```java
// 数据源
LxList list = new LxList();
// 生成 Adapter
LxAdapter.of(list)...
// 获取内容类型，在这里是中间的学生和老师
LxList contentTypeData = list.getContentTypeData();
// 获取自定义的 TOP_HEADER 类型
LxList extTypeData = list.getExtTypeData(TYPE_HEADER);
// 获取内置自定义的 VIEW_TYPE_HEADER 类型
LxList extTypeData = list.getExtTypeData(TYPE_FOOTER);
```

我们发现，拿到每种类型的区块数据后，添加和更改每种特殊的类型，是非常方便的，没有针对性的去做 `Header` `Footer` 这些固定的功能，其实它们只是数据的一种类型，可以按照自己的需要做任意的扩展，这样会灵活很多，其他的比如骨架屏、空载页、加载中效果都可以基于这个实现；

<span id="publishevent"></span>

## 功能：事件发布

一般来说我们数据和视图是分离的，`Adapter` 的数据源一般会被 `Presenter` 等逻辑层持有，一般会有以下两个场景：

- 从 `Presenter` 层有一些数据变化的需要，需要 `Adapter` 响应；
- 某一些 `Adapter` 的响应可以被抽离出来，更好的复用；

因此需要事件发布机制，他在 数据（LxList） 和 视图（Adapter） 中间搭建了一条事件通道，借助它可以发布和响应事件；这有点类似于 `EventBus` 不过他不是注册在内存中的，是依赖于 `LxList` 的；


```java
// 事件
public static final String HIDE_LOADING = "HIDE_LOADING";

// 定义事件拦截器
EventSubscriber subscriber = (event, adapter, extra) -> {
    LxList lxModels = adapter.getData();
    LxList extTypeData = lxModels.getExtTypeData(TYPE_LOADING);
    extTypeData.updateClear();
};

// 全局注入，会对所有 Adapter 生效
LxGlobal.subscribe(HIDE_LOADING, subscriber);

// 对 Adapter 注入，仅对当前 Adapter 生效
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        .subscribe(HIDE_LOADING, subscriber)
        .attachTo(mContentRv, LxManager.linear(getContext()));

// 直接在数据层注入，会对该数据作为数据源的 Adapter 生效
models.subscribe(HIDE_LOADING, subscriber);
```

发布事件：

```java
// 数据源
LxList list = new LxList();

// 一般我们在 Presenter 等数据处理层会拿到数据源，使用数据源可以直接向 Adapter 发布事件
list.postEvent(HIDE_LOADING);
list.postEvent(HIDE_LOADING, new LoadingData(LOADING_NONE));
```

事件也可以被抽象封装出来，作为一些公共的逻辑复用，例如框架内部内置了如下几个事件：

```java
// 设置加载更多开关
list.postEvent(Lx.Event.LOAD_MORE_ENABLE, false)
// 结束加载更多
list.postEvent(Lx.Event.FINISH_LOAD_MORE);

// 结束加载更多，顶部+底部
Lx.Event.FINISH_LOAD_MORE
// 结束加载更多，底部
Lx.Event.FINISH_END_EDGE_LOAD_MORE
// 结束加载更多，顶部
Lx.Event.FINISH_START_EDGE_LOAD_MORE
// 设置加载更多开关
Lx.Event.LOAD_MORE_ENABLE
// 设置底部加载更多开关
Lx.Event.END_EDGE_LOAD_MORE_ENABLE
// 设置顶部加载更多开关
Lx.Event.START_EDGE_LOAD_MORE_ENABLE
```
<span id="span"></span>

## 功能：跨越多列（Span）

当使用 `GridLayoutManager` 布局时，可能某种类型需要跨越多列，需要针对每种类型进行指定；

```java
static class StudentItemBind extends LxItemBinder<Student> {
    @Override
    protected TypeOpts newTypeOpts() {
        return TypeOpts.make(opts -> {
            opts.viewType = TYPE_STUDENT;
            opts.layoutId = R.layout.item_squire1;

            // 使用内置参数，跨越所有列
            opts.spanSize = Lx.SpanSize.ALL;
            // 使用内置参数，跨越总数的一半
            opts.spanSize = Lx.SpanSize.HALF;
            // 使用固定数字，跨越 3 列
            opts.spanSize = 3;
        });
    }
```

指定一个确定的 `SpanSize` 通常是不灵活的，因为我们不知道 `RecyclerView` 在使用时指定的列数 (spanCount)，因此建议使用一个标记表示：

```java
Lx.SpanSize.NONE // 不设置，默认值
Lx.SpanSize.ALL // 跨越整行
Lx.SpanSize.HALF // 跨越一半
Lx.SpanSize.THIRD // 跨越 1/3
Lx.SpanSize.QUARTER // 跨越 1/4
```

可能这些还不足以兼容到所有情况，可以设置 `SpanSize` 适配接口，自己来处理这些标记：

```java
// 跨越 1/5
public static final int SPAN_SIZE_FIFTH = --Lx.SpanSize.BASIC;

// 处理这个标记，返回真正的 spanSize
LxGlobal.setSpanSizeAdapter((spanCount, spanSize) -> {
    if (spanSize == SPAN_SIZE_FIFTH && spanCount % 5 == 0) {
        return spanCount / 5;
    }
    return spanSize;
});
```

<span id="space"></span>

## 功能：间隔（Space）

一般在业务开发中，我们希望布局周边带有一样的间隔，这样比较整齐，一般有两种方案：

- 使用 `padding` 来做，中间相接的地方就会变为间隔的两倍，不能均分，也可以动态设置左右不同 `padding`，但是相对耗时耗力；
- 使用 `ItemDecoration` 来做，可以根据位置动态的设置，上下左右间距，但是因为多类型的存在，每种类型的 `spanSize` 不同，很难一下处理好；

为此提供了 `LxSpaceComponent`，用来为所有类型布局周边添加相等的间隔，并且在数据增删变动时，也能及时自动修改间距，用法如下：

```java
LxAdapter.of(mLxModels)
    .bindItem(new SpaceItemBinder()...)
    .component(new LxSpaceComponent(50))
    .attachTo(mContentRv, LxManager.grid(getContext(), 3));

// 自定义扩展
LxAdapter.of(mLxModels)
    .bindItem(new SpaceItemBinder()...)
    .component(new LxSpaceComponent(50, new LxSpaceComponent.SpaceSetter() {
      @Override
      public void set(LxSpaceComponent comp, Rect outRect, LxSpaceComponent.SpaceOpts opts) {
          // 自己做一些定制改变
      }
    }))
    .attachTo(mContentRv, LxManager.grid(getContext(), 3));

```

<span id="loadmore"></span>

## 功能：加载更多（LoadMore）

加载更多功能由 `LxStartEdgeLoadMoreComponent` 和 `LxEndEdgeLoadMoreComponent` 承担，可以选择性的使用它们；

```java
LxAdapter.of(list)
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
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

<span id="selector"></span>

## 功能：选择器（Selector）

主要用于在列表中实现选择器的需求，单选、多选、状态变化等业务场景;

这部分功能交给 `LxSelectComponent`


```java
LxAdapter.of(list)
        .bindItem(new StudentItemBind())
        // 多选
        .component(new LxSelectComponent(Lx.SelectMode.MULTI))
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));

// 从 component 中获取选中的数据集
LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
if (component != null) {
    List<Student> result = component.getResult();
}
```

在 `BindView` 中描述当数据被选中时如何显示：

```java
class SelectItemBind extends LxItemBinder<NoNameData> {
    //...
    @Override
    public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
        LxModel model = context.model;

        // 根据选中状态显示 UI
        holder.setText(R.id.title_tv, model.isSelected() ? "我被选中" : "条件我没有被选中");
        holder.setImage(R.id.cover_iv, "image url");
    }
    @Override
    public void onBindEvent(LxContext context, NoNameData data, int eventType) {
        // 点击某项时执行选中操作
        LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
        if (component != null) {
            component.select(context.model);
        }
    }
}
```

选中某项时通常只是更改一个标记，我们不希望把整个 `BindView` 方法执行一遍，这会带来性能的损耗，有时还会造成图片闪烁等问题，当选中被触发时，框架也会发出一个 **[条件更新](#condition)** 的事件，关于 **[条件更新](#condition)** 可以参考后面相关的文档，这里简单说一下用法：


```java
class SelectItemBind extends LxItemBinder<NoNameData> {
    //...
    @Override
    public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
        LxModel model = context.model;

        // 选中触发时，会触发条件更新
        // 如果你的 bind 方法执行了很多操作，当条件更新发生时
        // 可以选择性的绑定部分数据，避免性能的损失
        if (context.bindMode == Lx.BindMode.CONDITION) {
            if (context.conditionKey.equals(Lx.Condition.CONDITION_SELECTOR)) {
                holder.setText(R.id.title_tv, model.isSelected() ? "我被选中" : "条件我没有被选中");
                return;
            }
        }
        // 根据选中状态显示 UI
        holder.setText(R.id.title_tv, model.isSelected() ? "我被选中" : "条件我没有被选中");
        holder.setImage(R.id.cover_iv, "image url");
    }

}
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

<span id="animator"></span>

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
LxAdapter.of(list)
        .bindItem(new StudentItemBind())
        // 缩放动画
        .component(new LxBindAnimatorComponent(new BindScaleAnimator()))
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

也可以分类型指定动画，每种类型给予不同的动画效果

```java
class StudentItemBind extends LxItemBinder<Student> {

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
LxAdapter.of(list)
        .bindItem(new StudentItemBind())
        // 缩放动画
        .component(new LxItemAnimatorComponent(new ScaleInAnimator()))
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

<span id="fixed"></span>

## 功能：悬挂效果（Fixed）

针对每种类型悬挂效果，可以支持所有类型所有布局文件的顶部悬挂效果，需要使用 `LxFixedComponent` 实现，支持两种实现方式：

- 采用绘制的方式，优点是悬挂的视图有挤压效果，效率上也更好，但是因为是绘制的所以不支持点击事件，可以采用覆盖一层 `View` 来解决这个问题；
- 采用生成 `View` 的方式，优点是实实在在的 `View`，点击事件什么的自然都支持，缺点是你需要提供一个容器，而且视图之间没有挤压的效果；

```java
LxAdapter.of(list)
        .bindItem(new StudentItemBind())
        // 悬挂效果
        .component(new LxFixedComponent())
        // 悬挂效果
        .component(new LxFixedComponent(mMyViewGroup))
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

同时在 `TypeOpts` 中说明哪些类型需要支持悬挂

```java
class StudentItemBind extends LxItemBinder<Student> {

    @Override
    protected TypeOpts newTypeOpts() {
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

<span id="dragswipe"></span>

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
options.dragFlags = ItemTouchquery.UP | ItemTouchquery.DOWN;
// 关闭触摸自动触发侧滑
options.touchItemView4Swipe = false;

LxAdapter.of(list)
        .bindItem(new StudentItemBind())
        // 当侧滑和拖拽发生时触发的时机，可以响应的做高亮效果
        .component(new LxDragSwipeComponent(options, (state, holder, context) -> {
            switch (state) {
                case Lx.DragState.NONE:
                    // 拖拽无状态
                    break;
                case Lx.DragState.ACTIVE:
                    // 触发拖拽
                    break;
                case Lx.DragState.RELEASE:
                    // 释放拖拽
                    break;
                case Lx.SwipeState.NONE:
                    // 侧滑无状态
                    break;
                case Lx.SwipeState.ACTIVE:
                    // 触发侧滑
                    break;
                case Lx.SwipeState.RELEASE:
                    // 释放侧滑
                    break;
            }
        }))
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

最后在 `TypeOpts` 里面配置该类型是否支持侧滑和拖拽，这样可以灵活的控制每种类型数据的行为：

```java
class StudentItemBind extends LxItemBinder<Student> {
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
class StudentItemBind extends LxItemBinder<Student> {

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
    public void onBindView(LxContext context, LxViewHolder holder, Student data) {
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
}
```

<span id="snap"></span>

## 功能：实现 ViewPager (Snap)

内部使用 `SnapHelper` 实现，很简单，只是要把他封装成 `LxComponent` 的形式，统一起来，由 `LxSnapComponent` 实现；

```java
LxAdapter.of(list)
        .bindItem(new StudentItemBind())
        // 实现 ViewPager 效果
        .component(new LxSnapComponent(Lx.SnapMode.PAGER))
        // 实现 ViewPager 效果，但是可以一次划多个 item
        .component(new LxSnapComponent(Lx.SnapMode.LINEAR))
        .attachTo(mRecyclerView, new LinearLayoutManager(getContext()));
```

模拟 `ViewPager` 添加了 `OnPageChangeListener`

```java
LxAdapter.of(mLxModels)
        .bindItem(new PagerItemBind())
        .component(new LxSnapComponent(Lx.SnapMode.PAGER, new LxSnapComponent.OnPageChangeListener() {

            @Override
            public void onPageSelected(int lastPosition, int position) {
                // 选中监听
                RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(position);
                RecyclerView.ViewHolder lastHolder = mRecyclerView.findViewHolderForAdapterPosition(lastPosition
                holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                if (lastHolder != null && !lastHolder.equals(holder)) {
                    lastHolder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 滑动状态监听
            }
        }))
        .attachTo(mRecyclerView, new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
```

<span id="expandable"></span>

## 功能：实现分组列表（Expandable）

基于我们基本的设计架构是可以很轻松的实现分组列表效果的，但是这个场景用到的时候比较多，所以内置一些辅助类，用来更好、更简单的实现分组列表；

针对分组列表的场景设计了 `LxExpandable` 辅助类；

首先 **组** 的数据结构需要实现接口 `LxExpandable.ExpandableGroup`:

```java
static class GroupData implements LxExpandable.ExpandableGroup<GroupData, ChildData> {

    public List<ChildData> children;
    public String          title;
    public boolean         expand;
    public int             groupId;

    @Override
    public List<ChildData> getChildren() {
        return children;
    }

    @Override
    public boolean isExpand() {
        return expand;
    }

    @Override
    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }
}
```

然后 **子** 的数据结构需要实现接口 `LxExpandable.ExpandableChild`：

```java
static class ChildData implements LxExpandable.ExpandableChild<GroupData, ChildData> {

    public String    title;
    public int       childId;
    public int       groupId;
    public GroupData groupData;

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public GroupData getGroupData() {
        return groupData;
    }
}
```

然后定义的 `GroupItemBind` 和  `ChildItemBind`：

点击分组可以展开或者收起当前的分组子数据：

```java
static class GroupItemBind extends LxItemBinder<GroupData> {

    GroupItemBind() {
        super(TypeOpts.make(opts -> {
            opts.spanSize = Lx.SpanSize.ALL;
            opts.viewType = Lx.ViewType.EXPANDABLE_GROUP;
            opts.layoutId = R.layout.item_group;
            opts.enableFixed = true;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, GroupData data) {
        holder.setText(R.id.section_tv, data.title + " " + (data.expand ? "展开" : "关闭"));
    }

    @Override
    public void onBindEvent(LxContext context, GroupData listItem, int eventType) {
        // 展开/关闭分组
        LxExpandable.toggleExpand(adapter, context, listItem);
    }
}
```

点击子数据，可以删除当前子数据：

```java
static class ChildItemBind extends LxItemBinder<ChildData> {

    ChildItemBind() {
        super(TypeOpts.make(opts -> {
            opts.spanSize = Lx.SpanSize.ALL;
            opts.viewType = Lx.ViewType.EXPANDABLE_CHILD;
            opts.layoutId = R.layout.item_simple;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, ChildData data) {
        holder.setText(R.id.sample_tv, data.title + " ，点击删除");
    }

    @Override
    public void onBindEvent(LxContext context, ChildData data, int eventType) {
        // 点击删除子项
        LxExpandable.removeChild(adapter, context, data);
    }
}
```

生成 `LxAdapter`:

```java
LxAdapter.of(mLxModels)
        .bindItem(new GroupItemBind(), new ChildItemBind())
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

我们模拟一些假数据：

```java
List<GroupData> groupDataList = new ArrayList<>();
for (int i = 0; i < 15; i++) {
    GroupData groupData = new GroupData("group -> " + i);
    groupData.groupId = i;
    groupDataList.add(groupData);
    List<ChildData> childDataList = new ArrayList<>();
    for (int j = 0; j < 5; j++) {
        ChildData childData = new ChildData("child -> " + j + " ,group -> " + i);
        childData.childId = j;
        childData.groupId = i;
        childData.groupData = groupData;
        childDataList.add(childData);
    }
    groupData.children = childDataList;
}
LxSource source = LxSource.just(Lx.ViewType.EXPANDABLE_GROUP, groupDataList);
mLxModels.update(source);
```

是不是很简单啊，感觉上还是写了一些代码，没有一行代码实现xxx 的感觉，只是提供一个思路，如果类库内部接管太多业务逻辑其实是不友好的，可以看下 `LxExpandable` 的代码，其实就是对数据处理的一些封装，基于基本的设计思想很容易抽离出来；

<span id="nesting"></span>

## 功能：实现嵌套滑动（Nesting）

开发中有种比较常见的场景，垂直的列表中，嵌套横向滑动的列表：

1. 横向滑动和纵向滑动事件不能冲突；
2. 上下滑动时，不能因为加载横向的列表造成滑动的卡顿；
3. 滑动过的横向列表，再回来时，要保持原先的滑动状态；

针对这种场景，设计了 `LxNesting` 辅助工具；

最外层列表的使用跟之前一样就不再赘述了，主要说一下横向列表如何使用 `LxNesting`

```java
class NestingItemBinder extends LxItemBinder<NoNameData> {

    @Override
    protected TypeOpts newTypeOpts() {
        return TypeOpts.make(opts -> {
            opts.viewType = TYPE_HORIZONTAL_CONTAINER;
            opts.layoutId = R.layout.item_horizontal_container;
            opts.spanSize = Lx.SpanSize.ALL;
        });
    }

    // 初始化没有 adapter 时的 callback，放在这里是避免多次创建造成性能问题
    // 使用 list 创建一个 Adapter 绑定到 view 上
    private LxNesting mLxNesting = new LxNesting((view, list) -> {
        LxAdapter.of(list)
                .bindItem(new HorizontalImgItemBind())
                .attachTo(view, LxManager.linear(view.getContext(), true));
    });

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
        holder.setText(R.id.title_tv, listItem.desc + " , offset = " + listItem.offset + " pos = " + listItem.pos);
        // 获取到控件
        RecyclerView contentRv = holder.getView(R.id.content_rv);
        // 数据源
        LxSource source = LxSource.just(TYPE_HORIZONTAL_IMG, listItem.datas);
        // 设置，这里会尝试恢复上次的位置，并计算接下来滑动的位置
        mLxNesting.setup(contentRv, context.model, source.asModels());
    }
}
```

<span id="picker"></span>

## 功能：实现滚轮选择器效果（Picker）

使用 `LxPicker` 实现滚轮选择器效果，内部使用 `LxPickerComponent` + `LxSnapComponent` 实现;

当多个选择器级联时，第一个选择后接着就会触发第二个选择，达到递归触发的效果；

```java
// 配置
LxPicker.Opts opts = new LxPicker.Opts();
opts.infinite = false; // 无限滚动
opts.exposeViewCount = 5; // 暴露的数量
opts.maxScaleValue = 1.3f; // 缩放比例
opts.itemViewHeight = SizeX.dp2px(50); // 每个 item 高度
opts.listViewWidth = SizeX.WIDTH / 3; // 宽度

// 容器控件
mPicker = new LxPicker<>(mPickerLl);

// 当选择流程结束时触发，在这里关闭 loading
mPicker.setOnPickerDataUpdateFinishListener(() -> mLoadingCl.setVisibility(View.GONE));

// 数据获取回调
LxPicker.PickerDataFetcher<AddressPickItemBean> fetcher = (index, pickValue, callback) -> {
    mLoadingCl.setVisibility(View.VISIBLE);
    mViewModel.requestPickerData(pickValue == null ? null : pickValue.getId(), callback);
    return null;
};

// 添加一个 picker
mPicker.addPicker(opts, new AddressItemBinder(), fetcher);
mPicker.addPicker(opts, new AddressItemBinder(), fetcher);
mPicker.addPicker(opts, new AddressItemBinder(), fetcher);

// 触发第一个 picker 获取数据
mPicker.active();
```

数据绑定很简单，可以自己实现

```java
static class AddressItemBinder extends LxItemBinder<AddressPickItemBean> {
    @Override
    protected TypeOpts newTypeOpts() {
        return TypeOpts.make(R.layout.pay_address_item);
    }
    @Override
    protected void onBindView(LxContext context, LxViewHolder holder, AddressPickItemBean listItem) {
        holder.setText(R.id.content_tv, listItem == null ? "" : listItem.getShortName());
    }
}
```

<span id="cache"></span>


## 进阶：使用缓存优化绑定性能

当列表滑动时，`onBindView` 方法会被执行很多次，因此如果在 `onBindView` 中执行了耗时操作就会影响列表的流畅度；应该尽量避免在 `bind` 方法中避免计算等操作，一些不会变的数据我们可以将其缓存起来，这部分功能借助  `LxCache` 实现；

以下是一个简单的例子，使用 `Id` 作为唯一标识

- 注册 `Mapper` 用户计算数据结果；
- 使用 `cache.getString()` 获取结果；

```java
public class StudentItemBinder extends LxItemBinder<Student> {

    @Override
    protected void onAdapterAttached(LxAdapter adapter) {
        super.onAdapterAttached(adapter);
        // 注册 Mapper 用来计算显示数据，计算后数据会被自动缓存
        cache.addMapper(R.id.time_tv, value -> FormatUtils.formatSeconds(value.getDuration()));
    }

    @Override
    protected void onBindView(LxContext context, LxViewHolder holder, Student listItem) {
      // 显示时，使用 Id 获取数据，数据会被自动缓存
      holder.setText(R.id.time_tv, cache.getString(R.id.time_tv, context.model));
    }
}
```

如果数据发生了变化，需要清除缓存，清除后数据下次绑定时数据会重新计算：

```java
LxCache.remove(R.id.time_tv, model);
```


<span id="extra"></span>

## 进阶：使用 Extra 扩展数据

在 `LxModel` 中增加了 `extra` 他是一个 `bundle` 类型的数据，可以在不增加字段的情况下扩展一下临时用的数据；

```java
LxModel model;
// 存
model.getExtra().putString("TEMP_DATA","Hello");
// 取
String tempData = model.getExtra().getString("TEMP_DATA","");
```

<span id="idable"></span>

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


<span id="typeable"></span>

## 进阶：使用 Typeable 内置类型

如果你的数据对象只有一个类型，也可以使用数据类实现 `Typeable` 接口，在接口方法中返回类型，这样打包数据的时候就不需要指定类型了，内部会检测是否是 `Typeable` 子类，获取真正的类型；

```java
static class InnerTypeData implements Typeable {

    int type;

    @Override
    public int getItemType() {
        return type;
    }
}
```

<span id="condition"></span>

## 进阶：使用条件更新

- 场景1：我们的数据并没有改变，但是我们仍旧想触发数据的更新；
- 场景2：只想更新一个控件，比如下载进度条，这个更新比较频繁，但是不想做不必要的刷新；

基于以上两种应用场景，条件更新应运而生，你可以不改变数据，但是触发更新，并且可以指定条件，仅刷新一个控件的显示，类似 payloads 但是不需要计算有效载荷，只需要制定一个条件即可；

```java
public static final String KEY_NEW_CONTENT       = "KEY_NEW_CONTENT";
public static final String CONDITION_UPDATE_NAME = "CONDITION_UPDATE_NAME";

static class StudentItemBind extends LxItemBinder<Student> {

    // ...

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, Student data) {
        if (context.bindMode == Lx.BindMode.CONDITION) {
            // 条件更新
            Bundle conditionValue = context.conditionValue;
            if (CONDITION_UPDATE_NAME.equals(context.conditionKey)) {
                String value = conditionValue.getString(KEY_NEW_CONTENT, "no content");
                holder.setText(R.id.title_tv, value + "," + data.name);
            }
        }
    }

    @Override
    public void onBindEvent(LxContext context, Student listItem, int eventType) {
      LxList models = adapter.getData();
      models.updateSet(context.layoutPosition, data -> {
          Bundle bundle = new Bundle();
          bundle.putString(KEY_NEW_CONTENT, "I AM NEW CONTENT");
          data.setCondition(CONDITION_UPDATE_NAME, bundle);
      });
    }
}
```

<span id="payloads"></span>

## 进阶：使用有效载荷（payloads）更新

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
            payloads.add("name_change");
        }
        return payloads;
    }

}
```

这样我们就通过比对拿到了 `payloads`, 那我们如何使用这些有效载荷呢？

```java
class StudentItemBind extends LxItemBinder<Student> {

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, Student data) {
      if (context.bindMode == Lx.BindMode.PAYLOADS) {
        // payloads 更新
        for (String payload : context.payloads) {
            if ("name_change".equals(payload)) {
                holder.setText(R.id.title_tv, data.name);
            }
        }
      }
    }
}
```

<span id="contract"></span>

## 联系我

- Android开发技术交流

![](http://hibropro.oss-cn-beijing.aliyuncs.com/208737.jpeg)

- 微信

![](http://cdn1.showjoy.com/shop/images/20190911/8DYEEANAVZR2EPI7D8BW1568191925378.jpeg)


<!-- <img style="width:100px;" src="http://cdn1.showjoy.com/shop/images/20190911/Y6HO22A85HL6LBHBGEMD1568190538159.gif"/>
    <img style="width:100px;" src="http://cdn1.showjoy.com/shop/images/20190911/GIWTASSPUTE8K6XXOP751568190536961.gif"/>
    <img style="width:100px;" src="http://cdn1.showjoy.com/shop/images/20190911/KNW6SI4H7INBVWE1Y3761568190536907.jpg"/> -->
 <!-- <a style="position:fixed;right:20px;bottom:20px;" href="#top">
  <span style="display:flex;flex-direction:column;justify-content:center;align-items:center;">
    <span style="font-size:16px;font-weight:bold;">点击回到顶部</span>
    <img style="width:100px;" src="http://cdn1.showjoy.com/shop/images/20190911/IEQ88UTNXOBZD1YISQ2E1568190538146.gif1"/>

  </span>
 </a> -->