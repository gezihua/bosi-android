package com.bosi.chineseclass.utils;



public class ReflectUtils {
	
	@SuppressWarnings("unchecked")
	public static <Z> Z getObjectFromPackage(String packageName,String mClassName ,Class<Z> z){
		Z mData = null;
		String className = packageName+"."+mClassName;
		
		try {
			Class mLoadingClass = Class.forName(className);
			try {
				mData =	(Z) mLoadingClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return mData;
	}

}
