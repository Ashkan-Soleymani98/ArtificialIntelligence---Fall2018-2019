package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;
import java.util.LinkedList;


public class Player95109467 extends Player {

    private int depth = 9;

    public Player95109467(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {

        ArrayList<IntPair> nextMoves = this.getMoves(board, super.getCol());

        int max = Integer.MIN_VALUE;
        int index = 0;

        Board b;

        for (int i = 0; i < nextMoves.size(); i++) {

            b = new Board(board);
            b.move(nextMoves.get(i), super.getCol());

            int temp = minimax(b, Integer.MIN_VALUE, Integer.MAX_VALUE, this.depth - 1, false);

            if (max < temp) {
                max = temp;
                index = i;
            }
        }


        return nextMoves.get(index);
    }

    private int minimax(Board board, int alpha, int beta, int depth, boolean maxNode) {

        if (depth == 0)
            return heuristic(board);


        int color = 0;
        int result = 0;

        if (maxNode) {
            color = super.getCol();
            result = Integer.MIN_VALUE;
        }
        else {
            color = 3 - super.getCol();
            result = Integer.MAX_VALUE;
        }

        ArrayList<IntPair> nextMoves = this.getMoves(board, color);

        int max = Integer.MIN_VALUE;
        int index = 0;

        Board b;

        for (int i = 0; i < nextMoves.size(); i++) {

            b = new Board(board);
            int show = b.move(nextMoves.get(i), super.getCol());

            int value;

            if (maxNode) {

                value = Integer.MIN_VALUE;

                if (show != -1)
                    value = minimax(b, alpha, beta, depth - 1, !maxNode);

                if (result < value)
                    result = value;

                if (beta <= result)
                    return result;

                if (alpha < result)
                    alpha = result;
            }

            else {

                value = minimax(b, alpha, beta, depth - 1, !maxNode);

                if (value < result)
                    result = value;

                if (result <= alpha)
                    return result;

                if (result < beta)
                    beta = result;
            }
        }

        return result;
    }

    private int heuristic(Board board) {
        int score1 = board.getLength(getCol()) - board.getLength(3 - getCol());
        //int score2 = board.getHead(super.getCol()).x - board.getTail(super.getCol()).x + board.getHead(super.getCol()).y - board.getTail(super.getCol()).y;
        return score1;
    }

    private ArrayList<IntPair> getMoves(Board board, int color) {

        ArrayList<IntPair> nextMoves = new ArrayList<>();

        int x = board.getHead(color).x;
        int y = board.getHead(color).y;

        if (this.isValid(x + 1, y, board, color)) {
            nextMoves.add(new IntPair(x + 1, y));
        }

        if (this.isValid(x, y + 1, board, color)) {
            nextMoves.add(new IntPair(x, y + 1));
        }

        if (this.isValid(x - 1, y, board, color)) {
            nextMoves.add(new IntPair(x - 1, y));
        }

        if (this.isValid(x, y - 1, board, color)) {
            nextMoves.add(new IntPair(x, y - 1));
        }

        return nextMoves;
    }

    private boolean isValid(int x, int y, Board board, int color) {
        return x >= 0 && x <= 19 && y >= 0 && y <= 19 && board.getCell(x, y).getColor() != color;
    }
}
