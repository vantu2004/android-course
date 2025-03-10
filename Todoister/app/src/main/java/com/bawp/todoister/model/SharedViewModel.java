package com.bawp.todoister.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// lớp này có tác dụng chia sẻ dữ liệu giữa các fragment. ViewModel đảm bảo duy trì dữ liệu ngay cả khi vòng đời của Activity hoặc Fragment thay đổi (ví dụ khi xoay màn hình).
public class SharedViewModel extends ViewModel {
    // LiveData chỉ read, MutableLiveData gồm read/write
    private final MutableLiveData<Task> selectedItem = new MutableLiveData<>();

    private boolean isEdit;

    public void selectItem(Task task) {
        selectedItem.setValue(task);
    }

    public LiveData<Task> getSelectedItem() {
        return selectedItem;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isEdit() {
        return isEdit;
    }
}
