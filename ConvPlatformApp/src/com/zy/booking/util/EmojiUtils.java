package com.zy.booking.util;

import java.io.IOException;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.zy.booking.wedget.gif.AnimatedGifDrawable;
import com.zy.booking.wedget.gif.AnimatedImageSpan;

public class EmojiUtils {
	
	
	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
	 * **/
	private static boolean isDeletePng(int cursor,EditText input) {
		String st = "#[face/png/f_static_000.png]#";
		String content = input.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(),
					content.length());
			String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}
	public static void deleteEmoji(EditText input){
			if (input.getText().length() != 0) {
				int iCursorEnd = Selection.getSelectionEnd(input.getText());
				int iCursorStart = Selection.getSelectionStart(input.getText());
				if (iCursorEnd > 0) {
					if (iCursorEnd == iCursorStart) {
						if (isDeletePng(iCursorEnd,input)) {
							String st = "#[face/png/f_static_000.png]#";
							((Editable) input.getText()).delete(
									iCursorEnd - st.length(), iCursorEnd);
						} else {
							((Editable) input.getText()).delete(iCursorEnd - 1,
									iCursorEnd);
						}
					} else {
						((Editable) input.getText()).delete(iCursorStart,
								iCursorEnd);
					}
				}
			}
	}
	
	
	public static void insertEmojiToEditText(EditText mEditTextView,CharSequence mCharSequence){
		int iCursorStart = Selection.getSelectionStart((mEditTextView.getText()));
		int iCursorEnd = Selection.getSelectionEnd((mEditTextView.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) mEditTextView.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((mEditTextView.getText()));
		((Editable) mEditTextView.getText()).insert(iCursor, mCharSequence);
	}

	public static SpannableStringBuilder getFace(String png,Context mContext) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			/**
			 * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
			 * 所以这里对这个tempText值做特殊处理
			 * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
			 * */
			String tempText = "#[" + png + "]#";
			sb.append(tempText);
			sb.setSpan(
					new ImageSpan(mContext, BitmapFactory
							.decodeStream(mContext.getAssets().open(png))), sb.length()
							- tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	
	
	public static  SpannableStringBuilder handler(final TextView gifTextView,
			String content ,Context mContext) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String num = tempText.substring(
						"#[face/png/f_static_".length(), tempText.length()
								- ".png]#".length());
				String gif = "face/gif/f" + num + ".gif";
				InputStream is = mContext.getAssets().open(gif);
				sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,
						new AnimatedGifDrawable.UpdateListener() {
							@Override
							public void update() {
								gifTextView.postInvalidate();
							}
						})), m.start(), m.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				is.close();
			} catch (Exception e) {
				String png = tempText.substring("#[".length(),
						tempText.length() - "]#".length());
				try {
					sb.setSpan(
							new ImageSpan(mContext, BitmapFactory
									.decodeStream(mContext.getAssets()
											.open(png))), m.start(), m.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return sb;
	}

}
