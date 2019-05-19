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
import static java.lang.String.valueOf;


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

		//POST请求
		FormBody formBody = new FormBody.Builder()
				.add("uid", "001")
				.build();

		Request request = new Request.Builder()
				.url("http://139.199.60.80/php/DLX/cv/public/index.php/index/wave/waveList")	//GET方式：http://139.199.60.80/php/DLX/cv/public/index.php/index/wave/waveList?uid=001
				.post(formBody)				//默认就是GET请求就可以不写，这里是post
				.build();

		//GET请求方法
//		Request request = new Request.Builder()
//				.url("http://139.199.60.80/php/DLX/cv/public/index.php/index/wave/waveList?uid=001")	//GET方式：http://139.199.60.80/php/DLX/cv/public/index.php/index/wave/waveList?uid=001
//				.get()			//默认就是GET请求就可以不写
//				.build();

		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.e("onFailure: ", valueOf(e));
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String jsonString = response.body().string();
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
//					Log.i("data>>>>>>>>>>>>>>", String.valueOf(jsonArray));
						JSONObject dataObject = (JSONObject) jsonArray.get(0);
						String voletString = dataObject.getString("volet");
						String voletStringArray[] = voletString.split(",");
						for (int i = 0;i < voletStringArray.length;i ++){
							volet[i] = Float.parseFloat(voletStringArray[i]);
						}
						V_rms = Float.parseFloat(dataObject.getString("V_rms"));
						Plusevalue = Float.parseFloat(dataObject.getString("Plusevalue"));
						Marginvalue = Float.parseFloat(dataObject.getString("Marginvalue"));
						Ppeakvalue = Float.parseFloat(dataObject.getString("Ppeakvalue"));
						Kurtuosis = Float.parseFloat(dataObject.getString("Kurtuosis"));
						create_time = dataObject.getString("create_time");
				} catch (JSONException e) {
					e.printStackTrace();
					System.out.print(">>>>>>>>>>>Json数据解析错误!");
				}
			}
		});
	}
}
