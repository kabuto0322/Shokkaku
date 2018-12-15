package com.example.android.shokkaku;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeachVegListFragment extends Fragment  {
    private ListView _listView;
    List< Map<String,Object>> _vegList;
    //private List< Map<String,Object>> vegiInfo;
    Toast _toast;
     private static final String[] FROM = {"name","price","shopName"};
     private static  final int[] TO = {R.id.tvFoodName,R.id.tvFoodPrice,R.id.tvShopName};
    Context _context;
    public SeachVegListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getActivity():フラグメントが現在関連付けられているアクティビティを返す
        //nullがかえって来ていたがFragmentのライフサイクルのうちonAttach()からonDetach()まで利用可能
        //ライフサイクルの終了したFragmentがActivityへの参照を持ち続けると、メモリリークに繋がるため
        //非同期処理でgetActivity()を呼び出すべきではない
          _context = getActivity();
        Log.d("SeachVegListFragment", "" + _context);
        View view =  inflater.inflate(R.layout.fragment_seach_veg_list, container, false);

        _listView = view.findViewById(R.id.lvVegList);
       _vegList = initVegList(_context,view);
       //simpleAdapterの生成
        SimpleAdapter adapter = new SimpleAdapter(_context,_vegList,R.layout.row,FROM,TO);
        //アダプターの登録
        _listView.setAdapter(adapter);
        Log.d("onCreateView","アダプターの登録");
        //TODO リスナクラスの登録
        _listView.setOnItemClickListener(new ListItemClickLitener());
        Log.d("onCreateView","リスナクラスの登録");
        return view;
    }

    /**
     *リストがタップされたときの処理
     */
    private class ListItemClickLitener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("_toast","" + _toast);
            //TODO 連続タップで反応しないように
                //タップされた行のデータを取得
                Map<String, Object> vegInfo = (Map<String, Object>) parent.getItemAtPosition(position);
                Log.d("ListItemClickLitener", "" + vegInfo);
                //食品の判定方法を取得
                String judge = (String) vegInfo.get("judge");
                Log.d("ListItemClickLitener", judge);
                //トーストの表示
               Toast.makeText(_context, judge, Toast.LENGTH_SHORT).show();
        }
    }


/**
 * DBを参照して野菜の一覧を表示
 */
    public List< Map<String,Object>> initVegList(Context _context,View view){
        Map<String,Object> vegInfo;
        List< Map<String,Object>> vegList = new ArrayList<>();

//        APIレベル使用不可
//        LocalDate localDate; int month = localDate.getDayOfMonth();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        Log.d("SeachVegListFragment","現在の月" + month);
        //本当はAPIで取得
        int _date = 11;


        //データベースヘルパーオブジェクトを作成
        DatabaseHelper helper = new DatabaseHelper(_context);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        //読み込み専用
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            //TODO checkboxに対応させる そもそもcheckboxが必要なのかを検討
            //FULL OUTER JOIN がいいかもしれない
            //情報取得用SQL
           String sqlGetVL = "SELECT MIN(price) as price,shopName,vegetablesName,vegetablesJudge FROM vegetablesInfo_t AS a " +
                   "JOIN shopName_t AS b ON  b.shopId = a.shopId  " +
                   "JOIN vegetablesList_t AS c ON c.vegetablesId = a.vegetablesId " +
                   " WHERE  month LIKE '%-" + _date + "-%' " +
                   "GROUP BY a.vegetablesId " +
                   "ORDER BY vegetablesName ASC; ";

           Log.d("SeachVegListFragment","SELECT文取得成功");
           //SQL文の実行                            バインド変数
            Cursor cursor = db.rawQuery(sqlGetVL,null);
            Log.d("SeachVegListFragment","取得できた行の数" + cursor.getCount());
            //データがなかった時の初期値を用意
            int price = 0;
            String shopName = "";
            String vegetablesName = "";
            String vegetablesJudge = "情報なし";

                int i = 0;

                //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得
                while (cursor.moveToNext()) {
                    //カラムのインデックス値をもとに実際のデータを取得
                    vegetablesName = cursor.getString(cursor.getColumnIndex("vegetablesName"));
                    price = cursor.getInt(cursor.getColumnIndex("price"));
                    shopName = cursor.getString(cursor.getColumnIndex("shopName"));
                    vegetablesJudge = cursor.getString(cursor.getColumnIndex("vegetablesJudge"));

                    //インスタンスの作成
                    vegInfo = new HashMap<>();
                    vegInfo.put("name", vegetablesName);
                    vegInfo.put("price", price);
                    vegInfo.put("shopName", shopName);
                    vegInfo.put("judge", vegetablesJudge);
                    vegList.add(vegInfo);

                    i++;
                    Log.d("SeachVegListFragment", "whileの回数 " + i);
                }

            Log.d("SeachVegListFragment","格納数 " + vegList.size());
            return vegList;

        }finally {
            db.close();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }



}
