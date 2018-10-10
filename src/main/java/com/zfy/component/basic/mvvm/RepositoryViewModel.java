package com.zfy.component.basic.mvvm;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * CreateAt : 2018/9/11
 * Describe : RepositoryViewModel 基类
 * 内部包含一个 IRepository 用来最数据交互
 *
 * @author chendong
 */
public abstract class RepositoryViewModel<R extends IRepository> extends BaseViewModel {

    protected R mRepository;

    public RepositoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = getRepository();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mRepository.onCleared();
    }

    protected abstract R getRepository();

}
