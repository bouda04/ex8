package com.example.tcalc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.widget.EditText;

/**
 * Created by bouda04 on 3/12/2016.
 */

public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return new EditTextContextMenuInfo(this);
    }

    public static class EditTextContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public EditText targetView;
        public EditTextContextMenuInfo(EditText targetView) {
            this.targetView = targetView;
        }
    }
}
