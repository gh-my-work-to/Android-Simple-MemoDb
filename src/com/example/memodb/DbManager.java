package com.example.memodb;

import java.util.ArrayList;

import com.example.memodb.BuildConfig;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbManager extends SQLiteOpenHelper
{

	private static final String TAG = "DbManager";
	private static final String DBNAME = "mydb.sqlite3";
	private static final int DBVERSION = 1;

	private static final String TABLE_NAME = "tWord";
	public static final String COL_ID = "word_id";
	private static final int COL_ID_INDEX = 0;
	public static final String COL_VALUE = "word";
	private static final int COL_VALUE_INDEX = 1;

	private static final String INIT_SQL = String.format(

			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s INTEGER PRIMARY KEY NOT NULL, "
					+ "%s TEXT NOT NULL)"

			, TABLE_NAME, COL_ID, COL_VALUE);


	public DbManager(Context context, String name, CursorFactory factory, int version)
	{
		super(context, DBNAME, null, DBVERSION);

	}

	public DbManager(Context context)
	{
		super(context, DBNAME, null, DBVERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(INIT_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		return;
	}

	/**
	 * DBのデータを返す
	 * @return
	 */
	public ArrayList<RecordClass> getDatas()
	{
		//戻り値
		ArrayList<RecordClass> ret = new ArrayList<RecordClass>();

		SQLiteDatabase db = getReadableDatabase();
		//DBの全データ取得
		Cursor cs = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (cs.moveToNext())
		{
			int id = cs.getInt(COL_ID_INDEX);
			String value = cs.getString(COL_VALUE_INDEX);
			//クラスに変換して戻り値に追加
			RecordClass rc = new RecordClass(id, value);
			ret.add(rc);
		}
		cs.close();
		db.close();

		return ret;
	}

	/**
	 * データを追加する
	 * @param rc　追加するデータ
	 */
	public void addData(RecordClass rc)
	{
		SQLiteDatabase db = getWritableDatabase();

		//インサート文を発行してデータ追加
		db.execSQL(
				String.format("INSERT INTO %s (%s, %s) VALUES(%d, '%s')",
						TABLE_NAME, COL_ID, COL_VALUE,
						rc.getmId(), rc.getmValue()
						));
		db.close();
	}

	/**
	 * データをアップデートする
	 * @param rc　アップデートするデータ
	 */
	public void updateData(RecordClass rc)
	{
		SQLiteDatabase db = getWritableDatabase();

		//アップデート文を発行してデータ追加
		db.execSQL(
				String.format("UPDATE %s SET %s='%s' WHERE %s=%d",
						TABLE_NAME,// 
						COL_VALUE,//
						rc.getmValue(),//
						COL_ID,//
						rc.getmId()//
				));
		db.close();
	}

	/**
	 * データを削除する
	 * @param id　削除するデータのid
	 */
	public void deleteData(int id)
	{
		SQLiteDatabase db = getWritableDatabase();

		//デリート文を発行してデータ追加
		db.execSQL(
				String.format("DELETE FROM %s WHERE %s=%d",
						TABLE_NAME,// 
						COL_ID, id));
		db.close();
	}

	/**
	 * データの欠番をなくして整理する＝idを振り直す
	 */
	public void orderData()
	{
		SQLiteDatabase db = getWritableDatabase();
		Cursor cs = db.query(TABLE_NAME, null, null, null, null, null, null);

		//make tmpTable
		db.execSQL("DROP TABLE IF EXISTS T_TMP");
		db.execSQL(String.format(
				"CREATE TABLE T_TMP ("
						+ "%s INTEGER PRIMARY KEY NOT NULL, "
						+ "%s TEXT NOT NULL)",
				COL_ID, COL_VALUE));

		int newId = 1;
		while (cs.moveToNext())
		{
			String oldValue = cs.getString(COL_VALUE_INDEX);

			db.execSQL(String.format(
					"INSERT INTO T_TMP (%s, %s) VALUES(%d, '%s')",
					COL_ID, COL_VALUE, newId, oldValue
					));
			if (BuildConfig.DEBUG)
			{
				Log.v(TAG, "newId:" + newId + " value:" + oldValue);
			}
			newId++;
		}
		cs.close();
		
		//drop
		db.execSQL(String.format(
				"DROP TABLE %s", TABLE_NAME));
		//alter
		db.execSQL(String.format(
				"ALTER TABLE T_TMP RENAME TO %s",
				TABLE_NAME));

		db.close();
	}

}
