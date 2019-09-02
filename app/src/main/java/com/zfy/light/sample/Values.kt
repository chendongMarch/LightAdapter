package com.zfy.light.sample

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
object Values {

    @JvmStatic
    fun getDragSwipeDesc() = """
    1. 针对某种类型开启/关闭拖拽和侧滑，在 ModelTypeConfigCallback 中配置他们

    2. 设置 Options， Drag 和 Swipe 功能由 DragSwipeDelegate 实现，最简单的方法设置
    adapter.dragSwipe().setOptions(new DragSwipeOptions())
    开启即可，开启之后每个条目长按可以触发拖拽，触摸可以触发侧滑，这些都是自动的。

    3. 如果不希望自动触发，而是需要手动触发拖拽和侧滑，则只需要在 DragSwipeOptions 中关闭自动触发的开关
    options.
    // 关闭自动侧滑，手动调用
    options.touchItemView4Swipe = false;
    // 关闭长按拖拽
    options.longPressItemView4Drag = false;
    在绑定数据时调用
    - holder.dragOnTouch()
    - holder.dragOnLongPress()
    - holder.swipeOnTouch()
    - holder.swipeOnLongPress()
    将触发操作绑定到指定的 View。

    4. 支持检测手势触发的状态，状态分为
    开始拖拽，释放拖拽，开始侧滑，释放侧滑
    您可以在合适的时机对 View 作出相应状态的改变。
    """.trimIndent()

    @JvmStatic
    fun getSectionDelegateMsg() = """
    1. 返回类型为 TYPE_SECTION 的数据将会做为隔断显示，隔断是单独占据一行的，可以使用 adapter.section() 来配置他。

    2. 任意类型的布局，包括隔断，都支持悬停在列表顶部，并且布局滑动时具有挤压效果，你可以在首页看到悬停在顶部的模块标题。

    3. 开启自定义类型的悬停效果，你需要在 ModelTypeConfigCallback 中对 ModelType 配置，设置 modelType.enableFixed = true；
    然后使用 adapter.section().setPinEnable() 开启支持悬停，则该类型就会悬停在顶部。
    """.trimIndent()

    @JvmStatic
    fun getModelTypeMsg() = """
    ModelType 是一个非常核心的配置类，它用来决定每种数据类型的详细配置：

    int viewType; // 数据类型
    int layoutId; // 布局资源
    int spanSize = LightValues.SPAN_NONE; // 跨越行数
    boolean enableClick = true; // 是否允许点击事件
    boolean enableLongPress = true; // 是否允许长按事件
    boolean enableDbClick = false; // 是否允许双击事件
    boolean enableDrag = false; // 是否允许拖动
    boolean enableSwipe = false; // 是否允许滑动
    boolean enableFixed; // 钉住，支持悬停效果

    每种类型都可以根据需要进行自定义的配置。
    """.trimIndent()

    @JvmStatic
    fun getModelTypeConfigCallbackMsg() = """
    ModelTypeRegistry 用来完成复杂多类型数据适配器的配置


    """.trimIndent()


    @JvmStatic
    fun getLoadingDesc() = """
    本页面用来展示加载状态和空白页面等效果
    1. LoadMoreDelegate 底部预加载更多

    2. TopMoreDelegate 顶部预加载更多

    3. LoadingViewDelegate 加载提示条状态变化，与 LoadMoreDelegate 联动

    4. EmptyViewDelegate 空白页面，点击封面上面的按钮，触发空白错误页面
    """.trimIndent()

    @JvmStatic
    fun getSelectorDesc() = """
    本页面用来展示选择器效果和滑动选中功能

    1. 借助 SelectorDelegate 可以快速实现选择器功能。

    2. 横向滑动可以触发滑动选中

    3. 点击封面上的按钮获取选中的元素个数

    """.trimIndent()

    @JvmStatic
    fun getHFDesc() = """
    本页面用来演示 Header/Footer 的添加效果
    1. Header/Footer 可以自由叠加，删除
    2. Header/Footer 支持使用 Holder 绑定数据，并可以随时更新
    3. 点击封面上面的按钮增加 Header，点击封面删除 Header，点击列表项更新显示。
    """.trimIndent()


    @JvmStatic
    fun getAnimatorDesc() = """
        动画效果展示，支持 ItemAnimator 和 BindAnimator

        - ItemAnimator 是 RecyclerView 支持的动画，只有在调用局部更新方法时才会生效；ItemAnimator 推荐 GitHub - wasabeef/recyclerview-animators
        - BindAnimator 是 Adapter 在执行 onBindViewHolder 时执行的 View 动画，他在 Item 被绑定时执行；

        其中 BindAnimator 支持不同类型设置不同的动画， 如下两种类型加载的动画也不同，当然也支持全局动画设置。

    """.trimIndent()


    @JvmStatic
    fun getEmptyDesc() = """
     EmptyViewDelegate 完成空白页面功能
    """.trimIndent()

    @JvmStatic
    fun getPayloadDesc() = """
     借助 payload 实现局部更新，不需要更新整个条目，同样也不会造成闪烁现象

     点击类型一更新标题字段，点击类型二更新描述字段

     使用全局更新时，绑定后后会跟有全局字样，使用局部更新时，绑定后会带有局部字样
    """.trimIndent()


    @JvmStatic
    fun getFakeDesc() = """
     先显示假数据

     更新数据后再显示真实数据
    """.trimIndent()
}
