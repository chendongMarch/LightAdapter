package com.zfy.component.basic.mvvm.binding.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * CreateAt : 2018/9/10
 * Describe : adapter
 * bindInputFocus
 * bindTextChanged
 *
 * @author chendong
 */
public final class EditTextAdapters {

    @BindingAdapter(value = {"bindInputFocus"})
    public static void bindInputFocus(EditText editText, final Boolean requestFocus) {
        if (requestFocus) {
            editText.setFocusableInTouchMode(true);
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        } else {
            editText.setEnabled(false);
            editText.setEnabled(true);
        }
    }

    @BindingAdapter(value = "bindTextChanged", requireAll = false)
    public static void bindTextChanged(EditText editText, android.text.TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

}

