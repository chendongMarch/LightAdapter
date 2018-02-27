
`LightAdapter` 的设计初衷是能够快速、简单的完成 `RecyclerView` 的数据适配工作，同时也对使用过程中的一些常用功能进行了扩展和封装。

随着功能的慢慢丰富，使用起来也变得越来越复杂，最后决定使用注解的方式对适配器进行配置。

[GitHub - LightAdapter](https://github.com/chendongMarch/LightAdapter)



<div style="width:100%;display: flex;height:30px;">

<!--<img style="margin-right:20px;" src="https://badge.juejin.im/entry/5a793a405188257a82111092/likes.svg?style=flat-square"/>-->

<img style="margin-right:20px;"  src="https://img.shields.io/github/stars/chendongMarch/LightAdapter.svg"/>

<img  style="margin-right:20px;"  src="https://img.shields.io/github/forks/chendongMarch/LightAdapter.svg"/>

</div>

<!--more-->

> - 基于注解实现基本的数据适配功能。
> - 预加载，支持顶部、底部预加载更多数据。
> - `Header & Footer`，为列表添加 头部 和 尾部。
> - 单击、双击、长按事件支持。
> - 自动 `UI` 线程更新数据，避免数据更新问题。
> - 选择器功能扩展，主要针对点击选中这种场景。



## 设计分析

在类库功能变的越来越丰富的同时，涉及的配置也越来越多，也因此造成了大量的功能堆积在 `Adaper` 里面，不容易维护也不利于扩展，因此对类库进行了重构，按照功能划分了多个模块，每个模块负责完全独立的功能，这样 `Adapter` 仅完成数据的加载和绑定，各部分扩展的功能由子模块完成，逻辑更清晰，当有新的功能加入时，只需要增加一个模块，更利于扩展。

目前有以下几个模块：

- `FullSpanModule`，负责处理跨越整行的布局类型。
- `HFModule`，`Header&Footer` 模块，负责添加 头尾布局。
- `LoadMoreModule`，底部预加载更多模块，负责列表到达底部时触发预加载。
- `TopLoadMoreModule`，顶部加载更多模块，负责列表到达顶部时触发预加载。
-  `UpdateModule`，数据更新模块，负责将数据更新操作发布到 `UI` 线程，同时对数据更新的方法做扩展。

功能模块化以后，整个类库的架构就更清晰了，不过与此同时也带来了另一个问题，就是模块过多，加大了使用难度，使用者需要关注所有的模块，为了避免这样情况，采用了注解来进行配置操作，类库内部解析注解自动添加适当的模块进去，这样一来使用者就不需要关注这些模块具体的功能，既做到了功能分离也对使用者足够友好。

目前有以下注解进行配置：

- `AdapterLayout`，负责 `Adapter` 布局资源文件配置，支持单类型和多类型。
- `Footer`，负责 `Footer` 布局文件的配置。
- `Header`，负责 `Header` 布局文件的配置。
- `PreLoading`，负责顶部、底部预加载配置。
- `FullSpan`，跨越整行的累心配置。


## 重要

适配器的配置使用注解来完成，但是由于 `Library Module` 中资源 ID 无法作为注解的参数，所以为每个注解也在 `LightAdapter` 提供了具有相同功能的方法对应，当无法使用注解时，可以使用这些方法，建议优先使用注解。

当声明好 `Adapter` 后，务必需要调用如下方法，一方面需要触发解析注解中的配置，另一方面也是为了把设置 `LayoutManager` 变成强制性的，因为总是会忘记:

```java
/**
 * 务必需要调用该方法
 * @param targetHost Adapter 对象所在的类
 * @param recyclerView RecyclerView
 * @param layoutManager 对应 LayoutManager
 */
public void bind(Object targetHost, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager)

// 🌰:
mAdapter.bind(MainActivty.this, mRecyclerView, mLayoutManager)
```

|描述|注解|方法|备注|
|:--|:--|:--|:--|
|Layout|@AdapterLayout|addType(int type,int layoutId)|为适配器添加布局文件资源|
|Header|@Header|configHeaderFooter(int headerLyId,int footerLyId)|添加头部布局资源|
|Footer|@Footer |configHeaderFooter(int headerLyId,int footerLyId)|添加尾部布局资源|
|预加载|@PreLoading | configPreLoading(int top,int bottom)| 预加载|
|FullSpan|@FullSpan|configFullSpan(int... types)|跨越整行|

## 数据适配

进行数据适配时，需要一个布局文件的资源文件，使用注解 `@AdapterLayout` 来配置，如一个简单的单类型适配器需要如下声明：

```java
@AdapterLayout(R.layout.item_layout)
LightAdapter<GuideData> mAdapter;
```

同样也支持多类型布局，下面类型 `TYPE_A` 对应布局文件 `R.layout.item_ly_a`，类型 `TYPE_B` 对应布局文件 `R.layout.item_ly_b`，两个数组长度需要相等：

```java
@AdapterLayout(itemTypes = {TYPE_A,TYPE_B},
        itemLayoutIds = {R.layout.item_ly_a,R.layout.item_ly_b})
LightAdapter<GuideData> mAdapter;
```

之后就可以创建 `Adapter`，进行数据绑定：

```java
mAdapter = new LightAdapter<GuideData>(mContext,mGuideDatas) {
    @Override
    public void onBindView(LightHolder holder, GuideData data, int pos, int type) {
    	// 数据绑定
    }
};
```

不使用注解的实现方式，建议首选注解进行配置：

```java
// 单类型
mAdapter = new LightAdapter<GuideData>(mContext, mGuideDatas, R.layout.item_layout) {
    @Override
    public void onBindView(LightHolder holder, GuideData data, int pos, int type) {
        // 数据绑定
    }
};

// 多类型
mAdapter = new LightAdapter<GuideData>(mContext, mGuideDatas) {
    @Override
    public void onBindView(LightHolder holder, GuideData data, int pos, int type) {
        // 数据绑定
    }
};
mAdapter.addType(TYPE_A, R.layout.item_ly_a)
        .addType(TYPE_B, R.layout.item_ly_b);
```

## 功能模块

### Header & Footer

为布局添加头尾，使用 `@Header` 和 `@Footer` 注解来完成

```java
@Header(R.layout.headerly)
@Footer(R.layout.footerly)
@AdapterLayout(R.layout.item)
private LightAdapter<HFData> mAdapter;
```

对应的不使用注解的方法是：

```java
public void configHeaderFooter(int headerLayoutId, int footerLayoutId)
```

对头尾数据进行绑定

```java
mAdapter = new LightAdapter<GuideData>(mContext, mGuideDatas) {
    @Override
    public void onBindHeaderView(LightHolder holder) {
    	// 绑定 header 数据
    }

    @Override
    public void onBindFooterView(LightHolder holder) {
    	// 绑定 footer 数据
    }
};
```

### PreLoading

预加载更多功能需要指定提前几项触发预加载，比如当距离到达列表底部还差 `3` 个 `item` 时触发预加载。使用 `@PreLoading` 进行配置。

```java
@AdapterLayout(R.layout.load_more_item)
@PreLoading(top = 2, bottom = 2)
private LightAdapter<LoadMoreModel> mAdapter;
```
对应的不使用注解的方法是：

```java
public void configPreLoading(int top, int bottom)
```

预加载更多触发时的方法：

```java
mAdapter = new LightAdapter<GuideData>(mContext, mGuideDatas) {
    @Override
    public void onTopLoadMore() {
    	// 顶部加载更多
    }

    @Override
    public void onBottomLoadMore() {
    	// 底部加载更多
    }
};
```

### FullSpan

当使用 `GridLayoutManager` 布局时，通常会有需求某种类型的数据作为标题内容出现，他需要跨越整行，实现类似隔断的效果，使用 `@FullSpan` 注解来配置。

下面的示例中，`TYPE_OK` 类型回跨越整行。

```java
@AdapterLayout(
        itemTypes = {TypeModel.TYPE_OK, TypeModel.TYPE_NO},
        itemLayoutIds = {R.layout.layout_ok, R.layout.layout_no})
@FullSpan(TypeModel.TYPE_OK)
private LightAdapter<TypeModel> mAdapter;
```
对应的不使用注解的方法：

```java
public void configFullSpan(int... fullSpanTypes)
```

### Update

用于更新数据，不需要注解支持，特点是对数据更新的方法进行了扩展，同时所有的数据更新都会到 `UI` 线程执行，不需要再为了更新适配器去切换线程啦。

```java
// 支持原来的更新方法，不过被切换到了 UI 线程
mAdapter.update().notifyDataSetChanged();
mAdapter.update().notifyItemChanged(0);
mAdapter.update().notifyItemInserted(0);
mAdapter.update().notifyItemRangeChanged(0,10);

//////////////////////////////  -- 扩展的新方法 --  //////////////////////////////
// 清空数据
mAdapter.update().clear();
// 改变某一个数据
mAdapter.update().set(100,new GuideData());
// 在头部添加数据，用于分页加载
mAdapter.update().appendHeadList(mGuideDatas,true);
// 在尾部添加数据，用于分页加载
mAdapter.update().appendTailList(mGuideDatas,true);
```

## 事件

支持  单击、双击、长按事件，设置简单且返回数据丰富。

```java
mAdapter.setOnItemListener(new SimpleItemListener<GuideData>() {
    @Override
    public void onClick(int pos, LightHolder holder, GuideData data) {
        // 单击事件
    }
    @Override
    public void onLongPress(int pos, LightHolder holder, GuideData data) {
        // 长按事件
    }
    @Override
    public void onDoubleClick(int pos, LightHolder holder, GuideData data) {
        // 双击事件
    }
    @Override
    public boolean isSupportDoubleClick() {
        // 是否支持双击，默认不支持
        return super.isSupportDoubleClick();
    }
    @Override
    public boolean isClickable(GuideData data) {
        // 该类型数据是否支持事件，默认支持
        return super.isClickable(data);
    }
});
```

## 数据绑定

数据绑定主要基于简化过 `LightHolder`，里面内置了很多绑定数据的简单方法，如：

```java
mAdapter = new LightAdapter<GuideData>(mContext, mGuideDatas) {
    @Override
    public void onBindView(LightHolder holder, GuideData data, int pos, int type) {
        holder
                // 设置文本
                .setText(R.id.test1, "test")
                // 对多个控件设置相同文字颜色
                .setTextColor(Ids.all(R.id.test1, R.id.test2, R.id.test3), Color.RED)
                // 显示
                .setVisible(R.id.test1, R.id.test2, R.id.test3)
                // 显示 || 隐藏
                .setVisibleGone(R.id.test1, true)
                // 点击事件
                .setClick(R.id.test1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                // 长按事件
                .setLongClick(R.id.test1, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                })
                // 对最外层 view 设置 layout params
                .setLayoutParams(100, 100)
                // 对指定 view 设置 layout params
                .setLayoutParams(R.id.test1, 100, 100)
                // 设置 checked
                .setChecked(Ids.all(R.id.test1, R.id.test2),false)
                // 自定义 callback，可以做任何事，主要为了保持链式代码风格
                .setCallback(R.id.test1, new LightHolder.Callback<ImageView>() {
                    @Override
                    public void bind(LightHolder holder, ImageView view, int pos) {
                        Glide.with(holder.getContext()).load(url).into(view);
                    }
                })
                // 设置图片
                .setImage(R.id.test1,R.mipmap.ic_launcher);
    }
};
```

## SelectManager

牵扯到列表时通常会有单选、多选的功能，`LightAdapter` 中的 `SelectManager` 就是针对这种业务场景进行的简化封装，借助它可以更简单的实现选择器功能，同时还支持局部更新数据。

支持单选 `SelectManager.TYPE_SINGLE` 和 多选 `SelectManager.TYPE_MULTI` 两种模式，

```java
mSelectManager = new SelectManager<>(mAdapter, SelectManager.TYPE_SINGLE, new AdapterViewBinder<TypeModel>() {
    @Override
    public void onBindViewHolder(LightHolder holder, TypeModel data, int pos, int type) {
        // 判断该数据是否被选中，然后进行不同的数据渲染
        if (!mSelectManager.isSelect(data)) {
            holder.setText(R.id.item_common_tv, "没选" + data.index);
        } else {
            holder.setText(R.id.item_common_tv, "选中" + data.index);
        }
    }
});
```

设置初始选中的项：

```java
mSelectManager.initSelect(0, 1, 2);
```

切换某一项的选中状态：

```java
mAdapter.setOnItemListener(new SimpleItemListener<TypeModel>() {
    @Override
    public void onClick(int pos, LightHolder holder, TypeModel data) {
        mSelectManager.select(pos);
    }
});
```

获取选择的数据：

```java
mSelectManager.getResult();
mSelectManager.getResults();
```

