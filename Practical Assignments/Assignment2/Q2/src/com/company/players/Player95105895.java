package com.company.players;

import com.company.Board;
import com.company.IntPair;

/**
 * Created by Aryan on 11/8/2018.
 */
class State{
    public int index = 0;
    public int value = 0;
    public State(int index, int value){
        this.index = index;
        this.value = value;
    }
}
public class Player95105895 extends Player {
    public final static int maxInt = 1000000000;
    public final static int minInt = -1000000000;
    public final static int maxDepth = 5;
    public Player95105895(int col)
    {
        super(col);
    }

    public int computeSeeds(Board board, int player)
    {
        IntPair h = board.getHead(player);
        int sum = 0;
        for (int i = 0; i < 20; i++)
        {
            for(int j = 0; j < 20; j ++)
            {
                if (board.getCellValues(i, j) > 0){
                    sum += Math.abs(i - h.x) + Math.abs(j - h.y);
                }
            }
        }
        return sum;
    }
    public boolean A(Board board, int player)
    {
        IntPair h = board.getHead(player);
        IntPair t = board.getTail(player);
        int startI = Math.min(t.x, h.x);
        int startJ = Math.min(t.y, h.y);
        int  sum = 0;
        for (int i = startI; i < startJ; i++)
        {
            for(int j = startJ; j < startJ; j++)
            {
                if (board.getCell(i, j).getColor() == player)
                {
                    sum += 1;
                }

            }
        }
        if((sum / ((Math.abs(t.x - h.x) + 1) * (Math.abs(t.y - h.y) + 1))) > 0.4)
            return false;
        return true;
    }

    public int computeDanger(Board board, int player)
    {
        IntPair h = board.getHead(player);
        int sum = 0;
        for(int i = h.y - 5; i < h.y + 5; i++)
        {
            for(int j = h.x - 5; j < h.x + 5; j++)
            {
                if (i < 0)
                    sum += 0 ;
                else if (i > 19)
                    sum += 0;
                else if (j < 0)
                    sum += 0;
                else if(j > 19)
                    sum += 0 ;
                else if (board.getCell(i, j).getColor() == player)
                    sum += 3;
                else if (board.getCell(i, j).getColor() == player - 3)
                    sum += 1 ;

            }
        }
        return sum;
    }
    public int numSeeds(Board board)
    {
        int num= 0;
        for (int i = 0; i < 20; i++)
        {
            for(int j = 0; j < 20; j ++)
            {
                num += board.getCellValues(i, j);
            }
        }
        return num;
    }
    public int headToTail(Board board, int player)
    {
        return Math.abs(board.getTail(player).x - board.getHead(player).x) + Math.abs(board.getTail(player).y - board.getHead(player).y);
    }
    public int maxDist(Board board, int player)
    {
        IntPair h = board.getHead(player);
        int max = 0;
        int dist = 0;
        for(int  i = h.y + 1; i < 20; i++)
        {
            if(board.getCell(h.x, i).getColor() != 0 || i == 19 ){
                dist = i - h.y;
                break;
            }
        }
        if(dist > max)
            max = dist;
        for(int  i = h.y - 1; i >= 0; i--)
        {
            if(board.getCell(h.x, i).getColor() != 0 || i == 0){
                dist = h.y - i;
                break;
            }
        }
        if(dist > max)
            max = dist;
        for(int  i = h.x + 1; i < 20; i++)
        {
            if(board.getCell(i, h.y).getColor() != 0 || i == 19){
                dist = i - h.x;
                break;
            }
        }
        if(dist > max)
            max = dist;
        for(int  i = h.x - 1; i >= 0; i--)
        {
            if(board.getCell(i, h.y).getColor() != 0 || i == 0){
                dist = h.x - i;
                break;
            }
        }
        if(dist > max)
            max = dist;
        return max;
    }

