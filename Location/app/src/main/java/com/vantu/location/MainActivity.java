package com.vantu.location;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import android.Manifest;

public class MainActivity extends AppCompatActivity implements LocationListener {

    // FusedLocationProviderClient là API trong Google Play Services giúp lấy vị trí thiết bị một cách hiệu quả và chính xác hơn so với LocationManager.
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayList<String> permissionToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionToRejected = new ArrayList<>();

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

        // context của activity hiện tại truy cập dịch vụ bằng cách dùng lớp tiện ích LocationServices để lấy đối tượng FusedLocationProviderClient truy cập vị trí
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionToRequest = permissionToRequest(permissions);

    }

    // duyệt danh sách các quyền mà ứng dụng muốn yêu cầu và lấy những quyền ch đc cấp phép
    private ArrayList<String> permissionToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String permission : wantedPermissions){
            if (!hasPermission(permission)){
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

    // hàm onResume() chạy ngay sau onStart() dùng để khôi phục trạng thái Activity, đăng ký listener, cập nhật dữ liệu, khởi động lại tiến trình tạm dừng trong onPause().
    // hàm onPostResume() chạy ngay sau onResume(), khi Activity đã hoàn toàn ở foreGround (lúc này Activity đã hiển thị và user có thể tương tác), xử lý công việc sau khi giao diện đã sẵn sàng, thường dùng để kiểm tra hoặc cập nhật UI.
    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkGooglePlayServices();
    }

    private void checkGooglePlayServices() {
        // kiểm trả Google Play Services có sẵn trên thiết bị hay ko
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        // nếu ko thì xử lý lỗi, tạo hộp thoại dialog, nếu người dùng hủy hộp thoại thì gọi onCancel()
        if (errorCode != ConnectionResult.SUCCESS){
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

}