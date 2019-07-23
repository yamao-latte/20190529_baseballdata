package com.example.okuyamatakahiro.a20190529_baseballdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static android.provider.BaseColumns._ID;

//import static android.support.v4.graphics.drawable.IconCompat.getResources;

public class BaseballDbHelper extends SQLiteOpenHelper{
    public String teamCentral[]={"DeNA","巨人","広島", "ヤクルト", "阪神","中日"};
    public String teamPaci[] = {"ソフトバンク", "西武","日本ハム", "楽天", "オリックス","ロッテ"};
    public String positionTable[]={"npbpitcher","npbbatter"};

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.okuyamatakahiro.a20190529_baseballdata/databases/";

    //private static String DB_NAME = "npbpitcher";
    //pitch:0,batter:1
    private static final String DB_NAME = "npbplayer.db";
    private SQLiteDatabase myDataBase;

    private final Context myContext;

    private Activity m_Activity;
    private AlertDialog.Builder builder;
    private Dialog dialog;


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public BaseballDbHelper(Context context,Activity m_Activity) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.m_Activity=m_Activity;
        //this.DB_NAME=DBname;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME+".db");

        //D:\20190529_baseballdata\app\src\main\assets\npbbatter.db
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME+".db" ;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;

        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream inputStream=null;
        try {
            inputStream = myContext.getResources().getAssets().open("npbbatter_utf8.csv");
        }catch(IOException e) {
            Log.d( "ERRORです: ","ファイル読み込みエラー");
        }
        InputStreamReader inputStreamReader=null;
        try{inputStreamReader = new InputStreamReader(inputStream,"utf-8");
        }
        catch (UnsupportedEncodingException e){
                }
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        //FileReader file = new FileReader(fileName);
        //BufferedReader buffer = new BufferedReader(file);

