package com.example.android.shokkaku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * データベースファイルの定数フィールド
     */
    private static final String DATABASE_NAME = "shokkaku_db";
    /**
     * バージョンバージョン情報の定数フィールド
     */
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        //親クラスのコンストラクタの呼び出し
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     *端末に同名データベース名が存在しない場合に1度だけ実行される
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("CreateDB","onCreate(SQLiteDatabase db) START");
        //テーブル作成用のSQL文字列の作成　野菜の情報管理用テーブル
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE vegetablesList_t(");
        sb.append("vegetablesId INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,");
        sb.append("vegetablesName TEXT UNIQUE NOT NULL,");
        sb.append("vegetablesJudge TEXT DEFAULT '情報なし' NOT NULL");
        sb.append(");");
        String sql = sb.toString();
        //SQL文実行
        db.execSQL(sql);
        Log.d("CreateDB","onCreate(SQLiteDatabase db) first");

        //2つめのテーブル　店名管理用テーブル
        sb = new StringBuilder();
        sb.append("CREATE TABLE shopName_t(");
        sb.append("shopId INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("shopName TEXT UNIQUE NOT NULL");
        sb.append(");");
        String sql2 = sb.toString();
        //SQL文実行
        db.execSQL(sql2);
        Log.d("CreateDB","onCreate(SQLiteDatabase db) second");

        //3つめのテーブル
        sb = new StringBuilder();
        sb.append("CREATE TABLE vegetablesInfo_t(");
        //_idにするとOSが自動的に主キーと判定する仕組みらしい
        sb.append("_Id INTEGER PRIMARY KEY AUTOINCREMENT,");
        //CURRENT_DATE はUTCらしく9時間時差がある
        sb.append("month  TEXT DEFAULT CURRENT_DATE NOT NULL,");
        sb.append("price INTEGER  NOT NULL CHECK(price > 0),");
        //sb.append("memo  TEXT  DEFAULT 'なし' NOT NULL,");
        sb.append("shopId INTEGER NOT NULL ,");
        sb.append("vegetablesId INTEGER NOT NULL,");
        sb.append("foreign key(shopId) REFERENCES shopName_t(shopId ),");
        sb.append("foreign key (vegetablesId) REFERENCES vegetablesList_t(vegetablesId)");
        sb.append(");");
        String sql3 = sb.toString();
        //SQL文実行
        db.execSQL(sql3);
        //試験用にINSERTする
        initInsert(db);
        Log.d("CreateDB","onCreate(SQLiteDatabase db) END");
    }

    /**
     *ダミー情報
     */
    private void initInsert(SQLiteDatabase db){
       //インサート文用のSQL文を用意 店名追加
        String shopInsert = "INSERT INTO shopName_t(shopName) values " +
                            "('A店'),('B店'),('C店'),('D店')";
        db.execSQL(shopInsert);
        //野菜情報追加
        String vegInsert = "INSERT INTO vegetablesList_t(vegetablesName,vegetablesJudge)" +
                 " VALUES ('トマト','皮にハリとつやがありヘタが緑色でピンとしているもの'), "+
                "('なす','ヘタの切り口がみずみずしくトゲがピンととがっているもの')," +
                "('春菊','茎の下の方まで葉がたくさんついているもの(品種によってはないものもある)')," +
                "('モヤシ','茎が太く短めで全体的に白く、透明感があり袋を手で持ってみて、硬い感触があるもの')," +
                "('ねぎ','緑と白の境目がくっきりしていて白い部分が長いもの')," +
                "('なめこ','肉厚でカサの大きさがなるべくそろっているもの')," +
                "('あしたば','あしたばあしたばあしたば')," +
                "('アスパラガス','アスパラガスアスパラガスアスパラガス')," +
                "('うり','うりうりうりうりうり')," +
                "('枝豆','枝豆枝豆枝豆枝豆枝豆枝豆')," +
                "('しいたけ','しいたけしいたけしいたけしいたけ')," +
                "('シソ','シソシソシソシソシソ')," +
                "('じゃがいも','じゃがいもじゃがいもじゃがいも')," +
                "('たけのこ','たけのこたけのこたけのこたけのこ')," +
                "('玉ねぎ','玉ねぎ玉ねぎ玉ねぎ玉ねぎ玉ねぎ')," +
                "('白菜','白菜白菜白菜白菜')," +
                "('ブロッコリー','ブロッコリーブロッコリーブロッコリー')," +
                "('やまいも','やまいもやまいもやまいも')," +
                "('まいたけ','まいたけまいたけまいたけまいたけまいたけ')," +
                "('れんこん','皮にツヤがあり褐色～黄色の自然な色味で穴が小さくサイズがそろっていて肉厚なもの');";
        db.execSQL(vegInsert);
        String buyInfoInsert = "INSERT INTO vegetablesInfo_t(month,price,shopId,vegetablesId)" +
                "VALUES ('2015-11-12',120,2,1)," +
                "('2015-11-12',120,2,1)," +
                "('2018-11-12',130,2,1)," +
                "('2018-10-12',120,1,2)," +
                "('2015-11-14',100,3,3)," +
                "('2015-11-21',110,4,4)," +
                "('2015-11-12',160,2,5)," +
                "('2015-11-12',190,3,6)," +
                "('2018-11-16',120,4,7)," +
                "('2015-11-01',80,1,8)," +
                "('2011-11-08',110,3,9)," +
                "('2013-11-12',160,2,10)," +
                "('2012-11-12',170,3,11)," +
                "('2018-11-12',180,2,12)," +
                "('2018-11-22',90,3,13)," +
                "('2018-11-22',120,1,14)," +
                "('2018-11-10',130,3,15)," +
                "('2018-11-12',140,2,16)," +
                "('2018-11-19',140,3,17)," +
                "('2015-11-18',150,4,18)," +
                "('2013-11-19',110,3,19)," +
                "('2014-11-18',150,1,20)," +
                "('2018-11-22',110,2,14)," +
                "('2018-11-15',120,4,15)," +
                "('2018-11-12',110,4,16)," +
                "('2018-11-20',150,2,17)," +
                "('2015-11-18',100,1,18)," +
                "('2013-11-10',130,1,19)," +
                "('2015-11-11',140,2,6)," +
                "('2015-11-11',190,1,6)," +
                "('2015-11-12',180,3,7);" ;
        db.execSQL(buyInfoInsert);

    }
    /**
     *内部のデータベースとのバージョン番号とコンストラクタの引数で渡された番号が違う場合
     * 自動実行 オーバーライド必須
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
