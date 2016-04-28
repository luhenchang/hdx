package com.accuvally.hdtui.utils;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ForbidPaster {
	private EditText editText;

	public void setEditText(EditText editText) {
		this.editText = editText;
		forbidPaster();
	}

	/**
	 * EditText文本禁止粘贴
	 */
	private void forbidPaster() {
		// TODO Auto-generated method stub
		editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

}
