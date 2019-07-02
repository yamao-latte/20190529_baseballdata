package com.example.okuyamatakahiro.a20190529_baseballdata;

/*投手の選手成績の参照・更新クラス
 * NOW!!
 * param:防御率,登板数,投球回,奪三振,与四球,勝利数,敗戦数
 * */
public class PlayerStats {
    public static class Pitcher_stats_DB {


            //GETHTML_PAGEのnumber
            long html_number;
            //所属チーム
            String team;
            //名前
            String name;
            //防御率　Earned Run Average
            float ERA;
            //登板数
            int Games_Played;
            //投球回
            int IP;
            //奪三振
            int StrikeOut;
            //与四球
            int BB;
            //勝利数
            int win;
            //敗戦数
            int loss;

        public Pitcher_stats_DB() {
        }

        public void setALLdata() {

        }

        /*START     GETメソッド*/
        public long getHtml() { return html_number; }
        public String getTeam(){return team;}
        public String getName() { return name; }
        public float getERA() { return ERA; }
        public int getGames_Played() { return Games_Played; }
        public int getIP() { return IP; }
        public int getStrikeOut() { return StrikeOut; }
        public int getBB() { return BB; }
        public int getWin() { return win; }
        public int getloss() { return loss; }
        /*END    GETメソッド*/
        /*START     SETメソッド*/
        public void setHtml(long html) { html_number = html; }
        public void setTeam(String data){team=data;}
        public void setName(String data) { name = data; }
        public void setERA(float data) { ERA = data; }
        public void setGames_Played(int data) { Games_Played = data; }
        public void setIP(int data) { IP = data; }
        public void setStrikeOut(int data) { StrikeOut = data; }
        public void setBB(int data) { BB = data; }
        public void setWin(int data) { win = data; }
        public void setloss(int data) { loss = data; }
        /*END    SETメソッド*/
    }

    /*打撃成績の参照操作クラス
     *NOW
     * param:打数,打率,本塁打,打点,盗塁,出塁率,長打率,得点圏
     *  */
//    public class Fielder_stats_DB {
//
//            //打数 At-Bat
//            int at_bat;
//            //打率 Batting Average
//            float AVG;
//            //本塁打 Home Run
//            int HR;
//            //打点 Runs Batted In
//            int RBI;
//            //盗塁 Stolen Base
//            int SB;
//            //出塁率 On-base Percentage
//            float OBP;
//            //長打率 Slugging Percentage
//            float SLG;
//            //得点圏 Runner In Scoring Position
//            float RISP;
//
//
//        Batting_Stats stat = new Batting_Stats();
//
//        public Fielder_stats_DB(Batting_Stats data) {
//            stat = data;
//        }
//
//        /*START     GETメソッド*/
//        public int getAtBat() {
//            return at_bat;
//        }
//
//        public float getAVG() {
//            return AVG;
//        }
//
//        public int getHR() {
//            return HR;
//        }
//
//        public int getRBI() {
//            return RBI;
//        }
//
//        public int getSB() {
//            return SB;
//        }
//
//        public float getOBP() {
//            return OBP;
//        }
//
//        public float getSLG() {
//            return SLG;
//        }
//
//        public float getRISP() {
//            return RISP;
//        }
//
//        /*END    GETメソッド*/
//        /*START     SETメソッド*/
//        public void setAtBat(int data) {
//            at_bat = data;
//        }
//
//        public void setAVG(float data) {
//            AVG = data;
//        }
//
//        public void setHR(int data) {
//            HR = data;
//        }
//
//        public void setRBI(int data) {
//            RBI = data;
//        }
//
//        public void setSB(int data) {
//            SB = data;
//        }
//
//        public void setOBP(float data) {
//            OBP = data;
//        }
//
//        public void setSLG(float data) {
//            SLG = data;
//        }
//
//        public void setRISP(float data) {
//            RISP = data;
//        }
//        /*END    SETメソッド*/
//    }

}
