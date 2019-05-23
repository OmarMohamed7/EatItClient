package com.example.rooot.eatit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.rooot.eatit.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "EatItDB.db";
    private static final int DB_VERSION = 1;


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<Order> getCarts(){

        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName" , "ProductId" , "Quantity" , "Price"};
        String sqlTable = "OrderDetails";

        queryBuilder.setTables(sqlTable);
        Cursor c = queryBuilder.query(db , sqlSelect , null , null , null, null, null);

        final List<Order> result = new ArrayList<>();

        if(c.moveToFirst()){

            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                                     c.getString(c.getColumnIndex("ProductName")),
                                     c.getString(c.getColumnIndex("Quantity")),
                                     c.getString(c.getColumnIndex("Price"))));
            }while(c.moveToNext());
        }

        return result;
    }

    public void addToCart(Order order){

        SQLiteDatabase db = getWritableDatabase();

        String query = String.format("INSERT INTO OrderDetails(ProductId,ProductName,Quantity,Price) VALUES ('%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice());

        db.execSQL(query);
    }

    public void CleanCart(){

        SQLiteDatabase db = getWritableDatabase();

        String query = String.format("DELETE FROM OrderDetails");

        db.execSQL(query);
    }

    public void removeItem(Order order ){

        SQLiteDatabase db = getReadableDatabase();

        String deleteQuery = String.format("DELETE FROM OrderDetails WHERE ProductId='%s';" , order.getProductId());

        db.execSQL(deleteQuery);

    }
}
