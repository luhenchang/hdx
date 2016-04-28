package com.accuvally.hdtui.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.accuvally.hdtui.R;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 可以显示表情的TextView
 * 
 * @author wangxiaojie
 */
public class EmoteTextView extends TextView {

	// 匹配表情的正则表达式
	// private static final String smileReg = "\\[/[0-9]+\\]";

	private static final String smileReg = "\\[\\\\[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]";

	private Resources mResources;

	private Map<String, Integer> smiles;

	public EmoteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mResources = context.getResources();
		initSmiles();
	}

	public EmoteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mResources = context.getResources();
		initSmiles();
	}

	private void initSmiles() {
		smiles = new HashMap<String, Integer>();
		smiles.put("微笑", R.drawable.smiley_0);
		smiles.put("撇嘴", R.drawable.smiley_1);
		smiles.put("色", R.drawable.smiley_2);
		smiles.put("吃惊", R.drawable.smiley_3);
		smiles.put("酷", R.drawable.smiley_4);
		smiles.put("大哭", R.drawable.smiley_5);
		smiles.put("害羞", R.drawable.smiley_6);
		smiles.put("闭嘴", R.drawable.smiley_7);
		smiles.put("睡觉", R.drawable.smiley_8);
		smiles.put("委屈", R.drawable.smiley_9);
		smiles.put("汗死", R.drawable.smiley_10);
		smiles.put("怒", R.drawable.smiley_11);
		smiles.put("调皮", R.drawable.smiley_12);
		smiles.put("嗤笑", R.drawable.smiley_13);
		smiles.put("惊讶", R.drawable.smiley_14);
		smiles.put("难过", R.drawable.smiley_15);
		smiles.put("装酷", R.drawable.smiley_16);
		smiles.put("尴尬", R.drawable.smiley_17);
		smiles.put("抓狂", R.drawable.smiley_18);
		smiles.put("吐", R.drawable.smiley_19);
		smiles.put("偷笑", R.drawable.smiley_20);
		smiles.put("可爱", R.drawable.smiley_21);
		smiles.put("白眼", R.drawable.smiley_22);
		smiles.put("傲慢", R.drawable.smiley_23);
		smiles.put("饿", R.drawable.smiley_24);
		smiles.put("困", R.drawable.smiley_25);
		smiles.put("惊恐", R.drawable.smiley_26);
		smiles.put("冷汗", R.drawable.smiley_27);
		smiles.put("憨笑", R.drawable.smiley_28);
		smiles.put("美国大兵", R.drawable.smiley_29);
		smiles.put("奋斗", R.drawable.smiley_30);
		smiles.put("骂人", R.drawable.smiley_31);
		smiles.put("问号", R.drawable.smiley_32);
		smiles.put("嘘", R.drawable.smiley_33);
		smiles.put("晕", R.drawable.smiley_34);
		smiles.put("郁闷", R.drawable.smiley_35);
		smiles.put("衰", R.drawable.smiley_36);
		smiles.put("骷髅", R.drawable.smiley_37);
		smiles.put("敲打", R.drawable.smiley_38);
		smiles.put("拜拜", R.drawable.smiley_39);
		smiles.put("擦汗", R.drawable.smiley_40);
		smiles.put("抠鼻", R.drawable.smiley_41);
		smiles.put("鼓掌", R.drawable.smiley_42);
		smiles.put("囧", R.drawable.smiley_43);
		smiles.put("坏笑", R.drawable.smiley_44);
		smiles.put("左哼哼", R.drawable.smiley_45);
		smiles.put("右哼哼", R.drawable.smiley_46);
		smiles.put("哈欠", R.drawable.smiley_47);
		smiles.put("鄙视", R.drawable.smiley_48);
		smiles.put("虫虫飞", R.drawable.smiley_49);
		smiles.put("要哭了", R.drawable.smiley_50);
		smiles.put("奸笑", R.drawable.smiley_51);
		smiles.put("亲亲", R.drawable.smiley_52);
		smiles.put("吓", R.drawable.smiley_53);
		smiles.put("可怜", R.drawable.smiley_54);
		smiles.put("菜刀", R.drawable.smiley_55);
		smiles.put("西瓜", R.drawable.smiley_56);
		smiles.put("汽水", R.drawable.smiley_57);
		smiles.put("篮球", R.drawable.smiley_58);
		smiles.put("兵乓", R.drawable.smiley_59);
		smiles.put("咖啡", R.drawable.smiley_60);
		smiles.put("饭", R.drawable.smiley_61);
		smiles.put("猪头", R.drawable.smiley_62);
		smiles.put("鲜花", R.drawable.smiley_63);
		smiles.put("凋谢", R.drawable.smiley_64);
		smiles.put("红唇", R.drawable.smiley_65);
		smiles.put("心", R.drawable.smiley_66);
		smiles.put("心碎", R.drawable.smiley_67);
		smiles.put("蛋糕", R.drawable.smiley_68);
		smiles.put("闪电", R.drawable.smiley_69);
		smiles.put("炸弹", R.drawable.smiley_70);
		smiles.put("小刀", R.drawable.smiley_71);
		smiles.put("足球", R.drawable.smiley_72);
		smiles.put("瓢虫", R.drawable.smiley_73);
		smiles.put("大便", R.drawable.smiley_74);
		smiles.put("月亮", R.drawable.smiley_75);
		smiles.put("太阳", R.drawable.smiley_76);
		smiles.put("礼物", R.drawable.smiley_77);
		smiles.put("拥抱", R.drawable.smiley_78);
		smiles.put("赞", R.drawable.smiley_79);
		smiles.put("踩", R.drawable.smiley_80);
		smiles.put("握手", R.drawable.smiley_81);
		smiles.put("耶", R.drawable.smiley_82);
		smiles.put("佩服", R.drawable.smiley_83);
		smiles.put("勾引", R.drawable.smiley_84);
		smiles.put("拳头", R.drawable.smiley_85);
		smiles.put("小手指", R.drawable.smiley_86);
		smiles.put("三只手指", R.drawable.smiley_87);
		smiles.put("NO", R.drawable.smiley_88);
		smiles.put("YES", R.drawable.smiley_89);
		smiles.put("甜蜜", R.drawable.smiley_90);
		smiles.put("飞吻", R.drawable.smiley_91);
		smiles.put("蹦蹦", R.drawable.smiley_92);
		smiles.put("发抖", R.drawable.smiley_93);
		smiles.put("上火", R.drawable.smiley_94);
		smiles.put("摆动", R.drawable.smiley_95);
	}

	// 格式化字符串
	protected CharSequence format(CharSequence value) {
		SpannableStringBuilder localObject = null;
		if (value == null) {
			return "";
		}
		localObject = new SpannableStringBuilder(value);
		Matcher matcher = Pattern.compile(smileReg).matcher(value);
		while (matcher.find()) {
			final String group = matcher.group();
			String t = group.substring(group.indexOf("\\") + 1, group.indexOf("]"));
			if (!(t.equals("图片") || t.equals("语音"))) {
				if (smiles.containsKey(t)) {
					try {
						final int i = smiles.get(t);
						((SpannableStringBuilder) localObject).setSpan(new SmileImageSpan(getContext(), i), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return localObject;
	}

	// 插入表情
	public void append(CharSequence value, int paramInt1, int paramInt2) {
		if (getText() instanceof Editable) {
			((Editable) getText()).append(format(value), paramInt1, paramInt2);
		} else {
			super.append(value, paramInt1, paramInt2);
		}
	}

	// 插入表情
	public void insert(CharSequence value) {
		super.append(format(value));
	}

	// 设置内容
	public void setText(CharSequence value, BufferType paramBufferType) {
		super.setText(format(value), paramBufferType);
	}
}
