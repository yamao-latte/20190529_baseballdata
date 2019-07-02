package com.example.okuyamatakahiro.a20190529_baseballdata;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PlayerList extends  AppCompatActivity{
    ArrayList<String> text;

    public String pitch_stats[]={
            "防御率 0.98 登板 6 IP 46 奪三振 46",
            "防御率 3.77 登板 8 IP 45.1 奪三振 40",
            "防御率 24.00 登板 1 IP 3 奪三振 3",
            "防御率 9.72 登板 9 IP 8.1 奪三振 12",
            "防御率 0.79 登板 12 IP 11.1 奪三振 12",
            "防御率 5.40 登板 12 IP 16.2 奪三振 17"
    };
    public String fielder_stats[]={
            "打率 .263 本塁打 2 打点 7 盗塁 0 出塁率 .314 長打率 .350 得点圏 .235",
            "打率 .238 本塁打 5 打点 10 盗塁 0 出塁率 .304 長打率 .413 得点圏 .200",
            "打率 .240 本塁打 11 打点 22 盗塁 0 出塁率 .313 長打率 .537 得点圏 .222",
            "打率 .247 本塁打 0 打点 8 盗塁 1 出塁率 .315 長打率 .268 得点圏 .267",
            "打率 .186 本塁打 3 打点 6 盗塁 0 出塁率 .241 長打率 .298 得点圏 .148",
            "打率 .311 本塁打 9 打点 22 盗塁 0 出塁率 .416 長打率 .613 得点圏 .261"
    };
    public int position_flag=0;



    public class ListItem {

        private int imageId;
        private String text;


        public int getImageId() {
            return imageId;
        }
        public void setImageId(int imageId) {
            this.imageId = imageId;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }
    public class ImageArrayAdapter extends ArrayAdapter<ListItem> {

        private int resourceId;
        private List<ListItem> items;
        private LayoutInflater inflater;

        public ImageArrayAdapter(Context context, int resourceId, List<ListItem> items) {
            super(context, resourceId, items);

            this.resourceId = resourceId;
            this.items = items;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = this.inflater.inflate(this.resourceId, null);
            }

            ListItem item = this.items.get(position);

            // テキストをセット
            TextView appInfoText = (TextView)view.findViewById(R.id.item_text);
            appInfoText.setText(item.getText());

            // アイコンをセット
            ImageView appInfoImage = (ImageView)view.findViewById(R.id.item_image);
            appInfoImage.setImageResource(item.getImageId());

            return view;
        }
    }



    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_league_1);

        List<ListItem> list = new ArrayList<ListItem>();

