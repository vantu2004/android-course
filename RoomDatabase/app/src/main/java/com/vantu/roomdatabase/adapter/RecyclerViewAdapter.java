package com.vantu.roomdatabase.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vantu.roomdatabase.R;
import com.vantu.roomdatabase.model.Contact;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // dùng để gán position cho event click của mỗi itemView
    private OnContactClickListener onContactClickListener;
    // vì hàm getAllContacts trong ContactViewModel trả về 1 list dạng liveData nên contactList này cx phải dạng liveData
    private List<Contact> contactList;
    private Context context;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
    }

    // tạo ra một ViewHolder mới để hiển thị một mục (item) trong danh sách
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // dùng LayoutInflater để "inflate" (tạo) view từ layout XML
        // view đại diện cho layout chứa các thành phần con như textview, button,...
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_row, parent, false);

        // truyền view vừa được tạo để tạo 1 viewholder, viewholder giữ tham chiếu để tương tác với các thành phần bên trong view như textview, button,...
        return new ViewHolder(view, onContactClickListener);
    }

    // Lấy dữ liệu tương ứng với vị trí position từ nguồn dữ liệu.
    // Gán dữ liệu này cho các view trong ViewHolder (ví dụ: setText cho TextView, setImage cho ImageView, v.v.).
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = Objects.requireNonNull(contactList.get(position));
        holder.name.setText(contact.getName());
        holder.occupation.setText(contact.getOccupation());
    }

    // trả về số lượng mục dữ liệu mà adapter sẽ hiển thị
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // giữ các tham chiếu đến các widget trong view đã được truyền vào
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnContactClickListener onContactClickListener;
        public TextView name;
        public TextView occupation;

        // vì class này ko dùng được findViewById() trực tiếp nên phải thông qua itemView
        // trong hàm onCreateViewHolder trả về 1 ViewHolder với tham số truyền vào là View, itemView đại diện cho view này
        public ViewHolder(@NonNull View itemView, OnContactClickListener onContactClickListener) {
            super(itemView);
            this.onContactClickListener = onContactClickListener;
            this.name = itemView.findViewById(R.id.textView_rowName);
            this.occupation = itemView.findViewById(R.id.textView_rowOccupation);

            // this đại diện cho instance của ViewHolder, ViewHolder lại implements View.OnClickListener => có hàm xử lý onClick. Việc truyền this vào nghĩa là set event click cho itemView nhưng hàm xử lý là của ViewHolder
            itemView.setOnClickListener(this);
        }

        // truyền position cho mỗi itemView
        @Override
        public void onClick(View view) {
            onContactClickListener.OnContactClick(getAdapterPosition());
        }
    }

    public  interface OnContactClickListener{
        void OnContactClick(int position);
    }
}
