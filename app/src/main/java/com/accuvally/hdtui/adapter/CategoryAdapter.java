package com.accuvally.hdtui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.accuvally.hdtui.R;

import java.util.ArrayList;

/**
 * Created by Andy Liu on 2016/11/24.
 */
public class CategoryAdapter extends BaseAdapter {

    ArrayList<String> categorys;
    private LayoutInflater mInflater;
    private Activity mContext;
    private ArrayList<String> select_categoryList =new ArrayList<>();

    public CategoryAdapter(Activity mContext, ArrayList<String> categorys) {
        this.mContext = mContext;
        this.categorys = categorys;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setSelect_categoryList(ArrayList<String> select_categoryList){
        this.select_categoryList=select_categoryList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categorys.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView textView;
        if (convertView == null) {
            textView = (TextView) mInflater.inflate(R.layout.item_grid_category, parent, false);
        } else {
            textView = (TextView) convertView;
        }

        String str=categorys.get(position);
        textView.setText(str);

        if(select_categoryList.contains(str)){
            textView.setBackgroundResource(R.drawable.category_back_set);
        }else {
            textView.setBackgroundResource(R.drawable.category_back_nor);
        }
        return textView;
    }
}
