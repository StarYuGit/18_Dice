package com.example.a18_dice;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //宣告顯示文字用的變數
    TextView player1_money, player2_money,
             player1_result, player2_result,
             message_text, total_bet,
             state_message;
    //宣告按鈕用的變數
    Button plus_1, plus_10, plus_100, plus_1000;
    Button multi_2, division_2, bet_min, bet_max, bet_start;
    //宣告圖片用的變數
    ImageView[] dice1 = new ImageView[4],
                dice2 = new ImageView[4];

    int bet_counter = 0, //設定起始下注總額
        bet = 0, //設定下注基數
        default_money = 100000, //設定預設金額
        default_p1_money = default_money, //設定玩家1初始金額
        default_p2_money = default_money, //設定玩家2初始金額
        DIALOG_ID; //判斷用何種通知用變數

    String operator, //宣告判斷運算用字串變數
           gambler = "player1"; //設定起始玩家

    player player1 = new player(); //將玩家1實體化
    player player2 = new player(); //將玩家2實體化

    Timer timer; //Timer
    TimerTask task; //TimerTask

    Random ran = new Random(); //亂數專用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public class player {
        private int money;
        private int[] dice_4 = new int[4];
        private int score = 0;
        int t_out = 10;


        public void play_dice(final TextView View) {
            if (timer == null) {
                timer = new Timer();
            }
            if (task == null) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t_out--;

                                for (int i = 0; i < 4; i++) {
                                    dice_4[i] = ran.nextInt(6) + 1;
                                }
                                if (gambler.equals("player1")){
                                    for (int i=0; i<4; i++)
                                        change_image(dice1[i], dice_4[i]);
                                } else if (gambler.equals("player2")){
                                    for (int i=0; i<4; i++)
                                        change_image(dice2[i], dice_4[i]);
                                }

                                if (t_out == 0) {
                                    t_out = 10;
                                    bet_start.setEnabled(true);
                                    timer.cancel();
                                    timer = null;
                                    task.cancel();
                                    task = null;
                                    score = chk_dice(dice_4);
                                    change_gambler_message(score, gambler);
                                    View.setText(String.valueOf(score + " 點"));

                                    if (player1.get_score() != 0) {
                                        if (gambler.equals("player2")) {
                                            if (player2.get_score() == 0) {
                                                state_message.setText("等待對手擲骰子..");
                                                p2();

                                            }
                                        }
                                    }
                                    if (player1.get_score() != 0 && player2.get_score() !=0){
                                        wolresult(player1.get_score(), player2.get_score());
                                    }
                                }
                            }
                        });
                    }
                };
                timer.schedule(task, 50, 50);
            }
        }

        void init_score() {
            this.score = 0;
        }

        int get_score() {
            return this.score;
        }

        void set_money(int m) {
            this.money = m;
        }

        int get_money() {
            return this.money;
        }
    }

    public int Betting(String op, int p1_money,int p2_money, int b_counter, int b) {
        switch (op) {
            case "plus":
                b_counter = b_counter + b;
                if (b_counter > p2_money)
                    b_counter = p2_money;
                break;
            case "multi":
                b_counter = b_counter * 2;
                if (b_counter > p2_money)
                    b_counter = p2_money;
                break;
            case "division":
                b_counter = b_counter / 2;
                break;
            case "min":
                b_counter = 1;
                break;
            case "max":
                b_counter = p1_money;
                if(b_counter > p2_money)
                    b_counter = p2_money;
                break;
        }
        state_message.setText("");
        if (p1_money < b_counter)
            b_counter = p1_money;
        bet_counter = b_counter;
        return bet_counter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_1:
                operator = "plus";
                bet = 1;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(), bet_counter, bet)));
                break;
            case R.id.plus_10:
                operator = "plus";
                bet = 10;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.plus_100:
                operator = "plus";
                bet = 100;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.plus_1000:
                operator = "plus";
                bet = 1000;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.multi_2:
                operator = "multi";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.division_2:
                operator = "division";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.bet_min:
                operator = "min";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.bet_max:
                operator = "max";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.bet_start:
                message_text.setText("");
                if (bet_counter == 0){
                    message_text.setText("請下注!!!");
                } else {
                    if (player1.get_score() != 0 && player2.get_score() != 0) {
                        player1_result.setText("");
                        player2_result.setText("");
                    }
                    if (gambler.equals("player1")) {
                        state_message.setText("擲骰中..");
                        bet_start.setEnabled(false);
                        btn_enabled(false);
                        p1();
                    }
                }
        }
    }

    public int chk_dice(int[] r) {
        int result = -1;
        Arrays.sort(r);
        if (r[0] == r[1] && r[1] == r[2] && r[2] == r[3]) {
            result = 100;
        } else if (r[0] == r[1] && r[1] == r[2] || r[1] == r[2] && r[2] == r[3]) {
            result = 0;
        } else if (r[0] == r[1] && r[2] == r[3]) {
            result = r[2] + r[3];

        } else if (r[0] == r[1]) {
            result = r[2] + r[3];
        } else if (r[0] == r[2]) {
            result = r[1] + r[3];
        } else if (r[0] == r[3]) {
            result = r[1] + r[2];

        } else if (r[1] == r[2]) {
            result = r[0] + r[3];
        } else if (r[1] == r[3]) {
            result = r[0] + r[2];

        } else if (r[2] == r[3]) {
            result = r[0] + r[1];
        } else {
            result = 0;
        }
        return result;
    }

    public void change_gambler_message(int s, String g) {
        message_text.setText("");
        if (s == 0) {
            message_text.setText("無點數!\n重骰一次");
        }  else {
            if (s == 100)
                message_text.setText("十八啦!");
            if (g.equals("player1"))
                gambler = "player2";
            if (g.equals("player2"))
                gambler = "player1";
        }

    }

    public void wolresult(int s1, int s2) {
        message_text.setText("");

        if (s1 > s2) {
            message_text.setText(MessageFormat.format("你贏了\n{0}元", bet_counter));
            player1.set_money(player1.get_money() + bet_counter);
            player2.set_money(player2.get_money() - bet_counter);
            bet_counter = 0;
            total_bet.setText(String.valueOf(bet_counter));
            state_message.setText("換你下注");

        } else if (s1 < s2) {
            message_text.setText(MessageFormat.format("你輸了\n{0}元", bet_counter));
            player1.set_money(player1.get_money() - bet_counter);
            player2.set_money(player2.get_money() + bet_counter);
            bet_counter = 0;
            total_bet.setText(String.valueOf(bet_counter));
            state_message.setText("換你下注");

        } else {
            message_text.setText("平手\n重來一次");
            message_text.setGravity(1);
            total_bet.setText(String.valueOf(bet_counter));
            state_message.setText("");
        }
        player1.init_score();
        player2.init_score();
        btn_enabled(true);
        if (player1.get_money() < 1){
            player1.set_money(0);
            player1_money.setTextColor(Color.RED);
            state_message.setText("");
            DIALOG_ID = 1;
            showDialog(DIALOG_ID);
        } else if (player2.get_money() < 1) {
            player2.set_money(0);
            player2_money.setTextColor(Color.RED);
            state_message.setText("");
            DIALOG_ID = 2;
            showDialog(DIALOG_ID);
        }
        player1_money.setText(String.valueOf(player1.get_money()));
        player2_money.setText(String.valueOf(player2.get_money()));
    }

    public void p1(){
        bet_start.setEnabled(false);
        player1.play_dice(player1_result);
    }

    public void p2(){
        bet_start.setEnabled(false);
        player2.play_dice(player2_result);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;

        switch(id)
        {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("訊息")
                        .setMessage("你破產了!!!")
                        .setNegativeButton("重新開始", new DialogInterface.OnClickListener()
                        { //設定確定按鈕
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                reset();
                            }
                        });
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                break;

            case 2:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("訊息") //設定標題文字
                        .setMessage("對手破產了\n你贏了!!!") //設定內容文字
                        .setNegativeButton("重新開始", new DialogInterface.OnClickListener()
                        { //設定確定按鈕
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                reset();
                            }
                        });
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                break;

            default:
                break;
        }
        return dialog;
    }

    public void init(){
        player1_money = (TextView) findViewById(R.id.player1_money);
        player2_money = (TextView) findViewById(R.id.player2_money);
        player1_result = (TextView) findViewById(R.id.player1_result);
        player2_result = (TextView) findViewById(R.id.player2_result);
        message_text = (TextView) findViewById(R.id.message_text);
        total_bet = (TextView) findViewById(R.id.total_bet);
        plus_1 = (Button) findViewById(R.id.plus_1);
        plus_10 = (Button) findViewById(R.id.plus_10);
        plus_100 = (Button) findViewById(R.id.plus_100);
        plus_1000 = (Button) findViewById(R.id.plus_1000);
        multi_2 = (Button) findViewById(R.id.multi_2);
        division_2 = (Button) findViewById(R.id.division_2);
        bet_min = (Button) findViewById(R.id.bet_min);
        bet_max = (Button) findViewById(R.id.bet_max);
        bet_start = (Button) findViewById(R.id.bet_start);

        dice1[0] = (ImageView) findViewById(R.id.dice1_1);
        dice1[1] = (ImageView) findViewById(R.id.dice1_2);
        dice1[2] = (ImageView) findViewById(R.id.dice1_3);
        dice1[3] = (ImageView) findViewById(R.id.dice1_4);
        dice2[0] = (ImageView) findViewById(R.id.dice2_1);
        dice2[1] = (ImageView) findViewById(R.id.dice2_2);
        dice2[2] = (ImageView) findViewById(R.id.dice2_3);
        dice2[3] = (ImageView) findViewById(R.id.dice2_4);

        total_bet.setText(String.valueOf(bet_counter));
        state_message = (TextView) findViewById(R.id.win_or_lose);
        state_message.setGravity(Gravity.CENTER);
        player1.set_money(default_p1_money);
        player2.set_money(default_p2_money);
        player1.init_score();
        player2.init_score();

        player1_money.setText(String.valueOf(player1.get_money()));
        player2_money.setText(String.valueOf(player2.get_money()));
        player1_money.setTextColor(Color.BLACK);
        player2_money.setTextColor(Color.BLACK);
        player1_result.setText("");
        player2_result.setText("");
        message_text.setText("");
        message_text.setGravity(Gravity.CENTER);
        state_message.setText("換你下注");

        plus_1.setOnClickListener(this);
        plus_10.setOnClickListener(this);
        plus_100.setOnClickListener(this);
        plus_1000.setOnClickListener(this);
        multi_2.setOnClickListener(this);
        division_2.setOnClickListener(this);
        bet_min.setOnClickListener(this);
        bet_max.setOnClickListener(this);
        bet_start.setOnClickListener(this);


        for (ImageView imageView : dice1) {
            change_image(imageView, 6);
        }
        for (ImageView imageView : dice2) {
            change_image(imageView, 6);
        }
    }

    public void btn_enabled(boolean t_or_f){
        plus_1.setEnabled(t_or_f);
        plus_10.setEnabled(t_or_f);
        plus_100.setEnabled(t_or_f);
        plus_1000.setEnabled(t_or_f);
        multi_2.setEnabled(t_or_f);
        division_2.setEnabled(t_or_f);
        bet_min.setEnabled(t_or_f);
        bet_max.setEnabled(t_or_f);
    }

    public void reset(){
        /*delaysometime(2,message_text,"重新設定中...");*/
        init();
        message_text.setText("");
    }
    public void change_image(ImageView View, int i){
        switch(i){
            case 1:
                View.setImageResource(R.drawable.dice1);
                break;
            case 2:
                View.setImageResource(R.drawable.dice2);
                break;
            case 3:
                View.setImageResource(R.drawable.dice3);
                break;
            case 4:
                View.setImageResource(R.drawable.dice4);
                break;
            case 5:
                View.setImageResource(R.drawable.dice5);
                break;
            case 6:
                View.setImageResource(R.drawable.dice6);
                break;
        }
    }
     public void delaysometime(int t, TextView View, String messaage){
             try{
                 
                 runOnUiThread(new Runnable() {    //可以臨時交給UI做顯示
                     public void run(){
                     }
             });
         } catch(Exception e){
             e.printStackTrace();
         }
     }
}

