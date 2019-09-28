package com.zfy.lxadapter.component;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;

import com.zfy.lxadapter.animation.BindAnimator;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxVh;
import com.zfy.lxadapter.data.TypeOpts;

import java.util.List;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxBindAnimatorComponent extends LxComponent {

    private int          lastPosition = -1;
    private boolean      animatorOnlyOnce;
    private BindAnimator bindAnimator;
    private boolean      enableAnimator;


    public LxBindAnimatorComponent() {
        this(null, true);
    }

    public LxBindAnimatorComponent(BindAnimator bindAnimator) {
        this(bindAnimator, true);
    }

    public LxBindAnimatorComponent(BindAnimator bindAnimator, boolean animatorOnlyOnce) {
        this.animatorOnlyOnce = animatorOnlyOnce;
        this.bindAnimator = bindAnimator;
        this.enableAnimator = true;
    }

    @Override
    public void onBindViewHolder(LxAdapter adapter, @NonNull LxVh holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(adapter, holder, position, payloads);
        if (!enableAnimator) {
            return;
        }
        int itemViewType = adapter.getItemViewType(position);
        TypeOpts typeOpts = adapter.getTypeOpts(itemViewType);
        BindAnimator animator = (typeOpts == null || typeOpts.bindAnimator == null) ? bindAnimator : typeOpts.bindAnimator;
        if (animator == null) {
            return;
        }
        int adapterPosition = holder.getAdapterPosition();
        if (!animatorOnlyOnce || adapterPosition > lastPosition) {
            for (Animator anim : animator.getAnimators(holder.itemView)) {
                anim.setDuration(animator.getDuration()).start();
                anim.setInterpolator(animator.getInterceptor());
            }
            lastPosition = adapterPosition;
        } else {
            clear(holder.itemView);
        }
    }

    public void setEnableAnimator(boolean enable) {
        enableAnimator = enable;
    }

    private void clear(View v) {
        v.setAlpha(1);
        v.setScaleY(1);
        v.setScaleX(1);
        v.setTranslationY(0);
        v.setTranslationX(0);
        v.setRotation(0);
        v.setRotationY(0);
        v.setRotationX(0);
        v.setPivotY(v.getMeasuredHeight() / 2);
        v.setPivotX(v.getMeasuredWidth() / 2);
        v.animate().setInterpolator(null).setStartDelay(0);
    }

}
