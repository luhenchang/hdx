package com.accuvally.hdtui.ui;



import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.UserInfo;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * 表情输入框
 * 
 * @author wangxiaojie
 */
public class EmoteEditText extends EmoteTextView {
	
	private UserInfo mAtUser;

	public void setAtUser(UserInfo user) {
		this.mAtUser = user;
	}

	public EmoteEditText(Context context, AttributeSet attrs) {
		super(context, attrs, R.style.default_edittext_style);
		setClickable(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public EmoteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setClickable(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	// 添加表情
	public void append(CharSequence value, int paramInt1, int paramInt2) {
		super.append(format(value), paramInt1, paramInt2);
	}

	// 插入表情
	public void insert(CharSequence value) {
		getText().insert(getSelectionStart(), format(value));
	}

	@Override
	protected boolean getDefaultEditable() {
		return true;
	}

	@Override
	protected MovementMethod getDefaultMovementMethod() {
		return ArrowKeyMovementMethod.getInstance();
	}

	@Override
	public Editable getText() {
		return (Editable) super.getText();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, BufferType.EDITABLE);
	}

	public void setSelection(int start, int stop) {
		Selection.setSelection(getText(), start, stop);
	}

	public void setSelection(int index) {
		Selection.setSelection(getText(), index);
	}

	@Override
	public void setEllipsize(TextUtils.TruncateAt ellipsis) {
		if (ellipsis == TextUtils.TruncateAt.MARQUEE) {
			throw new IllegalArgumentException("EditText cannot use the ellipsize mode " + "TextUtils.TruncateAt.MARQUEE");
		}
		super.setEllipsize(ellipsis);
	}
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_DEL){
			matchZn();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 删除@张三,最后一个,还有BUG,如果手动移动光标，j就会有问题
	 */
	private void matchZn() {
 
		if(mAtUser!=null && !TextUtils.isEmpty(mAtUser.getNick())){
			String match = mAtUser.getNick();
			final String text = getText().toString();
			char at = 'a';
			int index =-1;
			if(text.lastIndexOf('@')>=0){
				at='@';
				index = text.lastIndexOf('@');
			}else if(text.lastIndexOf("＠")>=0){
				at='＠';
				index = text.lastIndexOf("＠");
			}
			//Log.d("a","getSelectionStart() ="+getSelectionStart() + " index ="+index);
			if(index>=0){
				String name =text.substring(index, text.length());
				if((at+match).equals(name)){
					if(index>0){
						// 设置光标的位置
						CharSequence textChar = getText();
						if (textChar instanceof Spannable) {
							//删除@张三
							((Editable)getText()).delete(index, text.length());
						}
					}else{
						setText("");
					}
				}
			}
		}
	}

}
