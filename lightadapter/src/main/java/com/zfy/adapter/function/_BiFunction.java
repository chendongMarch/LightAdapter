package com.zfy.adapter.function;

import android.support.annotation.Nullable;

public interface _BiFunction<T, U, R> {
    R apply(@Nullable T t, @Nullable U u);
}
