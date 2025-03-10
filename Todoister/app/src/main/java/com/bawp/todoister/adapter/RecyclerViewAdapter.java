package com.bawp.todoister.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Task> tasks;
    private final OnTodoClickListener onTodoClickListener;

    public RecyclerViewAdapter(List<Task> tasks, OnTodoClickListener onTodoClickListener) {
        this.tasks = tasks;
        this.onTodoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);

        return new ViewHolder(view, this.onTodoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        String duedate = Utils.formatDate(task.getDueDate());

        // override set màu ở 2 trạng thái enabled và disabled
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                // disabled
                new int[]{-android.R.attr.state_enabled},
                // enabled
                new int[]{android.R.attr.state_enabled}
        },
                new int[]{
                        // disabled
                        Color.LTGRAY,
                        // enabled
                        Utils.priorityColor(task)
                }
        );

        holder.task.setText(task.getTask());
        holder.todayChip.setText(duedate);

        holder.todayChip.setTextColor(colorStateList);
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // dùng AppCompat... để đảm bảo vẫn dùng tốt trên phiên bản android cũ và mới, còn có thể tùy chỉnh màu sắc trong theme.xml
        private AppCompatRadioButton radioButton;
        private AppCompatTextView task;
        private Chip todayChip;
        private OnTodoClickListener onTodoClickListener;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView, OnTodoClickListener onTodoClickListener) {
            super(itemView);

            this.radioButton = itemView.findViewById(R.id.todo_radio_button);
            this.task = itemView.findViewById(R.id.todo_row_todo);
            this.todayChip = itemView.findViewById(R.id.todo_row_chip);

            this.onTodoClickListener = onTodoClickListener;

            // set event click cho itemView nhưng hàm xử lý là onClick của ViewHolder
            // itemView đại diện cho layout (todo_row.xml) nên khi click radioButton thì ko bắt đc event -> set riêng
            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            Task task = tasks.get(getAdapterPosition());
            if (viewId == R.id.todo_row_layout) {
                // gán trước taskId, task cho phương thức onTodoClick để khi từ bất kỳ class khác implements interface này thì chỉ việc truyền OnTodoClickListener của chính class đó đang triển khai vào là dùng đc
                onTodoClickListener.onTodoClick(task);
            } else if (viewId == R.id.todo_radio_button) {
                onTodoClickListener.onTodoRadioButtonClick(task);
            }
        }
    }
}
