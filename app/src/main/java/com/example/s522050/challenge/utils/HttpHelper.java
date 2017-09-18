package com.example.s522050.challenge.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.s522050.challenge.model.DataItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HttpHelper {

    private static final String LOG_TAG = HttpHelper.class.getSimpleName() ;

    public static List<DataItem> fetchOrderData(String requestUrl) {

        String jsonResult = null;

        try {
            jsonResult = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DataItem> result = extractData(jsonResult);

        return result;
    }


    public static String makeHttpRequest(String urlAddress) throws IOException {

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String jsonResponse = null;

        try {
            URL dataUrl = new URL(urlAddress);
            urlConnection = (HttpURLConnection) dataUrl.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readStream(inputStream);
            } else {
                throw new IOException("Error in connection!" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<DataItem> extractData(String orderJSON) {

        if (TextUtils.isEmpty(orderJSON)) {
            return null;
        }

        ArrayList<DataItem> items = new ArrayList<>();
        int quantity = 0;

        try {
            JSONObject rootJson = new JSONObject(orderJSON);
            JSONArray orderArray = rootJson.getJSONArray("orders");

            for (int i = 0; i < orderArray.length(); i++) {

                JSONObject item = orderArray.getJSONObject(i);

                String total_spent = item.getString("total_price");
                String email = item.getString("email");

                JSONArray lineItemArray = item.getJSONArray("line_items");
                for (int j = 0; j < lineItemArray.length(); j++) {

                    String bronzeBag = "Awesome Bronze Bag";

                    JSONObject eachItem = lineItemArray.getJSONObject(j);
                    String title = eachItem.getString("title");
                    int itemQuantity = eachItem.getInt("quantity");

                    Log.i(LOG_TAG, "extractData: itemQuantity "+ itemQuantity + ", "+title);

                    if(title.equals(bronzeBag)){
                        quantity+=itemQuantity;
                        Log.i(LOG_TAG, "Inside if state: "+item + ", " +quantity);
                    }
                }

                Log.i(LOG_TAG, "Before add: " + email +", " +item+", "+total_spent+", "+quantity);
                DataItem eachItem = new DataItem(email, total_spent, quantity);
                items.add(eachItem);
                quantity = 0;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return items;
    }
}
