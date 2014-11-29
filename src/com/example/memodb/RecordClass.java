package com.example.memodb;

public class RecordClass
{
	private int mId;
	private String mValue;
	
	
	public RecordClass(int mId, String mValue)
	{
		this.mId = mId;
		this.mValue = mValue;
	}

	
	public int getmId()
	{
		return mId;
	}


	public void setmId(int mId)
	{
		this.mId = mId;
	}


	public String getmValue()
	{
		return mValue;
	}


	public void setmValue(String mValue)
	{
		this.mValue = mValue;
	}
	
}
