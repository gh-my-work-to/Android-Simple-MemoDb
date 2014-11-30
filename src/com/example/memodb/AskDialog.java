package com.example.memodb;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AskDialog extends DialogFragment
{

	/**
	 * AskDialogと通信するインターフェース
	 * @author ub2er
	 *
	 */
	public interface AskDialogListener
	{
		/**
		 * yesボタンが押された時に実行される
		 */
		public void onClickYes();
		
		/**
		 * キャンセルボタンが押された時に実行される
		 */
		public void onClickCancel();
	}

	/**
	 * AskDialogと通信するリスナー
	 */
	private AskDialogListener mListener;
	
	/**
	 * ダイアログのタイトル
	 */
	private String mTitle;
	

	public AskDialog(AskDialogListener lis, String ttl)
	{
		super();
		mListener = lis;
		mTitle = ttl;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder bldr = new Builder(getActivity());
		bldr.setMessage(mTitle);
		
		/**
		 * yesボタンを設定
		 */
		bldr.setPositiveButton(android.R.string.yes, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mListener.onClickYes();
			}
		});
		
		/**
		 * キャンセルキーを有効化
		 */
		bldr.setCancelable(true);
		
		/**
		 * キャンセルボタンを設定
		 */
		bldr.setNegativeButton(android.R.string.cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mListener.onClickCancel();
			}
		});
		
		return bldr.create();
	}

	@Override
	public void onDismiss(DialogInterface dialog)
	{
		super.onDismiss(dialog);
		mListener = null;
	}

}
