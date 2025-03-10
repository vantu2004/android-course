package com.vantu.parsedata;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    RequestQueue queue;
    //    Sử dụng Volley để lấy dữ liệu từ API
    String url = "https://jsonplaceholder.typicode.com/posts";
    String url_jsonObject = "https://jsonplaceholder.typicode.com/posts/1";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView_jsonObject);

        //    đưa request vào queue để lần lượt xử lý (đưa vào main vì phải tạo context trước mới khởi tạo đc queue)
        // queue = Volley.newRequestQueue(MainActivity.this);

        // dùng MySingleton class đảm bảo tạo RequestQueue 1 lần và sử dụng cho nhiều activity, nhiều lần gọi onCreate()
        queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //getString();
        //getJsonArrayRequest();
        getJsonObject();
    }

    private void getString() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Xử lý dữ liệu trả về từ API
                    Log.d("string", "onCreate: " + response);
                },
                error -> {
                    // Xử lý lỗi
                    Log.d("string", "Failed to get info");
                });

        queue.add(stringRequest);
    }

    // vị trí tại null đại diện cho 1 JsonObject muốn truyền vào khi POST, PUT
    private void getJsonArrayRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    Log.d("id", "get id JsonObject: " + jsonObject.getString("id"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            Log.d("jsonArray", "getJsonObject: " + response);
        }, error -> Log.d("jsonArray", "Failed to get info"));

        queue.add(jsonArrayRequest);
    }

    private void getJsonObject(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_jsonObject, null,
                response -> {
                    try {
                        textView.setText(response.toString());
                        Log.d("jsonObject", "getJsonObject: " + response.getString("id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("jsonObject", "Failed to get info"));

        queue.add(jsonObjectRequest);
    }
}