package com.zy.booking.control;

import android.os.Bundle;

public abstract class OnViewContainerLifeCycleListener {
	
	public abstract void onCreate(Bundle mBundle);
	
	public abstract void onResume();
	
	public  void onStop(){};
	
	public abstract void onDestroy();
	
	public void onSaveInstance(){};
	
	public void onConfingChangedListener(){};

}
