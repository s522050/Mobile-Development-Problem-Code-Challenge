package com.example.s522050.challenge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.s522050.challenge.model.DataItem;
import com.example.s522050.challenge.utils.HttpHelper;
import com.example.s522050.challenge.utils.NetworkHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String ORDER_URL = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    public boolean networkOk;

    @BindView(R.id.output_total_spent)TextView total_spent_result;
    @BindView(R.id.output_total_quantity)TextView total_quantity_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        networkOk = NetworkHelper.hasNetworkConnection(this);

    }

    public void runClickHandler(View view) {
        SimpleAsyncTask task = new SimpleAsyncTask();
        task.execute(ORDER_URL);
    }

    public void clearClickHandler(View view) {
        total_quantity_result.setText("");
        total_spent_result.setText("");
    }

    private class SimpleAsyncTask extends AsyncTask<String, Void, List<DataItem>> {

        @Override
        protected List<DataItem> doInBackground(String... url) {

            if (url.length < 1 || url[0] == null) {
                return null;
            }

            List<DataItem> result = HttpHelper.fetchOrderData(url[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<DataItem> dataItems) {

            double totalAmount=0;
            int quantity=0;

            for (DataItem item : dataItems) {
                if (item.getCustomerEmail().equals("napoleon.batz@gmail.com")){
                    Log.i(TAG, "Inside if statement: "+ item.getCustomerEmail());
                    totalAmount += Double.valueOf(item.getTotalSpent());
                }
                quantity+=item.getQuantity();
            }
            total_spent_result.setText(String.valueOf(totalAmount));
            total_quantity_result.setText(String.valueOf(quantity));
        }
    }
}
