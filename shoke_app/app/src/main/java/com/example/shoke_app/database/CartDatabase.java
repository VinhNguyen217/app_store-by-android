package com.example.shoke_app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.shoke_app.model.Cart;

import java.util.ArrayList;

public class CartDatabase extends SQLiteOpenHelper {
    public static final String TableName = "Cart";
    public static final String Id = "id";
    public static final String UserName = "username";
    public static final String IdProduct = "idProduct";
    public static final String Count = "count";
    public static final String Price = "price";
    public static final String TimeCreate = "timeCreate";

    public CartDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public CartDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public CartDatabase(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TableName + "("
                + Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserName + " TEXT,"
                + IdProduct + " TEXT,"
                + Count + " INTEGER,"
                + Price + " INTEGER ,"
                + TimeCreate + " TEXT)";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TableName);
        onCreate(db);
    }

    // Get the list of carts by username
    public ArrayList<Cart> getAllCarts(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TableName + " WHERE " + UserName + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null)
            while (cursor.moveToNext()) {
                Cart cart = new Cart(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5));
                list.add(cart);
            }
        return list;
    }

    // Add item cart
    public void addCart(String userName, String idProduct, int count, int price, String timeCreate) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("INSERT INTO " + TableName + " VALUES(null,'" + userName + "','" + idProduct + "'," + count + "," + price + ",'" + timeCreate + "')");
    }

    // Delete an item cart by id
    public void Delete(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + TableName + " WHERE " + Id + " = " + id + "");
    }

    // Update count for cart by id
    public void UpdateCart(int count, int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TableName + " SET " + Count + " = " + count + " WHERE " + Id + " = " + id + "");
    }

    // Remove all of cart
    public void ClearCart() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + TableName + "");
    }

    // Check the product already exists in the cart
    public boolean checkData(String userName, String idProduct) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + UserName + " = '" + userName + "' AND " + IdProduct + " = '" + idProduct + "'";
        Cursor data = db.rawQuery(sql, null);
        return data.getCount() > 0;
    }


}
