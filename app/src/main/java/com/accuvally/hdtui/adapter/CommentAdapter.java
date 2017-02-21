package com.accuvally.hdtui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.CommentInfo;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.ui.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Liu on 2016/11/23.
 */
public class CommentAdapter extends BaseAdapter {


    public String reply_type;// 1:活动评价; 2:活动咨询

    public CommentAdapter(Activity mContext, ArrayList<CommentInfo> commentInfos) {
        this.mContext = mContext;
        this.commentInfos = commentInfos;
        mInflater = LayoutInflater.from(mContext);
    }

    private ArrayList<CommentInfo> commentInfos;
    private  LayoutInflater mInflater;
    private Activity mContext;


    @Override
    public int getCount() {
        return commentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return commentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.list_item_comment, null);

            holder = new ViewHolder();

            holder.llReply = (LinearLayout) convertView.findViewById(R.id.ll_reply);
            holder.head = (CircleImageView) convertView.findViewById(R.id.item_comment_head);
            holder.textName = (TextView) convertView.findViewById(R.id.item_comment_name);
            holder.Zanimg = (ImageView) convertView.findViewById(R.id.item_comment_zanimg);
            holder.Zantext = (TextView) convertView.findViewById(R.id.item_comment_zantext);
            holder.textTime = (TextView) convertView.findViewById(R.id.item_comment_time);
            holder.pingOrWen = (ImageView) convertView.findViewById(R.id.item_comment_pingOrWen);

            holder.textCommentContent = (TextView) convertView.findViewById(R.id.item_comment_CommentContent);
            holder.textCommentActivity = (TextView) convertView.findViewById(R.id.item_comment_CommentActivity);

            holder.gvComment = (MyGridView) convertView.findViewById(R.id.item_comment_gridview);

            holder.LLreplays = (LinearLayout) convertView.findViewById(R.id.item_comment_LLreplays);


            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final CommentInfo commentInfo = commentInfos.get(position);
        if (commentInfo != null) {

            if(reply_type.equals("1")){
                holder.pingOrWen.setBackgroundResource(R.drawable.text_ping);
            }else {
                holder.pingOrWen.setBackgroundResource(R.drawable.text_wen);
            }

            ImageLoader.getInstance().displayImage(commentInfo.logo, holder.head);

            holder.textName.setText(commentInfo.nick);
            switch (commentInfo.zanstatus) {
                case 0:
                    holder.Zanimg.setBackground(null);
                    holder.Zantext.setText("");
                    break;
                case 1:
                    holder.Zanimg.setBackgroundResource(R.drawable.icon_good);
                    holder.Zantext.setText("赞了我的活动");
                    break;
                case 2:
                    holder.Zanimg.setBackgroundResource(R.drawable.icon_bad);
                    holder.Zantext.setText("踩了我的活动");
                    break;
            }

            try {
                String timeStr[]=commentInfo.createdate.split("T");
                commentInfo.createdate=timeStr[0];
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.textTime.setText(commentInfo.createdate);

            holder.textCommentContent.setText(commentInfo.content);
            holder.textCommentActivity.setText("活动："+commentInfo.eventname);

            holder.LLreplays.removeAllViews();
            if ((commentInfo.replays != null) && (commentInfo.replays.size() == 0)) {
                holder.LLreplays.setVisibility(View.GONE);
            }

            if ((commentInfo.replays != null) && (commentInfo.replays.size() != 0)) {
                holder.LLreplays.setVisibility(View.VISIBLE);
                for (CommentInfo.CommentReplyInfo com : commentInfo.replays) {
                    View subView = getSubItemView(com.commentName+" 回复@"+commentInfo.nick, com.commentContent);
                    holder.LLreplays.addView(subView);
                }
            }

            if (commentInfo.imgs != null) {
                holder.gvComment.setAdapter(new ImageAdapter(mContext, commentInfo.imgs));
            }
        }
        return convertView;

    }



    static class ViewHolder {

        public LinearLayout llReply;//回复触发按钮

        public CircleImageView head;
        public TextView textName;
        public ImageView Zanimg;
        public TextView Zantext;
        public TextView textTime;
        public ImageView pingOrWen;
        public TextView textCommentContent;
        public TextView textCommentActivity;
        public FrameLayout gvCommentImgs;

