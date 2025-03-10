package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText enterTodo;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    private Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);

        // phương thức setOnClickListener yêu cầu tham số truyền vào phải có dạng View.OnClickListener, class đang triển khai View.OnClickListener -> hợp lệ
        // set event click cho view nhưng hàm xử lý là onClick của BottomSheetFragment
        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    // được gọi khi khi activity/fragment hiện lại trừ trường hợp bị hủy hoàn toàn (onDestroy())
    @Override
    public void onResume() {
        super.onResume();

        Task selectedTask = sharedViewModel.getSelectedItem().getValue();
        if (selectedTask != null) {
            isEdit = sharedViewModel.isEdit();
            enterTodo.setText(selectedTask.getTask());
        }
    }

    // chạy sau khi onCreateView chạy xong và chạy trc hàm onResume(), onCreateView tạo view (UI), onViewCreated xử lý logic
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // requireActivity() là một phương thức của Fragment, giúp lấy ra Activity chứa Fragment đó.
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // mặc định calendarGroup là GONE, còn bên dưới là phải click
        calendarButton.setOnClickListener(v -> {
            // khi click button thì ẩn bàn phím
            Utils.hideSoftKeyboard(v);

            calendarGroup.setVisibility(calendarGroup.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        priorityButton.setOnClickListener(v -> {
            // khi click button thì ẩn bàn phím
            Utils.hideSoftKeyboard(v);

            priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
                    selectedButtonId = checkedId;
                    selectedRadioButton = view.findViewById(selectedButtonId);

                    if (selectedRadioButton.getId() == R.id.radioButton_high) {
                        priority = Priority.HIGH;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_med) {
                        priority = Priority.MEDIUM;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_low) {
                        priority = Priority.LOW;
                    } else {
                        priority = Priority.LOW;
                    }
                } else {
                    priority = Priority.LOW;
                }
            });
        });

        // giống vs js, month bắt đầu từ 0 nên phải +1
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
        });

        saveButton.setOnClickListener(v -> {
            String t = enterTodo.getText().toString();
            if (!TextUtils.isEmpty(t) && dueDate != null && priority != null) {
                if (isEdit) {
                    Task task = sharedViewModel.getSelectedItem().getValue();
                    assert task != null;
                    task.setTask(t);
                    task.setDateCreated(Calendar.getInstance().getTime());
                    task.setDueDate(dueDate);
                    task.setPriority(priority);

                    TaskViewModel.updateTask(task);

                    sharedViewModel.setEdit(false);
                } else {
                    Task task = new Task(t, priority, dueDate, Calendar.getInstance().getTime(), false);
                    TaskViewModel.insertTask(task);
                }

                // ẩn fragment
                if (this.isVisible()) {
                    this.dismiss();
                }
            } else {
                Toast.makeText(requireContext(), "Task or Duedate or Priority not empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // implements View.OnClickListener cho class là để bắt event của các view con (view đã setOnclick) bên trong (mục đích để cùng xử lý trong hàm onCLick)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        // sau mỗi lần cộng thì biến calendar sẽ cập nhật lại vs giá trị mới -> reset cho mỗi lần click
        calendar = Calendar.getInstance();

        // phương thức add() cho phép lấy ngày hiện tại + số ngày, kết quả cập nhật lại vào calendar ko tạo biến mới
        if (id == R.id.today_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 0);
        } else if (id == R.id.tomorrow_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        dueDate = calendar.getTime();
        Log.d("time", "onClick: " + dueDate);
    }
}