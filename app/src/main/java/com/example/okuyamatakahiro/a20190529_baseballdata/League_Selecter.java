package com.example.okuyamatakahiro.a20190529_baseballdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class League_Selecter extends AppCompatActivity {

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
        Intent intent=getIntent();
        //int league_flag=0;
        final  int league_flag=intent.getIntExtra("LEAGUE_FLAG",0);
        ListItem item = new ListItem();
        if(league_flag==1) {
            item.setText("横浜DeNAベイスターズ");
            item.setImageId(R.drawable.team_dena);
            list.add(item);
            item = new ListItem();
            item.setText("読売ジャイアンツ");
            item.setImageId(R.drawable.team_yomiuri);
            list.add(item);
            item = new ListItem();
            item.setText("広島東洋カープ");
            item.setImageId(R.drawable.team_hiroshima);
            list.add(item);
            item = new ListItem();
            item.setText("東京ヤクルトスワローズ");
            item.setImageId(R.drawable.team_yakuruto);
            list.add(item);
            item = new ListItem();
            item.setText("阪神タイガース");
            item.setImageId(R.drawable.team_hanshin);
            list.add(item);
            item = new ListItem();
            item.setText("中日ドラゴンズ");
            item.setImageId(R.drawable.team_chunichi);
            list.add(item);
        }else if(league_flag==2){
            item.setText("福岡ソフトバンクホークス");
            item.setImageId(R.drawable.team_softbank);
            list.add(item);
            item = new ListItem();
            item.setText("埼玉西武ライオンズ");
            item.setImageId(R.drawable.team_lions);
            list.add(item);
            item = new ListItem();
            item.setText("北海道日本ハムファイターズ");
            item.setImageId(R.drawable.team_fighters);
            list.add(item);
            item = new ListItem();
            item.setText("東北楽天ゴールデンイーグルス" );
            item.setImageId(R.drawable.team_rakuten);
            list.add(item);
            item = new ListItem();
            item.setText("オリックスバファローズ");
            item.setImageId(R.drawable.team_orix);
            list.add(item);
            item = new ListItem();
            item.setText("千葉ロッテマリーンズ");
            item.setImageId(R.drawable.team_marines);
            list.add(item);


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
                //String msg = position + "番目のアイテムがクリックされました";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                Intent intent;
                intent = new Intent(League_Selecter.this,Position_Selector.class);
                intent.putExtra("TEAM_FLAG",position);
                intent.putExtra("LEAGUE_FLAG",league_flag);
                /*0:dena,1:巨人,2:カープ,3:ヤクルト,4:阪神,5:中日,*/
                /*0:SB,  1:西武,2:日ハム,3:楽天,   4:オリ ,5:ロッテ,*/
                startActivity(intent);
            }
        });


    }

}