        String line = "";
        String tableName ="npbbatter";
        String columns = "_id, 順位, 選手名, チーム, 打率, 試合, 打席数, 打数, 安打, 本塁打, 打点, 盗塁, 四球, 死球, 三振, 犠打, 併殺打, 出塁率, 長打率, OPS";
        //下は野地タブ
        //_id,
        //"_id,"順位","選手名","チーム","打率","試合","打席数","打数","安打","本塁打","打点","盗塁","四球","死球","三振","犠打","併殺打","出塁率","長打率","OPS"20ko
        //"_id, 順位, 選手名, チーム, 打率, 試合, 打席数, 打数, 安打, 本塁打, 打点, 盗塁, 四球, 死球, 三振, 犠打, 併殺打, 出塁率, 長打率, OPS";
        String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
        String str2 = ");";

        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE " + tableName
                    + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "順位" + " INTEGER,"
                    + "選手名"+" TEXT,"
                    + "チーム" + " TEXT,"
                    + "打率" + " REAL,"
                    + "試合" + " INTEGER,"
                    + "打席数" + " INTEGER,"
                    + "打数" + " INTEGER,"
                    + "安打" + " INTEGER,"
                    + "本塁打" + " INTEGER,"
                    + "打点" + " INTEGER,"
                    + "盗塁" + " INTEGER,"
                    + "四球" + " INTEGER,"
                    + "死球" + " INTEGER,"
                    + "三振" + " INTEGER,"
                    + "犠打" + " INTEGER,"
                    + "併殺打" + " INTEGER,"
                    + "出塁率" + " REAL,"
                    + "長打率" + " REAL,"
                    + "OPS" + " REAL);");
            db.setTransactionSuccessful();
            db.endTransaction();

            db.beginTransaction();
            line = buffer.readLine();
            while ((line = buffer.readLine()) != null) {
                StringBuilder sb = new StringBuilder(str1);
                String[] str = line.split(",");
                sb.append("'" + str[0] + "','");
                //sb.append("'" + str[2] + "','");
//                int i=Integer.parseInt(str[2]);
//                sb.append("'" + Integer.toString(i) + "','");

                sb.append(str[1] + "','");
                sb.append(str[2] + "','");
                sb.append(str[3] + "','");
                sb.append((str[4]) + "','");

                sb.append((str[5]) + "','");
                sb.append((str[6]) + "','");
                sb.append((str[7]) + "','");
                sb.append((str[8]) + "','");
                sb.append((str[9]) + "','");
                sb.append((str[10]) + "','");
                sb.append((str[11]) + "','");
                sb.append((str[12]) + "','");
                sb.append((str[13]) + "','");
                sb.append((str[14]) + "','");
                sb.append((str[15]) + "','");
                sb.append((str[16]) + "','");

                sb.append((str[17]) + "','");
                sb.append((str[18]) + "','");
                sb.append((str[19]) + "'");
                sb.append(str2);
                db.execSQL(sb.toString());
            }
        }catch (IOException e){
            Log.d( "ERRORです: ","rEADLINEerror");
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        ////////////////////////////
        //InputStream inputStream=null;
        try {
            inputStream = myContext.getResources().getAssets().open("npbpitcher_utf8.csv");
        }catch(IOException e) {
            Log.d( "ERRORです: ","ファイル読み込みエラー");
        }
        //InputStreamReader inputStreamReader=null;
        try{inputStreamReader = new InputStreamReader(inputStream,"utf-8");
        }
        catch (UnsupportedEncodingException e){
        }
        buffer = new BufferedReader(inputStreamReader);

        //FileReader file = new FileReader(fileName);
        //BufferedReader buffer = new BufferedReader(file);

        line = "";
         tableName ="npbpitcher";
         columns = "_id, 順位, 選手名, チーム, 防御率, 試合, 勝利, 敗北, セーブ, ホールド, 勝率, 打者, 投球回, 被安打, 被本塁打, 与四球, 与死球, 奪三振, 失点, 自責点, WHIP, DIPS";
        //"_id,順位,選手名,チーム,防御率,試合,勝利,敗北,セーブ,ホールド,勝率,打者,投球回,被安打,被本塁打,与四球,与死球,奪三振,失点,自責点,WHIP,DIPS"22ko

         str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
         str2 = ");";
        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE " + tableName
                    + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "順位" + " INTEGER,"
                    + "選手名"+" TEXT,"
                    + "チーム" + " TEXT,"
                    + "防御率" + " REAL,"
                    + "試合" + " INTEGER,"
                    + "勝利" + " INTEGER,"
                    + "敗北" + " INTEGER,"
                    + "セーブ" + " INTEGER,"
                    + "ホールド" + " INTEGER,"
                    + "勝率" + " REAL,"
                    + "打者" + " INTEGER,"
                    + "投球回" + " INTEGER,"
                    + "被安打" + " INTEGER,"
                    + "被本塁打" + " INTEGER,"
                    + "犠打" + " INTEGER,"
                    + "与四球" + " INTEGER,"
                    + "与死球" + " INTEGER,"
                    + "奪三振" + " INTEGER,"
                    + "失点" + " INTEGER,"
                    + "自責点" + " INTEGER,"
                    + "WHIP" + " REAL,"
                    + "DIPS" + " REAL);");
            db.setTransactionSuccessful();
            db.endTransaction();

            db.beginTransaction();
            line = buffer.readLine();
            while ((line = buffer.readLine()) != null) {
                StringBuilder sb = new StringBuilder(str1);
                String[] str = line.split(",");
                sb.append("'" + str[0] + "','");
                //sb.append("'" + str[2] + "','");
                //sb.append("'" + str[1] + "','");
//                int i=Integer.parseInt(str[2]);
//                sb.append("'" + Integer.toString(i) + "','");

                sb.append(str[1] + "','");
                sb.append(str[2] + "','");
                sb.append(str[3] + "','");
                sb.append((str[4]) + "','");

                sb.append((str[5]) + "','");
                sb.append((str[6]) + "','");
                sb.append((str[7]) + "','");
                sb.append((str[8]) + "','");
                sb.append((str[9]) + "','");
                sb.append((str[10]) + "','");
                sb.append((str[11]) + "','");
                sb.append((str[12]) + "','");
                sb.append((str[13]) + "','");
                sb.append((str[14]) + "','");
                sb.append((str[15]) + "','");
                sb.append((str[16]) + "','");

                sb.append((str[17]) + "','");
                sb.append((str[18]) + "','");
                sb.append((str[19]) + "','");
                sb.append((str[20]) + "','");
                sb.append((str[21]) + "'");
                sb.append(str2);
                db.execSQL(sb.toString());
            }
        }catch (IOException e){
            Log.d( "ERRORです: ","rEADLINEerror");
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        if(db != null) db.close();





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String[] getAppCategoryDetail(BaseballDbHelper playdb) {
        Log.v("テスト：","start");

        String selectQuery; //= "SELECT  * FROM " + DB_NAME[position];
        //            SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "打率" DESC);
        if(DB_NAME=="npbplayer.db"){
            selectQuery="SELECT * FROM 'npbbatter' WHERE チーム LIKE 'D%' ;";
            //selectQuery="SELECT '_rowid_',* FROM 'npbbatter' WHERE _id = 1";
        }else{
            //selectQuery="SELECT * FROM 'npbbatter' WHERE 'チーム' LIKE 'DeNA' ESCAPE '\' ORDER BY '打率' DESC ;";
        }
        //SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "選手名" DESC);
        //SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "選手名" DESC LIMIT 0, 49999;

        //this.close();


        SQLiteDatabase db  = playdb.getReadableDatabase();
        //
        Log.v("テスト：","haina");

        db.beginTransaction();
        //

        Cursor cursor      = db.rawQuery(selectQuery, null);
       // Cursor cursor      = db.query("npbbatter",new String[]{"_id"},null,null,null,null,null);
        String[] data      = null;
        String ko ="kara";

        try {
                while (cursor.moveToNext()){
                    ko+=cursor.getString(cursor.getColumnIndex("選手名"));

                }
            Log.v("テスト：","ダメだ"+ko);

        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            if(db != null) db.close();
//            db.endTransaction();
        }

        //cursor.close();
        //db.close();
        return data;
    }

    public ArrayList<String> getAppCategoryDetaila(BaseballDbHelper playdb,int league,int team,int position) {
        Log.v("テスト：","start");

        String selectQuery; //= "SELECT  * FROM " + DB_NAME[position];
        //            SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "打率" DESC);
        selectQuery="SELECT * FROM 'npbbatter' WHERE チーム LIKE 'D%' ;";
        //SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "選手名" DESC);
        //SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "選手名" DESC LIMIT 0, 49999;

        if(league==1){
            //セリーグ
            selectQuery="SELECT * FROM '"+positionTable[position]+"' WHERE チーム LIKE '" +teamCentral[team]+ "' ;";
        }else {
            //パリーグ
            selectQuery="SELECT * FROM '"+positionTable[position]+"' WHERE チーム LIKE '" +teamPaci[team]+ "' ;";
        }
        //this.close();


        SQLiteDatabase db  = playdb.getReadableDatabase();
        //
        Log.v("テスト：","haina");

        db.beginTransaction();
        //

        Cursor cursor      = db.rawQuery(selectQuery, null);
        // Cursor cursor      = db.query("npbbatter",new String[]{"_id"},null,null,null,null,null);
        //String[] data=new String[20];
        ArrayList<String> data=new ArrayList<String>();
        String ko ="kara";

        try {
            //int cnt=0;
            while (cursor.moveToNext()){
                ko+=cursor.getString(cursor.getColumnIndex("選手名"));
                //data[cnt]+=cursor.getString(cursor.getColumnIndex("選手名"));
                data.add(cursor.getString(cursor.getColumnIndex("選手名")));
                //cnt++;
            }
            Log.v("テスト：","ダメだ"+ko);

        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            if(db != null) db.close();
//            db.endTransaction();
        }

        //cursor.close();
        //db.close();
        return data;
    }
    public ArrayList<String> getPlayerdata(BaseballDbHelper playdb,int position,String name) {
        Log.v("テスト：","start");

        String selectQuery; //= "SELECT  * FROM " + DB_NAME[position];
        //            SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "打率" DESC);

        selectQuery="SELECT * FROM '"+positionTable[position]+"' WHERE 選手名 LIKE '" +name+ "' ;";

        SQLiteDatabase db  = playdb.getReadableDatabase();
        Log.v("テスト：","haina");
        db.beginTransaction();

        Cursor cursor      = db.rawQuery(selectQuery, null);
        // Cursor cursor      = db.query("npbbatter",new String[]{"_id"},null,null,null,null,null);
        //String[] data=new String[20];
        ArrayList<String> data=new ArrayList<String>();
        String ko ="kara";

        try {
            //int cnt=0;
            while (cursor.moveToNext()){
                ko+=cursor.getString(cursor.getColumnIndex("選手名"));
                if (position==0) {
                    data.add(new Float(cursor.getFloat(cursor.getColumnIndex("防御率"))).toString());
                    data.add(cursor.getString(cursor.getColumnIndex("勝利")));
                    data.add(cursor.getString(cursor.getColumnIndex("敗北")));
                    data.add(cursor.getString(cursor.getColumnIndex("試合")));
                    data.add(cursor.getString(cursor.getColumnIndex("投球回")));
                    data.add(cursor.getString(cursor.getColumnIndex("セーブ")));
                    data.add(cursor.getString(cursor.getColumnIndex("ホールド")));
                    data.add(cursor.getString(cursor.getColumnIndex("奪三振")));
                    data.add(cursor.getString(cursor.getColumnIndex("与四球")));
                    data.add(new Float(cursor.getFloat(cursor.getColumnIndex("WHIP"))).toString());
                }else{
                    String stats="";
                    Float num=new Float(cursor.getFloat(cursor.getColumnIndex("打率")));
                    int val;
                    val=Math.round(num*1000)%1000;
                    //stats=new Float(cursor.getFloat(cursor.getColumnIndex("打率"))).toString();
                    data.add( "."+Integer.toString(val) );
                    data.add(cursor.getString(cursor.getColumnIndex("本塁打")));
                    data.add(cursor.getString(cursor.getColumnIndex("打点")));
                    num=(new Float(cursor.getFloat(cursor.getColumnIndex("出塁率"))));
                    val=Math.round(num*1000)%1000;
                    data.add( "."+Integer.toString(val) );
                    num=(new Float(cursor.getFloat(cursor.getColumnIndex("長打率"))));
                    val=Math.round(num*1000)%1000;
                    //data.add( new Float(cursor.getFloat(cursor.getColumnIndex("長打率"))).toString());
                    data.add( "."+Integer.toString(val) );
                    data.add(cursor.getString(cursor.getColumnIndex("盗塁")));
                    data.add(cursor.getString(cursor.getColumnIndex("三振")));
                    data.add( new Float(cursor.getFloat(cursor.getColumnIndex("OPS"))).toString() );;
                }
                //cnt++;
            }
            Log.v("テスト：","ダメだ"+ko);

        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            if(db != null) db.close();
//            db.endTransaction();
        }

        //cursor.close();
        //db.close();
        return data;
    }
    public void userUpdata(SQLiteDatabase db,Activity activity){
        new updaterDB(activity).execute(db);
    }
    /*更新ボタンを押されたとき、firebaseに接続して、最新の選手情報を取得して
      データベースをUPSERTして更新する。
      非同期によって動かし、更新中はダイアログを表示する。
      Firebaseにcsvファイルがあるのでそれを取り込む感じ
    */
    public void updatePlayerdata(BaseballDbHelper db){
        ///////////
        builder = new AlertDialog.Builder(m_Activity);
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.show();
        /////////////
        final SQLiteDatabase ddb=db.getWritableDatabase();
        StorageReference storageRef;
        storageRef= FirebaseStorage.getInstance().getReference();

        String tableName ="npbbatter";
        String columns = "_id, 順位, 選手名, チーム, 打率, 試合, 打席数, 打数, 安打, 本塁打, 打点, 盗塁, 四球, 死球, 三振, 犠打, 併殺打, 出塁率, 長打率, OPS";
        final String str1 = "INSERT OR REPLACE INTO " + tableName + " (" + columns + ") values(";
        final String str2 = ");";
        //テーブルの全データ削除
        final String sqldeleteStr="DELETE FROM npbbatter";

        final StorageReference fileRef=storageRef.child("npbbatter_utf8.csv");
        final long ONE_MEGA=60000;

        fileRef.getBytes(ONE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String data=new String(bytes);
                String[] arr=data.split("\n");
                String res="";
                try{
                    /*トランザクション開始**************************************************************************/
                    ddb.beginTransaction();
                    try{
                        Thread.sleep(30); //3000ミリ秒Sleepする
                    }catch(InterruptedException e){}
                    ddb.execSQL(sqldeleteStr);
                    for (int i=1;i<arr.length;i++){

                        Log.d("TAG",arr[i]);
                    //arr[i]に一行分(一人分)の情報が入っている
                    //ここからsplitで ,(カンマ) 区切りで入れることで
                    //カラムごとのデータを取り出すことが出来る
                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = arr[i].split(",");
                        //sb.append("'" + str[0] + "','");
                        sb.append("'" + str[0] + "','");
                        sb.append(str[1] + "','");
                        sb.append(str[2] + "','");
                        sb.append(str[3] + "','");
                        sb.append((str[4]) + "','");
                        sb.append((str[5]) + "','");
                        sb.append((str[6]) + "','");
                        sb.append((str[7]) + "','");
                        sb.append((str[8]) + "','");
                        sb.append((str[9]) + "','");
                        sb.append((str[10]) + "','");
                        sb.append((str[11]) + "','");
                        sb.append((str[12]) + "','");
                        sb.append((str[13]) + "','");
                        sb.append((str[14]) + "','");
                        sb.append((str[15]) + "','");
                        sb.append((str[16]) + "','");
                        sb.append((str[17]) + "','");
                        sb.append((str[18]) + "','");
                        sb.append((str[19]) + "'");
                        sb.append(str2);
                         //Log.d("TAG",arr[i+1]);
                        ddb.execSQL(sb.toString());
                        //Log.d("TAG",arr[i+2]);


                        try{
                            Thread.sleep(30); //3000ミリ秒Sleepする
                        }catch(InterruptedException e){}


                    }
                }catch (IllegalStateException e) {
                    e.printStackTrace();
                } finally {
  //                  ddb.setTransactionSuccessful();
//                    ddb.endTransaction();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Test","だめです");
            }
        });

        tableName ="npbpitcher";
        columns = "_id, 順位, 選手名, チーム, 防御率, 試合, 勝利, 敗北, セーブ, ホールド, 勝率, 打者, 投球回, 被安打, 被本塁打, 与四球, 与死球, 奪三振, 失点, 自責点, WHIP, DIPS";
        final StorageReference fileRef2=storageRef.child("npbpitcher_utf8.csv");
        final String str3 = "INSERT OR REPLACE INTO " + tableName + " (" + columns + ") values(";
        //final long ONE_MEGA=30000;
        //テーブルの全データ削除
        final String sqldeleteStrP="DELETE FROM npbpitcher";


        fileRef2.getBytes(ONE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String data=new String(bytes);
                String[] arr=data.split("\n");
                String res="";
                try{
                    //ddb.beginTransaction();
                    ddb.execSQL(sqldeleteStrP);
                    for (int i=1;i<arr.length;i++){

                        Log.d("TAG",arr[i]);
                        //arr[i]に一行分(一人分)の情報が入っている
                        //ここからsplitで ,(カンマ) 区切りで入れることで
                        //カラムごとのデータを取り出すことが出来る
                        StringBuilder sb = new StringBuilder(str3);
                        String[] str = arr[i].split(",");
                        //sb.append("'" + str[0] + "','");
                        sb.append("'" + str[0] + "','");
                        sb.append(str[1] + "','");
                        sb.append(str[2] + "','");
                        sb.append(str[3] + "','");
                        sb.append((str[4]) + "','");
                        sb.append((str[5]) + "','");
                        sb.append((str[6]) + "','");
                        sb.append((str[7]) + "','");
                        sb.append((str[8]) + "','");
                        sb.append((str[9]) + "','");
                        sb.append((str[10]) + "','");
                        sb.append((str[11]) + "','");
                        sb.append((str[12]) + "','");
                        sb.append((str[13]) + "','");
                        sb.append((str[14]) + "','");
                        sb.append((str[15]) + "','");
                        sb.append((str[16]) + "','");
                        sb.append((str[17]) + "','");
                        sb.append((str[18]) + "','");
                        sb.append((str[19]) + "','");
                        sb.append((str[20]) + "','");
                        sb.append((str[21]) + "'");
                        sb.append(str2);
                        //Log.d("TAG",arr[i+1]);
                        ddb.execSQL(sb.toString());
                        //Log.d("TAG",arr[i+2]);
                        try{
                            Thread.sleep(30); //3000ミリ秒Sleepする
                        }catch(InterruptedException e){}


                    }
                }catch (IllegalStateException e) {
                    e.printStackTrace();
                } finally {
                    /*トランザクション終了***********************************************************************************/
                    ddb.setTransactionSuccessful();
                    ddb.endTransaction();
                    try{
                        Thread.sleep(30); //3000ミリ秒Sleepする
                    }catch(InterruptedException e){}
                    if(ddb != null) ddb.close();

                    dialog.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Test","だめです");
            }
        });
    }



    /**
     * CsvFileを読み込む関数
     * @param csv 読み込みたいCsvFile
     * @return Map<String, Map<String,String>>
     */
    private Map<String, Map<String,String>> InputCsvFile(String csv) {
        Map<String, Map<String, String>> outDataArray = null;

        try {

            //assetファイルにあるcsvファイルの読み込み
            InputStream inputStream = myContext.getResources().getAssets().open(csv);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            //outFileの準備
            outDataArray = new HashMap<String, Map<String,String>>();


            //csvの列名を取得
            ArrayList<String> csvColumnArray = new ArrayList<String>();
            //csvの最初の一行を読み取り列名を取得
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine(), ",");

            int column = stringTokenizer.countTokens();
            for(int i=0; i<column; i++ ){
                csvColumnArray.add(stringTokenizer.nextToken());
            }

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringTokenizer = new StringTokenizer(line, ",");

                String Key = stringTokenizer.nextToken();
                outDataArray.put(Key, new HashMap<String, String>());
                for(int i=1; i < csvColumnArray.size(); i++){
                    outDataArray.get(Key).put(csvColumnArray.get(i), stringTokenizer.nextToken());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return outDataArray;
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
    public class updaterDB extends AsyncTask<SQLiteDatabase,Void,Integer> {

        private Activity m_Activity;
        private AlertDialog.Builder builder;
        private Dialog dialog;

        public updaterDB(Activity activity) {
            // 呼び出し元のアクティビティ
            m_Activity = activity;
            builder = new AlertDialog.Builder(m_Activity);
            //View view = getLayoutInflater().inflate(R.layout.progress);
            builder.setView(R.layout.progress);
            dialog = builder.create();
        }

        @Override
        protected Integer doInBackground(SQLiteDatabase... sqLiteDatabases) {
            //ここにバックグラウンドで行う処理を書く
            dialog.show();
            BaseballDbHelper db=new BaseballDbHelper(this.m_Activity,this.m_Activity);
            db.updatePlayerdata(db);
            dialog.dismiss();
            return null;
        }

        /*
         * 実行前の事前処理
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

        }

        @Override
        protected void onPostExecute(Integer result) {

            //ここにdoInBackground終了後に行う処理を書く
            Log.v("AsyncTaskProgress", "onend()");
            // プログレスダイアログを閉じる
//        if (this.m_ProgressDialog != null && this.m_ProgressDialog.isShowing()) {
//            this.m_ProgressDialog.dismiss();
//        }
//        if(result=="処理") dialog.dismiss();
            dialog.dismiss();

            Log.d("test","ok");
            super.onPostExecute(result);

        }
        /*
         * キャンセル時の処理
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();

            Log.v("AsyncTaskProgress", "onCancelled()");

            return;
        }


    }


}
//public class BaseballDbHelper extends SQLiteOpenHelper {
//
//    private static String DB_NAME = "npbpitcher";
//    private static String DB_NAME_ASSET = "npbpitcher.db";
//    private static final int DATABASE_VERSION = 17;
//
//    private SQLiteDatabase mDatabase;
//    private final Context mContext;
//    private final File mDatabasePath;
//
//    public BaseballDbHelper(Context context) {
//        super(context, DB_NAME, null, DATABASE_VERSION);
//        mContext = context;
//        mDatabasePath = mContext.getDatabasePath(DB_NAME);
//    }
//
//    /**
//     * asset に格納したデータベースをコピーするための空のデータベースを作成する
//     */
//    public void createEmptyDataBase() throws IOException {
//        Log.v("テスト", "createEmptyDataBase");
//        boolean dbExist = checkDataBaseExists();
//
//        if (dbExist) {
//            // すでにデータベースは作成されている
//        } else {
//            // このメソッドを呼ぶことで、空のデータベースがアプリのデフォルトシステムパスに作られる
//            getReadableDatabase();
//
//            try {
//                // asset に格納したデータベースをコピーする
//                copyDataBaseFromAsset();
//
//                String dbPath = mDatabasePath.getAbsolutePath();
//                SQLiteDatabase checkDb = null;
//                try {
//                    checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
//                } catch (SQLiteException e) {
//                }
//
//                if (checkDb != null) {
//                    checkDb.setVersion(DATABASE_VERSION);
//                    checkDb.close();
//                }
//
//            } catch (IOException e) {
//                throw new Error("Error copying database");
//            }
//        }
//    }
//
//    /**
//     * 再コピーを防止するために、すでにデータベースがあるかどうか判定する
//     *
//     * @return 存在している場合 {@code true}
//     */
//    private boolean checkDataBaseExists() {
//        Log.v("テスト", "checkDataBaseExists");
//        String dbPath = mDatabasePath.getAbsolutePath();
//
//        SQLiteDatabase checkDb = null;
//        try {
//            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
//        } catch (SQLiteException e) {
//            // データベースはまだ存在していない
//        }
//
//        if (checkDb == null) {
//            // データベースはまだ存在していない
//            return false;
//        }
//
//        int oldVersion = checkDb.getVersion();
//        int newVersion = DATABASE_VERSION;
//
//        if (oldVersion == newVersion) {
//            // データベースは存在していて最新
//            checkDb.close();
//            return true;
//        }
//
//        // データベースが存在していて最新ではないので削除
//        File f = new File(dbPath);
//        f.delete();
//        return false;
//    }
//
//    /**
//     * asset に格納したデーだベースをデフォルトのデータベースパスに作成したからのデータベースにコピーする
//     */
//    private void copyDataBaseFromAsset() throws IOException{
//        Log.v("テスト", "copyDataBaseFromAsset");
//        // asset 内のデータベースファイルにアクセス
//        InputStream mInput = mContext.getAssets().open(DB_NAME_ASSET);
//
//        // デフォルトのデータベースパスに作成した空のDB
//        OutputStream mOutput = new FileOutputStream(mDatabasePath);
//
//        // コピー
//        byte[] buffer = new byte[1024];
//        int size;
//        while ((size = mInput.read(buffer)) > 0) {
//            mOutput.write(buffer, 0, size);
//        }
//
//        // Close the streams
//        mOutput.flush();
//        mOutput.close();
//        mInput.close();
//    }
//
//    public SQLiteDatabase openDataBase() throws SQLException {
//        Log.v("テスト", "openDataBase");
//        return getReadableDatabase();
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    }
//
//
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//public class BaseballDbHelper extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME  = "BASEBALL.db";
//    private static final int DATABASE_VERSION = 1;
//    private static final String TABLE_NAME  = "PITCH_VALUE";
//    private static final String FIELD_CRE  = "CREATED";
//    private static final String FIELD_MOD  = "MODIFIED";
//    private static final String FIELD_INX  = "INX";
//    private static final String FIELD_TITLE  = "TITLE";
//    private static final String FIELD_MEMO  = "MEMO";
//
//    private static final String HTML_NUMBER="HTML_NUMBER";
//    private static final String NAME="NAME";
//    private static final String ERA="ERA";
//    private static final String GAMES_PLAYED="GAMES_PLAYED";
//    private static final String IP="IP";
//    private static final String StrikeOut="StrikeOut";
//    private static final String BB="BB";
//    private static final String WIN="WIN";
//    private static final String LOSS="LOSS";
//
//    public BaseballDbHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//    /*
//    *       //html取り込み番号
//    *       //名前
//    *       //防御率　Earned Run Average float ERA;
//            //登板数int Games_Played;
//            //投球回 int IP;
//            //奪三振 int StrikeOut;
//            //与四球  int BB;
//            //勝利数 int win;
//            //敗戦数  int loss;*/
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
////        String url = "http://www.baseball-lab.jp/player/detail/1300003";
////        //Document document=Jsoup.connect(url).get();
////        String html = "<div><span>hoge</span><p>fuga</p></div>";
////        Document document = Jsoup.parse(html);
////        try {
////            document = Jsoup.connect(url).get();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        document=Jsoup.parse(document.html());
////        Elements title=document.select("td");
////        Log.v("テスト", "document"+document);
////        Log.v("テスト","title"+title);
//        //Picher_stats_DB player=new Picher_stats_DB(Picher_stats_DB);
//        PlayerStats.Pitcher_stats_DB player=new PlayerStats.Pitcher_stats_DB();
//        sqLiteDatabase.beginTransaction();
//        try {
//            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME
//                    + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + HTML_NUMBER + "INTEGER"
//                    + NAME+"TEXT"
//                    + ERA + " REAL,"
//                    + GAMES_PLAYED+ " INTEGER,"
//                    + IP + " INTEGER,"
//                    + StrikeOut + " INTEGER,"
//                    + BB + " INTEGER,"
//                    + WIN + " INTEGER,"
//                    + LOSS + " INTEGER);");
//            sqLiteDatabase.setTransactionSuccessful();
//
//
//            //Picher_stats_DB player=new Picher_stats_DB(stat);
//
//
//
//            String sqlInsert="INSERT INTO BASEBALL(HTML_NUMBER,NAME,ERA,GAMES_PLAYED,IP,StrikeOut,BB,WIN,LOSS) VALUES(?,?,?,?,?,?,?,?,?,?)";
//            SQLiteStatement st=sqLiteDatabase.compileStatement(sqlInsert);
//            //st=db.compleStatement(sqlInsert);
//            //変数のバインド
//
//            st.bindLong(1,player.getHtml());
//            st.bindString(2,player.getName());
//            st.bindDouble(3,player.getERA());
//            st.bindLong(4,player.getGames_Played());
//            st.bindLong(5,player.getIP());
//            st.bindLong(6,player.getStrikeOut());
//            st.bindLong(7,player.getBB());
//            st.bindLong(8,player.getWin());
//            st.bindLong(9,player.getloss());
//
//            st.executeInsert();
//        } finally {
//            sqLiteDatabase.endTransaction();
//            sqLiteDatabase.close();
//        };
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(sqLiteDatabase);
//    }
//}