//        for (int i = 1; i < 6; i++) {
//            ListItem item = new ListItem();
//            item.setText("アイテム" + i);
//            item.setImageId(R.drawable.team_dena);
//            list.add(item);
//        }
        //SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "打率" DESC);


        Intent intent=getIntent();
        // int league_flag=0;
        position_flag=intent.getIntExtra("POSITION_FLAG",0);
        int team_flag=intent.getIntExtra("TEAM_FLAG",0);
        int league_flag=intent.getIntExtra("LEAGUE_FLAG",0);
        PlayerList.ListItem item = new ListItem();
        if(position_flag==0) {
            BaseballDbHelper batterDb = new BaseballDbHelper(PlayerList.this);

            //batterDb.openDataBase();
            //batterDb.getWritableDatabase();
            //String[] text=batterDb.getAppCategoryDetail(batterDb);
            //String[] text=batterDb.getAppCategoryDetaila(batterDb);
            text=batterDb.getAppCategoryDetaila(batterDb,league_flag,team_flag,position_flag);
            //Log.v("テスト：",text[0]);
            for(int i=0;i<text.size();i++){
                item.setText( text.get(i) );
                item.setImageId(R.drawable.dena_patton+(i%7) );
                list.add(item);
                item = new ListItem();
            }

//            item.setText("今永昇太");
//            item.setImageId(R.drawable.dena_imanaga);
//            list.add(item);
//            item = new ListItem();
//            item.setText("田中将大");
//            item.setImageId(R.drawable.dena_masahiro);
//            list.add(item);
//            item = new ListItem();
//            item.setText("東克樹");
//            item.setImageId(R.drawable.dena_azuma);
//            list.add(item);
//            item = new ListItem();
//            item.setText("パットン＝スペンサー");
//            item.setImageId(R.drawable.dena_patton);
//            list.add(item);
//            item = new ListItem();
//            item.setText("山崎康晃");
//            item.setImageId(R.drawable.dena_yamasaki);
//            list.add(item);
//            item = new ListItem();
//            item.setText("国吉祐樹");
//            item.setImageId(R.drawable.dena_kuniyoshi);
//            list.add(item);
        }else if(position_flag==1){
//            SELECT COUNT(*) FROM (SELECT "_rowid_",* FROM "main"."npbbatter" WHERE "チーム" LIKE '%DeNA%' ESCAPE '\' ORDER BY "打率" DESC);
            BaseballDbHelper batterDb = new BaseballDbHelper(PlayerList.this);

            //batterDb.openDataBase();
            //batterDb.getWritableDatabase();
            //String[] text=batterDb.getAppCategoryDetail(batterDb);
            //String[] text=batterDb.getAppCategoryDetaila(batterDb);
            text=batterDb.getAppCategoryDetaila(batterDb,league_flag,team_flag,position_flag);
            //Log.v("テスト：",text[0]);
            for(int i=0;i<text.size();i++){
                item.setText( text.get(i) );
                item.setImageId(R.drawable.dena_itou+(i%7) );
                list.add(item);
                item = new ListItem();
            }

//            item.setText("伊藤光");
//            item.setImageId(R.drawable.dena_itou);
//            list.add(item);
//            item = new ListItem();
//            item.setText("ホセ＝ロペス");
//            item.setImageId(R.drawable.dena_ropez);
//            list.add(item);
//            item = new ListItem();
//            item.setText("ネフタリ＝ソト");
//            item.setImageId(R.drawable.dena_soto);
//            list.add(item);
//            item = new ListItem();
//            item.setText("前田大和" );
//            item.setImageId(R.drawable.dena_yamato);
//            list.add(item);
//            item = new ListItem();
//            item.setText("宮﨑敏郎");
//            item.setImageId(R.drawable.dena_tosirou);
//            list.add(item);
//            item = new ListItem();
//            item.setText("筒香嘉智");
//            item.setImageId(R.drawable.dena_tsutsugoh);
//            list.add(item);

        }
        else{}

        // adapterのインスタンスを作成
        ImageArrayAdapter adapter =
                new ImageArrayAdapter(this, R.layout.league_list_item, list);

        lv =  findViewById(R.id.listview);
        lv.setAdapter(adapter);
        //リスト項目が選択された時のイベントを追加
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseballDbHelper batterDb = new BaseballDbHelper(PlayerList.this);
                ArrayList<String> stats;
                stats=batterDb.getPlayerdata(batterDb,position_flag,text.get(position));
                String msg;
                if(0 == position_flag){
                    //msg=pitch_stats[position%6];
                    msg="防御率 "+stats.get(0)+" 勝利 "+stats.get(1)+" 敗北 "+stats.get(2)+" 試合 "+stats.get(3)+
                            " 投球回 "+stats.get(4)+" セーブ "+stats.get(5)+" ホールド "+stats.get(6)+" 奪三振 "+stats.get(7)+" 与四球 "
                            +stats.get(8)+" WHIP "+stats.get(9);
                }else{
                    //msg=fielder_stats[position%6];
                    msg=" 打率 "+stats.get(0)+" 本塁打 "+stats.get(1)+" 打点 "+stats.get(2)+" 出塁率 "+stats.get(3)+
                            " 長打率 "+stats.get(4)+" 盗塁 "+stats.get(5)+" 三振 "+stats.get(6)+" OPS "+stats.get(7);
                    /*data.add( new Float(cursor.getFloat(cursor.getColumnIndex("打率"))).toString() );*/
                }
                //String msg = position + "番目の防御率 0.98 登板 6 IP 46 奪三振 46";

                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                new QuickToastTask(getApplicationContext(),msg).execute();
            }
        });


    }
    //トーストコントローラー
    //SHORTの値を変更することでトーストの表示時間を変更できる
    public class QuickToastTask extends AsyncTask<String, Integer, Integer> {
        private Toast toast;
        private final Context context;
        //private final int msgResId;
        private String msg;
        private int dispTime;
        private static final int SHORT = 600;

        public QuickToastTask(final Context context,String msg) {
            this.msg = msg;
            this.context = context;
            this.dispTime = SHORT;
        }

        @Override
        protected void onPreExecute() {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            this.toast = toast;
            //toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        @Override
        protected Integer doInBackground(final String... params) {
            try {
                //ここでトースト表示時間分Sleepさせる。
                Thread.sleep(dispTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(final Integer i) {
            //キャンセルするとトーストが消えます。
            this.toast.cancel();
        }
    }


}