        public MyGridView gvComment;
//        public ImageView gvCommentImgs1;
//        public MyGridView gvCommentImgs2;
//        public MyGridView gvCommentImgs3;
        public LinearLayout LLreplays;//回复显示区域
    }


    private  View getSubItemView(String name, String content) {

        View view = mInflater.inflate(R.layout.list_item_comment_sub, null);
        TextView commentName = (TextView) view.findViewById(R.id.item_comment_sub_name);
        commentName.setText(name);
        TextView commentContent = (TextView) view.findViewById(R.id.item_comment_sub_content);
        commentContent.setText(content);
        return view;

    }

    private static View getSubItemView(Activity mContext,String name, String content) {

        View view = mContext.getLayoutInflater().inflate(R.layout.list_item_comment_sub, null);
        TextView commentName = (TextView) view.findViewById(R.id.item_comment_sub_name);
        commentName.setText(name);
        TextView commentContent = (TextView) view.findViewById(R.id.item_comment_sub_content);
        commentContent.setText(content);
        return view;

    }


    public void setList(ArrayList<CommentInfo> commentInfos) {
        this.commentInfos = commentInfos;
        notifyDataSetChanged();
    }


    public static View getView(final Activity mContext,final CommentInfo commentInfo){


        View convertView=null;
        ViewHolder holder = null;


            // 下拉项布局
            convertView = mContext.getLayoutInflater().inflate(R.layout.list_item_comment, null);

            holder = new ViewHolder();

            holder.llReply = (LinearLayout) convertView.findViewById(R.id.ll_reply);
            holder.head = (CircleImageView) convertView.findViewById(R.id.item_comment_head);
            holder.textName = (TextView) convertView.findViewById(R.id.item_comment_name);
            holder.Zanimg = (ImageView) convertView.findViewById(R.id.item_comment_zanimg);
            holder.Zantext = (TextView) convertView.findViewById(R.id.item_comment_zantext);
            holder.textTime = (TextView) convertView.findViewById(R.id.item_comment_time);
            holder.pingOrWen = (ImageView) convertView.findViewById(R.id.item_comment_pingOrWen);

            holder.textCommentContent = (TextView) convertView.findViewById(R.id.item_comment_CommentContent);
            holder.textCommentActivity = (TextView) convertView.findViewById(R.id.item_comment_CommentActivity);

            holder.gvComment = (MyGridView) convertView.findViewById(R.id.item_comment_gridview);

            holder.LLreplays = (LinearLayout) convertView.findViewById(R.id.item_comment_LLreplays);


        if (commentInfo != null) {

            holder.pingOrWen.setBackgroundResource(R.drawable.text_ping);
            ImageLoader.getInstance().displayImage(commentInfo.logo, holder.head);

            holder.textName.setText(commentInfo.nick);
            switch (commentInfo.zanstatus) {
                case 0:
                    holder.Zanimg.setBackground(null);
                    holder.Zantext.setText("");
                    break;
                case 1:
                    holder.Zanimg.setBackgroundResource(R.drawable.icon_good);
                    holder.Zantext.setText("赞了我的活动");
                    break;
                case 2:
                    holder.Zanimg.setBackgroundResource(R.drawable.icon_bad);
                    holder.Zantext.setText("踩了我的活动");
                    break;
            }

            try {
                String timeStr[]=commentInfo.createdate.split("T");
                commentInfo.createdate=timeStr[0];
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.textTime.setText(commentInfo.createdate);

            holder.textCommentContent.setText(commentInfo.content);
            holder.textCommentActivity.setText("活动："+commentInfo.eventname);

            holder.LLreplays.removeAllViews();
            if ((commentInfo.replays != null) && (commentInfo.replays.size() == 0)) {
                holder.LLreplays.setVisibility(View.GONE);
            }

            if ((commentInfo.replays != null) && (commentInfo.replays.size() != 0)) {
                holder.LLreplays.setVisibility(View.VISIBLE);
                for (CommentInfo.CommentReplyInfo com : commentInfo.replays) {
                    View subView = getSubItemView(mContext,com.commentName+" 回复@"+commentInfo.nick, com.commentContent);
                    holder.LLreplays.addView(subView);
                }
            }


            if (commentInfo.imgs != null) {
                holder.gvComment.setAdapter(new ImageAdapter(mContext, commentInfo.imgs));

            }

        }
        return convertView;
    }



}
