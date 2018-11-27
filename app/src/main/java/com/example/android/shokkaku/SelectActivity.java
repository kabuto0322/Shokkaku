package com.example.android.shokkaku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //登録画面へ飛ばすButtonオブジェクトを取得
        Button btRegister = findViewById(R.id.btRegister);
        //SelectListenerクラスのインスタンス生成
        SelectListener listener = new SelectListener();
        btRegister.setOnClickListener(listener);

        //検索画面へ飛ばすButtonオブジェクトを設定
        Button btSeach = findViewById(R.id.btReserch);
        btSeach.setOnClickListener(listener);

    }
    /**
     * ボタンをクリックしたときのリスナクラス(タッチするか見張っている)
     */
    private class SelectListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent;
            //タップされた画面部品のidのR値を取得
            int id = view.getId();
            //idのR値によって処理を分岐
            switch (id) {
                //登録ボタンの時
                case R.id.btRegister:
                    //インテントオブジェクト作成
                     intent = new Intent(SelectActivity.this, FoodRegisterActivity.class);
                    //実行
                    startActivity(intent);
                    break;

                case R.id.btReserch:
                    //インテントオブジェクト作成
                    intent = new Intent(SelectActivity.this, FoodSeachActivity.class);
                    //実行
                    startActivity(intent);
                    break;
            }
        }
    }
}
