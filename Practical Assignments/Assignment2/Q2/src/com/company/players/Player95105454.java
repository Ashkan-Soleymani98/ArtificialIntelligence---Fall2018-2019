package com.company.players;
import com.company.Board;
import com.company.Cell;
import com.company.IntPair;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Player95105454 extends Player
{

    public Player95105454(int col) {
        super(col);
    }

    @Override

    public IntPair getMove(Board s)
    {

        Nd max = maximizer(s,-1000,1000,8);



        if (max == null)
        {
            int currentx1 = s.getHead(this.getCol()).x;
            int currenty1 = s.getHead(this.getCol()).y;

            if (currentx1+1 != 20 && s.getCell(currentx1+1,currenty1).getColor() == 0)
            {
                IntPair output1 = new IntPair(currentx1+1,currenty1);
                return output1;
            }
            else if (currentx1-1 != -1 && s.getCell(currentx1-1,currenty1).getColor() == 0)
            {
                IntPair output2 = new IntPair(currentx1-1,currenty1);
                return output2;
            }
            else if (currenty1+1 != 20 && s.getCell(currentx1,currenty1+1).getColor() == 0)
            {
                IntPair output3 = new IntPair(currentx1,currenty1+1);
                return output3;
            }
            else if (currenty1-1 != -1 && s.getCell(currentx1+1,currenty1).getColor() == 0 )
            {
                IntPair output4 = new IntPair(currentx1,currenty1-1);
                return output4;
            }
            else
            {
                IntPair output5 = new IntPair(0,0);
                return output5;
            }
        }

        int xadd = max.place.x;
        int yadd = max.place.y;

        int currentx = s.getHead(this.getCol()).x;
        int currenty = s.getHead(this.getCol()).y;

        IntPair output = new IntPair(xadd + currentx,yadd + currenty);

        return output;

        /*Cell checker = board.getCell(1,1);
        int color_number = getCol();
        IntPair player_place = board.getHead(color_number);
        int place_x = player_place.x;
        int place_y = player_place.y;
        IntPair destination = new IntPair(0,0);
        if (place_x < 19 && board.getCell(place_x+1,place_y).getColor() != 1 && board.getCell(place_x+1,place_y).getColor() != 2)
        {
                IntPair finale = new IntPair(place_x+1,place_y);
                destination = finale;
        }
        else if (place_y < 19 && board.getCell(place_x,place_y+1).getColor() != 1 && board.getCell(place_x,place_y+1).getColor() != 2)
        {
                IntPair finale = new IntPair(place_x,place_y+1);
                destination = finale;
        }
        else if (place_x > 0 && board.getCell(place_x-1,place_y).getColor() != 1 && board.getCell(place_x-1,place_y).getColor() != 2)
        {
                IntPair finale = new IntPair(place_x-1,place_y);
                destination = finale;
        }
        else if (place_y > 0 && board.getCell(place_x,place_y-1).getColor() != 1 && board.getCell(place_x,place_y-1).getColor() != 2)
        {
                IntPair finale = new IntPair(place_x,place_y-1);
                destination = finale;
        }
        return destination;*/
    }

    public Nd maximizer (Board s, int alpha, int beta,int depth)
    {

        if (depth == 0)
        {
            return new Nd(null,state_score(s));
        }

        int color = this.getCol();

        ArrayList<IntPair> moves = childs(s,color);

        Nd max = null;

        for (IntPair move : moves)
        {
            Board next = new Board(s);
            IntPair iterator = new IntPair(move.x + s.getHead(color).x,move.y + s.getHead(color).y);
            next.move(iterator,color);
            Nd min = minimizer(next,alpha,beta,depth -1 );

            if (min != null && ((max == null) || min.value > max.value))
            {
                max = new Nd(move,min.value);
            }

            if (max != null && max.value > alpha)
            {
                alpha = max.value;
            }

            if (beta <= alpha)
            {
                break;
            }
        }
        return max;
    }

    public Nd minimizer (Board s, int alpha, int beta, int depth)
    {

        if (depth == 0)
        {
            return new Nd(null,state_score(s));
        }

        int color = this.getCol();

        if (color == 1)
        {
            color = 2;
        }
        else
        {
            color = 1;
        }

        ArrayList<IntPair> moves = childs(s,color);

        Nd min = null;

        for (IntPair move : moves)
        {
            Board next = new Board(s);
            IntPair iterator = new IntPair(move.x + s.getHead(color).x,move.y + s.getHead(color).y);
            next.move(iterator,color);
            Nd max = maximizer(next,alpha,beta,depth-1);

            if (max != null && ((min == null) || min.value > max.value))
            {
                min = new Nd(move,max.value);
            }

            if (min != null && min.value < beta)
            {
                beta = min.value;
            }

            if (beta <= alpha)
            {
                break;
            }
        }
        return min;
    }

    private int state_score(Board s)
    {
        int your_posib = 0;
        int op_posib = 0;
        int your_dest = 0;
        int op_dest = 0;

        int your_color = this.getCol();
        int op_color = 0;
        if (your_color == 1)
        {
            op_color = 2;
        }
        else
        {
            op_color = 1;
        }

        IntPair test1 = new IntPair(1,0);
        your_posib = your_posib + is_posib(s,your_color,test1);

        IntPair test2 = new IntPair(-1,0);
        your_posib = your_posib + is_posib(s,your_color,test2);

        IntPair test3 = new IntPair(0,1);
        your_posib = your_posib + is_posib(s,your_color,test3);

        IntPair test4 = new IntPair(0,-1);
        your_posib = your_posib + is_posib(s,your_color,test4);

        IntPair test5 = new IntPair(1,0);
        op_posib = op_posib + is_posib(s,op_color,test5);

        IntPair test6 = new IntPair(-1,0);
        op_posib = op_posib + is_posib(s,op_color,test6);

        IntPair test7 = new IntPair(0,1);
        op_posib = op_posib + is_posib(s,op_color,test7);

        IntPair test8 = new IntPair(0,-1);
        op_posib = op_posib + is_posib(s,op_color,test8);

        int your_x = s.getHead(your_color).x;
        int your_y = s.getHead(your_color).y;

        int op_x = s.getHead(op_color).x;
        int op_y = s.getHead(op_color).y;

        if (your_x > 10)
        {
            your_dest += (your_x - 10);
        }
        else
        {
            your_dest += (10 - your_x);
        }

        if (op_y > 10)
        {
            op_dest += (op_y - 10);
        }
        else
        {
            op_dest += (10 - op_y);
        }

        if (your_x > 10)
        {
            op_dest += (op_x - 10);
        }
        else
        {
            op_dest += (10 - op_x);
        }

        if (your_y > 10)
        {
            op_dest += (op_y - 10);
        }
        else
        {
            op_dest += (10 - op_y);
        }

        return op_dest - your_dest + your_posib * 100 - op_posib*50;
    }

    public int is_posib (Board s, int col, IntPair move)
    {
        int plc_x = s.getHead(col).x;
        int plc_y = s.getHead(col).y;
        IntPair destination = new IntPair(plc_x + move.x,plc_y+move.y);
        if (destination.x > -1 && destination.x < 20 && destination.y > -1 && destination.y < 20 && s.getCell(destination.x,destination.y).getColor() == 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public ArrayList<IntPair> childs(Board s,int col)
    {
        ArrayList<IntPair> childs = new ArrayList<>();
        IntPair test1 = new IntPair(0,1);
        if (is_posib(s,col,test1) == 1)
        {
            childs.add(test1);
        }

        IntPair test2 = new IntPair(0,-1);
        if (is_posib(s,col,test2) == 1)
        {
            childs.add(test2);
        }

        IntPair test3 = new IntPair(1,0);
        if (is_posib(s,col,test3) == 1)
        {
            childs.add(test3);
        }

        IntPair test4 = new IntPair(-1,0);
        if (is_posib(s,col,test4) == 1)
        {
            childs.add(test4);
        }

        return childs;
    }

    class Nd
    {
        IntPair place;
        int value;

        public Nd(IntPair place, int value)
        {
            this.value = value;
            this.place = place;
        }
    }
}