package com.example.hadar.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class GameActivity extends AppCompatActivity {
    public static final int BOARD_CELL10 = 10;
    public static final int BOARD_CELL5 = 5;
    public static final int EASY_FLAGS = 5;
    public static final int HARD_FLAGS = 10;
    public static final int LOSS = 0;
    public static final int WIN = 1;
    public GridView gridview=null;
    public int count, seconds, countOfPressed, chosenLevel;
    private boolean isLost=false, isFirstClick=true;
    private TextView flags, timeout;
    private MineSweeperCell[][] cells;
    private ImageButton newGame;
    private Set<Integer> setFlags;
    private static final String TAG = GameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        newGame=(ImageButton)findViewById(R.id.smile);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressNewGame();
            }
        });

        createNewGame();
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
            switch (chosenLevel) {
                case 0:
                    // mines=EASY_FLAGS;
                    InitBoard(EASY_FLAGS,BOARD_CELL10);
                    createNewGridView(BOARD_CELL10,EASY_FLAGS);
                    break;
                case 1:
                    // mines=HARD_FLAGS;
                    InitBoard(HARD_FLAGS,BOARD_CELL10);
                    createNewGridView(BOARD_CELL10,HARD_FLAGS);
                    break;
                case 2:
                    // mines=HARD_FLAGS;
                    InitBoard(HARD_FLAGS,BOARD_CELL5);
                    createNewGridView(BOARD_CELL5,HARD_FLAGS);
                    break;
            }
        }
    }

    //set the third row on the table layout
    public GridView createNewGridView(final int colsNum, final int mines) {
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setNumColumns(colsNum);
        gridview.setAdapter(new ImageAdapterLevel(this, cells));
        //short click on the cell to open
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                int indexMine;
                if (!cells[position/cells[0].length][position%cells[0].length].longPressed() && isLost==false) {
                    if (isFirstClick == true) {
                        isFirstClick = false;
                        timer(mines);
                    }
                    if (cells[position / cells[0].length][position % cells[0].length].getStatus() == -1) {
                        Iterator itr = setFlags.iterator();
                        indexMine = (int) itr.next();
                        while (itr.hasNext()) {
                            cells[indexMine / cells.length][indexMine % cells[0].length].pressButon();
                            ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                            indexMine = (int) itr.next();
                        }

                        cells[indexMine / cells.length][indexMine % cells[0].length].pressButon();
                        ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();

                        isLost = true;
                    } else {
                        openCellRec(position / cells[0].length, position % cells[0].length);

                    }
                    cells[position / cells[0].length][position % cells[0].length].pressButon();
                    ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                    Log.d(TAG, "number of pressed cells is " + countOfPressed);

                }
            }
        });
        //long click on the cell to put flag
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!cells[position / cells[0].length][position % cells[0].length].pressed() && isLost==false) {
                    if (!cells[position / cells[0].length][position % cells[0].length].longPressed()) {
                        if (count > 0) {
                            count -= 1;
                            cells[position / cells[0].length][position % cells[0].length].pressLongButon();
                            ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                        }
                    } else if (cells[position / cells[0].length][position % cells[0].length].longPressed()) {
                        count += 1;
                        cells[position / cells[0].length][position % cells[0].length].pressLongButon();
                        ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
                    }
                    flags.setText(String.valueOf(count));
                }
                return true;
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

    //timer thread method
    public void timer(final int mines) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                timeout.setText(String.valueOf(seconds));
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                seconds = 0;
                while (true) {
                    if (isFirstClick==true)
                        break;
                    if(isLost == true){

                        /** sleep in order to see all mines **/

                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }

                        GameActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Intent intent=new Intent(GameActivity.this, ResultActivity.class);
                                intent.putExtra("Result", LOSS);
                                intent.putExtra("Difficulty", chosenLevel);
                                startActivity(intent);
                            }
                        });
                        break;
                    }

                    else{

                        if(countOfPressed+mines>=cells.length*cells[0].length&& isLost==false){

                            Log.d(TAG,"you are winning! time = "+seconds);
                            GameActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Intent intent=new Intent(GameActivity.this, ResultActivity.class);
                                    intent.putExtra("Result", WIN);
                                    intent.putExtra("Points", seconds);
                                    intent.putExtra("Difficulty", chosenLevel);
                                    startActivity(intent);
                                }
                            });

                            break;
                        }
                    }
                    tick();
                    handler.sendEmptyMessage(0);

                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }

                try {
                    this.finalize();
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }

    //sec count for timer
    private void  tick() {
        seconds++;
    }

    //set the board game bomb
    public void InitBoard(int lavel, int boardSize) {
        int index;
        count=lavel;
        cells = new MineSweeperCell[boardSize][boardSize];
        // in order to avoid duplications
        setFlags = new LinkedHashSet<>();
        // five random cells with flags
        while(setFlags.size()<lavel){
            Random r = new Random();
            index = r.nextInt(boardSize*boardSize) + 0;
            setFlags.add(index);
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

    //calculate the bomb area for each cell helper
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

    //calculate the bomb area for each cell
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
                cells[x][y].pressButon();
                countOfPressed++;
                ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
            }
            return;
        }
        if (cells[x][y].getStatus() == 0 && cells[x][y].pressed() == false && cells[x][y].longPressed() == false) {
            cells[x][y].pressButon();
            countOfPressed++;
            ((ImageAdapterLevel) gridview.getAdapter()).notifyDataSetChanged();
            openCellRec(x - 1, y); //left
            openCellRec(x, y + 1);//down
            openCellRec(x, y - 1);//right
            openCellRec(x + 1, y); //up
        }
    }
}


class MineSweeperCell  {
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

    public int getStatus() {
        return status;
    }

    public void setStatus( int status){
        this.status = status;
    }

    public void pressButon() {
        this.isPressed = true;
    }

    public boolean pressed(){
        return isPressed;
    }

    public void pressLongButon() {
        this.isLongPressed ^= true;
    }

    public boolean longPressed(){
        return isLongPressed;
    }

}