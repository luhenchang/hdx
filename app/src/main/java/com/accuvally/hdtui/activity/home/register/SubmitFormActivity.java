package com.accuvally.hdtui.activity.home.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.buy.PaySuccessActivity;
import com.accuvally.hdtui.activity.home.buy.PayWebActivity;
import com.accuvally.hdtui.activity.home.buy.SureOrderActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.fragment.detailfragment.DetailLeft;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.FromInfo;
import com.accuvally.hdtui.model.ItemInfo;
import com.accuvally.hdtui.model.KeyValueInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.ui.CustomerScrollView;
import com.accuvally.hdtui.utils.AndroidHttpClient;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.FileCache;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ImageBig;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.chunk.ChunkInfo;
import com.accuvally.hdtui.utils.chunk.CustomFileBody;
import com.accuvally.hdtui.utils.chunk.FileInfo;
import com.accuvally.hdtui.utils.chunk.UploadChunkUtil;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.EventEnroll;
import com.accuvally.hdtui.utils.eventbus.EventRobSuccess;
import com.accuvally.hdtui.utils.hawkj2.Algorithm;
import com.accuvally.hdtui.utils.hawkj2.AuthorizationHeader;
import com.accuvally.hdtui.utils.hawkj2.HawkContext;
import com.accuvally.hdtui.utils.hawkj2.util.StringUtils;
import com.accuvally.hdtui.utils.timepicker.JudgeDate;
import com.accuvally.hdtui.utils.timepicker.ScreenInfo;
import com.accuvally.hdtui.utils.timepicker.WheelMain;
import com.alibaba.fastjson.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 购票-填写报名表单
 * 
 * @author Semmer Wang
 * 
 */
public class SubmitFormActivity extends BaseActivity implements OnClickListener {

    public static final String TAG="SubmitFormActivity";
	private LinearLayout lyAddForm;
	private AccuDetailBean info;
	private static final int RADIO_CHANGE_UI = 0;
	private static final int CHECK_BOX_CHANG_UI = 1;
	FromInfo fromInfo;
	List<KeyValueInfo> list = new ArrayList<KeyValueInfo>();//收集到的报名数据
	Map<String, String> radioMap = new HashMap<String, String>();//保存单选内容
	Map<String, List<String>> checkMap = new HashMap<String, List<String>>();//保存多选内容
	WheelMain wheelMain;
	private Dialog dialogTime;
	private TextView tvSecondNext;
	private int position;
	private Dialog radioDialog;
	private Dialog checkDialog;
	private LinearLayout ll_add_checkbox_info;
	private TextView bt_checkbox_submit;
	private CustomerScrollView buyTicketScrollview;

    private String scode;//抢票s码


