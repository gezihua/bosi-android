package com.bosi.chineseclass.control;


public class DownLoadResouceControl {

	private String [] mCurrentData;
	public interface DownloadCallback{
		public void onDownLoadCallback(int mCurrentSize,int wholeSize);
	}
	
	 int loadedData = -1;
		
		
		public void downloadimgs() {
			loadedData = -1;
			updateProgress();
			for(int i =0;i<mCurrentData.length;i++){}
			
		}

		private void updateProgress(){
			loadedData++;
		
		}
		

}
