package com.vantu.databinding.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.vantu.databinding.R;
import com.vantu.databinding.databinding.ItemListUserBinding;
import com.vantu.databinding.model.User;

import java.util.List;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.MyViewHolder> {

    private List<User> users;
    private OnItemClickListener onItemClickListener;

    public ListUserAdapter(List<User> users, OnItemClickListener onItemClickListener){
        this.users = users;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListUserBinding itemListUserBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_list_user, parent, false);
        return new MyViewHolder(itemListUserBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserAdapter.MyViewHolder holder, int position) {
        holder.setBinding(users.get(position), position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // việc dùng ObservableField để đảm bảo khi có thay đổi ữ liệu thì bên UI bắt được, nếu gán cứng như thường thì dữ liệu chỉ đc cập nhật 1 lần ngay lần tạo đầu tiên
        public ObservableField<String> stt = new ObservableField<>();
        public ObservableField<String> firstName = new ObservableField<>();
        public ObservableField<String> lastName = new ObservableField<>();

        // itemListUserBinding được tự động sinh ra bởi Data Binding từ file layout XML có sử dụng <layout>, là instance đại diện cho item_list_user
        private ItemListUserBinding itemListUserBinding;

        private OnItemClickListener onItemClickListener;

        // mặc định RecyclerView.ViewHolder cần View truyền vào nhưng vì dùng binding nên truyền thẳng layoutBinding, sao đó dùng getRoot để lấy view gốc của layout (chỉ "Layout" view cha bọc toàn bộ view con trong item_list_user), từ đó có thể binding trục tiếp mà ko cần findViewById
        public MyViewHolder(@NonNull ItemListUserBinding itemView, OnItemClickListener onItemClickListener) {
            super(itemView.getRoot());
            this.itemListUserBinding = itemView;
            this.onItemClickListener = onItemClickListener;

            itemView.getRoot().setOnClickListener(this);
        }

        public void setBinding(User user, int position) {
            // get/set cho biến viewHolder trong item_list_user.xml
            if (itemListUserBinding.getViewHolder() == null) {
                itemListUserBinding.setViewHolder(this);
            }
            stt.set(String.valueOf(position));
            firstName.set(user.getFirstName());
            lastName.set(user.getLastName());
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.itemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }
}
