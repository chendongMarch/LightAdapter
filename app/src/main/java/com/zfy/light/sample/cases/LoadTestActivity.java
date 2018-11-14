package com.zfy.light.sample.cases;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.march.common.exts.ListX;
import com.march.common.pool.ExecutorsPool;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.listener.AdapterCallback;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.EmptyState;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.model.LoadingState;
import com.zfy.adapter.model.Position;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.MultiTypeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.load_test_activity)
public class LoadTestActivity extends MvpActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoadTestActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.content_rv) RecyclerView mRecyclerView;

    private LightDiffList<MultiTypeEntity> mData;


    @Override
    public void init() {
        mData = new LightDiffList<>();
        ModelTypeConfigCallback updater = modelType -> {
            switch (modelType.type) {
                case MultiTypeEntity.TYPE_CAN_DRAG:
                    modelType.layoutId = R.layout.item_drag;
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    break;
                case MultiTypeEntity.TYPE_CAN_SWIPE:
                    modelType.layoutId = R.layout.item_swipe;
                    modelType.spanSize = SpanSize.SPAN_SIZE_ALL;
                    break;
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        LightAdapter<MultiTypeEntity> adapter = new LightAdapter<MultiTypeEntity>( mData, updater) {
            @Override
            public void onBindView(LightHolder holder, MultiTypeEntity data, Position pos) {
                holder.setText(R.id.title_tv, "标题 " + data.id)
                        .setText(R.id.desc_tv, "描述 " + data.id + " " + System.currentTimeMillis());
            }
        };
        // 底部加载更多
        adapter.loadMore().setLoadMoreListener(3, new AdapterCallback() {
            @Override
            public void call(LightAdapter adapter) {
                ExecutorsPool.ui(() -> {
                    List<MultiTypeEntity> items = ListX.range(20, index -> new MultiTypeEntity(index % 7 == 0 ? MultiTypeEntity.TYPE_CAN_SWIPE : MultiTypeEntity.TYPE_CAN_DRAG));
                    adapter.loadMore().finishLoadMore();
                    mData.updateAddAll(items);
                }, 1500);
            }
        });
        // 顶部加载更多
//        adapter.topMore().setTopMoreListener(3, new AdapterCallback() {
//            @Override
//            public void call(LightAdapter adapter) {
//                ExecutorsPool.ui(() -> {
//                    List<MultiTypeEntity> items = ListX.range(20, index -> new MultiTypeEntity(index % 7 == 0 ? MultiTypeEntity.TYPE_CAN_SWIPE : MultiTypeEntity.TYPE_CAN_DRAG));
//                    List<MultiTypeEntity> snapshot = mData.snapshot();
//                    snapshot.addAll(0, items);
//                    adapter.topMore().finishTopMore();
//                    post(() -> {
//                        mData.call(snapshot);
//                    }, 500);
//                }, 1500);
//            }
//        });
        // loadingView
        adapter.loadingView().setLoadingView(LightView.from(R.layout.loading_view), (holder, pos, data) -> {
            switch (data.state) {
                case LoadingState.LOADING:
                    holder.setVisible(R.id.pb)
                            .setText(R.id.content_tv, "加载中请稍候～");
                    break;
                case LoadingState.FINISH:
                    holder.setGone(R.id.pb)
                            .setText(R.id.content_tv, "加载完成");
                    break;
            }
        });
        // empty
        adapter.emptyView().setEmptyView(LightView.from(R.layout.empty_view), (holder, pos, data) -> {
                holder.setClick(R.id.refresh_tv, v -> {
                    adapter.header().setHeaderEnable(true);
                    mData.update(ListX.range(20, index -> new MultiTypeEntity(index % 7 == 0 ? MultiTypeEntity.TYPE_CAN_SWIPE : MultiTypeEntity.TYPE_CAN_DRAG)));
                    adapter.emptyView().setEmptyState(EmptyState.NONE);
                    adapter.loadingView().setLoadingEnable(true);
                    adapter.loadMore().setLoadMoreEnable(true);
                });
        });
        // header
        adapter.header().addHeaderView(LightView.from(R.layout.desc_header), holder -> {
            holder.setText(R.id.desc_tv, Values.getLoadingDesc())
                    .setClick(R.id.action_fab, v -> {
                        mData.update(new ArrayList<>());
                        adapter.header().setHeaderEnable(false);
                        adapter.loadingView().setLoadingEnable(false);
                        adapter.loadMore().setLoadMoreEnable(false);
                        adapter.emptyView().setEmptyState(EmptyState.ERROR);
                    })
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
        });
//        adapter.animator().setBindAnimator(new SlideAnimator(SlideAnimator.LEFT));
//        adapter.animator().setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator()));
        adapter.animator().setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator()){
            @Override
            protected long getAddDelay(RecyclerView.ViewHolder holder) {
                return 50;
            }
        });
        mRecyclerView.setAdapter(adapter);
        mData.update(ListX.range(20, index -> new MultiTypeEntity(index % 7 == 0 ? MultiTypeEntity.TYPE_CAN_SWIPE : MultiTypeEntity.TYPE_CAN_DRAG)));
    }
}
