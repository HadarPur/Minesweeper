package com.example.hadar.minesweeper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import com.example.hadar.minesweeper.quaries.CallData;
import com.yalantis.starwars.TilesFrameLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import tyrantgit.explosionfield.ExplosionField;


public class GameActivity extends AppCompatActivity  implements SensorEventListener, CallData {
    private static final String TAG =GameActivity.class.getSimpleName();
    private static final int EASY=0, NORMAL=1, HARD=2;
    public static final int BOARD_CELL10 = 10, BOARD_CELL5 = 5;
    public static final int EASY_FLAGS = 5, HARD_FLAGS = 10;
    public static final int LOSS = 0, WIN = 1;
    public final int MAX_RECORDS=10;
    private long lastUpdate = 0;
    private float oldX,oldY,oldZ;
    public int count, seconds, countOfPressed, chosenLevel, isMute;
    private boolean isLost=false, isFirstClick=true,isChangedOnce,isChangeMines, firstAsk=false;
    private TextView flags, timeout;
    private MineSweeperCell[][] cells;
    private ImageButton newGame;
    private Set<Integer> setFlags;
    private RotateAnimation rotate;
    private ExplosionField explode;
    private Thread timerThread;
    private SensorManager sensormanager;
    private Sensor accelerometer;
    private GridView gridview=null;
    private TilesFrameLayout mTilesFrameLayout;
    private MediaPlayer bomb, win;
    private GPSTracker gpsTracker;
    private Location currentLocation;
    private JsonData jsonData;
    private ArrayList<UserInfo> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        isChangedOnce = false;
        isChangeMines = false;
        jsonData=new JsonData();
        sensormanager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensormanager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        newGame=(ImageButton)findViewById(R.id.smile);

        setOnClickButton();
        createNewGame();

