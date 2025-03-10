package com.bawp.todoister;

import android.os.Bundle;

import com.bawp.todoister.adapter.OnTodoClickListener;
import com.bawp.todoister.adapter.RecyclerViewAdapter;

import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private TaskViewModel taskViewModel;
    private SharedViewModel sharedViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private BottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getApplication trả về dạng như this toàn cục (toàn chương trình), getApplicationContext trả về dạng this của class
        // dùng ViewModelProvider để tránh khi activity bị hủy hoặc fragment thay đổi cấu hình thì dữ liệu ko mất hoặc biến taskViewModel phải tạo lại
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(TaskViewModel.class);

        // tạo 1 viewmodel mới có tác dụng truyền data giữa mainActivity với bottomFragment vì viewModel tồn tại lâu hơn ngay cả khi activity/fragment bị hủy/thay đổi
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        // BottomSheetBehavior là behavior dùng để điều khiển BottomSheet, .from giúp truy cập và cho phép thao tác với layout của BottomSheet
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        // set state ban đầu là ẩn hoàn toàn BottomSheet
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                recyclerViewAdapter = new RecyclerViewAdapter(tasks, MainActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                    showBottomSheetDialog();
                }
            );
    }

    // show() sẽ thêm BottomSheetFragment vào FragmentManager, làm cho nó xuất hiện trên màn hình.
    // Vòng đời BottomSheetFragment bắt đầu → onCreateView() và onViewCreated() của BottomSheetFragment sẽ chạy.
    private void showBottomSheetDialog() {
        // getSupportFragmentManager() thuộc AppCompatActivity dùng để quản lý fragment
        // tag trong Fragment là một chuỗi định danh duy nhất giúp quản lý và tìm kiếm Fragment trong FragmentManager. getTag() trả về giá trị tag được gán khi Fragment được thêm vào FragmentManager.
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ViewModel có tác dụng lưu dữ liệu trong RAM và ko bị hủy trừ khi activity/fragment bị hủy hoàn toàn (gọi hàm onDestroy()) hoặc đóng ứng dụng, việc dùng sharedViewModel lưu task để cho bottomFragment có thể dùng đc task
    // khi gọi showBottomSheetDialog() thì BottomSheetFragment đc thêm vào FragmentManager -> life cycle bắt đầu -> chạy 2 hàm onCreateView/onViewCreated -> logic bên trong onViewCreated đc thực thi
    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setEdit(true);
        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {
        TaskViewModel.deleteTask(task);
    }

}