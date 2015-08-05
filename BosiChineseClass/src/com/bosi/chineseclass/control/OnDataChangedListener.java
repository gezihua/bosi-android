package com.bosi.chineseclass.control;

public interface OnDataChangedListener {
	public void chagePageData(int refid);

	public void chagePageData();// 如果没有id的话说明还是用当前的id 只是需要将学习的部分 开始介绍一下
	
	public void onSampleLoadBefore();
}