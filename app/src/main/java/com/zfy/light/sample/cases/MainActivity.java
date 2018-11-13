package com.zfy.light.sample.cases;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.march.common.exts.EmptyX;
import com.march.common.exts.SizeX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.Position;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.DescDialog;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.TypeGridSpaceItemDecoration;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.MultiTypeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.main_activity)
public class MainActivity extends MvpActivity {


    @BindView(R.id.content_rv) RecyclerView mRecyclerView;
    @BindView(R.id.cover_iv)   ImageView    mCoverIv;
    @BindView(R.id.app_bar)    View         mAppBarLayout;
    @BindView(R.id.toolbar)    Toolbar      mToolbar;

    private LightDiffList<MultiTypeEntity> mEntities;
    private LightAdapter<MultiTypeEntity>  mAdapter;

    @Override
    public void init() {
        mEntities = new LightDiffList<>();
        mToolbar.setTitleTextColor(Color.WHITE);
        mAppBarLayout.getLayoutParams().height = (int) (SizeX.WIDTH * 9f / 16);
        String coverUrl = "http://cdn1.showjoy.com/shop/images/20181108/KFHSC1SYSMFYSUAUHPFV1541688945136.png";
        Glide.with(getContext()).load(coverUrl).into(mCoverIv);

        // updater
        ModelTypeConfigCallback updater = modelType -> {
            switch (modelType.type) {
                case MultiTypeEntity.TYPE_LINK:
                    modelType.layoutId = R.layout.item_link;
                    modelType.spanSize = SpanSize.SPAN_SIZE_ALL;
                    break;
                case MultiTypeEntity.TYPE_DESC:
                    modelType.layoutId = R.layout.item_desc;
                    modelType.spanSize = SpanSize.SPAN_SIZE_ALL;
                    break;
                case MultiTypeEntity.TYPE_BASIC:
                    modelType.layoutId = R.layout.item_basic;
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    break;
                case MultiTypeEntity.TYPE_LIST:
                    modelType.layoutId = R.layout.item_list;
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    break;
                case MultiTypeEntity.TYPE_EVENT:
                    modelType.layoutId = R.layout.item_event;
                    modelType.spanSize = SpanSize.SPAN_SIZE_THIRD;
                    modelType.enableDbClick = true;
                    break;
                case MultiTypeEntity.TYPE_HOLDER:
                    modelType.layoutId = R.layout.item_holder;
                    modelType.spanSize = SpanSize.SPAN_SIZE_ALL;
                    break;
                case MultiTypeEntity.TYPE_DELEGATE:
                    modelType.layoutId = R.layout.item_deleate;
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    break;
                case MultiTypeEntity.TYPE_ASSISTANT:
                    modelType.layoutId = R.layout.item_assistant;
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    break;
                case MultiTypeEntity.TYPE_FUTURE:
                    modelType.layoutId = R.layout.item_future;
                    modelType.spanSize = SpanSize.SPAN_SIZE_THIRD;
                    break;
                case MultiTypeEntity.TYPE_PROJECT:
                    modelType.layoutId = R.layout.item_project;
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    modelType.spaceRect = new Rect(20, 20, 20, 20);
                    break;
            }
        };
        mAdapter = new LightAdapter<MultiTypeEntity>( mEntities, updater) {

            @Override
            public void onBindView(LightHolder holder, MultiTypeEntity data, Position pos) {
                holder.setText(R.id.title_tv, data.title)
                        .setText(R.id.desc_tv, data.desc);
                switch (data.type) {
                    case MultiTypeEntity.TYPE_LINK:
                        holder.setClick(all(R.id.github_tv, R.id.github_iv), v -> openBrowser("https://github.com/chendongMarch/LightAdapter"))
                                .setClick(all(R.id.blog_tv, R.id.blog_iv), v -> openBrowser("http://zfyx.coding.me/article/1632666977/"))
                                .setClick(all(R.id.download_tv, R.id.download_iv), v -> openBrowser("http://zfyx.coding.me/article/1632666977/"));
                        break;
                    case MultiTypeEntity.TYPE_DESC:
                        break;
                    case MultiTypeEntity.TYPE_BASIC:
                        break;
                    case MultiTypeEntity.TYPE_LIST:
                        break;
                    case MultiTypeEntity.TYPE_EVENT:
                        break;
                    case MultiTypeEntity.TYPE_HOLDER:
                        break;
                    case MultiTypeEntity.TYPE_DELEGATE:
                        holder.setText(R.id.subtitle_tv, data.subTitle);
                        break;
                    case MultiTypeEntity.TYPE_ASSISTANT:
                        break;
                    case MultiTypeEntity.TYPE_FUTURE:
                        break;
                    case MultiTypeEntity.TYPE_PROJECT:
                        holder.setCallback(R.id.cover_iv, new GlideCallback(data.cover));
                        break;
                }
            }

        };
        mAdapter.setClickEvent((holder, pos, data) -> {
            if (!EmptyX.isEmpty(data.url)) {
                openBrowser(data.url);
                return;
            }
            if (data.type == MultiTypeEntity.TYPE_EVENT) {
                ToastX.show("单击事件");
            }
            if (data.type == MultiTypeEntity.TYPE_DESC) {
                DescDialog.show(getContext(), data.title, data.desc);
                return;
            }
            if (data.msg != null) {
                DescDialog.show(getContext(), data.title, data.msg);
                return;
            }
            if (data.targetClazz != null) {
                launchActivity(new Intent(getContext(), data.targetClazz), 0);
            }
        });
        mAdapter.setLongPressEvent((holder, pos, data) -> {
            if (data.type == MultiTypeEntity.TYPE_EVENT) {
                ToastX.show("长按事件");
            }
        });
        mAdapter.setDbClickEvent((holder, pos, data) -> {
            if (data.type == MultiTypeEntity.TYPE_EVENT) {
                ToastX.show("双击事件");
            }
        });
        // 设置可悬停 section
        mAdapter.section().setOptions(R.layout.item_section, true, (holder, pos, data) -> {
            holder.setText(R.id.section_tv, data.sectionTitle);
        });
        // 分割线
        mRecyclerView.addItemDecoration(new TypeGridSpaceItemDecoration());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mRecyclerView.setAdapter(mAdapter);
        // 更新数据
        mEntities.update(initData());

    }


