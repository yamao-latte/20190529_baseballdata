package com.example.okuyamatakahiro.a20190529_baseballdata;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;
/* FIXME
        Firebaseによる選手情報の更新処理が遅い。
        ユーザ側で十分に待たないとクラッシュする恐れがある
*
* */

public class Baseball_home extends AppCompatActivity {
    mainProgressDialog updateThread;

    private AlarmManager am;
    private PendingIntent pending;
    private int requestCode = 1;

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
        scheduleNotification();

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



//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Drive.API)
//                .addScope(Drive.SCOPE_FILE)
//                .addScope(Drive.SCOPE_APPFOLDER)    // AppFolderの利用
//            .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        // 接続ボタンの実装
//        Button connectButton = (Button) findViewById(R.id.connectButton);
//        connectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (googleApiClient != null && !googleApiClient.isConnected()) {
//                    // 接続開始
//                    googleApiClient.connect();
//                }
//            }
//        });
//
//        // 接続解除ボタンの実装
//        Button disconnectButton = (Button) findViewById(R.id.disconnectButton);
//        disconnectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (googleApiClient != null && googleApiClient.isConnected()) {
//                    // 接続解除
//                    googleApiClient.disconnect();
//                }
//            }
//        });



        BaseballDbHelper pitchDb = new BaseballDbHelper(this,this);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //main.xmlの内容を読み込む
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
             default:
                 // 非同期(スレッド)処理クラスの生成
                 Toast.makeText(this, item.getItemId(), Toast.LENGTH_LONG).show();

//                 this.updateThread = new mainProgressDialog(this);
//                 // 非同期(スレッド)処理の実行
//                 this.updateThread.execute();
                 BaseballDbHelper db=new BaseballDbHelper(this,this);
                 db.updatePlayerdata(db);

                 /*toDO
                 *  下記はdbHelperで非同期処理を行うもの
                 *
                 * */
//                 BaseballDbHelper pitchDb = new BaseballDbHelper(this);
//                 SQLiteDatabase s=pitchDb.getWritableDatabase();
//                 pitchDb.userUpdata(s,this);


        }
        return super.onOptionsItemSelected(item);
    }
    //試合通知Notification
    //次の試合時刻になったら通知を入れる
    /*TODO
        webAPIなどから試合の開始時刻を取得して
        デーゲームとナイトゲームどちらの試合にも対応したい
        ・贔屓チームを設定しておくことで自チームの日程元に決めたり？
        ・試合経過なんかも取得出来たら。。。
     */
    private void scheduleNotification(){
        //呼び出す日時を設定する
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 10sec
        //int day = calendar.get(Calendar.DATE);         //(4)現在の日を取得
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //(5)現在の時を取得
        int addtime=1;
        if(hour<=15){
            //18時より前の時
            addtime=18-hour;
            //calendar.add((Calendar.HOUR),addtime);
            calendar.add(Calendar.SECOND,5);
        }else if(hour>15){
            //18時過ぎの時
            addtime=24-hour+18;
            //calendar.add(Calendar.HOUR,addtime);
            calendar.add(Calendar.SECOND,5);
        }
        //calendar.add(Calendar.SECOND, 1);

        Intent intent = new Intent(getApplicationContext(), Game_Notify.class);
        intent.putExtra("RequestCode",requestCode);

        pending = PendingIntent.getBroadcast(
                getApplicationContext(),requestCode, intent, 0);

        // アラームをセットする
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        //DEBUG用--------------------------------------------------------------------------------------------------------------
        if (am != null) {
            am.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pending);

            // トーストで設定されたことをを表示
            Toast.makeText(getApplicationContext(),
                    Integer.toString(addtime)+"時間後に設定されました\n試合通知Notifyデバッグ", Toast.LENGTH_SHORT).show();

            Log.d("debug", "start");
        }
        //---------------------------------------------------------------------------------------------------------------------
    }
    public void schduleNotifyCancel(){
        Intent indent = new Intent(getApplicationContext(), Game_Notify.class);
        PendingIntent pending = PendingIntent.getBroadcast(
                getApplicationContext(), requestCode, indent, 0);

        // アラームを解除する
        AlarmManager am = (AlarmManager)Baseball_home.this.
                getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.cancel(pending);
            Toast.makeText(getApplicationContext(),
                    "alarm cancel", Toast.LENGTH_SHORT).show();
            Log.d("debug", "cancel");
        }
        else{
            Log.d("debug", "null");
        }
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

//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//
//            googleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(Drive.API)
//                    .addScope(Drive.SCOPE_FILE)
//                    .addScope(Drive.SCOPE_APPFOLDER)　　// AppFolderの利用
//            .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .build();
//
//            // 接続ボタンの実装
//            Button connectButton = (Button) findViewById(R.id.connectButton);
//            connectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (googleApiClient != null && !googleApiClient.isConnected()) {
//                        // 接続開始
//                        googleApiClient.connect();
//                    }
//                }
//            });
//
//            // 接続解除ボタンの実装
//            Button disconnectButton = (Button) findViewById(R.id.disconnectButton);
//            disconnectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (googleApiClient != null && googleApiClient.isConnected()) {
//                        // 接続解除
//                        googleApiClient.disconnect();
//                    }
//                }
//            });
//        }

}

