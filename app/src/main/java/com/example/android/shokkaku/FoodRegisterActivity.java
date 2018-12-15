package com.example.android.shokkaku;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FoodRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_register);
        //ActionBarを取得
        ActionBar actionBar = getSupportActionBar();
        //ActionBarの「戻る」メニューをオプションメニューに追加
        actionBar.setDisplayHomeAsUpEnabled(true);
        //spinnerの要素を生成
        setSpinner(FoodRegisterActivity.this);

    }

    /**
     * spinnerに値をセット
     */
    private void setSpinner(Context context){
        Spinner spShop = (Spinner) findViewById(R.id.spShopName);
        List<String> shopNames = getShopName(context);
        Log.d("setSpinner", "shopNames:" + shopNames.size());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(FoodRegisterActivity.this,
                android.R.layout.simple_spinner_item,shopNames);
        Log.d("FoodRegisterActivity", "" + FoodRegisterActivity.this);
        //選択肢のリストが表示されたときに使用するレイアウトを指定する
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //アダプターをスピナーに適応
        spShop.setAdapter(arrayAdapter);

    }
/**
 * spinnerの要素をDBから店名を取得
 */
    public List<String> getShopName(Context context) {
        List<String> shopNameList = new ArrayList<>();
        //データベースヘルパーオブジェクトを作成
        DatabaseHelper helper = new DatabaseHelper(context);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        //読み込み専用
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            String sqlGetShopName = "SELECT shopName FROM shopName_t;";

            //SQL文実行
            Cursor cursor = db.rawQuery(sqlGetShopName,null);
            Log.d("FoodRegisterActivity","取得できた行の数" + cursor.getCount());
            //データがなかった時の初期値を用意
            String shopName = "";
            //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得
            while (cursor.moveToNext()) {
                shopName = cursor.getString(cursor.getColumnIndex("shopName"));
                Log.d("getShopName", "shopName: " + shopName);
                shopNameList.add(shopName);
            }
            Log.d("getShopName","格納数 " + shopNameList.size());
            return shopNameList;
        }finally {
            db.close();
        }
    }

    /**
     * オプションメニュー選択時の処理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //選択されたメニューidの取得
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home://R値はSDKで用意されているので固定
                //戻るメニューを押されたときの処理
                finish();
        }
        //親クラスの同名メソッドを呼び出し。その戻り値を返却
        return super.onOptionsItemSelected(item);
    }

}
