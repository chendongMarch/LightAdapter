package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.march.common.exts.ListX;
import com.march.common.pool.ExecutorsPool;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.ModelTypeRegistry;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.model.EmptyState;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.model.LoadingState;
import com.zfy.adapter.model.ModelType;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.Data;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * CreateAt : 2018/11/9
 * Describe : 底部加载更多测试
 *
 * @author chendong
 */
@MvpV(layout = R.layout.load_test_activity)
public class LoadTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView mRecyclerView;

    private LightAdapter<LoadData> mAdapter;
    private LightList<LoadData>    mData;

    static class LoadData implements Typeable, Diffable<LoadData> {

        static int ID = 100;

        int id;
        int type;

        public LoadData(int type) {
            this.type = type;
            this.id = ID++;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }
    @Override
    public void init() {
        mData = new LightDiffList<>();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ModelTypeRegistry registry = ModelTypeRegistry.create();
        registry.add(new ModelType(Data.TYPE_CAN_DRAG, R.layout.item_drag, SpanSize.SPAN_SIZE_HALF));
        registry.add(new ModelType(Data.TYPE_CAN_SWIPE, R.layout.item_swipe, SpanSize.SPAN_SIZE_ALL));
        mAdapter = new LightAdapter<>(mData, registry);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.title_tv, "标题 " + data.id)
                    .setText(R.id.desc_tv, "描述 " + data.id + " " + System.currentTimeMillis());
        });
        // 底部加载更多
        mAdapter.loadMore().setLoadMoreListener(3, adapter -> {
            ExecutorsPool.ui(() -> {
                List<LoadData> items = ListX.range(20, index -> {
                    return new LoadData(index % 7 == 0 ? Data.TYPE_CAN_SWIPE : Data.TYPE_CAN_DRAG);
                });
                mAdapter.loadMore().finishLoadMore();
                mData.updateAddAll(items);
            }, 1500);
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
        mAdapter.loadingView().setLoadingView(LightView.from(R.layout.loading_view), (holder, data, extra) -> {
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
        mAdapter.emptyView().setEmptyView(LightView.from(R.layout.empty_view), (holder, data, extra) -> {
                holder.setClick(R.id.refresh_tv, v -> {
                    mAdapter.header().setHeaderEnable(true);
                    mData.update(ListX.range(20, index -> new LoadData(index % 7 == 0 ? Data.TYPE_CAN_SWIPE : Data.TYPE_CAN_DRAG)));
                    mAdapter.emptyView().setEmptyState(EmptyState.NONE);
                    mAdapter.loadingView().setLoadingEnable(true);
                    mAdapter.loadMore().setLoadMoreEnable(true);
                });
        });
        // header
        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), holder -> {
            holder.setText(R.id.desc_tv, Values.getLoadingDesc())
                    .setClick(R.id.action_fab, v -> {
                        mData.update(new ArrayList<>());
                        mAdapter.header().setHeaderEnable(false);
                        mAdapter.loadingView().setLoadingEnable(false);
                        mAdapter.loadMore().setLoadMoreEnable(false);
                        mAdapter.emptyView().setEmptyState(EmptyState.ERROR);
                    })
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
        });
//        adapter.animator().setBindAnimator(new SlideAnimator(SlideAnimator.LEFT));
//        adapter.animator().setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator()));
        mAdapter.animator().setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator()) {
            @Override
            protected long getAddDelay(RecyclerView.ViewHolder holder) {
                return 50;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mData.update(ListX.range(20, index -> new LoadData(index % 7 == 0 ? Data.TYPE_CAN_SWIPE : Data.TYPE_CAN_DRAG)));
    }
}
