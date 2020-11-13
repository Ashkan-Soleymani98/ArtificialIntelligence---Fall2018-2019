package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Player96105635 extends Player{

    public Player96105635(int col){
        super(col);
    }

    static int maxDepth = 8;

    @Override
    public IntPair getMove(Board board) {
//        long startTime = System.nanoTime();
        IntPair a = alphaBetaSearch(board, maxDepth);
//        long endTime   = System.nanoTime();
//        long totalTime = endTime - startTime;
//        System.out.println(totalTime);
        return a;
    }


    private IntPair alphaBetaSearch(Board board, int depth){
//        board = (Board) deepClone(board);
        Node max = maxValue(board, -10000, 10000, depth, depth);
        while (max == null && depth > 0) {
            max = maxValue(board, -10000, 10000, --depth, depth);
        }
        int xadd = max.place.x;
        int yadd = max.place.y;

        int currentx = board.getHead(this.getCol()).x;
        int currenty = board.getHead(this.getCol()).y;

        IntPair output = new IntPair(xadd + currentx,yadd + currenty);

        return output;
    }

    private Node maxValue(Board board, float alpha, float beta, int depth, int maxDepth){
//        System.out.println("entered");
        int color = this.getCol();

        if (depth == 0)
            return new Node(null, stateScoreCalculate(board,  color, maxDepth));

        ArrayList<IntPair> possibleMoves = possibleMovesFinder(board, color);

        Node max = null;
//        System.out.println("entered");
//        System.out.println(possibleMoves.size());
        for (IntPair move : possibleMoves){
            Board tempBoard = new Board(board);
            IntPair iterator = new IntPair(move.x + board.getHead(color).x,move.y + board.getHead(color).y);
            tempBoard.move(iterator,color);
            Node min = minValue(tempBoard, alpha, beta, depth -1, maxDepth);

            if (min != null && ((max == null) || min.value > max.value)) {
                max = new Node(move, min.value);
//                System.out.println("Max node checked");
            }

            if (max != null && max.value > alpha) {
                alpha = max.value;
            }

            if (beta <= alpha) {
                break;
            }
        }
        return max;
    }

    private Node minValue(Board board, float alpha, float beta, int depth, int maxDepth){
        int color = this.getCol();

        if (depth == 0) {
            return new Node(null, stateScoreCalculate(board, color, maxDepth));
        }

        color = this.getCol() == 1 ? 2:1;

        ArrayList<IntPair> possibleMoves = possibleMovesFinder(board, color);

        Node min = null;
        for (IntPair move : possibleMoves){
            Board tempBoard = new Board(board);
            IntPair iterator = new IntPair(move.x + board.getHead(color).x,move.y + board.getHead(color).y);
            tempBoard.move(iterator,color);
            Node max = maxValue(tempBoard, alpha, beta, depth - 1, maxDepth);

            if (max != null && ((min == null) || max.value > min.value)) {
                min = new Node(move, max.value);
//                System.out.println("Min node checked");
            }

            if (min != null && min.value > alpha) {
                beta = min.value;
            }

            if (beta <= alpha) {
                break;
            }
        }
        return min;
    }

    class Node{
        IntPair place;
        float value;

        public Node(IntPair place, float value)
        {
            this.value = value;
            this.place = place;
        }
    }

    public boolean isPossible(Board board, int col, IntPair move){
        int plc_x = board.getHead(col).x;
        int plc_y = board.getHead(col).y;
        IntPair destination = new IntPair(plc_x + move.x,plc_y+move.y);
        int opponentColor = col == 1 ? 2 : 1;
        if (destination.x < 0 || destination.x >= board.size || destination.y < 0 ||
                destination.y >= board.size || board.getCell(destination.x, destination.y).getColor() == col)
            return false;
        if (board.getCell(destination.x, destination.y).getColor() == opponentColor &&
                board.getLength(col) <= board.getLength(opponentColor))
            return false;
        if (board.getCell(destination.x, destination.y).getColor() == 3)
            return false;
        return true;
    }

    public ArrayList<IntPair> possibleMovesFinder(Board board, int col) {
        ArrayList<IntPair> nextMoves = new ArrayList<IntPair>();
        IntPair test1 = new IntPair(0,1);
        if (isPossible(board,col,test1)) {
            nextMoves.add(test1);
        }

        IntPair test2 = new IntPair(0,-1);
        if (isPossible(board,col,test2)) {
            nextMoves.add(test2);
        }

        IntPair test3 = new IntPair(1,0);
        if (isPossible(board,col,test3)) {
            nextMoves.add(test3);
        }

        IntPair test4 = new IntPair(-1,0);
        if (isPossible(board,col,test4)) {
            nextMoves.add(test4);
        }

        return nextMoves;
    }

    public float stateScoreCalculate(Board board, int color, int depth){
//        Random random = new Random();
//        int test = random.nextInt(1000);
        float score = depth;
        int opponentColor = color == 1 ? 2 : 1;
//        if (depth == maxDepth && test > 631)
//        score += (0.11 + (0.17) * ((float) board.getNumberOfMoves() / 500)) * (board.getLength(color) +
//                (0.5 * (board.getCounter(color) - board.getCounter(opponentColor))) - board.getLength(opponentColor));
        score += (0.14 + (0.17) * ((float) board.getNumberOfMoves() / 500)) * (board.getLength(color) +
                (0.5 * (board.getCounter(color))));
//        System.out.println(score);
//        int[][] cellValues = new int[board.size][board.size];
//        for(int i = 0; i < board.size ; i++){
//            for(int j = 0; j < board.size; j++){
//                cellValues[i][j] = board.getCell(i, j)
//            }
//        }
//        score += dfs(board, color, 4, depth);
//        test = random.nextInt(100);
//        if (test > 601){
        score += (0.39) * ((1.5) * maxSideMove(board, color) - maxSideMove(board, opponentColor));
//            score -= (0.13) * (computeDanger(board, color));
        score += (barrierFinder(board, board.getHead(color), board.getTail(color), color)) *
                (0.01) * (headToTail(board, color) - (0.3) * headToTail(board, opponentColor));
//        }
        return score;
    }

    public int headToTail(Board board, int player)
    {
        return Math.abs(board.getTail(player).x - board.getHead(player).x) + Math.abs(board.getTail(player).y - board.getHead(player).y);
    }

    public int barrierFinder(Board board, IntPair a, IntPair b, int color){
        int counter = 0;
        int x1 = Math.min(a.x, b.x), x2 = Math.max(a.x, b.x);
        int y1 = Math.min(a.y, b.y), y2 = Math.max(a.y, b.y);
        for (int i = x1; i <= x2 ; i++){
            for (int j = y1; j <= y2; j++){
                if (board.getCell(i, j).getColor() == color)
                    counter += 1;
            }
        }
        float proportion = counter / (x2 - x1 + 1) * (y2 - y1 + 1);
        if (proportion < 0.75)
            return -1;
        else
            return +1;
    }
//
//    public int dfs(Board board, int color, int maxdepth, int depth){
//        ArrayList<IntPair> possibleMoves = possibleMovesFinder(board, color);
//        ArrayList<Integer> integers = new ArrayList<>();
//
//        if (depth >= maxdepth || possibleMoves.isEmpty())
//            return depth;
//        int score = depth;
//        for (IntPair move : possibleMoves){
//            Board tempBoard = new Board(board);
//            IntPair iterator = new IntPair(move.x + board.getHead(color).x,move.y + board.getHead(color).y);
//            tempBoard.move(iterator, color);
//            integers.add(dfs(tempBoard, color, maxdepth, depth + 1));
//        }
//        return score + Math.max(integers);
//    }

    private int maxSideMove(Board board, int color){
        int[] scores = {0, 21};
        int[] temps = {0, 0};

        IntPair place = board.getHead(color);

        int counter = 0;
        int i = place.x + 1;
        while (i < board.size){
            if (board.getCell(i, place.y).getColor() != 0){
                break;
            }
            counter++;
            i++;
        }
        scores[0] = Math.max(counter, scores[0]);
        temps[0] = Math.max(temps[0], counter);

        counter = 0;
        i = place.x - 1;
        while (i >= 0){
            if (board.getCell(i, place.y).getColor() != 0){
                break;
            }
            counter++;
            i--;
        }
        scores[0] = Math.max(counter, scores[0]);
        temps[0] = Math.max(temps[0], counter);

        counter = 0;
        i = place.y - 1;
        while (i >= 0){
            if (board.getCell(place.x, i).getColor() != 0){
                break;
            }
            counter++;
            i--;
        }
        scores[1] = Math.max(counter, scores[1]);
        temps[1] = Math.max(temps[1], counter);

        counter = 0;
        i = place.y + 1;
        while (i < board.size){
            if (board.getCell(place.x, i).getColor() != 0){
                break;
            }
            counter++;
            i++;
        }
        scores[1] = Math.max(counter, scores[1]);
        temps[1] = Math.max(temps[1], counter);

        temps[0] = Math.min(temps[0], temps[1]);
        temps[1] = Math.max(scores[0], scores[1]);
        if (temps[0] > (board.size / 2))
            temps[0] /= (1.5);
        if (temps[1] > (board.size / 7))
            temps[1] /= (1.5);
        if (temps[1] <= (board.size / 5))
            temps[1] /= (3);
        if (temps[0] <= (board.size / 8))
            temps[0] /= (3);
//        System.out.println("temp[0]= " + temps[0]);
//        System.out.println("temp[1]= " + temps[1]);
        return temps[0] + 4 * temps[1];
    }

    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
