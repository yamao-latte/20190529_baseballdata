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

public class Position_Selector extends AppCompatActivity {

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
        final int team_flag=intent.getIntExtra("TEAM_FLAG",0);
        final int league_flag=intent.getIntExtra("LEAGUE_FLAG",0);
        ListItem item = new ListItem();

        item.setText("投手");
        item.setImageId(R.drawable.pitcher);
        list.add(item);
        item = new ListItem();
        item.setText("野手");
        item.setImageId(R.drawable.fielder);
        list.add(item);


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

                intent = new Intent(Position_Selector.this,PlayerList.class);
                intent.putExtra("POSITION_FLAG",position);
                intent.putExtra("TEAM_FLAG",team_flag);
                intent.putExtra("LEAGUE_FLAG",league_flag);
                //0なら投手　
                //1なら野手
                startActivity(intent);
            }
        });


    }

}
