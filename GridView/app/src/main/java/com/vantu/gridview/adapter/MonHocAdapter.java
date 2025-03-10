package com.vantu.gridview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vantu.gridview.R;
import com.vantu.gridview.model.MonHoc;

import java.util.List;

public class MonHocAdapter extends BaseAdapter {
    //khai báo
    private Context context;
    private int layout;
    private List<MonHoc> monHocList;

    public MonHocAdapter(Context context, int layout, List<MonHoc> monHocList) {
        this.context = context;
        this.layout = layout;
        this.monHocList = monHocList;
    }

    @Override
    public int getCount() {
        return monHocList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //khởi tạo viewholder
        ViewHolder viewHolder;
        //lấy context
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //gọi view chứa layout
            convertView = inflater.inflate(layout,null);
            //ánh xạ view
            viewHolder = new ViewHolder();
            viewHolder.textName = (TextView) convertView.findViewById(R.id.textView_name);
            viewHolder.textDesc = (TextView)  convertView.findViewById(R.id.textView_desc);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //gán giá trị
        MonHoc monHoc = monHocList.get(position);
        viewHolder.textName.setText(monHoc.getName());
        viewHolder.textDesc.setText(monHoc.getDesscription());
        viewHolder.image.setImageResource(monHoc.getImage());

        //trả về view
        return convertView;
    }

    //tạo class viewholder
    private class ViewHolder{
        TextView textName,textDesc;
        ImageView image;
    }
}