        if (!isNetworkAvailable(this))
            showConnectionInternetFailed();
    }

    public  void setOnClickButton () {
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressNewGame();
                rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                newGame.startAnimation(rotate);
            }
        });
    }

    protected void onPause() {
        super.onPause();
        sensormanager.unregisterListener(this);
    }

    protected void onStart(){
        Bundle ex;
        super.onStart();
        gpsTracker = new GPSTracker(this, firstAsk);
        if(gpsTracker.getGPSEnable()&& gpsTracker.getPosition()!=null){
            currentLocation=gpsTracker.getPosition();
            gpsTracker.initLocation();
        }
        else
            showSettingsAlert();
    }

    protected void onResume() {
        super.onResume();
        sensormanager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    //restart the game
    public void pressNewGame() {
        isFirstClick=true;
        seconds=0;
        createNewGame();
    }

    //set a new game
    public void createNewGame() {
        Bundle ex;
        isLost = false;
        countOfPressed=0;
        Intent intent = getIntent();
        if (intent != null) {
            ex=intent.getExtras();
            chosenLevel=ex.getInt("Difficulty");
            isMute=ex.getInt("Volume");
            switch (chosenLevel) {
                case EASY:
                    InitBoard(EASY_FLAGS,BOARD_CELL10);
                    createNewGridView(BOARD_CELL10,EASY_FLAGS);
                    jsonData.readResults(this,EASY);
                    break;
                case NORMAL:
                    InitBoard(HARD_FLAGS,BOARD_CELL10);
                    createNewGridView(BOARD_CELL10,HARD_FLAGS);
                    jsonData.readResults(this,NORMAL);
                    break;
                case HARD:
                    InitBoard(HARD_FLAGS,BOARD_CELL5);
                    createNewGridView(BOARD_CELL5,HARD_FLAGS);
                    jsonData.readResults(this,HARD);
                    break;
            }
        }
    }

    //building the game board
    public GridView createNewGridView(final int colsNum, final int mines) {
        mTilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tiles_frame_layout);
        explode=ExplosionField.attach2Window(this);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setNumColumns(colsNum);
        gridview.setAdapter(new ImageAdapterLevel(this, cells));
        //short click on the cell to open
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(isChangeMines==false) {
                    if (!cells[position / cells[0].length][position % cells[0].length].longPressed()) {
                        if (isFirstClick == true) {
                            //  initialPosition = false;
                            isFirstClick = false;
                            timer(mines);
                        }
                        if (cells[position / cells[0].length][position % cells[0].length].getStatus() == -1) {
                            showAllMines();
                            isLost = true;
                        } else {
                            openCellRec(position / cells[0].length, position % cells[0].length);

                        }
                        cells[position / cells[0].length][position % cells[0].length].pressButton();
                        ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();

                        //animation when the player win
                        if (countOfPressed +setFlags.size() >= cells.length * cells[0].length && isLost == false) {
                            explodeVictoryAnimation();
                        }
                    }
                }
            }
        });
        //long click on the cell to put flag
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean answer=true;
                if(isChangeMines==false) {
                    if (!cells[position / cells[0].length][position % cells[0].length].pressed()) {
                        if (!cells[position / cells[0].length][position % cells[0].length].longPressed()) {
                            if (count > 0) {
                                count -= 1;
                                cells[position / cells[0].length][position % cells[0].length].pressLongButton();
                                ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                            }
                        }
                        else if (cells[position / cells[0].length][position % cells[0].length].longPressed()) {
                            count += 1;
                            cells[position / cells[0].length][position % cells[0].length].pressLongButton();
                            ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                        }
                        flags.setText(String.valueOf(count));
                    }
                    else {
                        answer=false;
                    }
                }
                return answer;
            }
        });
        return gridview;
    }

    //set the text view for the timer and flags count
    public void createTimeStartFlags() {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/mono.ttf");
        timeout=(TextView)findViewById(R.id.timer);
        flags=(TextView) findViewById(R.id.flags);
        flags.setTypeface(type);
        flags.setTextColor(Color.BLACK);
        flags.setText(String.valueOf(count));
        timeout.setTextColor(Color.BLACK);
        timeout.setTypeface(type);
        timeout.setText(String.valueOf(0));
    }

    public void explodeVictoryAnimation() {
        if (isMute==1) {
            win=MediaPlayer.create(this, R.raw.win);
            win.start();
        }
        explode.explode(newGame);
        explode.explode(flags);
        explode.explode(timeout);
        explode.explode(gridview);
    }

    public void showAllMines() {
        int indexMine;
        if (isMute==1) {
            bomb=MediaPlayer.create(this, R.raw.bomb);
            bomb.start();
        }
        Iterator itr = setFlags.iterator();
        while (itr.hasNext()) {
            indexMine = (int) itr.next();
            cells[indexMine / cells.length][indexMine % cells[0].length].pressButton();
            ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
        }
    }

    //timer thread method
    public void timer(final int mines) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                timeout.setText(String.valueOf(seconds));
            }
        };
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    seconds = 0;
                    while (true) {
                        // start counting time from the first click
                        if (isFirstClick == true)
                            break;
                        // going to loose screen when user presses a mine
                        if (isLost == true) {
                        /* sleep in order to see all mines */
                            Thread.sleep(300);
                            //animation when the player lose
                            mTilesFrameLayout.startAnimation();
                            Thread.sleep(1500);
                            GameActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                                    intent.putExtra("Result", LOSS);
                                    intent.putExtra("Difficulty", chosenLevel);
                                    intent.putExtra("Volume", isMute);
                                    if(currentLocation!=null) {
                                        intent.putExtra("locationlat", currentLocation.getLatitude());
                                        intent.putExtra("locationlong", currentLocation.getLongitude());
                                    }
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                                }
                            });
                            break;
                        }
                        else {
                            Thread.sleep(1000);
                            // going to win screen
                            if (countOfPressed + setFlags.size() >= cells.length * cells[0].length && isLost == false) {
                                Thread.sleep(800);
                                GameActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Intent intent=null;
                                        if(userInfo.size()>0) {
                                            if ((userInfo.size() < MAX_RECORDS || userInfo.get(userInfo.size() - 1).getPoints() > seconds)
                                                    && isNetworkAvailable(getApplicationContext())){
                                                intent = new Intent(GameActivity.this, ScoreActivity.class);
                                            }

                                            else {
                                                intent = new Intent(GameActivity.this, ResultActivity.class);
                                            }
                                        }

                                        else if (isNetworkAvailable(getApplicationContext()))
                                            intent = new Intent(GameActivity.this, ScoreActivity.class);
                                        else
                                            intent = new Intent(GameActivity.this, ResultActivity.class);

                                        intent.putExtra("list", userInfo);
                                        intent.putExtra("Result", WIN);
                                        intent.putExtra("Points", seconds);
                                        intent.putExtra("Difficulty", chosenLevel);
                                        intent.putExtra("Volume", isMute);
                                        if (currentLocation != null) {
                                            intent.putExtra("locationlat", currentLocation.getLatitude());
                                            intent.putExtra("locationlong", currentLocation.getLongitude());
                                        }
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    }
                                });
                                break;
                            }
                        }
                        tick();
                        handler.sendEmptyMessage(0);

                        Thread.sleep(1000);
                    }
                    Thread.currentThread().interrupt();

                } catch (Throwable throwable) {
                    finish();
                }
            }
        };

        timerThread = new Thread(r);
        timerThread.start();
    }

    //sec count for timer
    private void  tick() {
        seconds++;
    }

    /* interrupt thread while pressing back button */

    public void onBackPressed() {
        if(!isFirstClick) {
            timerThread.interrupt();
        }
        finish();
    }

    //set the board game with bomb
    public void InitBoard(int lavel, int boardSize) {
        int index;
        count=lavel;
        cells = new MineSweeperCell[boardSize][boardSize];
        // in order to avoid duplications
        setFlags = new LinkedHashSet<>();
        // five random cells with flags
        while(setFlags.size()<lavel){
            Log.d(TAG, "size = " +setFlags.size());
            Random r = new Random();
            index = r.nextInt(boardSize*boardSize) + 0;
            setFlags.add(index);
            Log.d(TAG, "size = " +setFlags.size());
        }

        // fill board with flags matrix
        for(int i=0; i<boardSize;i++) {
            for( int k=0 ; k<boardSize; k++) {
                if (setFlags.contains(i*boardSize+k))
                    cells[i][k]= new MineSweeperCell(i,k, -1);
                else {
                    cells[i][k]=new MineSweeperCell(i,k, 0);
                }
            }
        }
        createTimeStartFlags();
        setBombsNum();
    }

    //calculate the number of bomb in the area for each cell
    public void setBombsNum(){
        int sum,i,j;
        for( i=1 ; i<cells.length-1;i++){
            for( j=1; j<cells[i].length-1; j++){
                //sum = 0;
                if(cells[i][j].getStatus()!=-1){
                    sum = Math.abs(calcSum(i-1, i+1, j-1, j+1));
                    cells[i][j].setStatus(sum);
                }
            }
        }

        // first col
        j=0;
        for(i=1; i<cells.length-1; i++) {
            if(cells[i][j].getStatus()!=-1){
                sum = Math.abs(calcSum(i-1, i+1, j, j+1));
                cells[i][j].setStatus(sum);
            }
        }

        // first row
        i=0;
        for(j=1; j<cells.length-1; j++) {
            if(cells[i][j].getStatus()!=-1){
                sum = Math.abs(calcSum(i, i+1, j-1, j+1));
                cells[i][j].setStatus(sum);
            }
        }

        // last col
        j=cells[0].length-1;
        for(i=1; i<cells.length-1; i++) {
            if(cells[i][j].getStatus()!=-1){
                sum = Math.abs(calcSum(i-1, i+1, j-1, j));
                cells[i][j].setStatus(sum);
            }
        }

        // last row
        i = cells[0].length-1;
        for(j=1; j<cells.length-1; j++) {
            if(cells[i][j].getStatus()!=-1){
                sum = Math.abs(calcSum(i-1, i, j-1, j+1));
                cells[i][j].setStatus(sum);
            }
        }

        i=0;
        j=0;
        if(cells[i][j].getStatus()!=-1){
            sum = Math.abs(calcSum(i, i+1, j, j+1));
            cells[i][j].setStatus(sum);
        }

        j= cells[0].length-1;
        if(cells[i][j].getStatus()!=-1){
            sum = Math.abs(calcSum(i, i+1, j-1, j));
            cells[i][j].setStatus(sum);
        }

        i= cells.length -1;
        if(cells[i][j].getStatus()!=-1){
            sum = Math.abs(calcSum(i-1, i, j-1, j));
            cells[i][j].setStatus(sum);
        }

        j=0;
        if(cells[i][j].getStatus()!=-1){

            sum = Math.abs(calcSum(i-1, i, j, j+1));
            cells[i][j].setStatus(sum);
        }
    }

    //calculate the number of bomb in the area for each cell helper
    public int calcSum (int startRow, int endRow, int startCol, int endCol){
        int sum =0;
        for(int i=startRow; i<=endRow; i++){
            for(int j=startCol; j<=endCol; j++){
                if(cells[i][j].getStatus()== -1){
                    sum+= cells[i][j].getStatus();
                }
            }
        }
        return sum;
    }

    //recursive method to open the cells
    public void openCellRec(int x, int y) {
        if (x < 0 || y < 0 || x > cells.length - 1 || y > cells[x].length - 1)
            return;

        else if (cells[x][y].getStatus() > 0) {
            if(cells[x][y].pressed() == false) {
                cells[x][y].pressButton();
                countOfPressed++;
                ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
            }
            return;
        }
        if (cells[x][y].getStatus() == 0 && cells[x][y].pressed() == false && cells[x][y].longPressed() == false) {
            cells[x][y].pressButton();
            countOfPressed++;
            ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
            openCellRec(x - 1, y); //left
            openCellRec(x, y + 1);//down
            openCellRec(x, y - 1);//up
            openCellRec(x + 1, y); //right
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float x,y,z;
        int index;
        Sensor mySensor = sensorEvent.sensor;

        if(mySensor.getType()== Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {
                lastUpdate = curTime;
                if (isFirstClick) {
                    oldX = x;
                    oldY = y;
                    oldZ = z;
                    if(chosenLevel==0)
                        count=EASY_FLAGS;
                    else {
                        count=HARD_FLAGS;
                    }
                    Log.d(TAG, "still initial flags: "+count);
                }

                if ((Math.abs(oldX - x) >= 5) || (Math.abs(oldY - y) >= 5) || (Math.abs(oldZ - z) >= 5)) {

                    if(isChangeMines==false){
                        countOfPressed=0;
                        for (int i = 0; i < cells.length; i++) {
                            for (int j = 0; j < cells.length; j++) {
                                cells[i][j].unPress();
                                ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                            }
                        }
                    }

                    isChangeMines=true;
                    Random r = new Random();
                    index = r.nextInt(cells.length * cells[0].length) + 0;
                    Log.d(TAG,"index: "+index);
                    if(setFlags.contains(index)==false) {
                        cells[index / cells.length][index % cells.length].setStatus(-1);
                        ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                        setFlags.add(index);
                        setBombsNum();
                        count++;
                        flags.setText(String.valueOf(count));
                        Log.d(TAG,"mines: "+count);
                    }

                    if(setFlags.size() >= cells.length * cells[0].length) {
                        showAllMines();
                        isLost = true;
                    }
                }
                else {
                    isChangeMines=false;
                }

            }

        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Location is not available");
        alertDialog.setMessage("You must permit location to play");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    @Override
    public void performQuery(ArrayList<UserInfo> easyUsers) {
        userInfo=new ArrayList<>();
        userInfo.addAll(easyUsers);

    }


    public void showConnectionInternetFailed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network Connection Failed");
        alertDialog.setMessage("Network is not enabled." +
                "If you want to get in to record table you need" +
                "a connection to the internet / wifi");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null
                && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null
                && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }
        else {
            return false;
        }
    }
}

// this class describes a cell in the game
class MineSweeperCell {
    private int row;
    private int col;
    private int status;
    private boolean isPressed, isLongPressed;

    public MineSweeperCell( int row, int col, int status) {
        this.row = row;
        this.col = col;
        this.status = status;
        this.isPressed = false;
        this.isLongPressed=false;
    }

    // returns number of bombs around the cell
    public int getStatus() {
        return status;
    }

    //set number of bombs around the cell
    public void setStatus( int status){
        this.status = status;
    }

    // chang cell status to be pressed
    public void pressButton() {
        this.isPressed = true;
    }

    //update cell status to be unpressed
    public void unPress() {this.isPressed = false; }

    //returns if cell was pressed
    public boolean pressed(){
        return isPressed;
    }

    //returns if cell was long pressed
    public void pressLongButton() {
        this.isLongPressed ^= true;
    }

    // chang cell status to be long pressed
    public boolean longPressed(){
        return isLongPressed;
    }

}
