package com.zfy.light.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.march.common.exts.ListX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.listener.ViewHolderCallback;
import com.zfy.adapter.model.DragSwipeOptions;
import com.zfy.adapter.model.DragSwipeState;
import com.zfy.adapter.model.LightView;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.entity.MultiTypeEntity;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.drag_swipe_activity)
public class DragSwipeActivity extends MvpActivity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DragSwipeActivity.class);
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
                    modelType.spanSize = LightValues.SPAN_SIZE_HALF;
                    modelType.enableDrag = true;
                    break;
                case MultiTypeEntity.TYPE_CAN_SWIPE:
                    modelType.layoutId = R.layout.item_swipe;
                    modelType.spanSize = LightValues.SPAN_SIZE_ALL;
                    modelType.enableSwipe = true;
                    break;
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        LightAdapter<MultiTypeEntity> adapter = new LightAdapter<MultiTypeEntity>(getContext(), mData, updater) {
            @Override
            public void onBindView(LightHolder holder, MultiTypeEntity data, int pos) {
                switch (data.type) {
                    case MultiTypeEntity.TYPE_CAN_DRAG:
                        holder
                                .setText(R.id.title_tv, "本项支持拖拽")
                                .setText(R.id.desc_tv, "底部按钮，触摸/长按拖拽")
                                .dragOnTouch(R.id.touch_drag_iv)
                                .dragOnLongPress(R.id.press_drag_iv);
                        break;
                    case MultiTypeEntity.TYPE_CAN_SWIPE:
                        holder
                                .setText(R.id.title_tv, "本项支持侧滑")
                                .setText(R.id.desc_tv, "右侧按钮，触摸/长按侧滑")
                                .swipeOnTouch(R.id.touch_swipe_iv)
                                .swipeOnLongPress(R.id.press_swipe_iv);
                        break;
                }
            }
        };
        adapter.header().addHeaderView(LightView.from(R.layout.desc_header), new ViewHolderCallback() {
            @Override
            public void bind(LightHolder holder, int position) {
                holder.setText(R.id.desc_tv, Values.getDragSwipeDesc())
                        .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
            }
        });
        // 初始化拖拽和侧滑
        DragSwipeOptions options = new DragSwipeOptions();
        // 允许 4 个方向拖拽
        options.dragFlags =  ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        // 侧滑方向设置，允许左右方向侧滑
        options.swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        // 关闭自动侧滑，手动调用
        options.itemViewAutoSwipeEnable = false;
        // 关闭长按拖拽
        options.itemViewLongPressDragEnable = false;
        // 设置
        adapter.dragSwipe().setOptions(options);
        // 操作状态监听，做动画效果
        adapter.dragSwipe().setDragSwipeCallback(new BindCallback<DragSwipeState>() {
            @Override
            public void bind(LightHolder holder, int pos, DragSwipeState data) {
                switch (data.state) {
                    case DragSwipeState.ACTIVE_DRAG:
                        holder.setBgRes(R.id.item_view, R.drawable.shape_selected)
                                .setCallback(R.id.item_view, view -> {
                                    view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start();
                                });
                        break;
                    case DragSwipeState.ACTIVE_SWIPE:
                        holder.setBgRes(R.id.item_view, R.drawable.shape_selected);
                        break;
                    case DragSwipeState.RELEASE_DRAG:
                    case DragSwipeState.RELEASE_SWIPE:
                        holder.setBgColor(R.id.item_view, Color.WHITE)
                                .setCallback(R.id.item_view, view -> {
                                    view.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                                });
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        mData.update(ListX.range(100, index -> new MultiTypeEntity(index % 7 == 0 ? MultiTypeEntity.TYPE_CAN_SWIPE : MultiTypeEntity.TYPE_CAN_DRAG)));
    }
}
