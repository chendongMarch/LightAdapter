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
    1. 针对某种类型开启/关闭拖拽和侧滑，在 ModelTypeUpdater 中配置他们

    2. 设置 Options， Drag 和 Swipe 功能由 DragSwipeDelegate 实现，最简单的方法设置
    adapter.dragSwipe().setOptions(new DragSwipeOptions())
    开启即可，开启之后每个条目长按可以触发拖拽，触摸可以触发侧滑，这些都是自动的。

    3. 如果不希望自动触发，而是需要手动触发拖拽和侧滑，则只需要在 DragSwipeOptions 中关闭自动触发的开关
    options.
    // 关闭自动侧滑，手动调用
    options.itemViewAutoSwipeEnable = false;
    // 关闭长按拖拽
    options.itemViewLongPressDragEnable = false;
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
}