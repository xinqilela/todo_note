package com.example.linukey.util;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by linukey on 12/8/16.
 */

public class ViewHolder {
    // I added a generic return type to reduce the casting noise in client code
    @SuppressWarnings("unchecked")
    public static Object get(View view, int id) {
        SparseArray viewHolder = (SparseArray) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray();
            view.setTag(viewHolder);
        }
        View childView = (View) viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return childView;
    }
}
