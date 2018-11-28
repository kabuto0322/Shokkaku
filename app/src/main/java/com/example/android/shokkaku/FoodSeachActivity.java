package com.example.android.shokkaku;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;

public class FoodSeachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_seach);
        initTab();
        //ActionBarを取得
        ActionBar actionBar = getSupportActionBar();
        //ActionBarの「戻る」メニューをオプションメニューに追加
        actionBar.setDisplayHomeAsUpEnabled(true);


        //DBを参照して登録されている店名からチェックボックスの生成
        //データベースヘルパーオブジェクトを作成
        DatabaseHelper helper = new DatabaseHelper(FoodSeachActivity.this);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = helper.getWritableDatabase();
        //データがなかった時のために初期値を用意
        String shopName = "";
        Log.d("initCreateCheckbox","first");
        try{
            //店名の取得
            String sql = "SELECT shopName FROM shopName_t";
            //SQL文の実行
            Cursor cursor = db.rawQuery(sql, null);
            //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得
            while (cursor.moveToNext()) {
                //カラムのインデックス値を取得 引数はカラム名
                int idxShopName = cursor.getColumnIndex("shopName");
                //カラムのインデックス値をもとに実際のデータを取得
                shopName = cursor.getString(idxShopName);
                Log.d("CheckboxShopName",shopName);
                //checkbox作成
                CheckBox checkBox = new CheckBox(FoodSeachActivity.this);
                //表示する文字を設定
                checkBox.setText(shopName);

                LinearLayout layout = (LinearLayout) findViewById(R.id.llParentCheckbox);
                Log.d("LinearLayout","" + layout);
                //LinearLayoutにチェックボックス追加
                layout.addView(checkBox);
            }

        }finally {
            //データベース接続オブジェクトの開放
            db.close();
        }
        }
    /**
     * タブの生成
     */
    protected void initTab(){
        //xmlからFragmentTabHostを取得
        FragmentTabHost tabHost = findViewById(android.R.id.tabhost);
        //                              FragmentManagerの取得
        tabHost.setup(this,getSupportFragmentManager(), R.id.content);
        //TabSpec のインスタンス生成
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tab1");
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2");
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3");
        //実際に表示する文字を指定する
        tabSpec1.setIndicator("野菜");
        tabSpec2.setIndicator("肉・魚");
        tabSpec3.setIndicator("その他");
        //tabの追加(TabSpec,タブが選択されたときに表示したいFragment,渡したいデータをBundleでない場合はnull)
        tabHost.addTab(tabSpec1,SeachVegListFragment.class,null);
        tabHost.addTab(tabSpec2,SeachMeatAndFishFragment.class,null);
        tabHost.addTab(tabSpec3,SeachOthersFragment.class,null);
        //初期のタブ設定
        tabHost.setCurrentTabByTag("tab1");
    }

    /**
     *オプションメニューの生成
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //オプションメニュー用.xmlファイルをインフレート
        inflater.inflate(R.menu.menu_options_menu_list,menu);
        //親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * オプションメニュー選択時の処理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //選択されたメニューidの取得
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menuListOptionByOrderItemName:
                //TODO 商品名順に並び替える処理
                break;
            case R.id.menuListOptionByOrderShopName:
                //TODO 店舗ごとに並び替える処理
                break;
            case android.R.id.home://R値はSDKで用意されているので固定
                //戻るメニューを押されたときの処理
                finish();
        }
        //親クラスの同名メソッドを呼び出し。その戻り値を返却
        return super.onOptionsItemSelected(item);
    }





}
