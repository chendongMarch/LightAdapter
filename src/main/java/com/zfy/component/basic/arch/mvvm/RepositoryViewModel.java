package com.zfy.component.basic.arch.mvvm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.zfy.component.basic.arch.IRepository;

/**
 * CreateAt : 2018/9/11
 * Describe : ViewModel with Repository
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
        mRepository.onDestroy();
    }

    protected abstract R getRepository();

}
