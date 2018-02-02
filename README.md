`LightAdapter` 希望能够实现一个轻量级的、更容易使用和理解的`RecyclerView Adapter` 类库，吸取别人的长处，不断完善中。

基本的数据加载与绑定由 `LightAdapter` 来实现，更多增强功能由多个模块 `Module` 分工合作完成，现在具有以下模块

1. `HFModule`，实现为 `RecyclerView` 添加 `Header` 和 `Footer` 的功能，可以动态的显示和移除 `Header` 和 `Footer`。

2. `LoadMoreModule`，实现到达列表底部自动触发监听加载更多数据。

3. `TopLoadMoreModule`，实现到达列表顶部自动触发监听加载更多数据。

4. `SelectorModule`，实现选择器功能，辅助简化列表单选和多选操作和数据更新和存储。

5. `UpdateModule`，实现包装数据更新功能，在主线程更新数据，并提供更多更新数据的方法。

<!--more-->