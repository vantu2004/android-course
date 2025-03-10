package com.vantu.contactmanager.data;

import static com.vantu.contactmanager.util.Util.DATABASE_TABLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vantu.contactmanager.R;
import com.vantu.contactmanager.model.Contact;
import com.vantu.contactmanager.util.Util;

import java.util.ArrayList;
import java.util.List;
// SQLiteOpenHelper: đối tượng dùng để tạo, nâng cấp, đóng mở kết nối CSDL
// SQLiteDatabase: đối tượng dùng để thực thi các câu lệnh SQL trên một CSDL
public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY, "
                + Util.KEY_NAME + " TEXT NOT NULL, "
                + Util.KEY_PHONE_NUMBER + " TEXT NOT NULL)";
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.drop_table);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_TABLE});

        // recreate table
        onCreate(db);
    }

    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy thông tin liên hệ với ID cụ thể
        Cursor cursor = db.query(Util.DATABASE_TABLE,
                new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_PHONE_NUMBER},
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        // Nếu có dữ liệu, tạo đối tượng Contact từ dữ liệu trong cơ sở dữ liệu
        if (cursor != null) {
            cursor.moveToFirst(); // Di chuyển con trỏ đến dòng đầu tiên
            Contact contact = new Contact();
            contact.setId(Integer.parseInt(cursor.getString(0)));
            contact.setName(cursor.getString(1));
            contact.setPhoneNumber(cursor.getString(2));

            cursor.close();
            db.close();
            return contact;
        } else {
            db.close();
            return null;
        }
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn tất cả các cột trong bảng CONTACTS
        Cursor cursor = db.query(Util.DATABASE_TABLE,
                new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_PHONE_NUMBER},
                null, null, null, null, null);

        // Duyệt qua tất cả các dòng trong cursor
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Tạo một đối tượng Contact mới từ dữ liệu trong dòng hiện tại
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Tạo ContentValues để lưu trữ các giá trị cần cập nhật
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        // Cập nhật dữ liệu trong bảng CONTACTS, với điều kiện là ID khớp
        int rowsAffected = db.update(Util.DATABASE_TABLE, values,
                Util.KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

        // Đóng cơ sở dữ liệu
        db.close();

        // Trả về số dòng bị ảnh hưởng (nếu >= 1 thì cập nhật thành công)
        return rowsAffected;
    }

    public int deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Xóa một dòng khỏi bảng CONTACTS
        int rowsAffected = db.delete(Util.DATABASE_TABLE, Util.KEY_ID + " = ?", new String[]{String.valueOf(contact.getId())});

        db.close();

        // Trả về số dòng bị ảnh hưởng (nếu >= 1 thì xóa thành công)
        return rowsAffected;
    }

    public int getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn để đếm tổng số hàng trong bảng
        String countQuery = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

}
