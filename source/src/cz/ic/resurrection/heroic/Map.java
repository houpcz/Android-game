package cz.ic.resurrection.heroic;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class Map {
	public static byte[][] loadBoard(String name, Context context)
	{
		byte [][] board = new byte[Board.BOARD_WIDTH][Board.BOARD_WIDTH];
		
		AssetManager asset = context.getAssets();
        InputStream inputStream = null;

        try {
            inputStream = asset.open("maps/" + name + ".lev");
            DataInputStream in = new DataInputStream(inputStream);
            
            int layer = readInt(in);
            int width = readInt(in);
            int height = readInt(in);
            
            int loop1, loop2, loop3;
            for(loop1 = 0; loop1 < layer; loop1++)
                for(loop3 = 0; loop3 < height; loop3++)
                    for(loop2 = 0; loop2 < width; loop2++)
                    {
                        board[loop3][loop2] = getFigureNumber(readInt(in));
                    }
        } catch (IOException e) {
            Log.e(GameCore.LOG_TAG, e.getMessage());
        }
        
		return board;
	}
	private static byte getFigureNumber(int figure)
	{
		//Log.d(GameCore.LOG_TAG, figure + "");
		if(figure == 0)
			return 0;
		if(figure < 24)
			return (byte) -(figure - 7);
		else
			return (byte) (figure - 23);
	}
	
	private static int readInt(DataInputStream in)
    {
        byte [] b = new byte[4];
        try {
            in.read(b);
        } catch(Exception io) {System.out.println(io.getMessage());}
        int [] i = new int[4];
        for(int loop1 = 0; loop1 < 4; loop1++)
            i[loop1] = (b[loop1] < 0) ? b[loop1] + 256 : b[loop1];

        int temp = i[0] + i[1] * 256 + i[2] * 65535;    // treti nebude potreba
        //if(temp > 64) {
        //    System.out.println(temp);
        //    System.out.println(i[0] + "    " + i[1] + "     " + i[2] + "    " + i[3]);
        //}
        return temp;
    }
}
