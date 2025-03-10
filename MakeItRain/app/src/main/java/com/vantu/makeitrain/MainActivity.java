package com.vantu.makeitrain;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

//    private Button makeItRain;
//    private Button showInfo;
    private TextView moneyValue;
    private double moneyCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        makeItRain = findViewById(R.id.buttonMakeItRain);
//        showInfo = findViewById(R.id.buttonShowInfo);


//        click event of button makeItRain and showInfo
//        makeitrain.setonclicklistener(new view.onclicklistener() {
//            @override
//            public void onclick(view v) {
//
//            }
//        });

//        showinfo.setonclicklistener(v -> log.d("showinfo", "log here!"));

//        set color for moneyValue
        // outdated
        // moneyValue.setTextColor(getResources().getColor(R.color.green));


    }

    public void makeItRain(View view) {
        moneyCounter += 1000;

        // Định dạng số tiền với dấu phân cách hàng nghìn và 2 chữ số sau dấu chấm.
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        moneyValue.setText(String.valueOf(numberFormat.format(moneyCounter)));

        //Log.d("MainActivity", "Log here!" + moneyCounter);

        if (moneyCounter > 0 && moneyCounter < 10000){
            setTextviewColor(R.color.black);
        }
        else if (moneyCounter >= 10000 && moneyCounter < 20000){
            setTextviewColor(R.color.violet);
        }
        else {
            setTextviewColor(R.color.green);
        }
    }

    private void setTextviewColor(int colorCode){
        moneyValue.setTextColor(ContextCompat.getColor(MainActivity.this, colorCode));
    }

    public void showInfo(View view) {
        // xuất 1 thông báo tĩnh ngắn gọn trong 1 khoảng tg ngắn
        // MainActivity.this là ngữ cảnh hiện tại
        // R.string.app_info là lời thông báo
        // Toast.LENGTH_SHORT
        Toast.makeText(MainActivity.this, R.string.app_info, Toast.LENGTH_SHORT).show();

        // tạo 1 thông báo tương tác ngắn gọn trong 1 khoảng tg
        Snackbar.make(moneyValue, R.string.app_info, Snackbar.LENGTH_SHORT)
                .setAction("More", (View.OnClickListener) v -> {
                    Log.d("MainActivity", "OnClickListener!");
                })
                .show();
    }

    public void startApp(View view) {
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        moneyValue = findViewById(R.id.moneyValue);
        // set default color
        setTextviewColor(R.color.red);
    }
}