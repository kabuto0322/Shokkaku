package com.example.android.shokkaku;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FoodRegisterActivity extends AppCompatActivity {
    /**
     * 新規店舗名の保存ボタン
     */
    Button _btnShopName;
    EditText _etShopName;
    /**
     * 商品保存ボタン
     */
    Button _btnregisterItem;


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
        //新規店名追加ボタンを取得する
        _btnShopName = findViewById(R.id.btnShopSave);
       //新規店舗入力欄を取得
        _etShopName = findViewById(R.id.entryShopName);
        //商品名と値段を入力するEditTextを取得
        _btnregisterItem = findViewById(R.id.btRegisterItem);


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
     * 新規店舗追加の「保存」ボタンがタップされたときの処理メソッド
     */
    public void onSaveShopButtonClick(View view) {

        String shopName = _etShopName.getText().toString();
        Log.d("out if", shopName);
        //EditTextに何か値が入っていたら
        if (shopName != null && !(shopName.equals(""))) {
            //データベースヘルパーオブジェクトを作成。
            DatabaseHelper helper = new DatabaseHelper(FoodRegisterActivity.this);
            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                //既に登録されている店舗とかぶっていないかの確認用SQL
                String sqlCheck = "SELECT shopName FROM shopName_t " +
                        " WHERE shopName = '" + shopName + "';";

                //SQL文字列を元にプリペアドステートメントを取得。
                SQLiteStatement stmt; //= db.compileStatement(sqlCheck);
                Log.d("店名", shopName);
                // stmt.bindString(1,shopName);
                Log.d("sqlCheck", sqlCheck);
                //SELECT文の実行
                Cursor cursor = db.rawQuery(sqlCheck, null);
                Log.d("cursor.getCount()", "" + cursor.getCount());
                //店名テーブルに同じ名前のものがなければ
                if (cursor.getCount() == 0) {
                    Log.d("店名の重複確認", "重複なし");
                    String sqlInsert = "INSERT INTO shopName_t(shopName) VALUES (?);";
                    //SQL文字列を元にプリペアドステートメントを取得。
                    stmt = db.compileStatement(sqlInsert);
                    stmt.bindString(1, shopName);
                    Log.d("店名", shopName);
                    Log.d("sqlInsert", sqlInsert);
                    //実行
                    stmt.executeInsert();
                    setSpinner(FoodRegisterActivity.this);
                } else {
                    Log.d("context", "" + FoodRegisterActivity.this);
                    //店名の重複をトーストで知らせる
                    Toast.makeText(FoodRegisterActivity.this,
                            "この名前は既に登録されています", Toast.LENGTH_SHORT).show();
                }
            } finally {
                db.close();
            }
            //新規店名をから文字に変更
            _etShopName.setText("");
            //新規店名保存ボタンをタップできないように変更
            //_btnShopName.setEnabled(false);
        }
    }

    /**
     * 商品情報追加のボタンを押したときの処理メソッド
     */
    public void onRegisterItemInfoButtonClick(View view) {
        EditText edItemName = findViewById(R.id.edItemName);
        EditText edItemPrice = findViewById(R.id.itemPrice);
        Spinner spShop = (Spinner) findViewById(R.id.spShopName);
        String itemName = edItemName.getText().toString();
        String itemPrice = edItemPrice.getText().toString();
        String spStr = spShop.getSelectedItem().toString();
        Log.d("商品情報追加", "itemName: " + itemName);
        Log.d("商品情報追加","itemPrice: " +  itemPrice);
        Log.d("商品情報追加", "spStr: "+ spStr);
        //商品名と値段に何らかの値が入っているとき
        if(itemName != null && !(itemName.equals("")) &&
           itemPrice != null && !(itemPrice.equals(""))){
            Log.d("商品情報追加", "ifの中");

            //データベースヘルパーオブジェクトを作成。
            DatabaseHelper helper = new DatabaseHelper(FoodRegisterActivity.this);
            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
            SQLiteDatabase db = helper.getWritableDatabase();
            SQLiteStatement stmt;
            try{
                //まずすでに登録されている商品か確認
                String sqlCheck = "SELECT vegetablesId FROM vegetablesList_t" +
                        " WHERE vegetablesName = '" + itemName + "';";

                //SELECT文の実行
                Cursor cursor = db.rawQuery(sqlCheck, null);
                Log.d("cursor.getCount()", "" + cursor.getCount());
                //該当する野菜が野菜の情報管理用テーブルになければ
                if (cursor.getCount() == 0) {
                    //まず野菜の情報管理用テーブルに登録
                    String sqlInsert = "INSERT INTO vegetablesList_t(vegetablesName)" +
                                        " VALUES (?);";
                    //SQL文字列を元にプリペアドステートメントを取得。
                     stmt = db.compileStatement(sqlInsert);
                    //変数のバインド
                    stmt.bindString(1,itemName);
                    //インサートSQLの実行。
                    stmt.executeInsert();
                }
                //vegetablesInfo_tにインサート
                String sqlInsert = "INSERT INTO vegetablesInfo_t(month,price,shopId,vegetablesId)" +
                        "VALUES(?,?,(SELECT shopId FROM shopName_t WHERE shopName = ?)," +
                        " (SELECT vegetablesId FROM vegetablesList_t WHERE vegetablesName = ?));";
                 stmt = db.compileStatement(sqlInsert);
                stmt = db.compileStatement(sqlInsert);
                stmt.bindString(1, "2018-11-12");
                stmt.bindString(2,itemPrice);
                stmt.bindString(3,spStr);
                stmt.bindString(4,itemName);
                //インサートSQLの実行。
                stmt.executeInsert();
            }finally {
                db.close();
            }
            edItemName.setText("");
            edItemPrice.setText("");
        }else {
            Toast.makeText(FoodRegisterActivity.this,
                    "商品名と値段、両方の値を入力してください", Toast.LENGTH_SHORT).show();
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
