package com.vantu.location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;

import android.Manifest;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int INTERVAL_MILLIS = 5000;
    public static final int MIN_UPDATE_INTERVAL_MILLIS = 1000;
    public static final int REQUEST_CODE = 1;

    // FusedLocationProviderClient là API trong Google Play Services giúp lấy vị trí thiết bị một cách hiệu quả và chính xác hơn so với LocationManager.
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayList<String> permissionToRequest;
    private final ArrayList<String> permissions = new ArrayList<>();
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView textView_location;

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

        textView_location = findViewById(R.id.textView_location);

        // context của activity hiện tại truy cập dịch vụ bằng cách dùng lớp tiện ích LocationServices để lấy đối tượng FusedLocationProviderClient truy cập vị trí
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        create_lRequest_lCallback();

        // độ chính xác của COARSE thấp hơn FINE
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionToRequest = permissionToRequest(permissions);


    }

    private void create_lRequest_lCallback() {
        locationRequest = new LocationRequest
                // cập nhật 5s/lần
                .Builder(INTERVAL_MILLIS)
                // vị trí thay đổi -> dữ liệu thay đổi -> cập nhật 5s/lần
                .setMinUpdateIntervalMillis(MIN_UPDATE_INTERVAL_MILLIS)
                // Độ chính xác cân đối
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        // việc tạo instance của locationCallback như này để đảm bảo khi tạo và hủy là cùng 1 callback
        locationCallback = new LocationCallback() {
            // fusedLocationProviderClient liên tục lắng nghe sự thay đổi vị trí theo cấu hình của locationRequest. Khi thiết bị di chuyển hoặc hệ thống có vị trí mới, onLocationResult() sẽ tự động được gọi, dù chỉ gọi requestLocationUpdates() một lần.
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null)
                    textView_location.setText(String.format("Latitude: %s, Longitude: %s", location.getLatitude(), location.getLongitude()));
            }
        };
    }

    // duyệt danh sách các quyền mà ứng dụng muốn yêu cầu và lấy những quyền ch đc cấp phép
    private ArrayList<String> permissionToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String permission : wantedPermissions) {
            if (!hasPermission(permission)) {
                result.add(permission);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        // checkSelfPermission(permission): Kiểm tra xem quyền đã được cấp chưa.
        // PackageManager.PERMISSION_GRANTED: Nếu quyền đã được cấp, trả về true, nếu chưa có quyền, trả về false.
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkGooglePlayServices() {
        // kiểm tra Google Play Services có sẵn trên thiết bị hay ko
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        // nếu ko thì xử lý lỗi, tạo hộp thoại dialog, nếu người dùng hủy hộp thoại thì gọi onCancel()
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(MainActivity.this, "No services", Toast.LENGTH_SHORT).show();
                }
            });
            assert errorDialog != null;
            errorDialog.show();
        } else {
            Toast.makeText(MainActivity.this, "All is good", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkSelfPermission()) {
            getLastLocation();
        } else {
            // 2 tham số cuối là danh sách quyền cần cấp và mã quyền
            // yêu cầu cấp tất cả quyền trong danh sách truyền vào
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    // hàm onResume() chạy ngay sau onStart() dùng để khôi phục trạng thái Activity, đăng ký listener, cập nhật dữ liệu, khởi động lại tiến trình tạm dừng trong onPause().
    // hàm onPostResume() chạy ngay sau onResume(), khi Activity đã hoàn toàn ở foreGround (lúc này Activity đã hiển thị và user có thể tương tác), xử lý công việc sau khi giao diện đã sẵn sàng, thường dùng để kiểm tra hoặc cập nhật UI.
    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkGooglePlayServices();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    // xử lý kết quả của yêu cầu cấp quyền (permissions) mà ứng dụng đã yêu cầu từ người dùng.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            // trong onCreate() khởi tạo giá trị những quyền ch cấp phép cho permissionToRequest, sau khi yêu cầu cấp quyền đầu ra, duyệt lại để xử lý kết quả lần nữa
            for (String permission : permissionToRequest) {
                if (!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
            if (!permissionsRejected.isEmpty()) {
                // kiểm tra xem có nên hiển thị lời giải thích trước khi yêu cầu quyền hay không
                // trả về true <=> user ko cấp quyền và ko chọn "ko hỏi lại"
                // trả về false <=> user ko cấp quyền và chọn ko hỏi lại hoặc trước đó chưa đc giải thích
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("These permissions are mandatory to get location")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // chuyển List thành mảng chuỗi, số 0 giúp tạo 1 mảng có kích thước bằng list
                                    // chỉ yêu cầu cấp quyền lại cho những quyền bị từ chối
                                    requestPermissions(permissionsRejected.toArray(new String[0]), REQUEST_CODE);
                                    // startLocationUpdates();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
    }

    private void getLastLocation() {
        if (checkSelfPermission()) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null)
                    textView_location.setText(String.format("Latitude: %s, Longitude: %s", location.getLatitude(), location.getLongitude()));
            });
        }
    }

    private void startLocationUpdates() {
        if (checkSelfPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private boolean checkSelfPermission() {
        // chưa được cấp quyền thì yêu cầu cấp quyền
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}