package com.example.mycom.hims.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by BeomJoon 2015-12-2.
 */
public class MyRecyclerView extends RecyclerView {
    protected View emptyView;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (emptyView == null) {
                    return;
                }
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void setEmptyView(View view) {
        emptyView = view;
        Adapter adapter = getAdapter();
        if (adapter == null) {
            Log.e("empthyview ","visible1");
            emptyView.setVisibility(View.VISIBLE);
        } else {
            if (adapter.getItemCount() == 0) {
                Log.e("empthyview ","visible2");
                emptyView.setVisibility(View.VISIBLE);
            } else {
                Log.e("empthyview ","visible3");
                emptyView.setVisibility(View.GONE);
            }
        }
    }
}
