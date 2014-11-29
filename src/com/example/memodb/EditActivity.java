package com.example.memodb;

import com.example.memodb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends Activity
{
	public static final String NAME_DELETE = "delete-record";
	public static final String NAME_POSITION = "item-position";

	private EditText mEdt_id;
	private EditText mEdt_value;
	private Button mBtnSave;
	private Button mBtnCancel;
	private Button mBtnDelete;
	
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		mEdt_id = (EditText) findViewById(R.id.edit_id);
		mEdt_value = (EditText) findViewById(R.id.edit_value);

		mBtnSave = (Button) findViewById(R.id.editSave);
		mBtnSave.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doSave();
			}
		});
		mBtnCancel = (Button) findViewById(R.id.editCacel);
		mBtnCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doCancel();
			}
		});
		mBtnDelete = (Button) findViewById(R.id.editDelete);
		mBtnDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doDelete();
			}
		});
		//////////////
		if (getIntent() != null && getIntent().getExtras() != null)
		{
			Bundle bu = getIntent().getExtras();
			int id = bu.getInt(DbManager.COL_ID);
			String value = bu.getString(DbManager.COL_VALUE);

			mEdt_id.setText(id + "");
			mEdt_value.setText(value);
			
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

	private void doDelete()
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
