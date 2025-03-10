package com.vantu.recyclerviewadvance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vantu.recyclerviewadvance.R;
import com.vantu.recyclerviewadvance.model.User;

import java.util.List;

// RecyclerView.Adapter hỗ trợ nhiều loại dữ liệu khác nhau (Multiple View Types).
// Cụ thể, RecyclerView có thể hiển thị ba loại dữ liệu TEXT, IMAGE, USER
public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Object> mObjects;
    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    public static final int USER = 2;

    public CustomAdapter(Context context, List<Object> objects) {
        mContext = context;
        mObjects = objects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        switch (viewType) {
            case TEXT:
                View itemView0 = li.inflate(R.layout.row_text, parent, false);
                return new TextViewHolder(itemView0);
            case IMAGE:
                View itemView1 = li.inflate(R.layout.row_image, parent, false);
                return new ImageViewHolder(itemView1);
            case USER:
                View itemView2 = li.inflate(R.layout.row_user, parent, false);
                return new UserViewHolder(itemView2);
            default:
                break;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TEXT:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.tvText.setText(mObjects.get(position).toString());
                break;
            case IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.imvImage.setImageResource((int) mObjects.get(position));
                break;
            case USER:
                User user = (User) mObjects.get(position);
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.tvName.setText(user.getName());
                userViewHolder.tvAddress.setText(user.getAddress());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    // xác định kiểu dữ liệu của từng phần tử trong listObject
    @Override
    public int getItemViewType(int position) {
        if (mObjects.get(position) instanceof String)
            return TEXT;
        else if (mObjects.get(position) instanceof Integer)
            return IMAGE;
        else if (mObjects.get(position) instanceof User)
            return USER;
        return -1;
    }

    // xử lý văn bản
    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView tvText;

        public TextViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, mObjects.get(getAdapterPosition()).toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // xử lý ảnh
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imvImage = (ImageView) itemView.findViewById(R.id.imv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, mObjects.get(getAdapterPosition()).toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // xử lý user
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvAddress;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = (User) mObjects.get(getAdapterPosition());
                    Toast.makeText(mContext, user.getName() + ", " + user.getAddress(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

