package com.example.news;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.news.db.DBHelper;
import com.example.news.db.NewsEntity;
import com.j256.ormlite.dao.Dao;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


class Loadhtml extends AsyncTask<String, String, String>
{

	private Context mContext;
	private TextView tv;
	private Dialog loginingDialog;
	private DBHelper mDbHelper;
	private Dao<NewsEntity,Integer> mNewsDao;
	
	public Loadhtml(Context mContext,TextView tv,Dialog loginingDialog) {
		
		this.mContext = mContext;
		this.tv = tv;
		this.loginingDialog = loginingDialog;
	}
	
    @Override
    protected String doInBackground(String... params) {
    	
		mDbHelper = DBHelper.getInstance(mContext);
		try {
			mNewsDao = mDbHelper.getNewsDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String myString = null;
        StringBuffer sff = new StringBuffer();
        String html = getHtmlByUrl("http://daily.zhihu.com/");
		try {
//			Document doc = Jsoup.connect("http://daily.zhihu.com/").get();
			Document doc = Jsoup.parse(html);
			Elements links = doc.select("a[href]");
			int i = 0;
			for (Element link : links) {
				String url = link.attr("abs:href");
				if (url.contains("http://daily.zhihu.com/story/")) {
					i++;
					Element img = link.child(0);
					String src = img.attr("src");
					String fileName = NewsApp.MTC_DATA_ZHIHU_PATH+"/"+i+".jpg";
					String text = link.text();
					getImages(src, fileName);
					NewsEntity news = new NewsEntity(text, fileName);
					mNewsDao.createIfNotExists(news);
					sff.append(url).append("\n").append(src).append("\n")
							.append(link.text()).append("\n");
				}
			}
			myString = sff.toString();
			return myString;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    
	@Override
    protected void onPostExecute(String result) {
		
		if (loginingDialog != null && loginingDialog.isShowing())
			loginingDialog.dismiss();
		if(result == null){
			Toast.makeText(mContext, "加载失败",
					Toast.LENGTH_SHORT).show();
		}else{
			tv.setText(result);
		}

    }
	/**
	 * 根据URL获得所有的html信息
	 * @param url
	 * @return
	 */
	public static String getHtmlByUrl(String url){
		String html = null;
		HttpClient httpClient = new DefaultHttpClient();//创建httpClient对象
		HttpGet httpget = new HttpGet(url);//以get方式请求该URL
		try {
			HttpResponse responce = httpClient.execute(httpget);//得到responce对象
			int resStatu = responce.getStatusLine().getStatusCode();//返回码
			if (resStatu==HttpStatus.SC_OK) {//200正常  其他就不对
				//获得相应实体
				HttpEntity entity = responce.getEntity();
				if (entity!=null) {
					html = EntityUtils.toString(entity);//获得html源代码
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return html;
	}
	
	 /**
     * @param urlPath 图片路径
     * @throws Exception 
     */
    public static void getImages(String urlPath,String fileName) throws Exception{
        URL url = new URL(urlPath);//：获取的路径
        //:http协议连接对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(6 * 10000);
        if (conn.getResponseCode() <10000){
            InputStream inputStream = conn.getInputStream();
            byte[] data = readStream(inputStream);
            if(data.length>(1024*10)){
                FileOutputStream outputStream = new FileOutputStream(fileName);
                outputStream.write(data);
                outputStream.close();
            }
        }
         
    }
     
    /**
     * 读取url中数据，并以字节的形式返回
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inputStream.read(buffer)) !=-1){
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }

    
}
