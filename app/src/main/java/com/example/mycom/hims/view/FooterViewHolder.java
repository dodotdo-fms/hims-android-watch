package com.example.mycom.hims.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycom.hims.R;
/**
 * Created by Omjoon on 2015. 12. 4..
 */
public class FooterViewHolder extends MyRecyclerView.ViewHolder {
    public FooterViewHolder(View footerView) {
        super(footerView);
    }

    public FooterViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_footer, parent, false));
    }
}