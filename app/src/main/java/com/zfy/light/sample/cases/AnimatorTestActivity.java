package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.animation.ScaleAnimator;
import com.zfy.adapter.animation.SlideAnimator;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.ModelTypeRegistry;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.SampleUtils;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.Data;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * CreateAt : 2018/11/13
 * Describe : 测试动画效果，ItemAnimator/BindAnimator
 *
 * @author chendong
 */
@MvpV(layout = R.layout.animator_activity)
public class AnimatorTestActivity extends MvpActivity {

    private static boolean sUseBindAnimator = false;

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private LightAdapter<Data> mAdapter;
    private LightList<Data>    mEntities;

    @Override
    public void init() {
        sUseBindAnimator = !sUseBindAnimator;
        mEntities = new LightDiffList<>();
        ModelTypeRegistry registry = ModelTypeRegistry.create();
        registry.add(new ModelType(Data.TYPE_DELEGATE, R.layout.item_deleate, SpanSize.SPAN_SIZE_HALF)
                .animator(sUseBindAnimator ? new ScaleAnimator(.1f).duration(500).interceptor(new OvershootInterpolator()) : null));
        registry.add(new ModelType(Data.TYPE_PROJECT,R.layout.item_cover, SpanSize.SPAN_SIZE_HALF)
                .animator(sUseBindAnimator ? new SlideAnimator(SlideAnimator.LEFT).duration(500).interceptor(new OvershootInterpolator()) : null));
        mAdapter = new LightAdapter<>(mEntities, registry);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.title_tv, data.title)
                    .setText(R.id.desc_tv, data.desc);
            switch (data.type) {
                case Data.TYPE_DELEGATE:
                    holder.setText(R.id.subtitle_tv, sUseBindAnimator ? "BindAnimator-缩放动画" : "ItemAnimator-缩放动画");
                    break;
                case Data.TYPE_PROJECT:
                    holder.setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                            .setText(R.id.desc_tv, sUseBindAnimator ? "BindAnimator-左滑动画" : "ItemAnimator-缩放动画");
                    break;
            }
        });
        mAdapter.setClickEvent((holder, data, extra) -> {
            ToastX.show("click item");
        });
        // header
        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder) -> {
            holder.setText(R.id.desc_tv, Values.getAnimatorDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.action_fab, v -> {
                    });
        });
        //
        SampleUtils.addLoadingView(mAdapter);
        // loadMore
        mAdapter.loadMore().setLoadMoreListener(adapter -> {
            post(() -> {
                appendData();
                mAdapter.loadMore().finishLoadMore();
            }, 2000);
        });
        // animator
        mAdapter.animator().setAnimatorEnable(true);
        if (sUseBindAnimator) {
            mAdapter.animator().setBindAnimatorOnlyOnce(false);
            ToastX.showLong("使用 BindAnimator，并且开启每次绑定都会执行动画，再次进入切换到 ItemAnimator");
        } else {
            mAdapter.animator().setItemAnimator(new ScaleInAnimator(new OvershootInterpolator()) {
                @Override
                public long getAddDuration() {
                    return 200;
                }

                @Override
                protected long getAddDelay(RecyclerView.ViewHolder holder) {
                    return 50;
                }
            });
            ToastX.showLong("使用 ItemAnimator，只有局部刷新才会触发动画，再次进入切换到 BindAnimator");
        }
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentRv.setAdapter(mAdapter);
        appendData();
    }


    private void appendData() {
        List<Data> list = ListX.range(10, index -> {
            Data entity = new Data(index % 3 == 0 ? Data.TYPE_DELEGATE : Data.TYPE_PROJECT);
            entity.title = "Title " + index;
            entity.desc = "Desc " + index;
            entity.subTitle = "SubTitle " + index;
            return entity;
        });
        mEntities.updateAddAll(list);
    }
}
