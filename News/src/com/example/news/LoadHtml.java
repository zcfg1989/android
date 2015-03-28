package com.example.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;


class Loadhtml extends AsyncTask<String, String, String>
{

	private Context mContext;
	private TextView tv;
	private Dialog loginingDialog;
	
	public Loadhtml(Context mContext,TextView tv,Dialog loginingDialog) {
		
		this.mContext = mContext;
		this.tv = tv;
		this.loginingDialog = loginingDialog;
	}
	
    @Override
    protected String doInBackground(String... params) {

        String myString = null;
        StringBuffer sff = new StringBuffer();
		try {
			Document doc = Jsoup.connect("http://www.baidu.com").get();
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				sff.append(link.attr("abs:href")).append("  ")
						.append(link.text()).append(" ");
			}
			myString = sff.toString();
			return myString;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    

    @Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
    protected void onPostExecute(String result) {
		
		if (loginingDialog != null && loginingDialog.isShowing())
			loginingDialog.dismiss();
		if(result == null){
			Toast.makeText(mContext, "º”‘ÿ ß∞‹",
					Toast.LENGTH_SHORT).show();
		}else{
			tv.setText(result);
		}

    }

    
}