    public int evaluationFunction(Board board)
    {
        int w1 = 90;
        int w2 = -70;
        int w3 = -30;
        int w4 = 30;
        int w5 = -3;
        int w6 = 95;
        int w7 = -45;
        int w8 = -1;
        int w9 = 1;
        int w10 = 8;
        int w11  = -2;


        int f1 = board.getLength(getCol());
        int f2 = (board.getLength(3 - getCol()));
        int f3 = computeDanger(board, getCol());
        int f4 = computeDanger(board, 3 - getCol());
        int f5 = numSeeds(board);
        int f6 = maxDist(board, getCol());
        int f7 = maxDist(board, 3 - getCol());
        int f8 = computeSeeds(board,getCol());
        int f9 = computeSeeds(board, 3 - getCol());
        int f10 = headToTail(board, getCol());
        int f11 = headToTail(board, 3 - getCol());




        return w1 * f1 + w2 * f2 + w3 * f3 + w4 * f4 +  w6 * f6 + w7 * f7 + w8 * f8 + w9 * f9 + w10 * f10 + w11 * f11 ;

    }
    public boolean possible(Board board, int x,int y)
    {
        if (x >= 0 && x < 20 && y >= 0 && y < 20)
        {
            if(board.getCell(x, y).getColor() == 0 || (board.getCell(x, y).getColor() == 3 - getCol() && board.getLength(getCol()) > board.getLength(3 - getCol())))
                return true;
        }

        return false;
    }
    public IntPair getMove(Board board)
    {
        IntPair h = board.getHead(getCol());
        State state = maxValue(board, minInt, maxInt, 0,8);
        int index = state.index;
        IntPair move = new IntPair(-10, -10);
        if (index == 0){
            move =  new IntPair(h.x, h.y + 1);
        }
        else if(index == 1){
            move =  new IntPair(h.x + 1, h.y);
        }
        else if(index == 2){
            move =  new IntPair(h.x, h.y - 1);
        }
        else
        {
            move = new IntPair(h.x - 1, h.y);
        }
        if (possible(board, move.x, move.y)){
            return move;
        }
        else{

            if(possible(board,h.x + 1, h.y))
                return new IntPair(h.x + 1, h.y);
            if(possible(board,h.x - 1, h.y))
                return new IntPair(h.x - 1, h.y);
            if(possible(board, h.x, h.y + 1))
                return new IntPair(h.x, h.y + 1);
            if(possible(board, h.x, h.y - 1))
                return new IntPair(h.x , h.y - 1);
        }
        return new IntPair(h.x , h.y - 1);
    }
    public State maxValue(Board board, int alpha, int beta, int res, int depth)
    {
        if (depth == 0)
        {
            return new State(0,evaluationFunction(board));
        }
        else if(res == -2)
        {
            if (board.getLength(getCol()) > board.getLength(3 - getCol()))
            {
                return new State(0,10000);
            }
            else if (board.getLength(getCol()) < board.getLength(3 - getCol()))
            {
                return new State(0, -10000);
            }
            else
            {
                return new State(0, 0);
            }
        }
        else if (res == -1)
        {
            return new State(0,10000);
        }
        int v = minInt;
        int index = 0;
        IntPair h = board.getHead(getCol());
        for (int i = 0; i < 4; i++)
        {
            if (i == 0)
            {
                Board cb0 = new Board(board);
                int result = cb0.move(new IntPair(h.x, h.y + 1), getCol());
                int minResult = minValue(cb0, alpha, beta, result, depth - 1);
                if (minResult > v)
                {
                    index = i;
                    v = minResult;
                }
                if (v >= beta)
                {
                    return new State(i, v);
                }
                if (v > alpha)
                {
                    alpha = v;
                }
            }
            else if (i == 1)
            {
                Board cb1 = new Board(board);
                int result = cb1.move(new IntPair(h.x + 1, h.y), getCol());
                int minResult = minValue(cb1, alpha, beta, result, depth - 1);
                if (minResult > v)
                {
                    index = i;
                    v = minResult;
                }
                if (v >= beta)
                {
                    return new State(i, v);
                }
                if (v > alpha)
                {
                    alpha = v;
                }
            }
            else if (i == 2)
            {
                Board cb2 = new Board(board);
                int result = cb2.move(new IntPair(h.x, h.y - 1), getCol());
                int minResult = minValue(cb2, alpha, beta, result, depth - 1);
                if (minResult > v)
                {
                    index = i;
                    v = minResult;
                }
                if (v >= beta)
                {
                    return new State(i, v);
                }
                if (v > alpha)
                {
                    alpha = v;
                }
            }
            else
            {
                Board cb3 = new Board(board);
                int result = cb3.move(new IntPair(h.x - 1, h.y), getCol());
                int minResult = minValue(cb3, alpha, beta, result, depth - 1);
                if (minResult > v)
                {
                    index = i;
                    v = minResult;
                }
                if (v >= beta)
                {
                    return new State(i, v);
                }
                if (v > alpha)
                {
                    alpha = v;
                }
            }
        }
        return new State(index, v);
    }
    public int minValue(Board board, int alpha, int beta, int res, int depth)
    {
        if (depth == 0)
        {
            return evaluationFunction(board);
        }
        else if(res == -2)
        {
            if (board.getLength(getCol()) > board.getLength(3 - getCol()))
            {
                return 10000;
            }
            else if (board.getLength(getCol()) < board.getLength(3 - getCol()))
            {
                return -10000;
            }
            else
            {
                return 0;
            }
        }
        else if (res == -1)
        {
            return -10000;
        }
        int v = maxInt;
        int index = 0;
        IntPair h = board.getHead(3 - getCol());
        for (int i = 0; i < 4; i++)
        {
            if (i == 0)
            {
                Board cb0 = new Board(board);
                int result = cb0.move(new IntPair(h.x, h.y + 1), 3 - getCol());
                int maxResult = maxValue(cb0, alpha, beta, result, depth - 1).value;
                if (maxResult < v)
                {
                    v = maxResult;
                }
                if (v <= alpha)
                {
                    return v;
                }
                if (v < beta)
                {
                    beta = v;
                }
            }
            else if (i == 1)
            {
                Board cb1 = new Board(board);
                int result = cb1.move(new IntPair(h.x + 1, h.y), 3 - getCol());
                int maxResult = maxValue(cb1, alpha, beta, result, depth - 1).value;
                if (maxResult < v)
                {
                    v = maxResult;
                }
                if (v <= alpha)
                {
                    return v;
                }
                if (v < beta)
                {
                    beta = v;
                }
            }
            else if (i == 2)
            {
                Board cb2 = new Board(board);
                int result = cb2.move(new IntPair(h.x, h.y - 1), 3 - getCol());
                int maxResult = maxValue(cb2, alpha, beta, result, depth - 1).value;
                if (maxResult < v)
                {
                    v = maxResult;
                }
                if (v <= alpha)
                {
                    return v;
                }
                if (v < beta)
                {
                    beta = v;
                }
            }
            else
            {
                Board cb3 = new Board(board);
                int result = cb3.move(new IntPair(h.x - 1, h.y), 3 - getCol());
                int maxResult = maxValue(cb3, alpha, beta, result, depth - 1).value;
                if (maxResult < v)
                {
                    v = maxResult;
                }
                if (v <= alpha)
                {
                    return v;
                }
                if (v < beta)
                {
                    beta = v;
                }
            }
        }
        return v;
    }
}