package com.company;

import com.company.players.*;

public class Main {

    public static void main(String[] args) {
//        Player p1 = new NaivePlayer(1);
        Player p1 = new Player95105408(1);
//        Player p2 = new NaivePlayer(1);
        Player p2 = new Player95105635(2);
        Game g = new Game(p1, p2);
        g.start();
    }

}