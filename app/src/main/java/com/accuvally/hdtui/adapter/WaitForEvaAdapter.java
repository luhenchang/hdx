package com.accuvally.hdtui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.comment.CommentActivity;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.model.PublishBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Liu on 2017/7/10.
 */
public class WaitForEvaAdapter extends BaseAdapter{

    public WaitForEvaAdapter(Activity mContext, ArrayList<PublishBean> publishBeans) {
        this.mContext = mContext;
        this.publishBeans = publishBeans;
        mInflater = LayoutInflater.from(mContext);
    }

    private ArrayList<PublishBean> publishBeans;
    private LayoutInflater mInflater;
    private Activity mContext;



    @Override
    public int getCount() {
        return publishBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return publishBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.listitem_waitforevaluate, null);

            holder = new ViewHolder();

            holder.ll_listitem_waitforevaluate= (LinearLayout) convertView.findViewById(R.id.ll_listitem_waitforevaluate);
            holder.ivItemWaitForEvaluateImg = (ImageView) convertView.findViewById(R.id.ivItemWaitForEvaluateImg);
            holder.tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
            holder.start_evaluate = (ImageView) convertView.findViewById(R.id.start_evaluate);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final PublishBean publishBean = publishBeans.get(position);
        if (publishBean != null) {

            ImageLoader.getInstance().displayImage(publishBean.logo, holder.ivItemWaitForEvaluateImg, UILoptions.rectangleOptions);
            holder.tvItemTitle.setText(publishBean.title);

            holder.start_evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublishBean publishBean = publishBeans.get(position );

                    Intent evaluateIntent = new Intent(mContext, CommentActivity.class);
                    evaluateIntent.putExtra(CommentActivity.TITLE, publishBean.title);
                    evaluateIntent.putExtra(CommentActivity.TIME, publishBean.timestr);
                    evaluateIntent.putExtra(CommentActivity.LOCATION, publishBean.address);
                    evaluateIntent.putExtra(CommentActivity.LOGO, publishBean.logo);
                    evaluateIntent.putExtra(CommentActivity.ID, publishBean.id);
                    evaluateIntent.putExtra(CommentActivity.SPONSOR, publishBean.orgname);
                    mContext.startActivity(evaluateIntent);

                }
            });

            holder.ll_listitem_waitforevaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublishBean publishBean = publishBeans.get(position);
                    mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                            putExtra("id", publishBean.id));
                }
            });

        }
        return convertView;

    }



    class ViewHolder {

        public LinearLayout ll_listitem_waitforevaluate;//
        public ImageView ivItemWaitForEvaluateImg;
        public TextView tvItemTitle;
        public ImageView start_evaluate;



    }



}
