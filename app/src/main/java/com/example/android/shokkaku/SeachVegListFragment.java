package com.example.android.shokkaku;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeachVegListFragment extends Fragment  {




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
          Context _context = getActivity();
        Log.d("SeachVegListFragment", "" + _context);
        View view =  inflater.inflate(R.layout.fragment_seach_veg_list, container, false);
        initCreateCheckbox(_context,view);
        return view;

    }

    /**
     * DBを参照して登録されている店名からチェックボックスの生成
     */
    public void initCreateCheckbox(Context _context,View view) {


        //データベースヘルパーオブジェクトを作成
        DatabaseHelper helper = new DatabaseHelper(_context);
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
                CheckBox checkBox = new CheckBox(_context);
                //表示する文字を設定
                checkBox.setText(shopName);
                //FragmentでLinearLayoutのR値取
                Log.d("view1",""+ view);
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.llParentCheckbox);
                Log.d("LinearLayout","" + layout);
               //LinearLayoutにチェックボックス追加
               layout.addView(checkBox);
               }

        }finally {
            //データベース接続オブジェクトの開放
            db.close();
        }

    }
    @Override
    public void onStart() {
        super.onStart();
    }




}