    private List<MultiTypeEntity> initData() {
        List<MultiTypeEntity> list = new ArrayList<>();
        MultiTypeEntity homeEntity;
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_LINK);
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "LightAdapter 的设计初衷是以 轻量 和 面向业务 为主要目的，一方面希望可以快速、简单的的完成数据的适配，另一方面针对业务中经常出现的场景能提供统一、简单的解决方案。";
        list.add(homeEntity);
        // 数据适配
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "数据适配";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "只有一个适配器 LightAdapter，支持单类型、多类型数据适配，多类型适配需要配合 ModelTypeConfigCallback 使用。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_BASIC);
        homeEntity.title = "单类型";
        homeEntity.desc = "单类型数据适配";
        homeEntity.msg = "单类型适配内部也基于多类型适配，他只是针对单类型包装了一个更友好的构造方法，你可以使用它快速的构建单类型的适配器。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_BASIC);
        homeEntity.title = "多类型";
        homeEntity.desc = "多类型数据适配";
        homeEntity.msg = "多类型适配器支持多种类型数据的适配，它依赖于 Typeable / ModelType / ModelTypeConfigCallback 完成针对每种类型数据的配置工作";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_BASIC);
        homeEntity.title = "Typeable";
        homeEntity.desc = "实现 Typeable 接口声明支持多类型";
        homeEntity.msg = "多类型数据适配时，数据结构需要实现 Typeable 返回该数据的类型，这是首先也是必须的。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_BASIC);
        homeEntity.title = "ModelType";
        homeEntity.desc = "每种类型的配置";
        homeEntity.msg = Values.getModelTypeMsg();
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_BASIC);
        homeEntity.title = "ModelTypeConfigCallback";
        homeEntity.desc = "初始化 ModelTypeConfigCallback 来针对类型进行配置";
        homeEntity.msg = Values.getModelTypeConfigCallbackMsg();
        list.add(homeEntity);
        // 集合类 Diffable/payload/LightDiffList/LightAsyncDiffList
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "数据更新集合类";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "在使用 Adapter 过程中我们经常要去考虑更新哪里的数据（数据的位置）和如何更新数据（插入/删除/更新），现在我们可以借助 DiffUtil 更高效更简单的来完成数据比对更新，为了简化 DiffUtil 的使用，我们已经将他集成在了如下集合类中，只需要更换您的数据源即可，一起告别 notify；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_LIST);
        homeEntity.title = "Diffable";
        homeEntity.desc = "实现 Diffable 实现 DiffUtil 数据比对";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_LIST);
        homeEntity.title = "payload";
        homeEntity.desc = "借助 payload 实现增量更新，避免刷新整条 item";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_LIST);
        homeEntity.title = "LightDiffList";
        homeEntity.desc = "借助 DiffUtil 计算更新，简化数据更新逻辑";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_LIST);
        homeEntity.title = "AsyncLightDiffList";
        homeEntity.desc = "在 LightDiffList 基础上，内部使用 AsyncListDiffer 来在子线程计算 Diff，异步更新数据";
        list.add(homeEntity);
        // 事件
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "事件绑定";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "事件绑定，支持单击、双击、长按事件，双击事件使用 Gesture 手势实现，需要手动开启，支持双击将会导致事件检测时间变长，并且不支持按压等效果，如下几个条目开启了双击检测。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_EVENT);
        homeEntity.title = "单击事件";
        homeEntity.desc = "单击本条目触发单击事件";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_EVENT);
        homeEntity.title = "长按事件";
        homeEntity.desc = "长按本条目触发长按事件";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_EVENT);
        homeEntity.title = "双击事件";
        homeEntity.desc = "双击本条目触发双击事件";
        list.add(homeEntity);
        // holder
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "ViewHolder";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "使用 LightHolder 进行数据绑定，LightHolder 内置了很多数据绑定方法，同时为了不破坏链式编程风格，加载了 Callback 支持，尽量在一条链中完成数据绑定";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_HOLDER);
        homeEntity.title = "LightHolder";
        homeEntity.desc = "对 ViewHolder 进行扩展：\n支持多个 id 同时绑定 \n内部实现了常用调用绑定方法，可直接调用 \n借助 setCallback 避免链式调用被打断，可自定义扩展";
        list.add(homeEntity);
        // 功能代理
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "Delegate";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "为了避免 Adapter 太臃肿，也为了更好的扩展性，所有附加功能都被分离到了 Delegate 中，他让架构更加清晰，而且为了更好的性能，这些 Delegate 都是懒加载的，只有在您需要的时候才会被装载到 Adapter 中。\n当使用一个功能时，只需要从 adapter 中获取该 Delegate，这样可以快速定位到相关的 API，而不是在 Adapter 大量的 API 中查找；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "HFViewDelegate";
        homeEntity.subTitle = "adapter.header()/footer()";
        homeEntity.desc = "控制 header&footer 的叠加/更新/状态变化；";
        homeEntity.targetClazz = HFTestActivity.class;
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "LoadingViewDelegate";
        homeEntity.subTitle = "adapter.loadingView()";
        homeEntity.desc = "控制 Loading 条目状态，可与 LoadMoreDelegate 联动；";
        homeEntity.targetClazz = LoadTestActivity.class;
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "EmptyViewDelegate";
        homeEntity.subTitle = "adapter.emptyView()";
        homeEntity.desc = "控制空白页面的状态，可自定义扩展数据绑定和事件；";
        homeEntity.targetClazz = LoadTestActivity.class;
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "SpanDelegate";
        homeEntity.subTitle = "/";
        homeEntity.msg = "控制条目跨越列数，一般不会直接使用它，设置 ModelType 的 spanSize，可以设置某种类型布局的跨越到列数；";
        homeEntity.desc = "控制条目跨越列数，一般不会直接使用它，而是在 ModelTypeConfigCallback 中配置某种类型的 spanSize，请关注 ModelType；";

        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "NotifyDelegate";
        homeEntity.subTitle = "adapter.notifyItem()";
        homeEntity.desc = "控制数据更新，主要用来判断线程，将更新操作发送到主线程，避免在子线程更新数据造成问题；";
        homeEntity.msg = "使用 NotifyDelegate 可以保证安全的在主线程更新数据，推荐调用 adapter.notifyItem().xxxxx() 代替 adapter.notifyXxxx()";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "DragSwipeDelegate";
        homeEntity.subTitle = "adapter.dragSwipe()";
        homeEntity.targetClazz = DragSwipeTestActivity.class;
        homeEntity.desc = "控制条目的拖拽和侧滑，支持灵活的控制；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "LoadMoreDelegate";
        homeEntity.subTitle = "adapter.loadMore()";
        homeEntity.desc = "完成列表到达底部加载更多功能，支持设置预加载个数；";
        homeEntity.targetClazz = LoadTestActivity.class;
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "TopMoreDelegate";
        homeEntity.subTitle = "adapter.topMore()";
        homeEntity.desc = "完成列表到达顶部加载更多功能，支持设置预加载个数；";
        homeEntity.targetClazz = LoadTestActivity.class;
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "SectionDelegate";
        homeEntity.subTitle = "adapter.section()";
        homeEntity.desc = "控制隔断的数据绑定和更新，支持任意布局的顶部悬停效果等；";
        homeEntity.msg = Values.getSectionDelegateMsg();
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "SelectorDelegate";
        homeEntity.subTitle = "adapter.selector()";
        homeEntity.targetClazz = SelectorTestActivity.class;
        homeEntity.desc = "选择器功能实现，主要为了解决业务中常见的选择器效果；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DELEGATE);
        homeEntity.title = "AnimatorDelegate";
        homeEntity.subTitle = "mAdapter.animator()";
        homeEntity.targetClazz = AnimatorTestActivity.class;
        homeEntity.desc = "负责 Item 动画效果实现，支持 ItemAnimator 和 BindAnimator；";
        list.add(homeEntity);
        // 辅助
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "辅助";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "如下内容是在开发中比较常见的一些需求支持，他们没有被强关联到 LightAdapter，如果需要使用要手动调用。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_ASSISTANT);
        homeEntity.title = "GridSpaceItemDecoration";
        homeEntity.desc = "对 GridLayoutManger 每个条目周围添加空白分割；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_ASSISTANT);
        homeEntity.title = "LinearSpaceItemDecoration";
        homeEntity.desc = "对 LinearLayoutManger 每个条目周围添加空白分割；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_ASSISTANT);
        homeEntity.title = "LinearDividerDecoration";
        homeEntity.desc = "对 LinearLayoutManger 每个条目周围添加分割线，样式使用 drawable 定制；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_ASSISTANT);
        homeEntity.title = "滑动选中";
        homeEntity.desc = "实现类似 QQ 相册滑动时选中多个条目的效果；";
        homeEntity.targetClazz = SelectorTestActivity.class;
        list.add(homeEntity);
        // 未来
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "正在做的";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "正在开发和预备支持的特性。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_FUTURE);
        homeEntity.title = "分页器";
        homeEntity.desc = "用来对 pageNo/pageSize 分页加载数据、更新的业务场景进行支持；";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_FUTURE);
        homeEntity.title = "Expandable";
        homeEntity.desc = "分组列表，可展开和收起等；";
        list.add(homeEntity);


        // 项目
        homeEntity = new MultiTypeEntity();
        homeEntity.sectionTitle = "我的项目";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_DESC);
        homeEntity.desc = "我的几个正在维护的几个开源项目，求 🌟🌟🌟🌟🌟。";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_PROJECT);
        homeEntity.title = "SocialSdk";
        homeEntity.cover = "http://cdn1.showjoy.com/shop/images/20180828/MLI1YQGFQLZBRO3VKH6U1535432744013.png";
        homeEntity.desc = "提供 微博、微信、QQ 的登陆分享功能原生 SDK 的一键接入支持；";
        homeEntity.url = "https://github.com/chendongMarch/SocialSdkLibrary";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_PROJECT);
        homeEntity.title = "LightAdapter";
        homeEntity.cover = "http://cdn1.showjoy.com/shop/images/20181109/SLLLD6YG868KWLTQ8B1M1541732455752.jpg";
        homeEntity.desc = "为 RecyclerView 的数据加载更新提供更轻量级的解决方案；";
        homeEntity.url = "https://github.com/chendongMarch/LightAdapter";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_PROJECT);
        homeEntity.title = "WeexCube";
        homeEntity.cover = "http://cdn1.showjoy.com/shop/images/20181109/2JYYB5UKKCSQDO5MO2KK1541732418859.jpg";
        homeEntity.desc = "轻量级 Weex 容器，缓存、环境、数据优化处理，迭代中...";
        homeEntity.url = "https://github.com/chendongMarch/Weex-Cube";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_PROJECT);
        homeEntity.title = "Kotlin 学习系列总结";
        homeEntity.cover = "http://cdn1.showjoy.com/shop/images/20181109/PL8NCTUX1TRQXZYYEQDF1541732383928.jpg";
        homeEntity.desc = "共计 22 篇，主要涉及官方文档的基本语法及 Anko 的使用等。";
        homeEntity.url = "http://zfyx.coding.me/article/bbab636a/";
        list.add(homeEntity);
        homeEntity = new MultiTypeEntity(MultiTypeEntity.TYPE_PROJECT);
        homeEntity.title = "组件化架构层（component_basic）";
        homeEntity.cover = "http://images.pexels.com/photos/534182/pexels-photo-534182.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=350";
        homeEntity.desc = "正在开发和维护的组件化的架构层支持";
        homeEntity.url = "https://github.com/Android-Modularity/component_basic";
        list.add(homeEntity);
        return list;
    }

    public void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
