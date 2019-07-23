package com.example.okuyamatakahiro.a20190529_baseballdata;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class mainProgressDialog extends AsyncTask<String, Void, String> {

    private Activity m_Activity;
    public ProgressDialog m_ProgressDialog;
    private AlertDialog.Builder builder;
    private Dialog dialog;

    public mainProgressDialog(Activity activity) {
        // 呼び出し元のアクティビティ
        m_Activity = activity;
        builder = new AlertDialog.Builder(m_Activity);
        //View view = getLayoutInflater().inflate(R.layout.progress);
        builder.setView(R.layout.progress);
        dialog = builder.create();
    }

    /*
     * 実行前の事前処理
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog.show();

//        ProgressBar progressBar = new ProgressBar(m_Activity,null,android.R.attr.progressBarStyleLarge);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        m_Activity.addContentView(progressBar,params);
// //       layout.addView(progressBar,params);
//        progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar
//        progressBar.setVisibility(View.GONE);     // To Hide ProgressBar
        // プログレスダイアログの生成
//        this.m_ProgressDialog = new ProgressDialog(this.m_Activity);
//        // プログレスダイアログの設定
//        this.m_ProgressDialog.setMessage("実行中...");  // メッセージをセット
//        // プログレスダイアログの表示
//        this.m_ProgressDialog.show();

    }

    @Override
    protected String doInBackground(String... ImagePath) {
        //ここにバックグラウンドで行う処理を書く
        BaseballDbHelper db=new BaseballDbHelper(this.m_Activity,this.m_Activity);
        db.updatePlayerdata(db);

        return "処理";
    }

    @Override
    protected void onPostExecute(String result) {

        //ここにdoInBackground終了後に行う処理を書く
        Log.v("AsyncTaskProgress", "onend()");
        // プログレスダイアログを閉じる
//        if (this.m_ProgressDialog != null && this.m_ProgressDialog.isShowing()) {
//            this.m_ProgressDialog.dismiss();
//        }
//        if(result=="処理") dialog.dismiss();
        try{
            Thread.sleep(10000); //3000ミリ秒Sleepする
        }catch(InterruptedException e){}

        dialog.dismiss();

        Log.d("test",result);
        super.onPostExecute(result);

    }
    /*
     * キャンセル時の処理
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();

        Log.v("AsyncTaskProgress", "onCancelled()");
        if (this.m_ProgressDialog != null) {
            Log.v("this.m_Progress", String.valueOf(this.m_ProgressDialog.isShowing()));
            // プログレスダイアログ表示中の場合
            if (this.m_ProgressDialog.isShowing()) {
                // プログレスダイアログを閉じる
                this.m_ProgressDialog.dismiss();
            }
        }

        return;
    }


}