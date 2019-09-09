## 实现分组列表


组数据结构：

```java
static class GroupData {
    public List<ChildData> children;
    public String          title;
    public boolean         expand;
    public int             groupId;

    public GroupData(String title) {
        this.title = title;
    }
}
```

单项数据结构：

```java
static class ChildData {
    public String    title;
    public int       childId;
    public int       groupId;
    public GroupData groupData;

    public ChildData(String title) {
        this.title = title;
    }
}
```

构造 `LxAdapter`：

```java
public static final int TYPE_GROUP      = Lx.contentTypeOf();
public static final int TYPE_CHILD      = Lx.contentTypeOf();

LxList mLxModels = new LxList();
LxAdapter.of(mLxModels)
        .bindItem(new GroupItemBind(), new ChildItemBind())
        .component(new LxFixedComponent())
        .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));
```

构造假数据：

```java
private void setGroupChildData() {
    List<GroupData> groupDataList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
        GroupData groupData = new GroupData("group -> " + i);
        groupData.groupId = i;
        groupDataList.add(groupData);
        List<ChildData> childDataList = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            ChildData childData = new ChildData("child -> " + j + " ,group -> " + i);
            childData.childId = j;
            childData.groupId = i;
            childData.groupData = groupData;
            childDataList.add(childData);
        }
        groupData.children = childDataList;
    }

    List<LxModel> lxModels = LxTransformations.pack(TYPE_GROUP, groupDataList);
    mLxModels.update(lxModels);
}
```

组绑定规则：

```java
static class GroupItemBind extends LxItemBind<GroupData> {

    GroupItemBind() {
        super(TypeOpts.make(opts -> {
            opts.spanSize = Lx.SPAN_SIZE_ALL;
            opts.viewType = TYPE_GROUP;
            opts.layoutId = R.layout.item_section;
            opts.enableFixed = true;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxVh holder, GroupData data) {
        holder.setText(R.id.section_tv, data.title + " " + (data.expand ? "展开" : "关闭"));
    }

    @Override
    public void onEvent(LxContext context, GroupData listItem, int eventType) {
        LxList models = adapter.getData();
        if (listItem.expand) {
            // 收起
            models.updateRemove(item -> item.getItemType() == TYPE_CHILD && item.<ChildData>unpack().groupId == listItem.groupId);
            models.updateSet(context.model, item -> {
                GroupData groupData = item.unpack();
                groupData.expand = false;
            });
        } else {
            // 展开
            List<LxModel> lxModels = LxTransformations.pack(TYPE_CHILD, listItem.children);
            models.updateAddAll(context.position + 1, lxModels);
            models.updateSet(context.model, item -> {
                GroupData groupData = item.unpack();
                groupData.expand = true;
            });
        }
    }
}
```

子项绑定规则：

```java
static class ChildItemBind extends LxItemBind<ChildData> {

    ChildItemBind() {
        super(TypeOpts.make(opts -> {
            opts.spanSize = Lx.SPAN_SIZE_ALL;
            opts.viewType = TYPE_CHILD;
            opts.layoutId = R.layout.item_simple;
        }));
    }

    @Override
    public void onBindView(LxContext context, LxVh holder, ChildData data) {
        holder.setText(R.id.sample_tv, data.title + " ，点击删除");
    }

    @Override
    public void onEvent(LxContext context, ChildData data, int eventType) {
        // 点击删除子项
        data.groupData.children.remove(data);
        LxList models = adapter.getData();
        models.updateRemove(context.position);
    }
}
```