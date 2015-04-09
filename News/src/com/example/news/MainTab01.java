package com.example.news;

import java.sql.SQLException;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.news.db.DBHelper;
import com.example.news.db.NewsEntity;
import com.example.news.view.DialogFactory;
import com.j256.ormlite.dao.Dao;

public class MainTab01 extends Fragment
{
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mContext = this.getActivity();
		View myView = inflater.inflate(R.layout.main_tab_01, container, false);
		TextView tv = (TextView)myView.findViewById(R.id.zhihu_tx);
		Dialog loginingDialog = DialogFactory.getLoadingDialog((Activity) mContext,
				"ÕýÔÚ¼ÓÔØ", true, null);
		loginingDialog.setCanceledOnTouchOutside(false);
		loginingDialog.show();
		Loadhtml load = new Loadhtml(mContext,tv,loginingDialog);
		load.execute();
		return myView;
		
	}
	

	



	

}
