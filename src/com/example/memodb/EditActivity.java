package com.example.memodb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.example.memodb.AskDialog.AskDialogListener;

public class EditActivity extends Activity
{

	private static final String TAG = "EditActivity";

	public static final String NAME_DELETE = "delete-record";
	public static final String NAME_POSITION = "item-position";

	private EditText mEdt_id;
	private EditText mEdt_value;
	private Button mBtnSave;
	private Button mBtnCancel;
	private Button mBtnDelete;

	private int mPosition;
	private String mValueBuf;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//ソフトキーボード出現抑制
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.activity_edit);

		mEdt_id = (EditText) findViewById(R.id.edit_id);
		mEdt_value = (EditText) findViewById(R.id.edit_value);

		mBtnSave = (Button) findViewById(R.id.editBtnSave);
		mBtnSave.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doSave();
			}
		});
		mBtnCancel = (Button) findViewById(R.id.editBtnCacel);
		mBtnCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doCancel();
			}
		});
		mBtnDelete = (Button) findViewById(R.id.editBtnDelete);
		mBtnDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doDelete();
			}
		});
		///インテントからデータ取り出し///
		if (getIntent() != null && getIntent().getExtras() != null)
		{
			Bundle bu = getIntent().getExtras();
			int id = bu.getInt(DbManager.COL_ID);
			String value = bu.getString(DbManager.COL_VALUE);

			mEdt_id.setText(id + "");
			mEdt_value.setText(value);

			mValueBuf = value;
			mPosition = bu.getInt(NAME_POSITION);
		}
		else
		{
			finish();
		}
	}

	private void doSave()
	{
		int id = 0;
		try
		{
			id = Integer.parseInt(mEdt_id.getText().toString());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}

		String value = mEdt_value.getText().toString();

		Intent it = new Intent();
		it.putExtra(DbManager.COL_ID, id);
		it.putExtra(DbManager.COL_VALUE, value);
		it.putExtra(NAME_POSITION, mPosition);

		setResult(RESULT_OK, it);
		finish();
	}

	private void doCancel()
	{
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * レコードを削除するか尋ねるダイアログを表示する
	 */
	private void doDelete()
	{
		AskDialog dlg = new AskDialog(new AskDialogListener()
		{
			@Override
			public void onClickYes()
			{
				doDeleteReally();
				if (BuildConfig.DEBUG)
				{
					Log.v(TAG, "onClickYes");
				}
			}

			@Override
			public void onClickCancel()
			{
				if (BuildConfig.DEBUG)
				{
					Log.v(TAG, "onClickCancel");
				}
				return;
			}
		}, getApplicationContext().getResources().getString(R.string.askDelete));

		dlg.show(getFragmentManager(), "ask");
	}

	/**
	 * レコードを削除する
	 */
	private void doDeleteReally()
	{
		int id = 0;
		try
		{
			id = Integer.parseInt(mEdt_id.getText().toString());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}

		Intent it = new Intent();
		it.putExtra(DbManager.COL_ID, id);
		it.putExtra(NAME_DELETE, true);
		it.putExtra(NAME_POSITION, mPosition);

		setResult(RESULT_OK, it);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean ret = false;
		int id = item.getItemId();

		switch (id)
		{
		case R.id.menuUndo://編集の取り消しをする
			//valueを復帰させる
			mEdt_value.setText(mValueBuf);

			ret = true;
			break;
		default:
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		mEdt_id = null;
		mEdt_value = null;

		mBtnSave.setOnClickListener(null);
		mBtnSave = null;

		mBtnCancel.setOnClickListener(null);
		mBtnCancel = null;

		mBtnDelete.setOnClickListener(null);
		mBtnDelete = null;
	}

}
