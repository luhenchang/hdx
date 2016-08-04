package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用Adapter，只需要实现convert方法
 * 点击ItemView 要设置监听器setOnItemClickListener
 * viewHolder可以设置常用Textview ImageView
 * 
 * @author wan
 *
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mList = new ArrayList<T>();
	private int layoutId;
	protected OnItemListener onItemListener;
	
	public interface OnItemListener {
		public void onItemClick(Object object, int position);
	}

	public CommonAdapter(Context context, int itemLayoutId) {
		this.mContext = context;
		this.layoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position);

		convert(viewHolder, getItem(position), position);

		if (onItemListener != null) {
			viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onItemListener.onItemClick(getItem(position), position);
				}
			});
		}
		return viewHolder.getConvertView();
	}

	public abstract void convert(ViewHolder viewHolder, T item, int position);

	public OnItemListener getOnItemListener() {
		return onItemListener;
	}

	public void setOnItemListener(OnItemListener l) {
		this.onItemListener = l;
	}

	public void setList(List<T> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return mList;
	}

	public void add(T t) {
		if (mList != null) {
			mList.add(t);
			notifyDataSetChanged();
		}
	}
	
	public void addAll(List<T> list) {
		if (mList != null) {
			mList.addAll(list);
			notifyDataSetChanged();
		}
	}

	public void remove(int location) {
		if (mList != null) {
			mList.remove(location);
			notifyDataSetChanged();
		}
	}
	
	public void remove(T t) {
		if (mList != null) {
			mList.remove(t);
			notifyDataSetChanged();
		}
	}
	
	public void removeAll() {
		if (mList != null) {
			mList.clear();
			notifyDataSetChanged();
		}
	}

	public void clear() {
		if (mList != null) {
			mList.clear();
		}
	}

}
