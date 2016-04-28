package com.accuvally.hdtui.ui;

import java.util.ArrayList;
import java.util.List;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.BaseListAdapter;
import com.accuvally.hdtui.adapter.SmileAdapter;
import com.accuvally.hdtui.model.SmileInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 表情适配器
 * 
 * @author wangxiaojie
 */
public class SmileGridAdapter extends BaseListAdapter<SmileInfo> {

	private int row = 4;

	private int column = 8;

	public SmileGridAdapter(Context context) {
		super(context);
		mList = new ArrayList<SmileInfo>();
		mList.add(new SmileInfo("[\\微笑]", R.drawable.smiley_0));
		mList.add(new SmileInfo("[\\撇嘴]", R.drawable.smiley_1));
		mList.add(new SmileInfo("[\\色]", R.drawable.smiley_2));
		mList.add(new SmileInfo("[\\吃惊]", R.drawable.smiley_3));
		mList.add(new SmileInfo("[\\酷]", R.drawable.smiley_4));
		mList.add(new SmileInfo("[\\大哭]", R.drawable.smiley_5));
		mList.add(new SmileInfo("[\\害羞]", R.drawable.smiley_6));
		mList.add(new SmileInfo("[\\闭嘴]", R.drawable.smiley_7));
		mList.add(new SmileInfo("[\\睡觉]", R.drawable.smiley_8));
		mList.add(new SmileInfo("[\\委屈]", R.drawable.smiley_9));
		mList.add(new SmileInfo("[\\汗死]", R.drawable.smiley_10));
		mList.add(new SmileInfo("[\\怒]", R.drawable.smiley_11));
		mList.add(new SmileInfo("[\\调皮]", R.drawable.smiley_12));
		mList.add(new SmileInfo("[\\嗤笑]", R.drawable.smiley_13));
		mList.add(new SmileInfo("[\\惊讶]", R.drawable.smiley_14));
		mList.add(new SmileInfo("[\\难过]", R.drawable.smiley_15));
		mList.add(new SmileInfo("[\\装酷]", R.drawable.smiley_16));
		mList.add(new SmileInfo("[\\尴尬]", R.drawable.smiley_17));
		mList.add(new SmileInfo("[\\抓狂]", R.drawable.smiley_18));
		mList.add(new SmileInfo("[\\吐]", R.drawable.smiley_19));
		mList.add(new SmileInfo("[\\偷笑]", R.drawable.smiley_20));
		mList.add(new SmileInfo("[\\可爱]", R.drawable.smiley_21));
		mList.add(new SmileInfo("[\\白眼]", R.drawable.smiley_22));
		mList.add(new SmileInfo("[\\傲慢]", R.drawable.smiley_23));
		mList.add(new SmileInfo("[\\饿]", R.drawable.smiley_24));
		mList.add(new SmileInfo("[\\困]", R.drawable.smiley_25));
		mList.add(new SmileInfo("[\\惊恐]", R.drawable.smiley_26));
		mList.add(new SmileInfo("[\\冷汗]", R.drawable.smiley_27));
		mList.add(new SmileInfo("[\\憨笑]", R.drawable.smiley_28));
		mList.add(new SmileInfo("[\\美国大兵]", R.drawable.smiley_29));
		mList.add(new SmileInfo("[\\奋斗]", R.drawable.smiley_30));
		mList.add(new SmileInfo("[\\骂人]", R.drawable.smiley_31));
		mList.add(new SmileInfo("[\\问号]", R.drawable.smiley_32));
		mList.add(new SmileInfo("[\\嘘]", R.drawable.smiley_33));
		mList.add(new SmileInfo("[\\晕]", R.drawable.smiley_34));
		mList.add(new SmileInfo("[\\郁闷]", R.drawable.smiley_35));
		mList.add(new SmileInfo("[\\衰]", R.drawable.smiley_36));
		mList.add(new SmileInfo("[\\骷髅]", R.drawable.smiley_37));
		mList.add(new SmileInfo("[\\敲打]", R.drawable.smiley_38));
		mList.add(new SmileInfo("[\\拜拜]", R.drawable.smiley_39));
		mList.add(new SmileInfo("[\\擦汗]", R.drawable.smiley_40));
		mList.add(new SmileInfo("[\\抠鼻]", R.drawable.smiley_41));
		mList.add(new SmileInfo("[\\鼓掌]", R.drawable.smiley_42));
		mList.add(new SmileInfo("[\\囧]", R.drawable.smiley_43));
		mList.add(new SmileInfo("[\\坏笑]", R.drawable.smiley_44));
		mList.add(new SmileInfo("[\\左哼哼]", R.drawable.smiley_45));
		mList.add(new SmileInfo("[\\右哼哼]", R.drawable.smiley_46));
		mList.add(new SmileInfo("[\\哈欠]", R.drawable.smiley_47));
		mList.add(new SmileInfo("[\\鄙视]", R.drawable.smiley_48));
		mList.add(new SmileInfo("[\\虫虫飞]", R.drawable.smiley_49));
		mList.add(new SmileInfo("[\\要哭了]", R.drawable.smiley_50));
		mList.add(new SmileInfo("[\\奸笑]", R.drawable.smiley_51));
		mList.add(new SmileInfo("[\\亲亲]", R.drawable.smiley_52));
		mList.add(new SmileInfo("[\\吓]", R.drawable.smiley_53));
		mList.add(new SmileInfo("[\\可怜]", R.drawable.smiley_54));
		mList.add(new SmileInfo("[\\菜刀]", R.drawable.smiley_55));
		mList.add(new SmileInfo("[\\西瓜]", R.drawable.smiley_56));
		mList.add(new SmileInfo("[\\汽水]", R.drawable.smiley_57));
		mList.add(new SmileInfo("[\\篮球]", R.drawable.smiley_58));
		mList.add(new SmileInfo("[\\兵乓]", R.drawable.smiley_59));
		mList.add(new SmileInfo("[\\咖啡]", R.drawable.smiley_60));
		mList.add(new SmileInfo("[\\饭]", R.drawable.smiley_61));
		mList.add(new SmileInfo("[\\猪头]", R.drawable.smiley_62));
		mList.add(new SmileInfo("[\\鲜花]", R.drawable.smiley_63));
		mList.add(new SmileInfo("[\\凋谢]", R.drawable.smiley_64));
		mList.add(new SmileInfo("[\\红唇]", R.drawable.smiley_65));
		mList.add(new SmileInfo("[\\心]", R.drawable.smiley_66));
		mList.add(new SmileInfo("[\\心碎]", R.drawable.smiley_67));
		mList.add(new SmileInfo("[\\蛋糕]", R.drawable.smiley_68));
		mList.add(new SmileInfo("[\\闪电]", R.drawable.smiley_69));
		mList.add(new SmileInfo("[\\炸弹]", R.drawable.smiley_70));
		mList.add(new SmileInfo("[\\小刀]", R.drawable.smiley_71));
		mList.add(new SmileInfo("[\\足球]", R.drawable.smiley_72));
		mList.add(new SmileInfo("[\\瓢虫]", R.drawable.smiley_73));
		mList.add(new SmileInfo("[\\大便]", R.drawable.smiley_74));
		mList.add(new SmileInfo("[\\月亮]", R.drawable.smiley_75));
		mList.add(new SmileInfo("[\\太阳]", R.drawable.smiley_76));
		mList.add(new SmileInfo("[\\礼物]", R.drawable.smiley_77));
		mList.add(new SmileInfo("[\\拥抱]", R.drawable.smiley_78));
		mList.add(new SmileInfo("[\\赞]", R.drawable.smiley_79));
		mList.add(new SmileInfo("[\\踩]", R.drawable.smiley_80));
		mList.add(new SmileInfo("[\\握手]", R.drawable.smiley_81));
		mList.add(new SmileInfo("[\\耶]", R.drawable.smiley_82));
		mList.add(new SmileInfo("[\\佩服]", R.drawable.smiley_83));
		mList.add(new SmileInfo("[\\勾引]", R.drawable.smiley_84));
		mList.add(new SmileInfo("[\\拳头]", R.drawable.smiley_85));
		mList.add(new SmileInfo("[\\小手指]", R.drawable.smiley_86));
		mList.add(new SmileInfo("[\\三只手指]", R.drawable.smiley_87));
		mList.add(new SmileInfo("[\\NO]", R.drawable.smiley_88));
		mList.add(new SmileInfo("[\\YES]", R.drawable.smiley_89));
		mList.add(new SmileInfo("[\\甜蜜]", R.drawable.smiley_90));
		mList.add(new SmileInfo("[\\飞吻]", R.drawable.smiley_91));
		mList.add(new SmileInfo("[\\蹦蹦]", R.drawable.smiley_92));
		mList.add(new SmileInfo("[\\发抖]", R.drawable.smiley_93));
		mList.add(new SmileInfo("[\\上火]", R.drawable.smiley_94));
		mList.add(new SmileInfo("[\\摆动]", R.drawable.smiley_95));
		setList(mList);
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		final double count = row * column;
		return (int) Math.ceil(mList.size() / count);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyGridView gridView = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.common_smile_gridview, parent, false);
			gridView = (MyGridView) convertView;
			SmileAdapter adapter = new SmileAdapter(mContext);
			gridView.setAdapter(adapter);
		} else {
			gridView = (MyGridView) convertView;
		}
		final int start = position * (row * column) >= mList.size() ? mList.size() - 1 : position * (row * column);
		final int stop = (position + 1) * (row * column) >= mList.size() ? mList.size() : (position + 1) * (row * column);
		List<SmileInfo> list = mList.subList(start, stop);
		((SmileAdapter) gridView.getAdapter()).setList(list);
		return convertView;
	}
}