    public final static int getPicture=1;
    public final static int getFile=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_ticket_second);
		info = (AccuDetailBean) getIntent().getSerializableExtra("info");
        scode=getIntent().getStringExtra(DetailLeft.SCODE);
        if (info.ticks.size() != 0) {
            position = getIntent().getIntExtra("position", 0);
        }
		initView();
		initData();
		controlScrollView();
	}

	private void controlScrollView() {
		// TODO Auto-generated method stub

		buyTicketScrollview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (buyTicketScrollview.getHeight() >= lyAddForm.getHeight()) {
                            Log.i("height", "buyTicketScrollview.getHeight()=" + buyTicketScrollview.getHeight() + ",lyAddForm.getHeight()=" + lyAddForm.getHeight());
                            return true;
                        } else {
                            return false;
                        }
                }
                return false;

            }
        });
	}

	public void initView() {
		setTitle(getResources().getString(R.string.buy_ticket_second_title));

		lyAddForm = (LinearLayout) findViewById(R.id.lyAddForm);
		tvSecondNext = (TextView) findViewById(R.id.tvSecondNext);
		buyTicketScrollview = (CustomerScrollView) findViewById(R.id.buy_ticket_scrollview);
		buyTicketScrollview.setFocusable(false);
		buyTicketScrollview.fullScroll(ScrollView.FOCUS_UP);
		tvSecondNext.setOnClickListener(this);
	}


	public void setViewlayoutParam(View view, int left, int top, int right, int bottom) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		lp.setMargins(left, top, right, bottom);

		view.setLayoutParams(lp);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RADIO_CHANGE_UI:

				String[] stringMsgHandle = (String[]) msg.obj;
				TextView textView = (TextView) lyAddForm.findViewWithTag(stringMsgHandle[0]);
				textView.setText(stringMsgHandle[1]);
				break;
			case CHECK_BOX_CHANG_UI:
				TextView textView1 = (TextView) lyAddForm.findViewWithTag(msg.obj);
				List<String> listCheck = checkMap.get(msg.obj);
				String checkBoxText = "";
				if (listCheck != null && listCheck.size() != 0) {
					Log.i("viewTag", msg.obj.toString());
					for (int i = 0; i < listCheck.size(); i++) {
						if (i == listCheck.size() - 1) {
							checkBoxText += listCheck.get(i);
						} else {
							checkBoxText += listCheck.get(i) + "、";
						}
					}
					if (checkBoxText.length() > 8) {
						checkBoxText = checkBoxText.substring(0, 8);
						checkBoxText = checkBoxText + "...";
					}
					textView1.setText(checkBoxText);
					textView1.setTextColor(getResources().getColor(R.color.black));
				} else {
					Log.i("viewTag", msg.obj.toString());
					textView1.setText("选择");
					textView1.setTextColor(getResources().getColor(R.color.gary_title));
				}
                break;
			default:
				break;
			}
		};
	};

    //1.input/number   3.email    --->setTitle
	private void setSingleText(final ItemInfo itemInfo, String strTitle, int intSort, String strType) {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		TextView textView = setTitle(itemInfo, strTitle);
		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp1.setMargins(10, 10, 0, 10);
		textView.setLayoutParams(lp1);
		EditText editText = new EditText(mContext);
		editText.setHint("请输入" + strTitle);
		editText.setBackgroundResource(0);
		editText.setTextSize(16);
		editText.setTextColor(getResources().getColor(R.color.txt_gray));
		editText.setTag("I_" + intSort);
		editText.setSingleLine(true);
		editText.setPadding(10, 10, 10, 10);
		if ("number".equals(strType)) {
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			editText.setKeyListener(new NumberKeyListener() {
				protected char[] getAcceptedChars() {
					return new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
				}

				@Override
				public int getInputType() {
					return InputType.TYPE_CLASS_NUMBER;
				}
			});
		}
		LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		lp2.setMargins(0, 10, 0, 10);
		editText.setLayoutParams(lp2);
		layout.addView(textView);
		layout.addView(editText);
		lyAddForm.addView(layout);
	}


    private void setAreaText(final ItemInfo itemInfo, String strTitle, int intSort, String strType){

        TextView textView = new TextView(mContext);
        textView.setText(strTitle);
        textView.setTextColor(getResources().getColor(R.color.gary_title));
        if (itemInfo.isRequired()) {
            String text = strTitle + "*:";
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red2)), strTitle.length(), text.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(style);
        } else{
            textView.setText(strTitle + ":");
        }

        textView.setTextSize(16);
        setViewlayoutParam(textView, 0, 10, 0, 10);
        lyAddForm.addView(textView);
        EditText editText = new EditText(mContext);
        editText.setHint("请输入" + strTitle);
        editText.setBackgroundResource(0);
        editText.setTextSize(16);
        editText.setLines(5);
        editText.setTextColor(getResources().getColor(R.color.txt_gray));
        editText.setGravity(Gravity.TOP | Gravity.LEFT);
        editText.setTag("I_" + intSort);
        editText.setPadding(10, 10, 10, 10);
        setViewlayoutParam(editText, 0, 10, 0, 10);
        lyAddForm.addView(editText);

    }

	public void initData() {

        //1.input  2.number   3.email   4.textarea  5.radio(弹出单选对话框) 6.checkbox(弹出多选对话框)  7.select(下拉)  8.date(弹出日期对话框)   9.file
//        setTag("I_" + intSort)
        fromInfo = JSON.parseObject(info.form, FromInfo.class);
        Trace.e(TAG,"要填写的报名表单："+info.form);
		for (int i = 0; i < fromInfo.items.size(); i++) {
			final ItemInfo itemInfo = fromInfo.items.get(i);
			String strTitle = itemInfo.getTitle();
			String strType = itemInfo.getType();
			int intSort = itemInfo.getSort();
			if ("input".equals(strType) || "number".equals(strType) ||"email".equals(strType)) {
				setSingleText(itemInfo, strTitle, intSort, strType);
			}else if ("textarea".equals(strType)) {
                setAreaText(itemInfo, strTitle, intSort, strType);
            }else if ("radio".equals(strType)) {
				LinearLayout layout = choiceInfo(itemInfo, strTitle, intSort, strType);
				final String key = "I_" + intSort;
				final String title = strTitle;

				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showRadioDialog(itemInfo, key, title);
					}
				});
			} else if ("checkbox".equals(strType)) {
				final ArrayList<String> checkIntegersList = new ArrayList<String>();
				LinearLayout layout = choiceInfo(itemInfo, strTitle, intSort, strType);
				final String key = "I_" + intSort;
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						showCheckDialog(itemInfo, checkIntegersList, key);
					}

				});

			} else if ("select".equals(strType)) {
				TextView textView = new TextView(mContext);
				textView.setText(strTitle);
				textView.setTextColor(getResources().getColor(R.color.gary_title));
				if (itemInfo.isRequired()) {
					String text = strTitle + "*:";
					SpannableStringBuilder style = new SpannableStringBuilder(text);
					style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red2)), strTitle.length(), text.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					textView.setText(style);
				} else{
                    textView.setText(strTitle + ":");
                }
				textView.setTextSize(16);
				setViewlayoutParam(textView, 0, 10, 0, 10);
				lyAddForm.addView(textView);
				Spinner spinner = new Spinner(mContext);
				spinner.setTag("I_" + intSort);
				final List<String> stringArray = new ArrayList<String>();
				for (int j = 0; j < itemInfo.getSubItems().size(); j++) {
					TextView tvSpinner = new TextView(mContext);
					tvSpinner.setText(itemInfo.getSubItems().get(j));
					tvSpinner.setTextColor(getResources().getColor(R.color.txt_gray));
					stringArray.add(itemInfo.getSubItems().get(j));
				}
				ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, stringArray);
				mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(mAdapter);
                setViewlayoutParam(spinner, 0, 10, 0, 10);
				lyAddForm.addView(spinner);

				final String sort = "I_" + intSort;
                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Log.i("info", stringArray.get(arg2));
                        KeyValueInfo keyValueInfo = new KeyValueInfo();
                        keyValueInfo.setKey(sort);
                        keyValueInfo.setValue(stringArray.get(arg2));
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getKey().equals(sort)) {
                                list.remove(i);
                            }
                        }
                        list.add(keyValueInfo);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
			} else if ("date".equals(strType)) {
				LinearLayout layout = new LinearLayout(mContext);
				layout.setOrientation(LinearLayout.HORIZONTAL);
				LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				TextView textView = new TextView(mContext);
				textView.setLayoutParams(lp1);
				textView.setText(strTitle);
				textView.setTextColor(getResources().getColor(R.color.gary_title));
				if (itemInfo.isRequired()) {
					String text = strTitle + "*:";
					SpannableStringBuilder style = new SpannableStringBuilder(text);
					style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red2)), strTitle.length(), text.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					textView.setText(style);
				} else{
                    textView.setText(strTitle + ":");
                }
				textView.setTextSize(16);
				layout.addView(textView);
				final TextView textViewData = new TextView(mContext);
				textViewData.setBackgroundResource(0);
				textViewData.setTextSize(16);
				textViewData.setTextColor(getResources().getColor(R.color.txt_gray));
				textViewData.setHint("请选择" + strTitle);
				LayoutParams lp2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
				textViewData.setLayoutParams(lp2);
				textViewData.setTag("I_" + intSort);
				textViewData.setGravity(Gravity.CENTER | Gravity.LEFT);
				layout.addView(textViewData);
				setViewlayoutParam(layout, 0, 10, 0, 10);
				lyAddForm.addView(layout);
				final String title = "请选择" + strTitle;
				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showDialogTime(title, textViewData);
					}
				});
			}else if("file".equals(strType)){
                fileCache = new FileCache(mContext);
                // （报名表单附件上传文件名规则：活动ID+“-s”+报名表单附件sort值+"-sn"+票种ID，eg: "7365682477000-s3-sn1"）
                uploadFileName =info.id+"-s"+itemInfo.getSort()+"-sn"+info.ticks.get(position).getSN();
                Trace.e(TAG,"position:"+position);
                Trace.e(TAG,uploadFileName);
                View view=getFileFormView(itemInfo);
                lyAddForm.addView(view);
            }

			View view = new View(mContext);
			view.setBackgroundResource(R.drawable.details_icon_line3);
			setViewlayoutParam(view, 0, 20, 0, 20);
			lyAddForm.addView(view);

		}
	}


	private LinearLayout choiceInfo(final ItemInfo itemInfo, String strTitle, int intSort, String strType) {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams lp1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(lp1);
		textView.setText(strTitle);
		textView.setTextColor(getResources().getColor(R.color.gary_title));
		if (itemInfo.isRequired()) {
			String text = strTitle + "*:";
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red2)), strTitle.length(), text.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			textView.setText(style);
		} else
			textView.setText(strTitle + ":");
		textView.setTextSize(16);
		layout.addView(textView);
		TextView textViewContent = new TextView(mContext);
		LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textViewContent.setLayoutParams(lp2);
		textViewContent.setTextSize(16);
		textViewContent.setText("选择");
		textViewContent.setTextColor(getResources().getColor(R.color.gary_title));
		textViewContent.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.details_icon_jiantou2), null);
		textViewContent.setCompoundDrawablePadding(10);
		textViewContent.setTag("I_" + intSort);
		layout.addView(textViewContent);
		setViewlayoutParam(layout, 0, 10, 0, 10);
		lyAddForm.addView(layout);
		return layout;
	}

	private TextView setTitle(final ItemInfo itemInfo, String strTitle) {
		TextView textView = new TextView(mContext);
		textView.setTextColor(getResources().getColor(R.color.gary_title));
		if (itemInfo.isRequired()) {
			String text = strTitle + "*:";
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red2)), strTitle.length(), text.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			textView.setText(style);
		} else
			textView.setText(strTitle + ":");
		textView.setTextSize(16);
		return textView;
	}

    //====================================================================================================
	//多选
    protected void showCheckDialog(ItemInfo itemInfo, final List<String> checkIntegersList, final String checkKey) {
		// TODO Auto-generated method stub
		checkDialog = new Dialog(mContext, R.style.dialog);
		checkDialog.setCancelable(true);
		checkDialog.setCanceledOnTouchOutside(true);
		checkDialog.setContentView(R.layout.dialog_checkbox);
		ll_add_checkbox_info = (LinearLayout) checkDialog.findViewById(R.id.ll_add_checkbox_info);
		bt_checkbox_submit = (TextView) checkDialog.findViewById(R.id.bt_checkbox_submit);
		int intSort = itemInfo.getSort();
		final LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setTag("I_" + intSort);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		for (int j = 0; j < itemInfo.getSubItems().size(); j++) {
			final CheckBox checkBox = new CheckBox(mContext);
			checkBox.setPadding(40, 20, 20, 20);
			checkBox.setText(itemInfo.getSubItems().get(j));
			checkBox.setTextSize(16);
			checkBox.setTag(itemInfo.getSubItems().get(j));
			checkBox.setTextColor(getResources().getColor(R.color.gary_title));
			checkBox.setCompoundDrawablePadding(10);
			checkBox.setButtonDrawable(android.R.color.transparent);
			checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checkbox_message_item_select), null);

			List<String> listString = checkMap.get(checkKey);
			if (listString != null && listString.size() > 0) {
				for (int i = 0; i < listString.size(); i++) {
					if ((itemInfo.getSubItems().get(j)).equals(listString.get(i))) {
						checkBox.setChecked(true);
						break;
					}
				}
			}
			linearLayout.addView(checkBox, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			View view = new View(mContext);
			view.setBackgroundResource(R.drawable.details_icon_line3);
			setViewlayoutParam(view, 0, 10, 0, 10);
			linearLayout.addView(view);
		}
		setViewlayoutParam(linearLayout, 0, 10, 0, 10);
		ll_add_checkbox_info.addView(linearLayout);

		if (linearLayout.getTag().toString().equals("I_" + intSort))
			for (int j = 0; j < itemInfo.getSubItems().size(); j++) {
				final CheckBox box = (CheckBox) linearLayout.findViewWithTag(itemInfo.getSubItems().get(j));
				box.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						if (arg1) {
							checkIntegersList.add(box.getText().toString());
						} else {
							checkIntegersList.remove(box.getText().toString());
						}

						checkMap.put(checkKey, checkIntegersList);

					}

				});
			}
		bt_checkbox_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//多选确认按钮
				checkDialog.dismiss();
				Message msg = new Message();
				msg.obj = checkKey;
				msg.what = CHECK_BOX_CHANG_UI;
				mHandler.sendMessage(msg);
			}
		});
		DialogUtils.dialogSet(checkDialog, mContext, Gravity.CENTER, 0.95, 1, true, false, true);
		checkDialog.show();
	}


    //单选
	protected void showRadioDialog(ItemInfo itemInfo, final String radoiKey, final String title) {
		radioDialog = new Dialog(mContext, R.style.dialog);
		radioDialog.setCancelable(true);
		radioDialog.setCanceledOnTouchOutside(true);
		radioDialog.setContentView(R.layout.dialog_radio);
        LinearLayout ll_add_radio_info = (LinearLayout) radioDialog.findViewById(R.id.ll_add_radio_info);
		final RadioGroup radioGroup = new RadioGroup(mContext);
		radioGroup.setTag("I_" + itemInfo.getSort());
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		for (int j = 0; j < itemInfo.getSubItems().size(); j++) {
			RadioButton radioButton = new RadioButton(mContext);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(20, 20, 20, 20);
			radioButton.setLayoutParams(lp);
			radioButton.setPadding(20, 20, 20, 20);
			radioButton.setText(itemInfo.getSubItems().get(j));
			radioButton.setTextSize(16);
			radioButton.setCompoundDrawablePadding(10);
			radioButton.setTextColor(getResources().getColor(R.color.layaout_color));
			radioButton.setButtonDrawable(android.R.color.transparent);
			radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.selector_details_dialog2_radiobutton_btn), null);
			radioButton.setTag(itemInfo.getSubItems().get(j));
			radioButton.setId(j);
			radioButton.setTextSize(16);
			if (itemInfo.getSubItems().get(j).equals(radioMap.get(radoiKey))) {
				radioButton.setChecked(true);
			}
			radioGroup.addView(radioButton);
			if (j < itemInfo.getSubItems().size() - 1) {
				View view = new View(mContext);
				view.setBackgroundResource(R.drawable.details_icon_line3);
				setViewlayoutParam(view, 0, 10, 0, 10);
				radioGroup.addView(view);
			}
		}

		setViewlayoutParam(radioGroup, 0, 10, 0, 10);
		ll_add_radio_info.addView(radioGroup);
		RadioGroup radio = (RadioGroup) ll_add_radio_info.findViewWithTag("I_" + itemInfo.getSort());
		final String key = "I_" + itemInfo.getSort();
		if (radio.getTag().equals("I_" + itemInfo.getSort()))
			radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					Log.i("info", "--->>" + arg1);
					for (int j = 0; j < arg0.getChildCount(); j++, j++) {
						RadioButton btn = (RadioButton) arg0.getChildAt(j);
						if (btn.getId() == arg1) {
							String text = btn.getText().toString();
							Message msg = new Message();
							String[] stringMsg = new String[2];
							stringMsg[0] = radoiKey;
							stringMsg[1] = text;
							msg.obj = stringMsg;
							msg.what = RADIO_CHANGE_UI;
							mHandler.sendMessage(msg);
							radioMap.put(radoiKey, text);
						}
					}

					radioDialog.dismiss();

				}
			});

		DialogUtils.dialogSet(radioDialog, mContext, Gravity.CENTER, 0.95, 1, true, false, true);
		radioDialog.show();

	}

	// 添加时间dialog
	public void showDialogTime(String title, final TextView textView) {
		dialogTime = new Dialog(mContext, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View timepickerview = inflater.inflate(R.layout.dialog_time, null);
		dialogTime = new Dialog(mContext, R.style.dialog);
		dialogTime.setCancelable(true);
		dialogTime.setCanceledOnTouchOutside(true);
		dialogTime.setContentView(timepickerview);
		ScreenInfo screenInfo = new ScreenInfo(SubmitFormActivity.this);
		wheelMain = new WheelMain(timepickerview, false);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = textView.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
			try {
				calendar.setTime(TimeUtils.formatter.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		wheelMain.initDateTimePicker(year, month, day, hour, min);

		Button sureBtn = (Button) dialogTime.findViewById(R.id.sure);
		Button cancelBtn = (Button) dialogTime.findViewById(R.id.cancel);
		TextView titleTime = (TextView) dialogTime.findViewById(R.id.titleTime);
		titleTime.setText(title);
		sureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				textView.setText(wheelMain.getTime().substring(0, 10));
				dialogTime.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialogTime.dismiss();
			}
		});
		DialogUtils.dialogSet(dialogTime, mContext, Gravity.CENTER, 0.95, 1, true, false, true);
		dialogTime.show();
	}


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tvSecondNext://下一步按钮
                getInformation();
                if(checkRequireHasFill()){
                    submitSignUp(list.toString());
                }else {
                    Trace.e(TAG,"被本地拒绝了。。。。"+titleRequireNotFill);
                    AccuApplication.getInstance().showMsg(titleRequireNotFill+"不能为空");
                }

                break;

            case R.id.tvPhotograph:// 图片
                photoDialog.dismiss();
               /* OUTPUTFILEPATH = fileCache.getImageCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(OUTPUTFILEPATH)));
                Trace.e(TAG, OUTPUTFILEPATH);
                startActivityForResult(intent, PickPhoto.TAKE_PHOTO);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, getPicture);
                break;
            case R.id.tvAlbum:// 文件
                photoDialog.dismiss();
                Intent intent2 = new Intent();
                intent2.setType("file/*");
                intent2.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent2, getFile);
                break;
            case R.id.file_form_add://上传文件
                shwoPictureSelctDialog();
                break;
            case R.id.dialog_select_picture_dismiss://取消上传文件对话框
                photoDialog.dismiss();
                break;
            case R.id.file_form_remove_id://删除上传的文件
                fileSerialNumber=null;
                file_form_add.setVisibility(View.VISIBLE);
                ll_file_form_display.setVisibility(View.GONE);
                break;
        }
    }

    //===================================收集数据并报名====================================================

    //// 获取输入框数据
    public void getTextInfomation(int j) {
        //四个editText 都可以通过tag："I_" + intSort 获取到
        EditText editText = (EditText) lyAddForm.findViewWithTag("I_" + fromInfo.items.get(j).getSort());
        if (fromInfo.items.get(j).isRequired() && "".equals(editText.getText().toString())) {
            return;
        }
        if (editText.getTag() != null && editText.getTag().toString().equals("I_" + fromInfo.items.get(j).getSort())) {
            if ("".equals(editText.getText().toString())) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getKey().equals("I_" + fromInfo.items.get(j).getSort())) {
                        list.remove(i);
                    }
                }
            } else {
                KeyValueInfo keyValueInfo = new KeyValueInfo();
                keyValueInfo.setKey("I_" + fromInfo.items.get(j).getSort());
                keyValueInfo.setValue(editText.getText().toString());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getKey().equals("I_" + fromInfo.items.get(j).getSort())) {
                        list.remove(i);
                    }
                }
                list.add(keyValueInfo);
            }
        }
    }

    //收集表单数据
    private void getInformation(){
        for (int j = 0; j < fromInfo.items.size(); j++) {
            if ("input".equals(fromInfo.items.get(j).getType()) ||
                    "number".equals(fromInfo.items.get(j).getType()) ||
                    "textarea".equals(fromInfo.items.get(j).getType()) ||
                    "email".equals(fromInfo.items.get(j).getType())) {// 输入框
                getTextInfomation(j);
            } else if ("date".equals(fromInfo.items.get(j).getType())) {// 时间
                TextView textView = (TextView) lyAddForm.findViewWithTag("I_" + fromInfo.items.get(j).getSort());
                if (fromInfo.items.get(j).isRequired() && ("请选择" + fromInfo.items.get(j).getTitle()).equals(textView.getText().toString())) {
                    return;
                }
                if (("请选择" + fromInfo.items.get(j).getTitle()).equals(textView.getText().toString())) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getKey().equals("I_" + fromInfo.items.get(j).getSort())) {
                            list.remove(i);//remove()是防止重复增加
                        }
                    }
                } else {
                    Log.i("info", textView.getText().toString());
                    KeyValueInfo keyValueInfo = new KeyValueInfo();
                    keyValueInfo.setKey("I_" + fromInfo.items.get(j).getSort());
                    keyValueInfo.setValue(textView.getText().toString());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getKey().equals("I_" + fromInfo.items.get(j).getSort())) {
                            list.remove(i);
                        }
                    }
                    list.add(keyValueInfo);
                }
            } else if ("radio".equals(fromInfo.items.get(j).getType())) {// 单选
                Iterator it = radioMap.keySet().iterator();
                while (it.hasNext()) {
                    String key;
                    String value;
                    key = it.next().toString();
                    value = radioMap.get(key);
                    for (int i = 0; i < list.size(); i++) {
                        if (key.equals(list.get(i).getKey())) {
                            list.remove(list.get(i));
                        }
                    }
                    KeyValueInfo keyValueInfo = new KeyValueInfo();
                    keyValueInfo.setKey(key);
                    keyValueInfo.setValue(value);

                    list.add(keyValueInfo);
                }
            } else if ("select".equals(fromInfo.items.get(j).getType())) {// 下拉(这个不用处理，因为下拉，并且被点击的时候已经加到list中去了)
            } else if ("checkbox".equals(fromInfo.items.get(j).getType())) {// checkbox多选

                Iterator it = checkMap.keySet().iterator();
                while (it.hasNext()) {
                    String key;
                    List<String> value;
                    String checkboxString = new String();
                    key = it.next().toString();
                    value = checkMap.get(key);
                    for (int i = 0; i < list.size(); i++) {
                        if (key.equals(list.get(i).getKey())) {
                            list.remove(list.get(i));
                        }
                    }
                    KeyValueInfo keyValueInfo = new KeyValueInfo();
                    keyValueInfo.setKey(key);
                    for (int i = 0; value != null && i < value.size(); i++) {
                        if (i == value.size() - 1) {
                            checkboxString += (value.get(i));
                        } else {
                            checkboxString += (value.get(i)) + ",";
                        }
                    }
                    keyValueInfo.setValue(checkboxString);
                    list.add(keyValueInfo);
                    Log.i("Form", "checkbox" + (key + "--" + value));

                }
            }else if("file".equals(fromInfo.items.get(j).getType())){

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getKey().equals("I_" + fromInfo.items.get(j).getSort())) {
                        list.remove(i);
                    }
                }

                if(fileSerialNumber!=null){
                    KeyValueInfo keyValueInfo = new KeyValueInfo();
                    keyValueInfo.setKey("I_" + fromInfo.items.get(j).getSort());
                    keyValueInfo.setValue(fileSerialNumber);
                    list.add(keyValueInfo);
                }


            }
        }
        Trace.e(TAG,"所填写的报名表单："+list.toString());
    }


    private boolean checkAchieveSingleItem(ItemInfo itemInfo){
        boolean hasAchieve=false;

        if(itemInfo.isRequired()){//必填的话一点要提交表单list中有才能达到
            for(int i=0;i<list.size();i++){
                KeyValueInfo keyValueInfo=list.get(i);
                if(keyValueInfo.getKey().equals("I_"+itemInfo.getSort())){
                 hasAchieve=true;
                }
            }
        }else {//不是必填则此Item一定能达到要求
            hasAchieve=true;
        }

        Trace.e(TAG,itemInfo.getTitle()+","+itemInfo.getSort()+":"+hasAchieve);
        return hasAchieve;
    }


    private String titleRequireNotFill;//记录某个必填表单title没有填写，用作toast提示用

    //检查必填项是否都填写了 ture为都填写了
    private boolean checkRequireHasFill(){
        boolean requireHasFill=true;
        for (int j = 0; j < fromInfo.items.size(); j++) {
            ItemInfo itemInfo = fromInfo.items.get(j);
            if(!checkAchieveSingleItem(itemInfo)){

                titleRequireNotFill=itemInfo.getTitle();
                requireHasFill=false;
                break;
            }
        }
        return requireHasFill;
    }

    // 提交报名
    public void submitSignUp(String list) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Form", list));//报名表单

        if (info.ticks.size() != 0) {
            params.add(new BasicNameValuePair("SN", info.ticks.get(position).getSN() + ""));//票种
        } else
            params.add(new BasicNameValuePair("SN", ""));
        params.add(new BasicNameValuePair("id", info.id));//活动id

        if(scode!=null){
            params.add(new BasicNameValuePair("scode", scode));//s码，抢票的时候用的
            Trace.e(TAG,"scode"+scode);
        }
        String coupon = getIntent().getStringExtra("coupon");
        if (!TextUtils.isEmpty(coupon)) {
            params.add(new BasicNameValuePair("coupon", coupon));//优惠码
        }
        showProgress("正在报名");

        httpCilents.postB(Url.ACCUPASS_DETAILS_REG, params, new WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (msg.isSuccess()) {
                            RegSuccessInfo successInfo = JSON.parseObject(msg.getResult(), RegSuccessInfo.class);
                            if (successInfo.getPrice() > 0) {
                                // 收费票
                                try {
                                    if (!successInfo.isNeedApply()) {
                                        application.showMsg("报名活动成功");
                                        if (successInfo.getIsAppPay() == 1) {
                                            Intent intent = new Intent(mContext, SureOrderActivity.class);
                                            intent.putExtra("info", successInfo);
                                            intent.putExtra("DetailsInfo", info);
                                            intent.putExtra("tag", 1);
                                            startActivity(intent);
                                        } else {
                                            startActivity(new Intent(mContext, PayWebActivity.class).putExtra("payUrl", successInfo.getPayUrl()));
                                        }
                                        EventBus.getDefault().post(new ChangePaySuccessEventBus(0));
                                    } else {
                                        application.showMsg(successInfo.getMsg());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {//免费票
                                if (!getIntent().getBooleanExtra("isRobTicket", false)) {//不是抢票活动进入自动加圈页
                                    if (!successInfo.isNeedApply()) {
                                        application.showMsg("报名活动成功");
                                        EventBus.getDefault().post(new ChangeDetailsDialogEventBus(successInfo));
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("DetailsInfo", info);
                                        bundle.putSerializable("info", successInfo);
                                        intent.putExtras(bundle);
                                        intent.setClass(SubmitFormActivity.this, PaySuccessActivity.class);
                                        startActivity(intent);
                                    } else {
                                        application.showMsg(successInfo.getMsg());
                                    }
                                }
                                // 免费票
                                try {
                                    String endTimes = TimeUtils.formatter.format(new java.util.Date());
                                    int results = Utils.timeCompareTo(info.getStartutc(), endTimes);
                                    if (results > 0){
                                        if (!dbManager.isAccuDetails(info.id)){
                                            if (application.sharedUtils.readInt("remind") != 4) {
                                                TimeUtils.addEvent(mContext, info.getStartutc(), info.title, Html.fromHtml(info.summary).toString(), info.city);
                                                dbManager.insertAccuDetails(info.id);
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            EventBus.getDefault().post(new EventEnroll());
                            EventBus.getDefault().post(new EventRobSuccess());
                            dbManager.insertSaveBeHavior(application.addBeHavior(40, 0+"", info.id, "", "", "",""));
                            finish();
                        } else {
                            application.showMsg(msg.getMsg());
                            if ("1".equals(msg.getResult())) {
                                finish();
                            }
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
    }

    //=========================上传文件有关===============================================================

    private ImageView file_form_add;
    private RelativeLayout ll_file_form_display;
    private TextView file_form_fileName;
    private ImageView file_form_remove;
    private Dialog photoDialog,upLoadingDialog;
    private AnimationDrawable dialogAnimationDrawable;
    private String fileSerialNumber;//文件序列码，提交文件后 后台返回的，用于提交报名的时候用
    private String OUTPUTFILEPATH;//拍照的路径
    private FileCache fileCache;
    private String uploadFileName;

    private View getFileFormView(final ItemInfo itemInfo) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.form_file_item, null);

        if(itemInfo.isRequired()){
            view.findViewById(R.id.file_form_require).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.file_form_require).setVisibility(View.GONE);
        }

        file_form_add = (ImageView) view.findViewById(R.id.file_form_add);
        file_form_add.setOnClickListener(this);
        ll_file_form_display= (RelativeLayout) view.findViewById(R.id.ll_file_form_display);
        file_form_fileName = (TextView) view.findViewById(R.id.file_form_fileName);
        file_form_remove = (ImageView) view.findViewById(R.id.file_form_remove_id);
        file_form_remove.setOnClickListener(this);
        return view;

    }





    // 修改头像
    private void shwoPictureSelctDialog() {
        photoDialog = new Dialog(mContext, R.style.dialog);
        photoDialog.setCancelable(true);
        photoDialog.setCanceledOnTouchOutside(true);
        photoDialog.setContentView(R.layout.dialog_select_pitcture);
        TextView tvPhotograph = (TextView) photoDialog.findViewById(R.id.tvPhotograph);
        TextView tvAlbum = (TextView) photoDialog.findViewById(R.id.tvAlbum);
        tvPhotograph.setOnClickListener(this);
        tvAlbum.setOnClickListener(this);
        photoDialog.findViewById(R.id.dialog_select_picture_dismiss).setOnClickListener(this);
        DialogUtils.dialogSet(photoDialog, mContext, Gravity.BOTTOM, 1, 1, true, false, true);
        photoDialog.show();
    }


    private void showUpLoadingDialog(){
        upLoadingDialog = new Dialog(mContext, R.style.dialog);
        upLoadingDialog.setCancelable(false);
        upLoadingDialog.setCanceledOnTouchOutside(false);
        upLoadingDialog.setContentView(R.layout.dialog_upload_file_loading);

        ImageView progressbar = (ImageView) upLoadingDialog.findViewById(R.id.diaolg_uploading_header_progressbar);
        progressbar.setBackgroundResource(R.drawable.loading);
        dialogAnimationDrawable = (AnimationDrawable) progressbar.getBackground();

        progressbar.post(new Runnable() {
            @Override
            public void run() {
                dialogAnimationDrawable.start();
            }
        });
        upLoadingDialog.show();
    }

    private void dismissUpLoadingDialog(){
        if (dialogAnimationDrawable.isRunning()) {
            dialogAnimationDrawable.stop();
        }
        upLoadingDialog.dismiss();

    }


    private void deleteCacheFile(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }


    private void uploadPicture(final String filePath,String fileName){
        showUpLoadingDialog();
        try {
            MultipartEntity mutiEntity = new MultipartEntity();
            File file = new File(filePath);
            mutiEntity.addPart("f", new StringBody("form_attach", Charset.forName("utf-8")));
            mutiEntity.addPart("n", new StringBody(fileName, Charset.forName("utf-8")));
            mutiEntity.addPart("file", new FileBody(file));
            httpCilents.setMutiEntity(mutiEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpCilents.postFile(Url.UPLOAD_FILES, new WebServiceCallBack() {
            @Override
            public void callBack(int code, Object result) {
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        Trace.e(TAG, result.toString());
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            fileSerialNumber = response.getResult();
                            setUploadSuccess(filePath);
                        } else {
                            application.showMsg(response.getMsg());
                        }
                        dismissUpLoadingDialog();
                        //删除上传的压缩文件
//                        deleteCacheFile(filePath);

                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("上传失败，请检查网络");
                        dismissUpLoadingDialog();
                        //删除上传的压缩文件
//                        deleteCacheFile(filePath);
                        break;
                }
            }
        });
    }



    private void setUploadSuccess(String path){
        file_form_add.setVisibility(View.GONE);
        ll_file_form_display.setVisibility(View.VISIBLE);
        String paths[]=path.split("/");
        if(paths.length>0){
            file_form_fileName.setText(paths[paths.length - 1]);
        }
    }



    HashSet<String> stringHashSet;
    private boolean checkSuffix(String path){

        String  lastSuffix="";

        String paths[]=path.split("\\.");
        if(paths.length>0){
            lastSuffix= "."+paths[paths.length - 1];
            System.out.println("lastSuffix:"+lastSuffix);
        }

        if(stringHashSet==null){
            stringHashSet=new HashSet<>();
            for(String str:suffix){
                stringHashSet.add(str);
            }
        }

        return stringHashSet.contains(lastSuffix);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            String path = null;
            switch (requestCode) {
                case getPicture://从图片中选择

                   /* try{
                        Uri uri = data.getData();
                        path=uri.getPath();
                        Log.e(TAG, "getPicture ------->" + path);

                        if (path != null) {
                            chunkAndUpload(path, uploadFileName);
                        }
                    }catch (Exception e){
                        application.showMsg("选择图片失败，请重新选择");
                    }*/


                    try {
                        Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
                        cursor.moveToFirst();
                        path=cursor.getString(1);


//                        path = ImageBig.scalePicture(mContext, cursor.getString(1), 800, 800);
//                        Trace.e(TAG,"相册中原图片路径："+cursor.getString(1));
//                        Trace.e(TAG,"相册中压缩的图片路径："+path);
//                        cursor.close();
                        Log.e(TAG, "getPicture ------->" + path);
                        if (path != null) {
                            if(checkSuffix(path)){
                                chunkAndUpload(path, uploadFileName);
                            }else {
                                application.showMsg("文件后缀名不对，请重新选择");
                            }

                        }
                    } catch (Exception e) {
                        application.showMsg("选择相册失败，请重新选择");
                    }


                   /* path = ImageBig.scalePicture(mContext, OUTPUTFILEPATH, 600, 600);
                    Trace.e(TAG,"拍照后原图片路径："+OUTPUTFILEPATH);
                    Trace.e(TAG,"拍照后压缩的图片路径："+path);
                    if (path != null) {
                        uploadPicture(path, uploadFileName);
                    }*/
                    break;
                case getFile://从文件中选择
                    try {
                        Uri uri2 = data.getData();
                        path= uri2.getPath();
                        Log.e(TAG, "getFile ------->" + path);

                        if (path != null) {
                            if(checkSuffix(path)){
                                chunkAndUpload(path, uploadFileName);
                            }else {
                                application.showMsg("文件后缀名不对，请重新选择");
                            }
                        }
                       /* path = ImageBig.scalePicture(mContext, cursor.getString(1), 800, 800);
                        Trace.e(TAG,"相册中原图片路径："+cursor.getString(1));
                        Trace.e(TAG,"相册中压缩的图片路径："+path);
                        cursor.close();
                        if (path != null) {
                            uploadPicture(path, uploadFileName);
                        }*/
                    } catch (Exception e) {
                        application.showMsg("选择文件失败，请重新选择");
                    }
                    break;
                default:
                    break;
            }
        }
    }




    //================================================================================
    public void chunkAndUpload(String path,String fileName){
        showUpLoadingDialog();

        File file=new File(path);
        FileInfo fileInfo=new FileInfo();
        fileInfo.setFilePath(path);
        fileInfo.setFileLength(file.length());
        fileInfo.setFileName(file.getName());
        long fileLength=fileInfo.getFileLength();
        int chunks=(int)(fileLength/ ChunkInfo.chunkLength+(fileLength%ChunkInfo.chunkLength>0?1:0));

        String  lastSuffix=null;

        String paths[]=path.split("\\.");
        if(paths.length>0){
            lastSuffix= paths[paths.length - 1];
            Trace.e(TAG,"lastSuffix:"+lastSuffix);
        }


        ArrayList<ChunkInfo> chunkInfos=new ArrayList<>();

        for (int i=0;i<chunks;i++) {//当前分片值从1开始
            ChunkInfo chunkInfo = new ChunkInfo();
//            chunkInfo.setChunkName(fileInfo.getFileName()+".part_"+(i+1)+"."+chunks);//filename.jpg.part_2.10
            chunkInfo.setChunkName(fileName+"."+lastSuffix+".part_"+(i+1)+"."+chunks);//filename.jpg.part_2.10

            chunkInfo.setChunk(i);
            chunkInfo.setChunks(chunks);
            chunkInfo.setFileLength(fileLength);
            chunkInfo.setFilePath(fileInfo.getFilePath());
            chunkInfos.add(chunkInfo);
            Trace.e("要提交的 chunks", chunkInfo.toString());
        }

        uploadChunks(path,Url.UPLOAD_CHUNK, chunkInfos,fileName);

    }

    //===================================================================================
    public String  HawkPost(String url,AccuApplication application) throws MalformedURLException {
        String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
        String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
        Log.i("info", accu_id);
        Log.i("info", accu_key);
        AuthorizationHeader authorizationHeader = null;
        URL realUrl = new URL(url);
        try {
            HawkContext.HawkContextBuilder_C b = HawkContext.offset(application.hawkOffset).request("POST", realUrl.getPath(), Url.ACCUPASS_SERVICE_HOST, url.indexOf("https") != -1 ? Url.ACCUPASS_SERVICE_PORT2 : Url.ACCUPASS_SERVICE_PORT).credentials(accu_id, accu_key, Algorithm.SHA_256);
            authorizationHeader = b.build().createAuthorizationHeader();
            return authorizationHeader.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    String[] suffix={ ".jpg", ".jpeg", ".png", ".gif", ".bmp",
            ".txt", ".doc", ".docx", "xls", ".xlsx", ".ppt",
            ".pptx", ".pdf", ".rar", ".zip", ".7z" };


    boolean hasUpload=false;
    void uploadChunks(final String path,final String url,final ArrayList<ChunkInfo> chunkInfos,final String fileName){
        final AccuApplication accuApplication=AccuApplication.getInstance();
        hasUpload=false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                int time=0;
                final int MAXTIME=3*chunkInfos.size();

                while ((chunkInfos.size()!=0) &&(time<MAXTIME)){
                    hasUpload=true;
                    time++;
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(accuApplication));
                    ChunkInfo chunkInfo=null;
                    try {
//                        ImageItem imageItem=chunkInfos.get(0);
                        chunkInfo=chunkInfos.get(0);

                        HttpPost request = new HttpPost(url);
                        request.addHeader("Authorization", HawkPost(url,accuApplication).toString());
                        request.addHeader("api-version", "v3.5.0");


                        MultipartEntity mutiEntity = new MultipartEntity();
                        mutiEntity.addPart("f", new StringBody("form_attach", Charset.forName("utf-8")));
                        mutiEntity.addPart("n", new StringBody(fileName, Charset.forName("utf-8")));
                        mutiEntity.addPart("file", new CustomFileBody(chunkInfo));
                        mutiEntity.addPart("fsize", new StringBody(chunkInfo.getFileLength()+"", Charset.forName("utf-8")));

                        request.setEntity(mutiEntity);

                        // getConsumer().sign(request);
                        HttpResponse response = httpClient.execute(request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        String content = HttpCilents.convertStreamToString(response.getEntity().getContent());
                        Trace.e("TAG","statusCode:"+statusCode+"  content:"+content);
                        if(statusCode==200){

                            try {
                                BaseResponse info = JSON.parseObject(content, BaseResponse.class);
                                if (info.isSuccess()) {
                                    chunkInfos.remove(0);//图片提交成功，
                                    Trace.e("提交成功的 chunks", chunkInfo.toString());
                                    if(!TextUtils.isEmpty(info.getResult())){
                                        fileSerialNumber=info.getResult();
                                        Trace.e(TAG,"fileSerialNumber:"+fileSerialNumber);
                                    }
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else {
                            Trace.e("提交失败的 chunks", chunkInfo.toString());
                        }

                    } catch (Exception e) {
                        Trace.e("提交失败的 e  chunks", chunkInfo.toString()+"    "+e.getMessage());
                        e.printStackTrace();
                    } finally {
                        httpClient.close();
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissUpLoadingDialog();
                        if(hasUpload &&  chunkInfos.size()==0){
                            setUploadSuccess(path);

                        }else {
                            application.showMsg("上传失败，请重试");
                        }

                    }
                });

                Trace.e("TAG","chunks上传线程退出");
            }
        }).start();
    }
}
