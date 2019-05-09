package com.example.Web;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.MyThread.ClassReadPCMdata_From_Web.Kurtuosis;
import static com.example.MyThread.ClassReadPCMdata_From_Web.Marginvalue;
import static com.example.MyThread.ClassReadPCMdata_From_Web.Plusevalue;
import static com.example.MyThread.ClassReadPCMdata_From_Web.Ppeakvalue;
import static com.example.MyThread.ClassReadPCMdata_From_Web.V_rms;
import static com.example.MyThread.ClassReadPCMdata_From_Web.create_time;
import static com.example.MyThread.ClassReadPCMdata_From_Web.volet;


/**
 *从后台获取数据
 */

public class WebDataReceive {

	/**
	* 暂时存放数据的Bean
	* */
	static WebDataBean webDataBean;

	public static void ReceiveDataFromWeb() {
		OkHttpClient client = new OkHttpClient();

		FormBody formBody = new FormBody.Builder()
				.add("uid", "001")
				.build();

		Request request = new Request.Builder()
				.url("http://139.199.60.80/php/DLX/shopping/public/index.php/index/wave/waveList")
				.post(formBody)				//默认就是GET请求，可以不写
				.build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.e("onFailure: ", String.valueOf(e));
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String jsonString = response.body().string();
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
//					Log.i("data>>>>>>>>>>>>>>", String.valueOf(jsonArray));
					for(int i = 0;i < jsonArray.length();i++){
						JSONObject dataObject = (JSONObject) jsonArray.get(i);
						volet.add(Float.valueOf(dataObject.getString("volet")));
						V_rms.add(Float.valueOf(dataObject.getString("V_rms")));
						Plusevalue.add(Float.valueOf(dataObject.getString("Plusevalue")));
						Marginvalue.add(Float.valueOf(dataObject.getString("Marginvalue")));
						Ppeakvalue.add(Float.valueOf(dataObject.getString("Ppeakvalue")));
						Kurtuosis.add(Float.valueOf(dataObject.getString("Kurtuosis")));
						create_time.add(dataObject.getString("create_time"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
