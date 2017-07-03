package com.accuvally.hdtui.fragment.core;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.message.NewFriendActivity;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.message.core.MailActivity;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.db.MailMessageTable;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.MailCountUtil;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.accuvally.hdtui.utils.eventbus.EventRedDot;
import com.accuvally.hdtui.utils.eventbus.MailCountChange;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.client.methods.HttpGet;
public class MessageFragment extends BaseFragment implements OnClickListener {

	private SwipeMenuListView mListView;
	private CommonAdapter<SessionInfo> mAdapter;//消息fragme的adapter

	private final List<SessionInfo> list = new ArrayList<SessionInfo>();

	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;
	
	private int allUnReadNum;//所有session的未读数

    private int sysMsgUnReadNum;
    private int recommendUnReadNum;
    private int newFriendUnReadNum;
    public static final String TAG="MessageFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_message, container, false);
		EventBus.getDefault().register(this);
		initView(rootView);
		setData();//创建
		return rootView;
	}
//    int i=0;
    private void initView(View view) {
		initListView(view);

        view.findViewById(R.id.mine_test).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                i++;
//                deleteMessage(MailMessageTable.recommend,i+"");
//                getMessageCount(MailMessageTable.recommend);
//                getMessageCount(MailMessageTable.sysMsg);
//                getMessageCount(MailMessageTable.newFriend);
            }
        });

        view.findViewById(R.id.mine_test2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                getMessageCount(MailMessageTable.recommend);
//                Intent intent = new Intent(mContext, MailActivity.class);
//                intent.putExtra("inboxType",MailMessageTable.recommend);
//                startActivity(intent);

            }
        });

        view.findViewById(R.id.mine_test3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //                List<SessionInfo> tempList = SessionTable.queryReadSession(AccountManager.getAccount());
//                Trace.e("",tempList.toString());

//                                getMessageCount(MailMessageTable.sysMsg);

//                                getMessageCount(MailMessageTable.newFriend);

            }
        });

		lyFailure = (LinearLayout) view.findViewById(R.id.lyFailure);
		tvNoData = (TextView) view.findViewById(R.id.tvNoData);//提示
		ivFailure = (ImageView) view.findViewById(R.id.ivFailure);//避免单调的图片，固定
		SquareBtn = (Button) view.findViewById(R.id.SquareBtn);//按钮：登录，
		ivFailure.setBackgroundResource(R.drawable.no_emty);
	}

    //1.view   2.swipe  3.onclick
	private void initListView(View view) {
		mListView = (SwipeMenuListView) view.findViewById(R.id.listview);
		mAdapter = new CommonAdapter<SessionInfo>(mContext, R.layout.listitem_main_right_new) {

			@Override
			public void convert(ViewHolder viewHolder, SessionInfo item, int position) {
                switch (item.inboxType){
                    case MailMessageTable.recommend:
                        viewHolder.setText(R.id.tvMainCenterTitle, item.getTitle());
                        viewHolder.setText(R.id.tvMainRightTitle, "");
                        viewHolder.setText(R.id.tvMainRightContent, "");
//                        if (item.getUnReadNum() > 0) {
//                            viewHolder.setText(R.id.tvMainRightContent, "你有"+item.getUnReadNum()+"条推荐未读");
//                        }else {
//                            viewHolder.setText(R.id.tvMainRightContent, "");
//                        }
//                        Trace.e(TAG,item.inboxType+","+item.getUnReadNum());
                        viewHolder.setText(R.id.tvMainRightTime, "");
                        viewHolder.setImageResource(R.id.ivMainRightLogoUrl, R.drawable.mail_recommend_circle);
                        break;
                    case MailMessageTable.sysMsg:
                        viewHolder.setText(R.id.tvMainCenterTitle, item.getTitle());
                        viewHolder.setText(R.id.tvMainRightTitle, "");
                        viewHolder.setText(R.id.tvMainRightContent, "");
                        viewHolder.setText(R.id.tvMainRightTime, "");
                        viewHolder.setImageResource(R.id.ivMainRightLogoUrl, R.drawable.mail_sys_msg_circle);
//                        Trace.e(TAG, item.inboxType + "," + item.getUnReadNum());
                        break;
                    case MailMessageTable.newFriend:
                        viewHolder.setText(R.id.tvMainCenterTitle, item.getTitle());
                        viewHolder.setText(R.id.tvMainRightTitle, "");
                        viewHolder.setText(R.id.tvMainRightContent, "");
                        viewHolder.setText(R.id.tvMainRightTime, "");
                        viewHolder.setImageResource(R.id.ivMainRightLogoUrl, R.drawable.mail_newfriend_circle);
//                        Trace.e(TAG, item.inboxType + "," + item.getUnReadNum());
                        break;
                    case "":
                        viewHolder.setText(R.id.tvMainCenterTitle, "");
                        viewHolder.setText(R.id.tvMainRightTitle, item.getTitle());
                        viewHolder.setText(R.id.tvMainRightContent, item.getTypeContent());
                        viewHolder.setText(R.id.tvMainRightTime, item.getSessionTime());
                        if (item.isPrivateChat()) {
                            viewHolder.setImageUrl(R.id.ivMainRightLogoUrl, item.getLogoUrl(), UILoptions.defaultUser);
                        } else {
                            viewHolder.setImageUrl(R.id.ivMainRightLogoUrl, item.getLogoUrl(), UILoptions.squareOptions);
                        }
                        break;
                }



                TextView tvUnreadNum = viewHolder.getView(R.id.tvUnreadNum);
                if (item.getUnReadNum() > 0) {
                    tvUnreadNum.setVisibility(View.VISIBLE);
                    tvUnreadNum.setText(item.getUnReadNum() + "");
                } else {
                    tvUnreadNum.setVisibility(View.GONE);
                }
			}
		};

		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				deleteItem.setWidth(SystemUtil.dpToPx(mContext, 90));
				deleteItem.setIcon(R.drawable.ic_delete);
				menu.addMenuItem(deleteItem);
			}
		};
		mListView.setMenuCreator(creator);

		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                SessionInfo sessionInfo=list.get(position);
                switch (sessionInfo.inboxType){
                    case MailMessageTable.recommend:
                        ToastUtil.showMsg("不能删除官方推荐");
                        break;
                    case MailMessageTable.sysMsg:
                        ToastUtil.showMsg("不能删除系统消息");
                        break;
                    case MailMessageTable.newFriend:
                        ToastUtil.showMsg("不能删除新同伴");
                        break;
                    case "":
                        allUnReadNum-=sessionInfo.getUnReadNum();
                        EventBus.getDefault().post(new EventRedDot(allUnReadNum,false));//如果有未读消息，也要更新tab红点

                        SessionTable.deleteSessionById(sessionInfo.getSessionId());
                        list.remove(position);
                        mAdapter.notifyDataSetChanged();

                        break;
                }
            }
        });

		mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }

                SessionInfo info = list.get(position);
                allUnReadNum = allUnReadNum - info.getUnReadNum();
                info.setUnReadNum(0);

                switch (info.inboxType) {
                    case MailMessageTable.recommend:
                        Intent recommendIntent = new Intent(mContext, MailActivity.class);
                        recommendIntent.putExtra("inboxType", MailMessageTable.recommend);
                        startActivity(recommendIntent);
                        break;
                    case MailMessageTable.sysMsg:
                        Intent sysIntent = new Intent(mContext, MailActivity.class);
                        sysIntent.putExtra("inboxType", MailMessageTable.sysMsg);
                        startActivity(sysIntent);
                        break;
                    case MailMessageTable.newFriend:
                        Intent newFriendIntent = new Intent(mContext, NewFriendActivity.class);
                        newFriendIntent.putExtra("inboxType", MailMessageTable.newFriend);
                        startActivity(newFriendIntent);
                        break;
                    case "":
                        SessionTable.clearSessionUnreadNum(info.getSessionId());
                        application.setCurrentSession(info);

                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("isPrivateChat", info.isPrivateChat());
                        mContext.startActivity(intent);
                        break;
                }
                mAdapter.notifyDataSetChanged();//更新listview中本item的红点
                EventBus.getDefault().post(new EventRedDot(allUnReadNum, false));//更新消息tab下的红点，allUnReadNum>0，则红点继续显示

            }
        });

	}

    //查询的是本地数据库的会话列表,
    //调用时间：1.fragment初始化，  2.用户登录的时候  3.消息变化的时候   4.把leancloud收件箱的未读数都请求过来之后
	public void setData() {
		if (application.checkIsLogin()) {
			lyFailure.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			new TaskQuerySession().execute();

		} else {
			lyFailure.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);

            SquareBtn.setTextColor(getResources().getColor(R.color.white));
            SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
            int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
            SquareBtn.setPadding(padding, padding, padding, padding);

			tvNoData.setText("您还没有登录哦");
			SquareBtn.setVisibility(View.VISIBLE);
			SquareBtn.setText("登录");
            SquareBtn.setOnClickListener(this);
		}
	}

    public void onEventMainThread(MailCountChange eventBus) {
        setData();//MailCountChange
    }

	public void onEventMainThread(ChangeUserStateEventBus eventBus) {

		setData();//ChangeUserStateEventBus
	}

	public void onEventMainThread(ChangeMessageEventBus eventBus) {
		setData();//ChangeMessageEventBus
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()){
            case R.id.SquareBtn:
                if (!application.checkIsLogin()) {
                    toActivity(LoginActivityNew.class);
                    }
                break;
        }
	}

    //=========================================================================
	class TaskQuerySession extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			queryAllSession();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new EventRedDot(allUnReadNum,false));//红点
			if (list.size() == 0) {
				tvNoData.setText("您还没有加入任何活动群哦\n快去参加新活动加入活动群吧");
				SquareBtn.setVisibility(View.GONE);
				lyFailure.setVisibility(View.VISIBLE);
				mListView.setEmptyView(lyFailure);
			}
		}
	}


    private SessionInfo getSessionInfo(String inboxType,int unRead){
        SessionInfo sessionInfo=new SessionInfo();

        switch (inboxType){
            case MailMessageTable.recommend:
                sessionInfo.setTitle("官方推荐");
                sessionInfo.inboxType=inboxType;
                sessionInfo.setUnReadNum(unRead);
                break;
            case MailMessageTable.sysMsg:
                sessionInfo.setTitle("系统消息");
                sessionInfo.inboxType=inboxType;
                sessionInfo.setUnReadNum(unRead);
                break;
            case MailMessageTable.newFriend:
                sessionInfo.setTitle("新同伴");
                sessionInfo.inboxType=inboxType;
                sessionInfo.setUnReadNum(unRead);
                break;
        }

        return sessionInfo;
    }



    //从本地数据库中查找聊天列表，
    private void queryAllSession() {
        allUnReadNum = 0;
        list.clear();

        recommendUnReadNum=MailCountUtil.getUnreadCount(MailMessageTable.recommend);
        sysMsgUnReadNum=MailCountUtil.getUnreadCount(MailMessageTable.sysMsg);
        newFriendUnReadNum=MailCountUtil.getUnreadCount(MailMessageTable.newFriend);

        allUnReadNum=allUnReadNum+recommendUnReadNum+sysMsgUnReadNum+newFriendUnReadNum;

        if(recommendUnReadNum>0){
            list.add(getSessionInfo(MailMessageTable.recommend,recommendUnReadNum));
        }
        if(sysMsgUnReadNum>0){
            list.add(getSessionInfo(MailMessageTable.sysMsg,sysMsgUnReadNum));
        }
        if(newFriendUnReadNum>0){
            list.add(getSessionInfo(MailMessageTable.newFriend,newFriendUnReadNum));
        }

        List<SessionInfo> unReadTempList = SessionTable.queryUnreadSession(AccountManager.getAccount());
        for (SessionInfo info : unReadTempList) {
            if(!info.isAddNewFriend() && (!info.isNotification()) && (!info.getSessionId().startsWith("1f543f5")) ){
                allUnReadNum += info.getUnReadNum();
                list.add(info);
            }
        }

//-----------------------------------已读------------------------------------------------------
        if(recommendUnReadNum==0){
            if(MailMessageTable.isMailTypeExit(MailMessageTable.recommend)){
                list.add(getSessionInfo(MailMessageTable.recommend,recommendUnReadNum));
            }
        }
        if(sysMsgUnReadNum==0){
            if(MailMessageTable.isMailTypeExit(MailMessageTable.sysMsg)){
                list.add(getSessionInfo(MailMessageTable.sysMsg,sysMsgUnReadNum));
            }

        }
        if(newFriendUnReadNum==0){
            if(MailMessageTable.isMailTypeExit(MailMessageTable.newFriend)){
                list.add(getSessionInfo(MailMessageTable.newFriend,newFriendUnReadNum));
            }

        }

        List<SessionInfo> readTempList = SessionTable.queryReadSession(AccountManager.getAccount());
        for (SessionInfo info : readTempList) {
            if(!info.isAddNewFriend() &&(!info.isNotification())  && (!info.getSessionId().startsWith("1f543f5")) ){
                allUnReadNum += info.getUnReadNum();
                list.add(info);
            }
        }

        Trace.e(TAG,"allUnReadNumallUnReadNum:"+allUnReadNum+",unReadTempList.size():"+unReadTempList.size()+",readTempList.size():"+readTempList.size()
            +",recommendUnReadNum:"+recommendUnReadNum+",sysMsgUnReadNum:"
                +sysMsgUnReadNum+",newFriendUnReadNum:"+newFriendUnReadNum);
    }

}
