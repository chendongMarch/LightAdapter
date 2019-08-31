package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.march.common.exts.ListX;
import com.march.common.exts.SizeX;
import com.march.common.pool.ExecutorsPool;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.model.LoadingState;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/9
 * Describe : 底部加载更多测试
 *
 * @author chendong
 */
@MvpV(layout = R.layout.load_test_activity)
public class LoadTestActivity2 extends MvpActivity {

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

        public LoadData() {

        }

        @Override
        public int getItemType() {
            return type;
        }
    }

    @Override
    public void init() {
        mData = new LightDiffList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.HORIZONTAL, false));
        mAdapter = new LightAdapter<>(mData, R.layout.item_simple2);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.title_tv, "标题 " + data.id)
                    .setText(R.id.desc_tv, "描述 " + data.id + " " + System.currentTimeMillis());
        });
        mAdapter.setClickEvent((holder, data, extra) -> {

        });
        // 底部加载更多
        mAdapter.loadMore().setLoadMoreListener(3, adapter -> {
            ExecutorsPool.ui(() -> {
                List<LoadData> items = ListX.range(20, index -> new LoadData());
                mData.updateAddAll(items);
                mAdapter.loadMore().finishLoadMore();
                if (mData.size() > 100) {
                    mAdapter.loadMore().setLoadMoreEnable(false);
                    mAdapter.loadingView().setLoadingEnable(false);
                } else {
                    mAdapter.loadingView().setLoadingEnable(true);
                    mAdapter.loadMore().setLoadMoreEnable(true);
                }

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
        LightView from = LightView.from(R.layout.loading_view2);
        from.params = new LinearLayout.LayoutParams(SizeX.dp2px(200), ViewGroup.LayoutParams.MATCH_PARENT);
        mAdapter.loadingView().setLoadingView(from, (holder, data, extra) -> {
            switch (data.state) {
                case LoadingState.LOADING:
                    holder.setVisible(R.id.item_view);
                    holder.setVisible(R.id.pb)
                            .setText(R.id.content_tv, "加载中请稍候～");
                    break;
                case LoadingState.FINISH:
//                    holder.setGone(R.id.pb)
//                            .setText(R.id.content_tv, "加载完成");
                    break;
            }
        });
//        // empty
//        mAdapter.emptyView().setEmptyView(LightView.from(R.layout.empty_view), (holder, unpack, extra) -> {
//            holder.setClick(R.id.refresh_tv, v -> {
//                mAdapter.header().setHeaderEnable(true);
//                mData.update(ListX.range(20, index -> new LoadData(index % 7 == 0 ? Data.TYPE_CAN_SWIPE : Data.TYPE_CAN_DRAG)));
//                mAdapter.emptyView().setEmptyState(EmptyState.NONE);
//                mAdapter.loadingView().setLoadingEnable(true);
//                mAdapter.loadMore().setLoadMoreEnable(true);
//            });
//        });
//        // header
//        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), holder -> {
//            holder.setText(R.id.desc_tv, Values.getLoadingDesc())
//                    .setClick(R.id.action_fab, v -> {
//                        mData.update(new ArrayList<>());
//                        mAdapter.header().setHeaderEnable(false);
//                        mAdapter.loadingView().setLoadingEnable(false);
//                        mAdapter.loadMore().setLoadMoreEnable(false);
//                        mAdapter.emptyView().setEmptyState(EmptyState.ERROR);
//                    })
//                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
//        });
//        adapter.animator().setBindAnimator(new SlideAnimator(SlideAnimator.LEFT));
//        adapter.animator().setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator()));
//        mAdapter.animator().setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator()) {
//            @Override
//            protected long getAddDelay(RecyclerView.ViewHolder holder) {
//                return 50;
//            }
//        });
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.loadMore().activeLoadMore();
    }
}
