![](https://cdn1.showjoy.com/shop/images/20190904/9HTXBS92UUT4GOTN2PM21567587709442.jpeg)


# LxAdapter


`LxAdapter` **轻量** 、 **面向业务** 为主要目的，一方面希望可以快速、简单的的完成数据的适配工作，另一方面针对业务中经常出现的场景能提供统一、简单的解决方案。

> LxAdapter 是我做通用适配器的第三次重构版本，尝试了很多种方案，这次摒弃了很多复杂的东西，回归简单，希望会越来越好；

> [GitHub - LxAdapter](https://github.com/chendongMarch/LxAdapter)

> com.zfy:lxadapter:2.0.5

<!--more-->

<div style="width:100%;display:flex;height:30px;">

<img style="margin-right:20px;"  src="https://img.shields.io/github/stars/chendongMarch/LxAdapter.svg"/>

</div>

<span id="top"> </span>

<img src="http://hibropro.oss-cn-beijing.aliyuncs.com/202949.gif"/>

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
  - [基础：LxItemBind ～ 类型绑定](itembind)
  - [基础：LxList ～ 数据源，自动更新，告别 notify](lxlist)
  - [基础：LxViewHolder ～ 扩展 ViewHolder](#LxViewHolder)
  - [基础：点击事件 ～ 单击、双击、长按、焦点](#event)
  - [基础：扩展自定义类型 ～ 灵活扩展](#multitype)
- 功能
  - [功能：事件发布 ～ 将数据更新抽象成事件](#publishevent)
  - [功能：跨越多列（Span）～ 灵活布局](#span)
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

<span id="data"></span>

## 内置的数据类型

<span id="typeopts"></span>

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
    private int     type = Lx.VIEW_TYPE_DEFAULT; // 类型
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

数据的包装可以使用 `LxPacker` 转换，更加方便；

```java
// 将你的数据包装成 LxModel
LxModel header = LxPacker.pack(Lx.VIEW_TYPE_HEADER, new NoNameData());

// 包装列表
ArrayList<AddressBean> list = new ArrayList<>();
List<LxModel> models = LxPacker.pack(Lx.VIEW_TYPE_HEADER, list);
```
<span id="lxcontext"></span>

### LxContext

`LxContext` 是数据绑定过程中的上下文对象，承载了一些附加的数据，易于扩展；

```java
public class LxContext {

    public int          layoutPosition; // 布局中的位置
    public int          dataPosition; // 数据位置
    public int          viewType; // 类型
    public int          bindStrategy; // 绑定类型
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

LxGlobal.addAdapterEventDispatcher(CLEAR_ALL_DATA, (event, adapter, extra) -> {
    adapter.getData().updateClear();
});
```
<span id="lxadapter"></span>

## 基础：LxAdapter

一般适配器的使用会有单类型和多类型的区分，不过单类型也是多类型的一种，数据的绑定使用 `LxItemBinder` 来做，所以 `LxAdapter` 就只作为一个容易， 不再考虑单类型和多类型的问题；

```java
// 构造数据源
LxList list = new LxList();
// Builder 模式
LxAdapter.of(list)
        // 这里指定了两个类型的数据绑定
        .bindItem(new StudentItemBind(), new TeacherItemBind())
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));

// 为 Adapter 更新数据
List<Student> students = ListX.range(count, index -> new Student());
// 数据打包成 LxModel 类型
List<LxModel> tempList = LxPacker.pack(TYPE_STUDENT, students);
// 发布更新
list.update(tempList);
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
    public void onItemEvent(LxContext context, Student listItem, int eventType) {
       
    }
}
```

也支持使用构建者模式快速创建新的类型绑定：

```java
TypeOpts opts = TypeOpts.make(R.layout.order_item);
LxItemBinder<PayMethod> binder = LxItemBinder.of(PayMethod.class, opts)
        .onViewBind((itemBinder, context, holder, data) -> {
          
        })
        .onItemEvent((itemBinder, context, data, eventType) -> {
          
        })
        .build();
```

<span id="lxlist"></span>

## 基础：LxList

`LxList` 内部基于 `DiffUtil` 实现，辅助完成数据的自动比对和更新，彻底告别 `notify`；

`LxList` 是 `LxAdapter` 的数据源，本质上是 `List` 对象，继承关系如下：

```bash
AbstractList -> DiffableList -> LxList
```

以下是 `LxList` 内置的各种方法 `updateXXX()`，基本能满足开发需求，另外也可以使用 `snapshot` 获取快照，然后自定义扩展操作；

```java
// 内部使用 DiffUtil 实现
LxList list = new LxList();

// 内部使用 DiffUtil + 异步 实现，避免阻塞主线程
LxList list = new LxList(true);

List<LxModel> newList = new ArrayList<>();
LxModel item = new LxModel(new Student("name"));

// 添加元素
list.updateAdd(item);
list.updateAdd(0, item);

// 添加列表
list.updateAddAll(newList);
list.updateAddAll(0, newList);

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

同时，`LxList` 专门给 `LxAdapter` 使用，扩展了获取类型数据的方法，这部分可以结合后面多类型的介绍来看：

```java
LxList list = new LxList();

// 获取内容类型的数据
list.getContentTypeData();

// 获取指定类型的数据
list.getExtTypeData(Lx.VIEW_TYPE_HEADER);
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
重写 `onItemEvent` 方法，根据 `eventType` 的不同，对不同事件进行处理；


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
    public void onItemEvent(LxContext context, Student data, int eventType) {
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
            case Lx.EVENT_FOCUS_CHANGE:
                // 焦点变化，可以通过 context.holder.itemView.hasFocus() 判断有没有焦点
                break;
            case Lx.EVENT_FOCUS_ATTACH:
                // 焦点变化，获得焦点
                break;
            case Lx.EVENT_FOCUS_DETACH:
                // 焦点变化，失去焦点
                break;

        }
    }
}
```

<span id="multitype"></span>

## 基础：扩展自定义类型

> 多类型是 `LxAdapter` 的核心思想；

开发过程中，我们通常会有一些特殊类型的数据，比如:

- `Header`
- `Footer`
- `空载页`
- `骨架屏`
- `Loading`

作为一个框架来说，无法完全覆盖这些业务场景；

所以我们把数据分为两种:

- 一种称为 **内容类型** 数据，通常是我们的业务类型，比如之前的学生、老师，也有可能是一些自定义类型，比如隔断，它们穿插在业务数据中间，也是一种业务类型；
- 一种称为扩展类型数据，它们是业务无关的，可以单独分离的，比如 Header，Footer, 空载页， Loading, 骨架屏等等；

比如下面这个数据:

⚠️ 内容类型一定要是连续的，也就是说不能出现 `Header` 插在 `学生类型` 中间，所有的内容类型要在一起，不能被其他类型分离；

```
- Header1
- Header2
- 学生（内容类型1）
- 老师（内容类型2）
- 学生（内容类型1）
- 老师（内容类型2）
- Footer1
- Footer2
- Loading
```

这种类型组合的主要问题在于数据混合在一起，我们分不出来哪些业务类型，哪些是扩展类型，因此数据的更新和处理变得很困难，对数据的操作要满足以下两点：

1. 操作内容类型时，要屏蔽扩展类型的影响，要保持和网络获取的业务数据列表的一致性，就好像我们的列表中只有内容类型数据一样；
2. 操作扩展类型时，可能针对任何一种扩展类型做增删改，不会影响内容类型的数据列表；

因此我们在创建类型时就要声明好哪些是内容类型，哪些是扩展类型，我们通过如下方式标记：

```java
// Lx.java 内置部分类型，可以直接使用
public static final int VIEW_TYPE_DEFAULT = Lx.contentTypeOf(); // 默认 viewType
public static final int VIEW_TYPE_SECTION = Lx.contentTypeOf(); // 隔断 viewType
public static final int VIEW_TYPE_HEADER  = Lx.extTypeOf(); // 内置 header
public static final int VIEW_TYPE_FOOTER  = Lx.extTypeOf(); // 内置 footer
public static final int VIEW_TYPE_EMPTY   = Lx.extTypeOf(); // 内置空载
public static final int VIEW_TYPE_LOADING = Lx.extTypeOf(); // 内置 loading
public static final int VIEW_TYPE_FAKE    = Lx.extTypeOf(); // 内置假数据

// 也可以自己扩展，使用不同方法生成类型，标记类型的归属
// 指定老师、学生类型，是我们的业务类型，其他的是扩展类型
public static final int TYPE_TOP_HEADER = Lx.extTypeOf(); // 扩展类型
public static final int TYPE_STUDENT    = Lx.contentTypeOf(); // 业务类型
public static final int TYPE_TEACHER    = Lx.contentTypeOf();
```

构建 `LxAdapter` 和平常一样使用，这里我们使用了 5 种类型：

```java
LxList list = new LxList();
LxAdapter.of(list)
        // 这里指定了 5 种类型的数据绑定
        .bindItem(new StudentItemBind(), new TeacherItemBind(),
                new HeaderItemBind(),new FooterItemBind(),new EmptyItemBind())
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));
```

添加数据，它们被添加到一个数据源列表中：

```java
List<LxModel> snapshot = list.snapshot();

// 添加两个 header
snapshot.add(LxPacker.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header1")));
snapshot.add(LxPacker.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header2")));

// 交替添加 10 个学生和老师
List<Student> students = ListX.range(10, index -> new Student());
List<Teacher> teachers = ListX.range(10, index -> new Teacher());
for (int i = 0; i < 10; i++) {
    snapshot.add(LxPacker.pack(TYPE_STUDENT, students.get(i)));
    snapshot.add(LxPacker.pack(TYPE_TEACHER, teachers.get(i)));
}

// 添加两个 footer
snapshot.add(LxPacker.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer1")));
snapshot.add(LxPacker.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer2")));

// 发布数据更新
list.update(snapshot);
```

从源数据中获取类型数据，使用下面两个方法获取分离的类型的数据列表：

```java
// 数据源
LxList list = new LxList();
// 生成 Adapter
LxAdapter.of(list)...
// 获取内容类型，在这里是中间的学生和老师
LxList contentTypeData = list.getContentTypeData();
// 获取自定义的 TOP_HEADER 类型
LxList extTypeData = list.getExtTypeData(TYPE_TOP_HEADER);
// 获取内置自定义的 VIEW_TYPE_HEADER 类型
LxList extTypeData = list.getExtTypeData(Lx.VIEW_TYPE_HEADER);
```

更新数据，增删改内容类型数据，增删改扩展类型数据：

```java
class StudentItemBind extends LxItemBinder<Student> {

    // ... 省略部分代码

    @Override
    public void onItemEvent(LxContext context, Student listItem, int eventType) {
        LxModel model = context.model;
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
                LxList headerData = getData().getExtTypeData(Lx.VIEW_TYPE_HEADER);
                // 更新 header
                headerData.updateSet(0, d -> {
                    CustomTypeData firstData = d.unpack();
                    firstData.desc = "新设置的";
                });

                // 获取 footer，会把底部的两个 footer 单独获取出来
                LxList footerData = getData().getExtTypeData(Lx.VIEW_TYPE_FOOTER);
                // 清空 footer
                footerData.updateClear();
                break;
            case Lx.EVENT_DOUBLE_CLICK:
                // 更新当前这一个数据
                LxList list = getData();
                list.updateSet(context.layoutPosition, d -> {
                    Student unpack = d.unpack();
                    unpack.name = String.valueOf(System.currentTimeMillis());
                });
                break;
        }
    }
}
```

我们发现，添加和更改每种特殊的类型，是非常方便的，没有针对性的去做 `Header` `Footer` 这些固定的功能，其实它们只是数据的一种类型，可以按照自己的需要做任意的扩展，这样会灵活很多，其他的比如骨架屏、空载页、加载中效果都可以基于这个实现；

<span id="publishevent"></span>

## 功能：事件发布

比如如下场景，`Presenter` 层持有数据源，网络加载数据，数据加载完成发现没有更多数据了，列表底部此时要展示一条 ‘没有更多数据～’ 的文案，而我们的 `Adapter` 在 `UI` 层，这个更新变得非常困难；

开发过程中，我们通常会对业务分层，数据层和视图层通常是分离的，数据层改变从而影响视图层变化，并且这部分逻辑最好是可以抽离的，但实际场景下不是所有的操作都能完美的映射到数据变化上面，因此需要事件发布机制；

使用事件，这个过程将会变得很方便而且容易提取为单独的业务逻辑：

```java
// 事件
public static final String HIDE_LOADING = "HIDE_LOADING";

// 定义事件处理器
AdapterEventDispatcher handler = (event, adapter, extra) -> {
    LxList lxModels = adapter.getData();
    LxList extTypeData = lxModels.getExtTypeData(Lx.VIEW_TYPE_LOADING);
    extTypeData.updateClear();
};

// 全局注入，会对所有 Adapter 生效
LxGlobal.addAdapterEventDispatcher(HIDE_LOADING, handler);

// 对 Adapter 注入，仅对当前 Adapter 生效
LxAdapter.of(models)
        .bindItem(new StudentItemBind())
        .onAdapterEvent(HIDE_LOADING, handler)
        .attachTo(mContentRv, LxManager.linear(getContext()));

// 直接在数据层注入，会对该数据作为数据源的 Adapter 生效
models.addAdapterEventDispatcher(HIDE_LOADING, handler);
```

当我们数据更新完成时，需要发布事件：

```java
// 数据源
LxList list = new LxList();

// 一般我们在 Presenter 等数据处理层会拿到数据源，使用数据源可以直接向 Adapter 发布事件
list.publishEvent(HIDE_LOADING);
```

内置了两个事件，可以直接使用以后看需求扩展：

```java
// 设置加载更多开关
list.publishEvent(Lx.EVENT_LOAD_MORE_ENABLE, false)
// 结束加载更多
list.publishEvent(Lx.EVENT_FINISH_LOAD_MORE);

// Lx.java
public static final String EVENT_FINISH_LOAD_MORE            = "EVENT_FINISH_LOAD_MORE"; // 结束加载更多，开启下一次
public static final String EVENT_FINISH_END_EDGE_LOAD_MORE   = "EVENT_FINISH_END_EDGE_LOAD_MORE";  // 结束底部加载更多，开启下一次
public static final String EVENT_FINISH_START_EDGE_LOAD_MORE = "EVENT_FINISH_START_EDGE_LOAD_MORE"; // 结束顶部加载更多，开启下一次
public static final String EVENT_LOAD_MORE_ENABLE            = "EVENT_LOAD_MORE_ENABLE"; // 设置加载更多开关
public static final String EVENT_END_EDGE_LOAD_MORE_ENABLE   = "EVENT_END_EDGE_LOAD_MORE_ENABLE"; // 设置底部加载更多开关
public static final String EVENT_START_EDGE_LOAD_MORE_ENABLE = "EVENT_START_EDGE_LOAD_MORE_ENABLE"; // 设置顶部加载更多开关

// 其他自定义扩展和处理
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
            opts.spanSize = Lx.SPAN_SIZE_ALL;
            // 使用内置参数，跨越总数的一半
            opts.spanSize = Lx.SPAN_SIZE_HALF;
            // 使用固定数字，跨越 3 列
            opts.spanSize = 3;
        });
    }
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
        .component(new LxSelectComponent(Lx.SELECT_MULTI))
        .attachTo(mRecyclerView, LxManager.grid(getContext(), 3));

// 从 component 中获取选中的数据集
LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
if (component != null) {
    List<Student> result = component.getResult();
}
```

在 `BindView` 中描述当数据被选中时如何显示：

```java
static class SelectItemBind extends LxItemBinder<NoNameData> {

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
        holder
              .setLayoutParams(SizeX.WIDTH / 3, SizeX.WIDTH / 3)
              // 选中时，更改文字和颜色
              .setText(R.id.title_tv, context.model.isSelected() ? "我被选中" : "我没有被选中")
              .setTextColor(R.id.title_tv, context.model.isSelected() ? Color.RED : Color.BLACK);

        // 选中时，执行缩放动画，提醒用户
        View view = holder.getView(R.id.container_cl);
        if (context.model.isSelected()) {
            if (view.getScaleX() == 1) {
                view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(300).start();
            }
        } else {
            if (view.getScaleX() != 1) {
                view.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
            }
        }
    }

    @Override
    public void onEvent(LxContext context, NoNameData data, int eventType) {
        // 点击选中
        LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
        if (component != null) {
            component.select(context.model);
        }
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
options.dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
// 关闭触摸自动触发侧滑
options.touchItemView4Swipe = false;

LxAdapter.of(list)
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
        .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER))
        // 实现 ViewPager 效果，但是可以一次划多个 item
        .component(new LxSnapComponent(Lx.SNAP_MODE_LINEAR))
        .attachTo(mRecyclerView, new LinearLayoutManager(getContext()));
```

模拟 `ViewPager` 添加了 `OnPageChangeListener`

```java
LxAdapter.of(mLxModels)
        .bindItem(new PagerItemBind())
        .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER, new LxSnapComponent.OnPageChangeListener() {

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
            opts.spanSize = Lx.SPAN_SIZE_ALL;
            opts.viewType = Lx.VIEW_TYPE_EXPANDABLE_GROUP;
            opts.layoutId = R.layout.item_group;
            opts.enableFixed = true;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, GroupData data) {
        holder.setText(R.id.section_tv, data.title + " " + (data.expand ? "展开" : "关闭"));
    }

    @Override
    public void onEvent(LxContext context, GroupData listItem, int eventType) {
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
            opts.spanSize = Lx.SPAN_SIZE_ALL;
            opts.viewType = Lx.VIEW_TYPE_EXPANDABLE_CHILD;
            opts.layoutId = R.layout.item_simple;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, ChildData data) {
        holder.setText(R.id.sample_tv, data.title + " ，点击删除");
    }

    @Override
    public void onEvent(LxContext context, ChildData data, int eventType) {
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
List<LxModel> lxModels = LxPacker.pack(Lx.VIEW_TYPE_EXPANDABLE_GROUP, groupDataList);
mLxModels.update(lxModels);
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
class HorizontalImgContainerItemBind extends LxItemBinder<NoNameData> {
    @Override
    protected TypeOpts newTypeOpts() {
      return TypeOpts.make(opts -> {
            opts.viewType = TYPE_HORIZONTAL_CONTAINER;
            opts.layoutId = R.layout.item_horizontal_container;
            opts.spanSize = Lx.SPAN_SIZE_ALL;
        }));
    }

    // 初始化没有 adapter 时的 callback，放在这里是避免多次创建造成性能问题
    // 使用 list 创建一个 Adapter 绑定到 view 上
    private LxNesting.OnNoAdapterCallback callback = (view, list) -> {
        LxAdapter.of(list)
                .bindItem(new HorizontalImgItemBind())
                .attachTo(view, LxManager.linear(view.getContext(), true));
    };

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
        // 获取到控件
        RecyclerView contentRv = holder.getView(R.id.content_rv);
        // 打包横向滑动的数据，这个 type 可自定义
        List<LxModel> packDatas = LxPacker.pack(TYPE_HORIZONTAL_IMG, listItem.datas);
        // 设置，这里会尝试恢复上次的位置，并计算接下来滑动的位置
        LxNesting.setup(contentRv, context.model, packDatas, callback);
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

上面的例子中我们主要使用 `LxPacker` 打包数据，这样相对来说可以尽量少的修改你的数据类，但是每次都需要设置一个类型，否则将会使用默认类型，也可以使用数据类实现 `Typeable` 接口，在接口方法中返回类型，这样打包数据的时候就不需要指定类型了，内部会检测是否是 `Typeable` 子类，获取真正的类型；

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
static class StudentItemBind extends LxItemBinder<Student> {

    StudentItemBind() {
        super(TypeOpts.make(opts -> {
            opts.layoutId = R.layout.item_squire;
            opts.viewType = TYPE_STUDENT;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxViewHolder holder, Student data) {
        Bundle condition = context.condition;
        // 条件更新，数据没有变化
        if (!condition.isEmpty()) {
          // 只更新 title 显示
          boolean needUpdate = condition.getBoolean("update_name", false);
          if (needUpdate) {
            String updateNameContent = condition.getString("update_name_content");
            holder.setText(R.id.title_tv, updateNameContent + "," + data.name);
          }
          return;
        }
        // 其他更新逻辑...
    }

    @Override
    public void onItemEvent(LxContext context, Student listItem, int eventType) {
         adapter.getData().updateSet(context.position, data -> {
            // 发布条件更新，数据不用更改
            Bundle condition = data.getCondition();
            condition.putBoolean("update_name", true);
            condition.putString("update_name_content", "条件更新");
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
            payloads("name_change");
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
        if (context.payloads.isEmpty()) {
            // 没有 payloads 正常绑定数据
            holder.setText(R.id.title_tv, "学：" + data.name);
        } else {
            // 有 payloads 通过 payloads 绑定数据
            for (String payload : context.payloads) {
                if (payload.equals("name_change")) {
                    holder.setText(R.id.title_tv, "payloads：" + data.name);
                }
            }
        }
    }
}
```

<span id="contract"></span>

## 联系我

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