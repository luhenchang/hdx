package com.accuvally.hdtui.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;

import de.greenrobot.event.EventBus;

public class RemindActivity extends BaseActivity implements OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

	private RadioGroup mRadioGroup;

	private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;

	private CheckBox cbExpire;
	private CheckBox cbFilled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		initView();
	}

	public void initView() {
		// setTitle(getResources().getString(R.string.setting_accu_remind));
		setTitle("活动设定");

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioButton1 = (RadioButton) findViewById(R.id.radioButton11);
		radioButton2 = (RadioButton) findViewById(R.id.radioButton22);
		radioButton3 = (RadioButton) findViewById(R.id.radioButton33);
		radioButton4 = (RadioButton) findViewById(R.id.radioButton44);

		if (application.sharedUtils.readInt("remind") == 1)
			radioButton1.setChecked(true);
		else if (application.sharedUtils.readInt("remind") == 2)
			radioButton2.setChecked(true);
		else if (application.sharedUtils.readInt("remind") == 3)
			radioButton3.setChecked(true);
		else if (application.sharedUtils.readInt("remind") == 4)
			radioButton4.setChecked(true);
		mRadioGroup.setOnCheckedChangeListener(this);

		cbExpire = (CheckBox) findViewById(R.id.cbExpire);
		cbFilled = (CheckBox) findViewById(R.id.cbFilled);
		cbExpire.setOnCheckedChangeListener(this);
		cbFilled.setOnCheckedChangeListener(this);

		Boolean onlyunexpired = application.sharedUtils.readBoolean("onlyunexpired", true);
		cbExpire.setChecked(onlyunexpired);// 未过期默认true
		Boolean full = application.sharedUtils.readBoolean("full", false);
		cbFilled.setChecked(full);// 未额满默认false
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.radioButton11:
			application.sharedUtils.writeInt("remind", 1);
			break;
		case R.id.radioButton22:
			application.sharedUtils.writeInt("remind", 2);
			break;
		case R.id.radioButton33:
			application.sharedUtils.writeInt("remind", 3);
			break;
		case R.id.radioButton44:
			application.sharedUtils.writeInt("remind", 4);
			break;
		}
		// finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.cbExpire:
			application.sharedUtils.writeBoolean("onlyunexpired", arg1);
			EventBus.getDefault().post(new ChangeCityEventBus(application.sharedUtils.readString("cityName")));
			break;
		case R.id.cbFilled:
			application.sharedUtils.writeBoolean("full", arg1);
			EventBus.getDefault().post(new ChangeCityEventBus(application.sharedUtils.readString("cityName")));
			break;
		default:
			break;
		}
	}

}
