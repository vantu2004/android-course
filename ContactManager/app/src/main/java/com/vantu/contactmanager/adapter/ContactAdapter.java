package com.vantu.contactmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vantu.contactmanager.R;
import com.vantu.contactmanager.model.Contact;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Contact> contactList;
    //tạo Constructors
    public ContactAdapter(Context context, int layout, List<Contact> contactList) {
        this.context = context;
        this.layout = layout;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return  this.contactList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // khởi tạo viewholder
        ViewHolder viewHolder;
        // lấy context
        if (view==null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // gọi view chứa layout
            view = inflater.inflate(layout,null);
            //ánh xạ view
            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) view.findViewById(R.id.textView_name);
            viewHolder.textView_contact = (TextView)  view.findViewById(R.id.textView_contact);

            view.setTag(viewHolder);
        }else{
            // Nếu view đã được tạo trước đó, lấy lại ViewHolder từ view.getTag()
            viewHolder= (ViewHolder) view.getTag();
        }

        //gán giá trị
        Contact contact = contactList.get(i);
        viewHolder.textView_name.setText(contact.getPhoneNumber());
        viewHolder.textView_contact.setText(contact.getName());

        //trả về view
        return view;
    }

    //tạo class viewholder
    private class ViewHolder{
        TextView textView_name;
        TextView textView_contact;
    }
}
