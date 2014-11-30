package com.example.memodb;

import java.util.ArrayList;

import com.example.memodb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterCustom extends ArrayAdapter<RecordClass>
{

	public class ViewHolder
	{
		TextView mmtvId;
		TextView mmtvValue;
	}

	private LayoutInflater mInflator;

	public ArrayAdapterCustom(Context context, ArrayList<RecordClass> al)
	{
		super(context, 0, al);
		mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder hld;

		if (convertView == null)
		{
			convertView = mInflator.inflate(R.layout.list_row, parent, false);

			hld = new ViewHolder();
			hld.mmtvId = (TextView) convertView.findViewById(R.id.listRowId);
			hld.mmtvValue = (TextView) convertView.findViewById(R.id.listRowValue);

			convertView.setTag(hld);
		}
		else
		{
			hld = (ViewHolder) convertView.getTag();
		}

		//idを表示する
		hld.mmtvId.setText(getItem(position).getmId() + "");

		//valueが複数行なら最初の行だけ表示する
		String[] vals = getItem(position).getmValue().split("\n");

		hld.mmtvValue.setText((vals.length == 1) ? //
		getItem(position).getmValue()
				: vals[0]);

		return convertView;
	}

}
