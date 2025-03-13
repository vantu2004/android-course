package com.vantu.gpsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATES_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int PERMISSION_FINE_LOCATION = 99;
    private TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address, tv_countOfCrumbs;
    private Switch sw_gps, sw_locationsupdates;
    private Button btn_newWayPoint, btn_showWayPointList, btn_showMap;

    // lớp tiện ích của GoogleApi giúp lấy vị trí, cung cấp phương thức sử dụng để thực hiện
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private List<Location> savedLocation;

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

        initViews();

        createLocationRequest();

        createLocationCallback();

        // set độ ưu tiên chính xác chứ ko phải set thời gian cập nhật
        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int priority = sw_gps.isChecked() ?
                        // cập nhật từ GPS -> nhanh, chính xác cao (khoảng 5s/lần)
                        Priority.PRIORITY_HIGH_ACCURACY :
                        // cập nhật từ tháp sóng/wifi -> chậm, chính xác thấp hơn (khoảng 30s/lần)
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY;

                locationRequest = new LocationRequest
                        // việc đặt .Builder(priority, 5000) chỉ mang tính tương đối chứ không tuyệt đối là 5 giây.
                        .Builder(priority, 5000)
                        .setMinUpdateIntervalMillis(1000)
                        .build();

                tv_sensor.setText(sw_gps.isChecked() ? "Using GPS Sensor" : "Using Towers + WIFI");
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()) {
                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }
            }
        });

        // lấy location và lưu vào list toàn cục (singleton) bên class MyApplication
        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getApplicationContext() trả về applicaiton context nhưng có thể ép kiểu cụ thể
                MyApplication myApplication = (MyApplication) getApplicationContext();
                savedLocation = myApplication.getLocation();
                savedLocation.add(currentLocation);

                updateUIValues(currentLocation);
            }
        });

        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowSavedLocationList.class);
                startActivity(intent);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        // thời điểm ban đầu cần yêu cầu cấp quyền, nều cấp quyền r thì dùng getLastLocation() để lấy vị trí đã lưu trc đó và xuất UI
        updateGps();
    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location is NOT being tracked");
        tv_lat.setText("Not tracking location.");
        tv_lon.setText("Not tracking location.");
        tv_altitude.setText("Not tracking location.");
        tv_accuracy.setText("Not tracking location.");
        tv_speed.setText("Not tracking location.");
        tv_address.setText("Not tracking location.");
        tv_sensor.setText("Not tracking location.");

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        Toast.makeText(this, "Stop tracking.", Toast.LENGTH_SHORT).show();
    }

    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // requestLocationUpdates() cập nhật ví trí liên tục và chính xác còn getLastLocation lấy 1 lần vị trí cuối cùng mà hệ thống lưu và có thể là dữ liệu cũ
            // requestLocationUpdates() tự động yêu cầu locationRequest, locationCallback, khi yêu cầu locationCallback thì tự dộng gọi onLocationResult(), hàm này chịu trách nhiệm lấy vị trí mới và update UI
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

            Toast.makeText(this, "Start tracking.", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm này xử lý kết quả yêu cầu quyền truy cập vị trí sau khi người dùng chập nhận hoặc từ chối quyền.
    // requetsCode mã yêu cầu cấp quyền, permissions là danh sách quyền, grantResults là kết quả chọn của user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGps();
            } else {
                Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }

    private void updateGps() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        // lấy vị trí hiện tại và cập nhật UI
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);
                }
            });
        }
        // yêu cầu user cấp quyền
        else {
            // Nếu chưa có quyền, ứng dụng sẽ yêu cầu quyền truy cập vị trí từ người dùng.
            // hàm requestPermission nhận 2 tham số là danh sách quyền và mã số quyền yêu cầu
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }
    }
    
    private void updateUIValues(Location location) {
        currentLocation = location;

        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not available");
        }

        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("Not available");
        }

        // Geocoder để lấy địa chỉ dựa trên tọa độ (vĩ độ, kinh độ) của vị trí hiện tại.
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            assert addresses != null;
            tv_address.setText(addresses.get(0).getAddressLine(0));

        } catch (Exception e) {
            tv_address.setText("Unable to get street address");
            throw new RuntimeException(e);
        }

        // show the number of waypoint saved
        MyApplication myApplication = (MyApplication) getApplicationContext();
        savedLocation = myApplication.getLocation();
        tv_countOfCrumbs.setText(String.valueOf(savedLocation.size()));

    }

    private void initViews() {
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        tv_countOfCrumbs = findViewById(R.id.tv_countOfCrumbs);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointList = findViewById(R.id.btn_showWayPointList);
        btn_showMap = findViewById(R.id.btn_showMap);
    }

    // thiết lập tần suất cập nhật vị trí, độ ưu tiên của GPS hoặc mạng
    private void createLocationRequest() {
        locationRequest = new LocationRequest
                // cập nhật 30s/lần
                .Builder(DEFAULT_UPDATES_INTERVAL * 1000)
                // vị trí thay đổi -> dữ liệu thay đổi -> cập nhật 5s/lần
                .setMinUpdateIntervalMillis(FAST_UPDATE_INTERVAL * 1000)
                // Độ chính xác cân đối
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .build();
    }

    private void createLocationCallback() {
        // LocationCallback là một callback để nhận cập nhật vị trí mới nhất khi thiết bị thay đổi vị trí.
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // Lấy vị trí mới nhất từ danh sách các vị trí được trả về trong locationResult.
                // mục đích lấy vị trí để đăng ký cho locationCallback -> dùng cho requestLocationUpdates() để update vị trí liên tục
                Location location = locationResult.getLastLocation();
                assert location != null;
                updateUIValues(location);
            }
        };
    }
}