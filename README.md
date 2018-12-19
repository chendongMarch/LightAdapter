
![](https://images.pexels.com/photos/841120/pexels-photo-841120.jpeg)

# LightAdapter

`LightAdapter` 的设计初衷是以 **轻量** 和 **面向业务** 为主要目的，一方面希望可以快速、简单的的完成数据的适配工作，另一方面针对业务中经常出现的场景能提供统一、简单的解决方案。

> [本文博客地址](http://zfyx.coding.me/article/1632666977/)

> [GitHub - LightAdapter](https://github.com/chendongMarch/LightAdapter)


<!--more-->

<div style="width:100%;display:flex;height:30px;">

<img style="margin-right:20px;"  src="https://img.shields.io/github/stars/chendongMarch/LightAdapter.svg"/>

<img  style="margin-right:20px;"  src="https://img.shields.io/github/forks/chendongMarch/LightAdapter.svg"/>

</div>

## Feature

- 使用唯一的 `LightAdapter` 完成单类型、多类型数据适配。
- 良好的扩展性，可自定义实现 `Delegate` 扩展新的功能。
- 对数据类型更细粒度配置，可针对类型设置点击事件、拖拽、侧滑、悬挂等。
- 自动检测线程，保证所有数据更新发布在主线程。
- 使用 `DiffUtil` 实现更高效（`payloads`）、更简单（`LightList`）的数据更新。
- 点击事件支持单击事件、双击事件、长按事件。
- 扩展 `ViewHolder`，借助 `LightHolder` 可以更简单的实现数据绑定。
- 支持添加 `Header`/`Footer`，叠加、数据绑定、灵活更新。
- 支持在列表底部定制 `LoadingView` 效果。
- 支持空白页面显示，并可自定义显示、事件。
- 支持 列表顶部、列表底部，预加载更多数据。
- 支持拖拽排序、侧滑删除，一行代码绑定相关事件。
- 支持常见业务场景 - 快速实现选择器单选/多选效果。
- 支持隔断显示，隔断支持悬挂效果，理论上所有类型的布局均支持悬挂。
- 支持动画，`ItemAnimator` / `BindAnimator` 两种方式实现。
- 支持假数据展示，先展示假数据列表，数据获取后显示为真实数据。

## 设计分析

由于功能比较多，当所有的逻辑都在 `Adapter` 里面实现时，会导致 `Adapter` 变得很臃肿，代码阅读和扩展功能变得越来越困难。

为了解决这个问题，类库的设计借鉴了 **委托模式** 的设计方法，`Adapter` 只负责数据的加载，而其他功能都通过注册不同的 **功能代理** 实现，各个代理分工合作，都只关注自己负责的那部分功能：

- `HFViewDelegate`： 负责 `Header`/`Footer` 的装载、更新、显示、隐藏等功能；
- `LoadingViewDelegate`：负责完成加载更多底部提示的装载、更新、显示、隐藏等功能；
- `EmptyViewDelegate`：负责完成空白页面的装载、更新、显示、隐藏等功能；
- `DragSwipeDelegate`: 负责完成拖拽，侧滑等功能。
- `LoadMoreDelegate`： 负责到达底部加载更多数据的功能；
- `TopMoreDelegate`：负责到达顶部触发加载功能；
- `NotifyDelegate`：负责数据更新的扩展功能；
- `SelectorDelegate`：负责实现数据选择器功能；
- `SpanDelegate`：负责完成不同类型数据跨越不同列数的功能；
- `FakeDelegate`: 负责完成填充假数据显示功能。
- `SectionDelegate`: 负责完成隔断显示和隔断功能。
- `AnimatorDelegate`: 负责完成列表项动画显示功能。

所有的功能代理都统一被 `DelegateRegistry` 管理，他们之间是完全解耦的，`DelegateRegistry` 内部维护一个注册表，所有的代理都在此处注册，由 `DelegateRegistry` 统一调度，同时，我们也可以根据自己的业务需求向 `DelegateRegistry` 注册代理实现；

## 内置的数据类型

### ModelType

通常我们的数据类型使用一个 `int` 值来表示，但是当业务变得相对复杂时，一个单纯的 `int` 类型已经不足够表达这个类型所包含的信息，因此针对类型这个概念定义了 `ModelType` 类;

```java
public class ModelType {
    public int type; // 数据类型
    public int layoutId; // 布局资源
    public int     spanSize        = SpanSize.NONE; // 跨越列数
    public boolean enableClick     = true; // 是否允许点击事件
    public boolean enableLongPress = true; // 是否允许长按事件
    public boolean enableDbClick   = false; // 是否允许双击事件
    public boolean enableDrag      = false; // 是否允许拖动，不需要手动设置
    public boolean enableSwipe     = false; // 是否允许滑动，不需要手动设置
    public boolean enablePin       = false; // 钉住，支持悬停效果
    public BindAnimator animator; // 加载动画效果
}
```

### Extra

主要用来承载一些状态数据，使用 `Extra` 来统一管理这些状态可以获得更好的扩展性；

```java
public class Extra {

    // 数据集索引 和 布局索引 的差异是因为内置了很多自定义的类型，比如 Header 等，因此他们并不一致
    // 原则就是操作数据，则使用 modelIndex, 操作布局则使用 layoutIndex

    /**
     * 数据集索引，使用他从集合中获取数据
     * adapter.getDatas().get(modelIndex)
     */
    public int modelIndex;
    /**
     * 布局索引，使用它来更新界面显示
     * adapter.notifyItem.change(layoutIndex)
     */
    public int layoutIndex;
    /**
     * 用来标记当前数据是否处于绑定状态，配合选择器使用
     */
    public boolean selected;
    /**
     * 子控件 id
     * 配合 {@link LightAdapter#setChildViewClickEvent(EventCallback)} 使用
     */
    public int     viewId;
    /**
     * 使用 payload 绑定时的 msg
     */
    public String  payloadMsg;
    /**
     * 当前是不是 payload 更新
     */
    public boolean byPayload;
}
```

## 快速构建适配器

### 单类型数据适配

单类型数据适配，针对较简单的场景;

```java
LightAdapter<Data> adapter = new LightAdapter<>(list,R.layout.item_content);
```

为了支持更细化的配置，可以使用 `ModelType` 构造 `Adapter`;

```java
ModelType modelType = ModelType.singleType(R.layout.item_content)
        .animator(new ScaleAnimator()) // 设置 BindAnimator
        .enableDbClick(true); // 开启双击事件检测
LightAdapter<Data> adapter = new LightAdapter<>(list, modelType);
```

### 多类型数据适配

`STEP1`: 首先数据结构要实现 `Typeable` 接口暴露自己的类型；

```java
class Data implements Typeable {
    int type;
    @Override
    public int getItemType() {
        return type;
    }
}
```

`STEP2`: 借助 `ModelTypeRegistry` 来管理多种类型的注册和配置；

```java
// ModelType 注册表
ModelTypeRegistry registry = new ModelTypeRegistry();
// 第一种类型，注册一个复杂的类型
ModelType modelType = ModelType.multiType(Data.TYPE_BASIC, R.layout.item_basic)
        .animator(new ScaleAnimator()) // 设置 BindAnimator
        .enableDbClick(true); // 开启双击事件检测
registry.add(modelType);
// 第二种类型
registry.add(Data.TYPE_CONTENT, R.layout.item_content);
// 使用 ModelTypeRegistry 构造 Adapter
LightAdapter<Data> adapter = new LightAdapter<>(list, registry);
```

### 拆分可复用的类型

在实际开发中，一些类型会多次出现在不同的列表中，可以借助 `LightItemAdapter` 将每种类型的数据适配分离出来，使每种类型可被快速的复用到其他的列表中;

```java
// 视频类型
class VideoItemAdapter extends LightItemAdapter<Data> {
    @Override
    public ModelType newModelType() {
        return ModelType.singleType(Data.TYPE_VIDEO, R.layout.item_video);
    }
    @Override
    public void onBindView(LightHolder holder, Data data, Extra extra) {
        // bind video data
    }
}
// 音频类型
class AudioItemAdapter extends LightItemAdapter<Data> {
    @Override
    public ModelType newModelType() {
        return ModelType.singleType(Data.TYPE_AUDIO, R.layout.item_audio);
    }
    @Override
    public void onBindView(LightHolder holder, Data data, Extra extra) {
        // bind audio data
    }
}
```

上面定义的视频类型和音频类型可以被灵活的插入到其他列表当中。

```java
// ModelType 注册表
ModelTypeRegistry registry = new ModelTypeRegistry();
// 添加一种普通类型
registry.add(Data.TYPE_CONTENT, R.layout.item_content);
// 添加可被复用视频类型
registry.add(new VideoItemAdapter());
// 添加可被复用音频类型
registry.add(new AudioItemAdapter());
// 使用 ModelTypeRegistry 构建 Adapter
LightAdapter<Data> adapter = new LightAdapter<>(list, registry);
```


### 数据绑定

类库中 `LightAdapter` 没有使用抽象类，数据的绑定通过一个回调函数完成，创建 `Adapter` 后设置数据绑定回调函数即可完成数据绑定操作。

类库支持使用 `payloads` 局部刷新数据，这更加高效，通过 `extra.byPayload` 判断是否是局部刷新，使用 `extra.payloadMsg` 确定本次局部刷新数据的类型，这部分还需要结合后面数据更新的部分介绍，暂时只说一下用法，详细可以查看后文数据更新部分。

```java
// 设置数据绑定回调
mMyAdapter.setBindCallback((holder, data, extra) -> {
    // 判读是否使用 payloads 局部更新数据
    if (extra.byPayload) {
        switch (extra.payloadMsg) {
            // 获取到仅更新 name 的消息
            case NAME_CHANGED:
                // 仅重新绑定 name 即可
                holder.setText(R.id.name_tv, data.name);
                break;
        }
        return;
    }
    // 绑定整个条目的数据
    holder.setText(R.id.all_data, data);
});
```

## 点击事件和手势

默认每个列表项支持单击事件和长按事件，但是因为支持双击事件的会导致事件监测的时间变长，所以默认不去支持双击事件，如果想要开启双击事件，需要在构造 `ModelType` 时针对类型开启；

```java
 // 列表项单击事件
adapter.setClickEvent((holder, data, extra) -> {
});
// 列表项长按事件
adapter.setLongPressEvent((holder, data, extra) -> {
});
// 列表项双击事件
modelType.enableDbClick = true;
adapter.setDbClickEvent((holder, data, extra) -> {
});
// 子 View 单击事件，需要配合 LightHolder 绑定
// 没有 listener 将会把事件发送到这边处理
holder.setClick(R.id.tv);
adapter.setChildViewClickEvent((holder, data, extra) -> {
	switch(extra.viewId) {

	}
});
// 子 View 长按事件，需要配合 LightHolder 绑定
// 没有 listener 将会把事件发送到这边处理
holder.setLongClick(R.id.tv);
adapter.setChildViewLongPressEvent((holder, data, extra) -> {
	switch(extra.viewId) {

	}
});
```


## 被扩展的 LightHolder

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
        .swipeOnTouch(R.id.tv)
        // 使用回调风格，LightHolder.IMAGE 用来声明范型类型
        .setCallback(R.id.tv, LightHolder.IMAGE, imgView -> {
            Glide.with(imgView.getContext()).load("url").into(imgView);
        })
        // 将 glide 加载封装成单独的 callback，直接使用
        .setCallback(R.id.tv, new GlideCallback("url"));
```

## 功能：添加 Header 和 Footer

主要用于在列表顶部和底部添加布局，并且可随时更新布局显示等业务场景，使用 `adapter.header()` 方法获取 `header` 代理对象进行操作；

```java
// 获取 Header 功能代理对象
HeaderRef header = mMyAdapter.header();

// 使用布局文件创建 LightView
LightView lightView = LightView.from(R.layout.adapter_item_header);
// OR 同样也支持使用 View 对象创建 LightView
LightView lightView = LightView.from(new ImageView(context));

// 添加一个 Header，并在回调中绑定数据显示
header.addHeaderView(lightView, holder -> {
    holder.setText(R.id.header_tv, headerDesc);
});
// 更新 Header 的数据绑定
header.notifyHeaderUpdate();
// 显示 / 隐藏 Header
header.setHeaderEnable(true);
// 清除添加的所有 Header
header.removeAllHeaderViews();
// 删除指定的某个 Header
header.removeHeaderView(lightView);
// 获取 Header 布局的父容器
ViewGroup headerView = header.getHeaderView();
// header 当前的状态
boolean headerEnable = header.isHeaderEnable();
```

使用 `adapter.footer()` 方法获取 `footer` 代理对象进行操作；

```java
// 获取 Header 功能代理对象
FooterRef footer = mMyAdapter.footer();

// 使用布局文件创建 LightView
LightView lightView = LightView.from(R.layout.adapter_item_footer);
// OR 同样也支持使用 View 对象创建 LightView
LightView lightView = LightView.from(new ImageView(context));

// 添加一个 Header，并在回调中绑定数据显示
footer.addFooterView(lightView, holder -> {
    holder.setText(R.id.footer_tv, footerDesc);
});
// 更新 Header 数据绑定
footer.notifyFooterUpdate();
// 显示 / 隐藏 Footer
footer.setFooterEnable(true);
// 清除添加的所有 Footer
footer.removeAllFooterViews();
// 删除指定的某个 Footer
footer.removeFooterView(lightView);
// 获取 Footer 布局的父容器
ViewGroup footerView = footer.getFooterView();
// header 当前的状态
boolean footerEnable = footer.isFooterEnable();
```

## 功能：加载更多数据(LoadMore)

主要用于到达列表顶部和列表底部触发加载更多数据的业务场景，使用 `adapter.loadMore()` 获取 `loadMore` 代理对象；

```java
// 获取 loadMore 代理对象
LoadMoreRef loadMore = mMyAdapter.loadMore();
// 设置加载更多监听，提前预加载 10 个，默认 3 个
loadMore.setLoadMoreListener(10, adapter -> {
    // 加载数据
    loadDatas();
    // 结束加载更多，开启下次检测
    loadMore.finishLoadMore();
});
// 设置是否加载加载更多
loadMore.setLoadMoreEnable(false);
```

使用 `adapter.topMore()` 获取 `topMore` 代理对象；

```java
// 获取 topMore 代理对象
TopMoreRef topMore = mMyAdapter.topMore();
topMore.setTopMoreListener(10, adapter -> {
    // 加载数据
    loadDatas();
    // 结束加载，开启下次检测
    topMore.finishTopMore();
});
// 设置是否支持顶部加载更多
topMore.setTopMoreEnable(false);
```

## 功能：选择器(Selector)

主要用于在列表中实现选择器的需求，单选、多选、状态变化等业务场景，使用 `adapter.selector()` 获取选择器代理实现：


```java
// 获取 selector 代理实现
SelectorRef<String> selector = mMyAdapter.selector();
// 设置单选模式，数据绑定
selector.setSingleSelector((holder, data, extra) -> {
    holder.setText(R.id.tv, extra.selected ? "选中" : "不选中");
});
// 设置多选模式，数据绑定
selector.setMultiSelector((holder, data, extra) -> {
    holder.setText(R.id.tv, extra.selected ? "选中" : "不选中");
});
// 获取单选的结果
String result = selector.getResult("default value");
// 获取多选的结果
List<String> results = selector.getResults();
// 该数据是否被选中
boolean isSelect = selector.isSelect(data);
// 取消选中该元素
selector.releaseItem(data);
// 选中该元素
selector.selectItem(data);
// 切换状态，选中改为不选中，不选中改为选中
selector.toggleItem(data);
// 设置选择监听，返回 false 将阻止选择操作
selector.setOnSelectListener((data, toSelect) -> {
    // 返回 false 将阻止这次选择操作，用于最多选择 10 个这种场景
    return true;
});
```

支持滑动选中，具体效果可参考 `QQ` 相册照片选择效果，如果需要使用滑动选中，只需要在 `xml` 中使用 `SlidingSelectLayout` 即可，不需要做其他操作。

```xml
<com.zfy.adapter.assistant.SlidingSelectLayout
    android:id="@+id/ssl"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.RecyclerView
        android:id="@+id/content_rv"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</com.zfy.adapter.assistant.SlidingSelectLayout>
```

## 功能：数据绑定动画效果(Animator)

主要用于需要在数据更新时显示动画效果的业务场景，使用 `adapter.animator()` 获取动画功能代理实现；

说明一下，动画被分为了两种类型

- `BindAnimator` 实际是在绑定数据时对 `View` 执行动画操作
- `ItemAnimator` 是 `RecyclerView` 官方的动画实现

 这部分参考 [wasabeef-recyclerview-animators](https://github.com/wasabeef/recyclerview-animators) 实现，它可以提供更多动画类型的实现。

```java
// 获取动画代理实现
AnimatorRef animator = mMyAdapter.animator();
// 关闭动画效果
animator.setAnimatorEnable(false);

// 使用 BindAnimator
BindAnimator scaleAnimator = new ScaleAnimator(0.5f)
        .interceptor(new OvershootInterpolator())
        .duration(500);
// 针对某个类型设置动画效果
modelType.animator = scaleAnimator;
// 设置动画效果，所有 Item 都会执行该动画
animator.setBindAnimator(scaleAnimator);

// 使用 ItemAnimator,
animator.setItemAnimator(new ScaleInBottomAnimator());
```

## 功能：安全的数据更新(Notify)

主要用于简化数据更新操作，自动线程检测，避免数据更新错误等场景，使用 `adapter.notifyItem()` 获取数据更新代理实现。

在开发中经常会出现不小心在子线程发布数据更新造成无法更新列表数据的问题，针对这种情况，提供了数据更新的功能代理，内部会判断当前所在线程，如果在子线程会将更新操作发布到主线程进行；


```java
NotifyRef notifyRef = mMyAdapter.notifyItem();
// 同 adapter.notifyDataSetChanged();
notifyRef.change(2);

// 同 adapter.notifyItemRangeChanged(2, 20);
notifyRef.change(2, 20);

// 同 adapter.notifyItemRangeChanged(2, 20, null);
notifyRef.change(2, 20, null);

// 同 adapter.notifyItemInserted(2);
notifyRef.insert(2);

// 同 adapter.notifyItemRangeInserted(2, 20);
notifyRef.insert(2, 20);

// 同 adapter.notifyItemRemoved(2);
notifyRef.remove(2);

// 同 adapter.notifyItemRangeRemoved(2, 20);
notifyRef.remove(2, 20);

// 同 adapter.notifyItemMoved(10, 20);
notifyRef.move(10, 20);
```


## 功能：加载中效果(LoadingView)

主要用于在列表底部增加一个 `LoadingView` 并且可根据状态变换场景的业务场景，使用 `adapter.loadingView()` 获取他的代理实现

```java
LoadingViewRef loadingView = mMyAdapter.loadingView();
// 获取 loadingView 状态
loadingView.isLoadingEnable();
// 设置 loadingView 状态
loadingView.setLoadingEnable(true);
// 设置 loadingView 布局和数据绑定
LightView view = LightView.from(R.layout.loading_view);
loadingView.setLoadingView(view, (holder, data, extra) -> {
    switch (data.state) {
        case LoadingState.INIT: // 初始化
            break;
        case LoadingState.FINISH: // 结束加载
            break;
        case LoadingState.LOADING: // 加载中
            break;
        case LoadingState.NO_DATA: // 无数据
            break;
        case CUSTOM_STATE: // 自定义的状态
            break;
    }
});
// 手动设置状态
loadingView.setLoadingState(LoadingState.NO_DATA);
loadingView.setLoadingState(CUSTOM_STATE);
```

## 功能：添加空白页(EmptyView)

主要列表中出现错误、失败、无数据等状态时空白页的显示，使用 `adapte.emptyView()`  获取空白页代理实现；

```java
// 获取 empty view 功能代理实现
EmptyViewRef emptyViewRef = mMyAdapter.emptyView();
// 获取 empty view 当前状态
boolean emptyEnable = emptyViewRef.isEmptyEnable();
LightView view = LightView.from(R.layout.empty_view);
emptyViewRef.setEmptyView(view, (holder, data, extra) ->{
    switch (data.state) {
        case EmptyState.NONE: // 隐藏空白页
            break;
        case EmptyState.ERROR: // 错误
            break;
        case EmptyState.SUCCESS: // 成功
            break;
        case EmptyState.NO_DATA: // 无数据
            break;
        case CUSTOM_STATE: // 自定义的状态
            break;
    }
});
// 隐藏空白页
emptyViewRef.setEmptyState(EmptyState.NONE);
// 设置为自定义状态
emptyViewRef.setEmptyState(CUSTOM_STATE);
```

## 功能：隔断显示和悬挂(Section)

主要用于在列表中添加隔断，并且让隔断支持悬挂在列表顶部的业务场景，使用 `adapter.section()` 获取隔断代理实现；

```java
SectionRef<String> section = mMyAdapter.section();
// 是否支持悬挂
section.setPinEnable(true);
// 设置隔断的布局和数据绑定
section.setOptions(R.layout.item_section, true, (holder, data, extra) -> {
    holder.setText(R.id.title_tv, data);
});
```
数据结构需要实现 `Sectionable` 接口，返回 `true` 时表明这是一个隔断数据，他的数据绑定将会被类库内部接管，不过这个数据需要自己来构造；

```java
class Student implements Typeable, Sectionable {
    int type;
    @Override
    public int getItemType() {
        return type;
    }
    @Override
    public boolean isSection() {
        return type > 0;
    }
}
```

## 功能：拖拽和侧滑(drag/swipe)

主要用于实现拖拽排序和侧滑删除功能，使用 `adapter.dragSwipe()` 获取代理实现；

```java
DragSwipeRef dragSwipeRef = mMyAdapter.dragSwipe();

// 配置拖动和侧滑的一些自定义配置项
DragSwipeOptions options = new DragSwipeOptions();
// 设置仅支持左右拖动
options.dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
// 滑动超过 0.7 触发 swipe 事件
options.swipeThreshold = 0.7f;
dragSwipeRef.setOptions(options);

// 设置监听事件，可以在想应的时机更改 UI 的显示
dragSwipeRef.setDragSwipeCallback((holder, data, extra) -> {
    switch (data.state) {
        case DragSwipeState.ACTIVE_DRAG:
            // 开始拖动，更改显示
            break;
        case DragSwipeState.RELEASE_DRAG:
            // 结束拖动，更改显示
            break;
        case DragSwipeState.ACTIVE_SWIPE:
            // 开始侧滑，更改显示
            break;
        case DragSwipeState.RELEASE_SWIPE:
            // 结束侧滑，更改显示
            break;
    }
});
```
当然仅仅进行配置是没办法正常使用拖拽和侧滑功能的，还需要一个触发时机，这个可以在 `Adapter` 的数据绑定时指定，如下：

```java
mMyAdapter.setBindCallback((holder, data, extra) -> {
    holder
            // 设置触发触发拖拽
            .dragOnTouch(R.id.title_tv, R.id.desc_tv)
            // 设置长按触发拖拽
            .dragOnLongPress(R.id.title_tv, R.id.desc_tv)
            // 设置触摸触发侧滑
            .swipeOnTouch(R.id.title_tv, R.id.desc_tv)
            // 设置长按触发侧滑
            .swipeOnLongPress(R.id.title_tv, R.id.desc_tv);
});
```


## 功能：填充假数据(fake)

主要用于显示假数据列表，真实数据回来后再更新显示，目前功能比较简单，使用 `adapter.fake()` 获取代理实现：

```java
FakeRef<String> fake = mMyAdapter.fake();
// 显示假数据，指定假数据个数，布局和数据绑定
fake.showFake(10, R.layout.item_fake, (holder, data, extra) -> {

});
// 隐藏假数据显示
fake.hideFake();
```

## LightList

为了更方便的使用 `DiffUtil` 来更新数据，我们对 `List` 进行了扩展，可以像使用普通集合类那样使用 `LightList`;

```java
LightList<Data>  list = new LightDiffList<>();
LightList<Data>  list = new LightAsyncDiffList<>();
```

使用 `LightList` 要求数据结构实现 `Diffable` 接口，同时也需要实现 `Parcelable` 接口，他会为 `DiffUtil` 提供数据比对的依据，你可以选择性的实现这些比对规则。

```java
class Student implements Diffable<Student> {

    public static final String MSG_NAME_CHANGED = "MSG_NAME_CHANGED";

    int    id;
    String name;

    @Override
    public boolean areItemsTheSame(Student newItem) {
        return this.equals(newItem) && id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(Student newItem) {
        return name.equals(newItem.name);
    }

    @Override
    public Set<String> getChangePayload(Student newItem) {
        Set<String> set = new HashSet<>();
        if (!name.equals(newItem.name)) {
            set.add(MSG_NAME_CHANGED);
        }
        return set;
    }
}
```

针对上面的回调方法，做一个简单的介绍：

- areItemsTheSame

> 当返回 true 的时候表示是相同的元素，调用 areContentsTheSame，推荐使用 id 比对
> 当返回 false 的时候表示是一个完全的新元素，此时会调用 insert 和 remove 方法来达到数据更新的目的

- areContentsTheSame

> 用来比较两项内容是否相同，只有在 areItemsTheSame 返回 true 时才会调用
> 返回 true 表示内容完全相同不需要更新
> 返回 false 表示虽然是同个元素但是内容改变了，此时会调用 changed 方法来更新数据

- getChangePayload

> 只有在 areItemsTheSame 返回 true 时才会调用，areContentsTheSame 返回 false 时调用
> 返回更新事件列表，会触发 payload 更新

### payloads

针对 `payloads` 单独说一下，在进行数据绑定时，可以判断当前是不是使用 `payloads` 更新，借助 `payloads` 局部刷新数据具有更高的效率。

```java
// 设置数据绑定回调
mMyAdapter.setBindCallback((holder, data, extra) -> {
    // 判读是否使用 payloads 局部更新数据
    if (extra.byPayload) {
        switch (extra.payloadMsg) {
            // 获取到仅更新 name 的消息
            case Student.MSG_NAME_CHANGED:
                // 仅重新绑定 name 即可
                holder.setText(R.id.name_tv, data.name);
                break;
        }
        return;
    }
    // 绑定整个条目的数据
    holder.setText(R.id.all_data, data);
});
```

### 扩展方法

```java
LightList<Data> list = new LightDiffList<>();
Data item = null;
List<Data> newList = null;

// 添加元素
list.updateAdd(item);
list.updateAdd(0, item);
// 添加列表
list.updateAddAll(newList);
list.updateAddAll(0, newList);

// 设置元素
list.updateSet(0, data -> {
    data.title = "new title";
});

// 清空列表
list.updateClear();

// 删除元素
list.updateRemove(item);
list.updateRemove(0);
// 删除符合规则的元素
list.updateRemove(data -> {
    return data.id > 0;
});
list.updateRemove(10, true, data -> {
    return data.id > 10;
});

// 遍历列表，找到符合规则的元素，执行 set 操作
list.updateForEach(data -> {
    return data.id > 10;
}, data -> {
    data.title = "new title";
});
// 遍历列表，执行 set 操作
list.updateForEach(data -> {
    data.title = "new title";
});

// 获取列表快照
List<Data> snapshot = list.snapshot();
// 删除第一个元素
snapshot.remove(0);
// 发布更新
list.update(newList);
```

