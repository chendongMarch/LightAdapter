状态管理
加载更多
header-footer
数据更新



Header&Footer

参数：
headerLayoutId
footerLayoutId

方法：
setHeaderEnable();
setFooterEnable();
notifyHeaderUpdate();
notifyFooterUpdate();

需要重写：
onBindHeader();
onBindFooter();



LoadMore

参数：
topPreloadNum, bottomPreloadNum

方法：
setOnTopLoadMoreListener()
finishTopLoadMore();
setTopLoadMoreEnable();

setOnLoadMoreListener()
finishLoadMore();
setLoadMoreEnable();





