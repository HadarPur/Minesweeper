package com.example.hadar.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//based on https://developer.android.com/guide/topics/ui/layout/gridview.html
public class ImageAdapterLevel extends BaseAdapter {
    private Context mContext;
    private MineSweeperCell[][] board;
    private static final String TAG = GameActivity.class.getSimpleName();

    public ImageAdapterLevel(Context c, MineSweeperCell[][] cells) {
        this.mContext = c;
        this.board = new MineSweeperCell[cells.length][cells[0].length];
        createNewBoard(cells);
    }

    public int getCount() {
        return board.length*board[0].length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView
    public View getView(int position, View convertView, ViewGroup parent) {
        CellView vCell;
        vCell = (CellView) convertView;
        if (convertView == null) {
            vCell = new CellView(mContext);
        }
        else {
            if(board[position/board.length][position%board[0].length].pressed()==true){
                //Log.d(TAG,"position "+position + " is pressed");
                if(board[position/board.length][position%board[0].length].getStatus() == -1) {
                    vCell.setBackgroundResource(R.drawable.tablebombopen);
                }
                else {
                    vCell.setBackgroundResource(R.drawable.tableopen);
                    vCell.bringToFront();
                    if(board[position/board.length][position%board[0].length].getStatus()!=0)
                        vCell.txt.setText(String.valueOf(board[position / board.length][position % board[0].length].getStatus()));
                }
            }
            else {
                if (board[position / board.length][position % board[0].length].longPressed() == true) {
                    vCell.setBackgroundResource(R.drawable.tableflag);
                    vCell.bringToFront();
                }
                if (board[position / board.length][position % board[0].length].longPressed() == false) {
                    vCell.setBackgroundResource(R.drawable.table);
                    vCell.bringToFront();
                }
            }
        }
        return vCell;
    }

    // references to our images
    private void createNewBoard(MineSweeperCell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                // Log.d(TAG,"position: "+(i*cells.length+j)+ " value: "+cells[i][j].getStatus());
                this.board[i][j]= cells[i][j];
            }
        }
    }
}