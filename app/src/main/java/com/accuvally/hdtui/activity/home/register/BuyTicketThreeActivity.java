package com.accuvally.hdtui.activity.home.register;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.buy.PaySuccessActivity;
import com.accuvally.hdtui.activity.home.buy.PayWebActivity;
import com.accuvally.hdtui.activity.home.buy.SureOrderActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.FromInfo;
import com.accuvally.hdtui.model.ItemInfo;
import com.accuvally.hdtui.model.KeyValueInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.ui.CustomerScrollView;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.EventEnroll;
import com.accuvally.hdtui.utils.eventbus.EventRobSuccess;
import com.accuvally.hdtui.utils.timepicker.JudgeDate;
import com.accuvally.hdtui.utils.timepicker.ScreenInfo;
import com.accuvally.hdtui.utils.timepicker.WheelMain;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
public class BuyTicketThreeActivity extends BaseActivity implements OnClickListener {

	private LinearLayout lyAddForm;
	private AccuDetailBean info;
	private static final int RADIO_CHANGE_UI = 0;
	private static final int CHECK_BOX_CHANG_UI = 1;
	FromInfo fromInfo;
	List<KeyValueInfo> list = new ArrayList<KeyValueInfo>();
	Map<String, String> radioMap = new HashMap<String, String>();
	Map<String, List<String>> checkMap = new HashMap<String, List<String>>();
	WheelMain wheelMain;
	private Dialog dialogTime;
	private TextView tvSecondNext;
	private int position;
	private Dialog radioDialog;
	private Dialog checkDialog;
	private LinearLayout ll_add_checkbox_info;
	private TextView bt_checkbox_submit;
	private CustomerScrollView buyTicketScrollview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_ticket_second);
		info = (AccuDetailBean) getIntent().getSerializableExtra("info");
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvSecondNext:
			Registration();
			break;
		}
	}

	public void layoutParam(View view, int left, int top, int right, int bottom) {
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

			default:
				break;
			}
		};
	};

	private void singleText(final ItemInfo itemInfo, String strTitle, int intSort, String strType) {
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

	public void initData() {
		fromInfo = JSON.parseObject(info.form, FromInfo.class);
		for (int i = 0; i < fromInfo.items.size(); i++) {
			final ItemInfo itemInfo = fromInfo.items.get(i);
			String strTitle = itemInfo.getTitle();
			String strType = itemInfo.getType();
			int intSort = itemInfo.getSort();
			if ("input".equals(strType) || "number".equals(strType)) {
				singleText(itemInfo, strTitle, intSort, strType);
			} else if ("radio".equals(strType)) {
				LinearLayout layout = choiceInfo(itemInfo, strTitle, intSort, strType);
				final String key = "I_" + intSort;
				final String title = strTitle;

				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						radioDialog(itemInfo, key, title);
					}
				});
			} else if ("email".equals(strType)) {
				singleText(itemInfo, strTitle, intSort, strType);

			} else if ("textarea".equals(strType)) {
				TextView textView = new TextView(mContext);
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
				layoutParam(textView, 0, 10, 0, 10);
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
				layoutParam(editText, 0, 10, 0, 10);
				lyAddForm.addView(editText);

			} else if ("checkbox".equals(strType)) {
				final ArrayList<String> checkIntegersList = new ArrayList<String>();
				LinearLayout layout = choiceInfo(itemInfo, strTitle, intSort, strType);
				final String key = "I_" + intSort;
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						checkDialog(itemInfo, checkIntegersList, key);
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
				} else
					textView.setText(strTitle + ":");
				textView.setTextSize(16);
				layoutParam(textView, 0, 10, 0, 10);
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
				layoutParam(spinner, 0, 10, 0, 10);
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
				} else
					textView.setText(strTitle + ":");
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
				layoutParam(layout, 0, 10, 0, 10);
				lyAddForm.addView(layout);
				final String title = "请选择" + strTitle;
				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogTime(title, textViewData);
					}
				});
			}

			View view = new View(mContext);
			view.setBackgroundResource(R.drawable.details_icon_line3);
			layoutParam(view, 0, 20, 0, 20);
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
		layoutParam(layout, 0, 10, 0, 10);
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

	protected void checkDialog(ItemInfo itemInfo, final List<String> checkIntegersList, final String checkKey) {
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
			layoutParam(view, 0, 10, 0, 10);
			linearLayout.addView(view);
		}
		layoutParam(linearLayout, 0, 10, 0, 10);
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
			public void onClick(View v) {
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

	private LinearLayout ll_add_radio_info;

	protected void radioDialog(ItemInfo itemInfo, final String radoiKey, final String title) {
		radioDialog = new Dialog(mContext, R.style.dialog);
		radioDialog.setCancelable(true);
		radioDialog.setCanceledOnTouchOutside(true);
		radioDialog.setContentView(R.layout.dialog_radio);
		ll_add_radio_info = (LinearLayout) radioDialog.findViewById(R.id.ll_add_radio_info);
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
				layoutParam(view, 0, 10, 0, 10);
				radioGroup.addView(view);
			}
		}

		layoutParam(radioGroup, 0, 10, 0, 10);
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
	public void dialogTime(String title, final TextView textView) {
		dialogTime = new Dialog(mContext, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View timepickerview = inflater.inflate(R.layout.dialog_time, null);
		dialogTime = new Dialog(mContext, R.style.dialog);
		dialogTime.setCancelable(true);
		dialogTime.setCanceledOnTouchOutside(true);
		dialogTime.setContentView(timepickerview);
		ScreenInfo screenInfo = new ScreenInfo(BuyTicketThreeActivity.this);
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

	public void Registration() {
		for (int j = 0; j < fromInfo.items.size(); j++) {
			if ("input".equals(fromInfo.items.get(j).getType()) || "number".equals(fromInfo.items.get(j).getType()) || "textarea".equals(fromInfo.items.get(j).getType()) || "email".equals(fromInfo.items.get(j).getType())) {// 输入框
				initContraol(j);
			} else if ("date".equals(fromInfo.items.get(j).getType())) {// 时间
				TextView textView = (TextView) lyAddForm.findViewWithTag("I_" + fromInfo.items.get(j).getSort());
				if (fromInfo.items.get(j).isRequired() && ("请选择" + fromInfo.items.get(j).getTitle()).equals(textView.getText().toString())) {
					// application.showMsg("请选择" +
					// fromInfo.items.get(j).getTitle());
					return;
				}
				if (("请选择" + fromInfo.items.get(j).getTitle()).equals(textView.getText().toString())) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getKey().equals("I_" + fromInfo.items.get(j).getSort())) {
							list.remove(i);
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
			} else if ("radio".equals(fromInfo.items.get(j).getType())) {
				// 单选

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
					Log.i("Form", "radio" + (key + "--" + value));

				}
			} else if ("select".equals(fromInfo.items.get(j).getType())) {// 下拉
			} else if ("checkbox".equals(fromInfo.items.get(j).getType())) {// checkbox

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
			}
		}
		getDetailsReg(list.toString());
		Log.i("info", list.toString());
	}

	public void initContraol(int j) {
		EditText editText = (EditText) lyAddForm.findViewWithTag("I_" + fromInfo.items.get(j).getSort());
		Log.i("boolean", "fromInfo=" + (fromInfo == null) + ",fromInfo.items=" + (fromInfo.items == null) + "fromInfo.items.get(j)=" + (fromInfo.items.get(j) == null) + ",editText.getText()=" + (editText.getText() == null));
		if (fromInfo.items.get(j).isRequired() && "".equals(editText.getText().toString())) {
			return;
		}
		if ("email".equals(fromInfo.items.get(j).getType())) {
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

	// 报名
	public void getDetailsReg(String list) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Form", list));
		if (info.ticks.size() != 0) {
			position = getIntent().getIntExtra("position", 0);
			params.add(new BasicNameValuePair("SN", info.ticks.get(position).getSN() + ""));
		} else
			params.add(new BasicNameValuePair("SN", ""));
		Log.i("Form", list);
		params.add(new BasicNameValuePair("id", info.id));
		String coupon = getIntent().getStringExtra("coupon");
		if (!TextUtils.isEmpty(coupon)) {
			params.add(new BasicNameValuePair("coupon", coupon));
		}
		showProgress("正在报名");
		httpCilents.postA(Url.ACCUPASS_DETAILS_REG, params, new WebServiceCallBack() {

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
									intent.setClass(BuyTicketThreeActivity.this, PaySuccessActivity.class);
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
}
