package com.example.memodb;

import java.util.ArrayList;

import com.example.memodb.BuildConfig;
import com.example.memodb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener
{

	/**
	 * ログtag用
	 */
	private static final String TAG = "MainActivity";

	private static final int REQODE_EDIT = 1;

	/**
	 * DB表示リストビュー
	 */
	private ListView mListView;

	/**
	 * リストアダプター
	 */
	private ArrayAdapterCustom mAdptr;

	/**
	 * DB管理
	 */
	private DbManager mDbm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//DB管理　取得
		mDbm = new DbManager(getApplicationContext());
		//colのインデックスを取得する
		mDbm.initColIdx();

		//DBからデータ取得
		ArrayList<RecordClass> al = mDbm.getDatas();
		//アダプター作成
		mAdptr = new ArrayAdapterCustom(getApplicationContext(), al);

		//リストビュー取得
		mListView = (ListView) findViewById(R.id.listView1);
		//アダプターセット、表示
		mListView.setAdapter(mAdptr);
		//リスナー
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		mAdptr.clear();
		mAdptr = null;

		mDbm.close();
		mDbm = null;

		mListView.setAdapter(null);
		mListView.setOnItemClickListener(null);
		mListView = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean ret = false;
		int id = item.getItemId();

		switch (id)
		{
		//DBにデータを追加する
		case R.id.menuAdd:
			menuAdd();
			ret = true;
			break;
		//DBのデータを整理する
		case R.id.menuSeiri:
			menuOrder();
			ret = true;
			break;
		default:
			ret = super.onOptionsItemSelected(item);
		}

		return ret;
	}

	/**
	 * データの欠番が無いようにする
	 */
	private void menuOrder()
	{
		mAdptr.clear();
		mAdptr = null;
		mListView.setAdapter(null);
		
		mDbm.orderData();
		
		ArrayList<RecordClass> al = mDbm.getDatas();
		mAdptr = new ArrayAdapterCustom(getApplicationContext(), al);
		mListView.setAdapter(mAdptr);
	}

	/**
	 * データを追加する
	 */
	private void menuAdd()
	{
		RecordClass rc;//追加するデータ

		int last_pos = mAdptr.getCount() - 1;//最後尾に追加する
		if (last_pos >= 0)
		{
			RecordClass last = mAdptr.getItem(last_pos);
			int id = last.getmId() + 1;
			String val = "val-" + id;

			if (BuildConfig.DEBUG)
			{
				Log.d(TAG, "id,val:" + id + "," + val);
			}
			rc = new RecordClass(id, val);
		}
		else
		{
			rc = new RecordClass(1, "val-" + 1);
		}
		//アダプターに追加
		mAdptr.add(rc);
		//DBに追加
		mDbm.addData(rc);

		if (BuildConfig.DEBUG)
		{
			Log.v(TAG, "current datas num:" + mAdptr.getCount());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		RecordClass rc = (RecordClass) parent.getAdapter().getItem(position);
		Intent it = new Intent(getApplicationContext(), EditActivity.class);

		it.putExtra(DbManager.COL_ID, rc.getmId());//idを送る
		it.putExtra(DbManager.COL_VALUE, rc.getmValue());//valueを送る
		it.putExtra(EditActivity.NAME_POSITION, position);//positionも送る

		startActivityForResult(it, REQODE_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case REQODE_EDIT:
			if (resultCode != RESULT_OK)
			{
				break;
			}
			int id = data.getIntExtra(DbManager.COL_ID, 0);
			if (id == 0)
			{
				break;
			}

			int position = data.getIntExtra(EditActivity.NAME_POSITION, 0);

			boolean isDelete = data.getBooleanExtra(EditActivity.NAME_DELETE, false);
			if (isDelete)
			{
				mDbm.deleteData(id);
				//削除箇所を取得して削除する
				RecordClass tobeDel = mAdptr.getItem(position);
				mAdptr.remove(tobeDel);
			}
			else
			{
				String value = data.getStringExtra(DbManager.COL_VALUE);
				mDbm.updateData(new RecordClass(id, value));

				//更新箇所を取得して更新する
				RecordClass tobeUpd = mAdptr.getItem(position);
				tobeUpd.setmValue(value);
				mAdptr.notifyDataSetChanged();
			}

			break;
		}
	}

}
