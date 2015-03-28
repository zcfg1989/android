package com.example.news.view;

import com.example.news.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class DialogFactory {
	
	public static Dialog getLoadingDialog(Activity activity, String msg, boolean cancelable, final DialogInterface.OnCancelListener cancelEvent) {
		final Dialog dialog = new Dialog(activity, R.style.Theme_CustomDialog);
		
		View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
		
		ImageView aniImage = (ImageView)contentView.findViewById(R.id.ani_img);
		TextView msgView = (TextView)contentView.findViewById(R.id.message);
		
		Animation ani = AnimationUtils.loadAnimation(activity, R.anim.loading_ani);
		aniImage.startAnimation(ani);
		if(TextUtils.isEmpty(msg)) {
			msgView.setVisibility(View.GONE);
		}
		else {
			msgView.setVisibility(View.VISIBLE);
			msgView.setText(msg);
		}
		
		if(cancelable) {
			if(cancelEvent != null) {
				dialog.setOnCancelListener(cancelEvent);
			}
		}
		else {
			dialog.setCancelable(false);
		}
		
		dialog.setContentView(contentView);
		return dialog;
	}

}
