package com.spark.demo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpDemo {

	public static void main(String[] args) throws IOException {

		// MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		// RequestBody body = RequestBody.create(JSON, "");

		RequestBody formBody = new FormBody.Builder().add("data", "onlytest").build();

		OkHttpClient client = new OkHttpClient();
		OkHttpClient client2 = client.newBuilder().connectTimeout(30, TimeUnit.SECONDS).build();
		System.out.println(client.connectTimeoutMillis());
		System.out.println(client2.connectTimeoutMillis());
		Request request = new Request.Builder().url("http://localhost:8080/bvrc/data/test/query").post(formBody)
				.build();
		Response response = client.newCall(request).execute();

		System.out.println("Result: " + response.isSuccessful());
		System.out.println();
		System.out.println("Server: " + response.header("Server"));
		System.out.println();
		System.out.println("ResponseBody: " + response.body().string());

	}

}
