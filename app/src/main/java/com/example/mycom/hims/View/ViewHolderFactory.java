package com.example.mycom.hims.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by BeomJoon 2015-12-2.
 */
public abstract class ViewHolderFactory {
    public abstract RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);


    public static interface Updateable<T> {
        public void update(T data);

    }

}