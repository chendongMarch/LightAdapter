package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightAdapterBuilder;
import com.zfy.adapter.animations.ScaleAnimator;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.EmptyState;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.SampleUtils;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.entity.MultiTypeEntity;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/13
 * Describe : 空白页面测试
 *
 * @author chendong
 */
@MvpV(layout = R.layout.sample_activity)
public class EmptyTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private int mLoadCount = 0;

    private LightAdapter<MultiTypeEntity> mAdapter;
    private LightList<MultiTypeEntity>    mEntities;

    @Override
    public void init() {
        mEntities = LightList.diffList();
        // type callback
        ModelTypeConfigCallback callback = modelType -> {
            switch (modelType.type) {
                case MultiTypeEntity.TYPE_DELEGATE:
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    modelType.layoutId = R.layout.item_deleate;
                    break;
                case MultiTypeEntity.TYPE_PROJECT:
                    modelType.spanSize = SpanSize.SPAN_SIZE_HALF;
                    modelType.layoutId = R.layout.item_cover;
                    break;
            }
        };
        // adapter
        mAdapter = new LightAdapterBuilder<>(mEntities, callback).onBindView((holder, pos, data) -> {
            holder.setText(R.id.title_tv, data.title)
                    .setText(R.id.desc_tv, data.desc);
            switch (data.type) {
                case MultiTypeEntity.TYPE_DELEGATE:
                    holder.setText(R.id.subtitle_tv, "子标题");
                    break;
                case MultiTypeEntity.TYPE_PROJECT:
                    holder.setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
                    break;
            }
        }).onClickEvent((holder, pos, data) -> {
            ToastX.show("click item");
        }).build();
        // header
        // SampleUtils.addHeader(mAdapter, Values.getEmptyDesc(), null);
        SampleUtils.addEmpty(mAdapter, v -> {
            setEmpty(false);
            appendData();
        });
        SampleUtils.addLoadingView(mAdapter);
        // loadMore
        mAdapter.loadMore().setLoadMoreListener(adapter -> {
            post(() -> {
                appendData();
                mAdapter.loadMore().finishLoadMore();
            }, 2000);
        });
        // animator
        mAdapter.animator().setBindAnimator(new ScaleAnimator(.1f));
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentRv.setAdapter(mAdapter);

        // 模拟失败
        setEmpty(true);
    }


    public void setEmpty(boolean empty) {
        if (empty) {
            mLoadCount = 0;
            mAdapter.emptyView().setEmptyState(EmptyState.ERROR);
        } else {
            mAdapter.emptyView().setEmptyState(EmptyState.NONE);
        }
    }

    private void appendData() {
        mLoadCount++;
        if (mLoadCount > 2) {
            mEntities.updateClear();
            setEmpty(true);
            return;
        }
        List<MultiTypeEntity> list = ListX.range(10, index -> {
            MultiTypeEntity entity = new MultiTypeEntity(index % 3 == 0 ? MultiTypeEntity.TYPE_DELEGATE : MultiTypeEntity.TYPE_PROJECT);
            entity.title = "Title " + index;
            entity.desc = "Desc " + index;
            entity.subTitle = "SubTitle " + index;
            return entity;
        });
        mEntities.updateAddAll(list);
    }
}
