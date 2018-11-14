package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightAdapterBuilder;
import com.zfy.adapter.animations.ScaleAnimator;
import com.zfy.adapter.animations.SlideAnimator;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.LightView;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.MultiTypeEntity;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/13
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.animator_activity)
public class AnimatorTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private LightAdapter<MultiTypeEntity>  mAdapter;
    private LightDiffList<MultiTypeEntity> mEntities;

    @Override
    public void init() {
        mEntities = new LightDiffList<>();
        ModelTypeConfigCallback callback = modelType -> {
            switch (modelType.type) {
                case MultiTypeEntity.TYPE_DELEGATE:
                    modelType.spanSize = SpanSize.SPAN_SIZE_THIRD;
                    modelType.layoutId = R.layout.item_deleate;
                    modelType.animator = new ScaleAnimator(.1f).duration(500).interceptor(new OvershootInterpolator());
                    break;
                case MultiTypeEntity.TYPE_PROJECT:
                    modelType.spanSize = SpanSize.SPAN_SIZE_THIRD;
                    modelType.layoutId = R.layout.item_project;
                    modelType.animator = new SlideAnimator(SlideAnimator.LEFT).duration(500).interceptor(new OvershootInterpolator());
                    break;
            }
        };
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

        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder) -> {
            holder.setText(R.id.desc_tv, Values.getAnimatorDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.action_fab, v -> {
                    });
        });
        mAdapter.loadMore().setLoadMoreListener(adapter -> {
            post(() -> {
                appendData();
                mAdapter.loadMore().finishLoadMore();
            }, 2000);
        });
        mAdapter.animator().setAnimatorEnable(true);
        mAdapter.animator().setBindAnimatorOnlyOnce(false);
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mContentRv.setAdapter(mAdapter);
        appendData();
    }


    private void appendData() {
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
