
![](http://olx4t2q6z.bkt.clouddn.com/18-2-6/27323356.jpg)

# LightAdapter

`LightAdapter` 的设计初衷是以 **轻量** 和 **面向业务** 为主要目的，一方面希望可以快速、简单的的完成数据的适配，另一方面针对业务中经常出现的场景能提供统一、简单的解决方案。

> [本文博客地址](http://zfyx.coding.me/article/1632666977/)

> [GitHub - LightAdapter](https://github.com/chendongMarch/LightAdapter)


<!--more-->

<div style="width:100%;display: flex;height:30px;">

<img style="margin-right:20px;"  src="https://img.shields.io/github/stars/chendongMarch/LightAdapter.svg"/>

<img  style="margin-right:20px;"  src="https://img.shields.io/github/forks/chendongMarch/LightAdapter.svg"/>

</div>

## 优势

- 使用简单，只有 `LightAdapter` 一个适配器，获取其中组件进行配置。
- 更好的扩展性，更清晰的代码结构，支持单类型、多类型数据适配。
- 更细粒度的配置，可针对每种数据类型进行自定义配置。
- 更安全(线程检测)、更简单(`DiffUtil`)、更高效(`payloads`) 的数据更新。
- 点击事件支持单击、双击、长按，可自定义开启。
- 经过扩展和完善的 `LightHolder`，几乎可以直接完成所有数据绑定。
- 支持 `Header&Footer` 叠加、数据绑定、即时更新。
- 支持定制列表底部 `LoadingView` 效果。
- 支持空白页面，并可自定义显示、事件。
- 支持 顶部、底部预加载 更多数据。
- 支持常见业务场景 - 选择器效果。
- 支持拖拽、侧滑删除，使用简单、并高度可定制。
- 列表顶部悬停效果，可以为任意一种类型的布局设置顶部悬停效果
- 隔断效果显示，隔断同样支持悬停效果
- 未来：分页器 - 对使用 `pageNo/pageSize` 的分页加载的的业务场景进行支持。
- 未来：ExpandableAdapter
- 未来：AsyncLightDiffList，异步计算 `diff` 并更新数据

## 设计分析

由于功能比较多，当所有的逻辑都在 `Adapter` 里面实现时，会导致 `Adapter` 变得很臃肿，扩展功能变得越来越困难。

为了解决这个问题，类库的设计借鉴了 **委托模式** 的设计方法，`Adapter` 只负责数据的加载，而其他功能都通过注册不同的 **功能代理** 实现，各个代理分工合作，都只关注自己负责的那部分功能，现在内置如下几个代理实现：

- `HFViewDelegate`： 负责 `Header`/`Footer` 的装载、更新、显示、隐藏等功能；
- `LoadingViewDelegate`：负责完成加载更多底部提示的装载、更新、显示、隐藏等功能；
- `EmptyViewDelegate`：负责完成空白页面的装载、更新、显示、隐藏等功能；
- `DragSwipeDelegate`: 负责完成拖拽，侧滑等功能。
- `LoadMoreDelegate`： 负责到达底部加载更多数据的功能；
- `TopMoreDelegate`：负责到达顶部触发加载功能；
- `NotifyDelegate`：负责数据更新的扩展功能；
- `SelectorDelegate`：负责实现数据选择器功能；
- `SpanDelegate`：负责处理某个 `item` 是否跨越整行的功能；

所有的功能代理都统一被 `DelegateRegistry` 管理，他们之间是完全戒内部维护一个注册表，所有的代理都在此处注册，由 `DelegateRegistry` 统一调度，同时，我们也可以根据自己的业务需求向 `DelegateRegistry` 注册代理实现；



## 单类型数据适配

单类型的适配器其实是多类型的简化版本，只是针对大多数的使用场景暴露出来更简单的构造方法，内部仍旧使用多类型实现。

```java
LightAdapter<Student> adapter = new LightAdapter<Student>(getContext(), list, R.layout.adapter_item) {
    @Override
    public void onBindView(LightHolder holder, Student data, int pos) {
        holder.setText(R.id.tv, data.name + " " + data.age);
    }
};
```

## 多类型数据适配

STEP1: 首先数据结构应该继承 `ModelTypeable` 接口，来向外暴露数据的类型;

```java
// 分为了大于 12 岁和小于等于 12 岁两种类型
class Student implements ModelTypeable {

    public static final int TYPE_BIG = 0;
    public static final int TYPE_SMALL = 1;

    public String name;
    public int age;
    public int type;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.type = age > 12 ? TYPE_BIG : TYPE_SMALL;
    }

    @Override
    public int getModelType() {
        return type;
    }
}
```

STEP2: 多类型适配器需要构造一个 `ModelTypeFactory`，他用来决定每种类型的配置，包括:

- 布局资源
- 是否可被点击
- 跨越的列数，支持 `整行` / `一半` / `三分之一` / `具体列数`

```java
ModelTypeFactory factory = modelType -> {
    switch (modelType.getType()) {
        case Student.TYPE_A:
            modelType.setLayout(R.layout.adapter_item);
            break;
        case Student.TYPE_B:
            // 该类型的布局文件
            modelType.setLayout(R.layout.adapter_item_b);
            // 设置该类型跨越整行
            modelType.setSpanSize(Values.SPAN_SIZE_ALL);
            // 设置该类型不允许点击
            modelType.setEnableClick(false);
            // 设置该类型不允许双击
            modelType.setEnableDbClick(false);
            // 设置该类型不允许长按
            modelType.setEnableLongPress(false);
            break;
    }
};
```

STEP3: 借助 `ModelTypeFactory` 可以构建多类型适配器

```java
mStudentAdapter = new LightAdapter<Student>(getContext(), mStudents, factory) {
    @Override
    public void onBindView(LightHolder holder, Student data, int pos) {
        holder.setText(R.id.tv, data.name + " " + data.age);
    }
};
```


## 触摸事件

默认每个 `Item` 支持单击事件和长按事件，因为支持双击事件会导致回调的事件变长，所以默认不去支持双击事件，如果想要开启双击事件，需要在构造 `ModelType` 时针对类型开启；

```java
ModelTypeFactory factory = modelType -> {
    switch (modelType.getType()) {
        case Student.TYPE_A:
            modelType.setLayout(R.layout.adapter_item);
            break;
        case Student.TYPE_B:
            modelType.setLayout(R.layout.adapter_item_b);
            // 支持双击事件
            modelType.setEnableDbClick(true);
            break;
    }
};
```

然后调用 `adapter.setOnItemListener()` 重写相关的事件方法，接受触摸事件的回调。

```java
// 添加点击事件
adapter.setOnItemListener(new SimpleItemListener<Student>() {
    @Override
    public void onClick(int pos, LightHolder holder, Student data) {
    	// 单击事件
    }
    @Override
    public void onLongPress(int pos, LightHolder holder, Student data) {
	// 长按事件
    }
    @Override
    public void onDoubleClick(int pos, LightHolder holder, Student data) {
        // 双击事件
    }
});
```

## LightHolder

为了支持同时对多个控件进行一样的绑定操作，可以使用 `Ids` 来包含多个 `id`

```java
Ids ids = Ids.all(R.id.test_tv, R.id.tv_count);
```
为了更好的性能，每个 `Adapter` 会维护一个可复用的 `Ids`， 因此在 `Adapter` 建议直接使用 `all()` 方法创建，避免每次都创建新的。

```java
new LightAdapter<Student>(getContext(), list, R.layout.adapter_item)
    @Override
    public void onBindView(LightHolder holder, Student data, int pos) {
        holder
                // 单个控件绑定
                .setVisibility(R.id.tv, View.GONE)
                // 在 Adapter 中可以直接使用 all() 方法绑定多个控件
                .setVisibility(all(R.id.tv_count, R.id.test_tv), View.GONE)
                // 不在 Adapter 中可以使用 holder.all()
                .setVisibility(holder.all(R.id.tv_count, R.id.test_tv), View.GONE)
                // 最差的方法使用 Ids.all() 每次都会创建新的
                .setVisibility(Ids.all(R.id.tv_count, R.id.test_tv), View.GONE);
    }
};
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
        // 设置 check
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
        // 给 itemView 设置 LayoutParams
        .setLayoutParams(100, 100)
        // 给指定控件设置 LayoutParams
        .setLayoutParams(R.id.test_tv, 100, 100)
        // 点击事件
        .setClick(R.id.test_tv, view -> {
            ToastX.show("点击事件");
        })
        // 长按事件
        .setLongClick(R.id.test_tv, view -> {
            ToastX.show("长按事件");
            return true;
        })
        // 使用回调风格，LightHolder.IMAGE 用来声明范型类型
        .setCallback(R.id.tv, LightHolder.IMAGE, imgView -> {
            Glide.with(imgView.getContext()).load("url").into(imgView);
        });
```

关于使用回调来绑定数据主要是为了不破坏链式编程的风格，他会查找到指定的 `View` 并回调绑定数据的方法，使用 `LightHolder.IMAGE`/`LightHolder.TEXT` 来声明范型可以更好的结合 `lambda` 表达式来简化代码。

```java
// 使用回调风格，LightHolder.IMAGE 用来声明范型类型
holder.setCallback(R.id.tv, LightHolder.IMAGE, imgView -> {
    Glide.with(imgView.getContext()).load("url").into(imgView);
})
```

你也可以定义自己的 `Callback` 来处理某些常用场景的数据加载，例如使用 `Glide` 加载图片：

```java
static class GlideCallback implements LightHolder.Callback<ImageView> {

    private RequestOptions mOptions;
    private String mUrl;

    public GlideCallback(String url, RequestOptions options) {
        mOptions = options;
        mUrl = url;
    }

    @Override
    public void bind(ImageView view) {
        Glide.with(view.getContext()).load(mUrl)
                .apply(mOptions)
                .into(view);
    }
}
```

在绑定数据时使用

```java
// 自定义图片加载的 callback 配置加载参数
holder.setCallback(R.id.tv, new GlideCallback("imgUrl", RequestOptions.overrideOf(100, 100).placeholder(R.drawable.wx_logo)));
```



## Header&Footer


使用 `adapter.header()` 方法获取 `HFDelegate` 对 `Header` 进行操作

```java
// 使用布局资源添加一个 Header，你可以像在 adapter 中使用 holder 绑定数据
adapter.header().addHeaderView(R.layout.adapter_item_header, (holder, position) -> {
    holder.setText(R.id.header_tv, headerDesc);
});
// 使用创建好的 View 添加一个 Header
adapter.header().addHeaderView(new ImageView(getContext()), (holder, position) -> {
    // 绑定数据
});
// 显示/隐藏 Header
adapter.header().setHeaderEnable(true);
// 清除添加的所有 Header
adapter.header().clearHeaderView();
// 更新 Header，此方法会调用添加 Header 时的绑定方法
adapter.header().notifyHeaderUpdate();
```

使用 `adapter.footer()` 方法获取 `HFDelegate` 对 `Footer` 进行操作

```java
// 使用布局资源添加一个 Footer，你可以像在 adapter 中使用 holder 绑定数据
adapter.footer().addFooterView(R.layout.adapter_item_footer, (holder, position) -> {
    holder.setText(R.id.footer_tv, headerDesc);
});
// 使用创建好的 View 添加一个 Footer
adapter.footer().addFooterView(new ImageView(getContext()), (holder, position) -> {
    // 绑定数据
});
// 显示/隐藏 Footer
adapter.footer().setFooterEnable(true);
// 清除添加的所有 Footer
adapter.footer().clearFooterView();
// 更新 Header，此方法会调用添加 Footer 时的绑定方法
adapter.footer().notifyFooterUpdate();
```

## 加载更多

使用 `adapter.loadMore()` 获取 `LoadMoreDelegete` 添加底部加载更多的监听，当列表滑动到底部时，会触发该事件：

```java
// 添加加载更多事件
// count = 3 表示提前 3 个 item 到达底部开始加载
adapter.loadMore().setLoadMoreListener(3, adapter -> {
    // 请求数据
    fetchData((data) -> {
        // 存储和数据更新
        saveUpdateData(data);
        // 结束加载，才能开始新的加载
        adapter.loadMore().finishLoadMore();
    });
});
```

使用 `adapter.topMore()` 获取 `TopMoreDelegete` 添加顶部加载更多的监听，当列表滑动到顶部时，会触发该事件：

```java
adapter.topMore().setTopMoreListener(3, adapter -> {
    // 请求数据
    fetchData((data) -> {
        // 存储和数据更新
        saveUpdateData(data);
        // 结束加载
        adapter.topMore().finishTopMore();
    });
});
```

## 选择器

在开发过程中我们经常会遇到 **选中** 和 **取消选中** 列表中的某一项这种需求，针对这种场景增加了 `SelectorDelegate`，他负责实现选择器的选中、取消选中、数据更新等逻辑：

为了能够存储和获取选择器的状态，数据结构需要实现 `Selectable` 接口，来表明它是一个可以支持选择器的数据类型。

```java
public class Student implements Selectable {

    public boolean selected;

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        selected = isSelected;
    }
}
```

使用 `adapter.selector()` 获取 `SelectorDelegate` 来初始化选择器需要的参数：

- 类型，支持多选和单选，单选时，选中一个会自动取消其他选中。
- 绑定回调，用来根据是否选中的状态来显示不同的 `UI`，如果需要使用具体的数据，可以将 `Selectable` 对象强转转换为目标对象；


```java
mStudentAdapter.selector().setSelectorBinder(LightValues.SINGLE, (holder, position, obj) -> {
    holder.setTextColor(R.id.tv, obj.isSelected() ? Color.GREEN : Color.RED);
});
```

然后可以在点击事件中选中或者取消选中某一项

```java
mStudentAdapter.setOnItemListener(new SimpleItemListener<Student>() {
    @Override
    public void onClick(int pos, LightHolder holder, Student data) {
        // 选中切换为不选中，不选中切换为选中
        mStudentAdapter.selector().toggleItem(data);
        // 选中
        mStudentAdapter.selector().selectItem(data);
        // 不选中
        mStudentAdapter.selector().releaseItem(data);
    }
});
```
## 数据更新

可以使用 `NotifyDelegate` 代替 `Adapter` 进行数据的更新，与 `Adapter` 相比，`NotifyDelegate` 会把所有的更新操作发布到主线程执行，避免不小心在 **子线程** 更新造成闪退的问题。

使用 `adapter.notifyItem()` 获取内部的 `NotifyDelegate` 然后调用相应的更新方法，他和 `Adapter` 的更新数据的方法是一一对应的，只是他会帮你检测当前线程避免线程引起的更新问题。

```java
adapter.notifyItem().change(2);
adapter.notifyItem().change(2, 20);
adapter.notifyItem().change(2, 20, null);
adapter.notifyItem().insert(2);
adapter.notifyItem().insert(2, 20);
adapter.notifyItem().remove(2);
adapter.notifyItem().remove(2, 20);
adapter.notifyItem().move(10, 20);
```

### LightDiffList

我们更建议使用 `LightDiffList` 来更新数据，`LightDiffList` 是 `List` 的子类，内部使用 `DiffUtil` 实现了数据的自动比对和更新，使用 `DiffUtil` 可以把您从如何更新数据的困境中解放出来。

首先使用 `LightDiffList` 直接替换掉原来的数据源即可

```java
// 将
private List<Student> mStudents = new ArrayList<>();
// 替换为
private LightDiffList<Student> mStudents = new LightDiffList<>();
```

为了可以使用 `DiffUtil` 自动进行数据对比，我们的数据结构应该实现 `Diffable` 接口来告知 `LightDiffList` 如何对数据进行比对更新。

```java
public class DiffableStudent implements Diffable<DiffableStudent> {

    public static final String MSG_NAME_CHANGED = "MSG_NAME_CHANGED";

    public String name;
    public int age;
    public int id;

    /* Diffable 继承了 Parcelable ，因此要是实现 Parcelable 接口*/
    // 此处实现 Parcelable 接口

    @Override
    public boolean areItemsTheSame(DiffableStudent newItem) {
        return id == newItem.id;
    }
    @Override
    public boolean areContentsTheSame(DiffableStudent newItem) {
        return name.equals(newItem.name) && age == newItem.age;
    }
    @Override
    public Set<String> getChangePayload(DiffableStudent newItem) {
        Set<String> set = new HashSet<>();
        if (!name.equals(newItem.name)) {
            set.add(MSG_NAME_CHANGED);
        }
        return set;
    }
}
```

上面提到的 3 个方法与 `DiffUtil` 中的 `Callback` 一致，可以参考该接口的文档，下面做简单解释：

- `areItemsTheSame` ：是否为同一个 `Item`，如果你的数据结构有 `id`，则应该使用 `id` 比对，否则可以使用 `equals`，如果返回 `false` 会调用 `notifyItemRangeRemoved` / `notifyItemRangeInserted` 来更新数据。
- `areContentsTheSame` ：`Item` 内容是否相同，只有当 `areItemsTheSame` 返回 `true` 时会调用该方法，如果返回 `false` 会调用 `notifyItemRangeChanged` 来更新数据。
- `getChangePayload` ：增量更新数据，使用该方法可以避免每次都将整个 `Item` 无脑的 `Bind` 一遍，那样会导致那行出现明显的闪烁现象，此方法只有在 `areContentsTheSame` 返回 `false` 才会调用，此方法在下小节我们会更细致的介绍。

使用了 `LightDiffList` 之后我们就可以告别 `Adapter` 的 `notify` 方法了，而是使用 `LightDiffList` 的 `update` 方法，

```java
// 获取数据快照，对快照数据只能做增删操作
// 更新操作需要使用 update 方法
// 对数据更新完毕后，使用 update 提交更新
List<Student> students = mStudents.snapshot();
students.add(new Student());
mStudents.update(students);

// 在原有数据后拼接新数据，并更新
List<Student> list = ListX.range(30, index -> new Student("name" + (index), (index)));
mStudents.append(list);

// 原有数据 与 新数据 做 diff，并更新
mStudents.update(list);

// 遍历所有数据，根据条件查找指定 item，并对其更新
// 如下：查找年龄大于10的学生将其年龄改为100
mStudents.update(student -> {
    return student.age > 10;
}, student -> {
    student.age = 100;
});

// 更新某个位置的数据
mStudents.update(100, student -> {
    student.age = 100;
});
```

### payloads

上面提到过了使用 `payloads` 来增量更新数据，使用该方法可以避免每次都将整个 `Item` 无脑的 `Bind` 一遍，那样会导致那行出现明显的闪烁现象，此方法只有在 `areContentsTheSame` 返回 `false` 才会调用。

首先在数据结构中我们需要对数据进行比对，并返回一个 `Msg` 的列表，如下当两个数据的 `name` 不同时，我们返回添加一个 `MSG_NAME_CHANGED` 的消息来标记此次变化。

```java
public class DiffableStudent implements Diffable<DiffableStudent> {

    public static final String MSG_NAME_CHANGED = "MSG_NAME_CHANGED";

    //... 其他代码之前介绍过了，暂时不关注

    @Override
    public Set<String> getChangePayload(DiffableStudent newItem) {
        Set<String> set = new HashSet<>();
        if (!name.equals(newItem.name)) {
            set.add(MSG_NAME_CHANGED);
        }
        return set;
    }
}
```

在 `Adapter` 的绑定过程中，我们要再重写 `onBindViewUsePayload` 方法来处理使用 `payloads` 更新显示的情况，在 `onBindViewUsePayload` 方法中根据传递过来的 `Msg` 不同选择性的去绑定指定的控件。

需要注意的是 `onBindViewUsePayload` 是对 `onBindView` 的补充，`onBindView` 仍然要完成完整的数据绑定过程，但是当数据只是局部改变时，我们可以使用 `payload` 获得更好的性能和刷新体验。


```java
new LightAdapter<Student>(getContext(), mStudents, factory) {

    // 一般绑定数据
    @Override
    public void onBindView(LightHolder holder, Student data, int pos) {
        holder.setText(R.id.tv, data.name + " " + data.age);
    }

    // 使用 payload 绑定数据
    @Override
    public void onBindViewUsePayload(LightHolder holder, Student data, int pos, String msg) {
        switch (msg) {
            case Student.MSG_NAME_CHANGED:
                holder.setText(R.id.tv, data.name + " " + data.age);
                break;
        }
    }
};
```


