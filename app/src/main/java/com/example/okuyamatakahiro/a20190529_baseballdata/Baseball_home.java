package com.example.okuyamatakahiro.a20190529_baseballdata;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;

public class Baseball_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("テスト", "AAAAAAdocument");

        super.onCreate(savedInstanceState);
        Log.v("テスト", "1ERRRRROO");

        //BaseballDbHelper pitcherDbHelper = new BaseballDbHelper(this);
        //setDatabase();
        Log.v("テスト", "ERRRRROO");
        //BaseballDbHelper batterDbHelper = new BaseballDbHelper(getApplicationContext(),"npbbatter");



        setContentView(R.layout.baseball_home);
        Log.v("テスト", "2ERRRRROO");
//        BaseballDbHelper batterDb = new BaseballDbHelper(Baseball_home.this,new String("npbbatter"));
//        try {
//
//            batterDb.createDataBase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }
//        try {
//
//            batterDb.openDataBase();
//        }catch(SQLException sqle){
//            throw sqle;
//        }



        BaseballDbHelper pitchDb = new BaseballDbHelper(this);
        pitchDb.getWritableDatabase();
        pitchDb.close();
//
//        try {
//            pitchDb.createDataBase();
//
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }
//        try {
//            pitchDb.openDataBase();
//
//        }catch(SQLException sqle){
//            throw sqle;
//        }finally {
//            pitchDb.close();
//        }




        //BaseballDbHelper yakyu= new BaseballDbHelper(this);
        //yakyu.getWritableDatabase();
        Log.v("テスト", "3ERRRRROO");

//        Thread downloadThread = new Thread() {
//            public void run() {
//                String url = "https://baseball-data.com/player/yb/%E3%83%AD%E3%83%9A%E3%82%B9";
//                Document doc;
//                try {
//                    doc = Jsoup.connect(url).get();
//                    String title = doc.title();
//
//                    Elements atitle = doc.getElementsByTag("title");
//                    Elements body = doc.getElementsByTag("body");
//                    Log.v("テスト", "YeahhhhhERRRRROO");
//                    Log.v("テスト", "document"+doc);
//                    Log.v("テスト","title"+title);
//                    Log.v("テスト","BOOOOODDDDDYYY"+body);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        downloadThread.start();
//        //Document document=null;
// //       Document document=null; //=Jsoup.connect(url).get();
//        Log.v("テスト", "44ERRRRROO");
//        Log.v("テスト","44ERROR");
////        try {
////            Log.v("テスト", "55ERRRRROO");
////            Log.v("テスト","55ERROR");
////            //Document document = Jsoup.connect(url).get();
////            Log.v("テスト", "1ERRRRROO");
////            Log.v("テスト","1ERROR");
////
////            document=Jsoup.parse(document.html());
////            Log.v("テスト", "document"+document);
////            Log.v("テスト","title"+title);
////
////        } catch (IOException e) {
////            Log.v("テスト", "ERRRRROO");
////            Log.v("テスト","ERROR");
////            e.printStackTrace();
////            Log.v("テスト", "ERRRRROO");
////            Log.v("テスト","ERROR");
////
////        }
//        Log.v("テスト", "6ERRRRROO");
//        Log.v("テスト","6ERROR");
//        //String html = "<div><span>hoge</span><p>fuga</p></div>";
////        Document document = Jsoup.parse(html);
////        try {
////            document = Jsoup.connect(url).get();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        document=Jsoup.parse(document.html());
////        Elements title=document.select("td");



        findViewById(R.id.imgButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック処理
                Intent intent;
                intent = new Intent(Baseball_home.this, League_Selecter.class);
                intent.putExtra("LEAGUE_FLAG", 1);
                startActivity(intent);

            }
        });
        findViewById(R.id.imgButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Baseball_home.this, League_Selecter.class);
                intent.putExtra("LEAGUE_FLAG", 2);
                startActivity(intent);

            }
        });
    }
//    private void setDatabase() {
//        mDbHelper = new BaseballDbHelper(this);
//        try {
//            Log.v("テスト", "try");
//            mDbHelper.createEmptyDataBase();
//            db = mDbHelper.openDataBase();
//        } catch (IOException ioe) {
//            Log.v("テスト", "IOException ioe");
//            throw new Error("Unable to create database");
//        } catch(SQLException sqle){
//            throw sqle;
//        }
//    }
//    @Override
//    public void onDestroy() {
//        db.close();
//        super.onDestroy();
//    }
}

