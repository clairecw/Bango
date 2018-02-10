package clairecw.bango;

/**
 * Created by admin on 2/10/18.
 */

public class BoardItem {
        int id;
        int diff;
        String name;
        String shortname;   //80 character cap
        boolean marked;     // marked by player or nah
        int game;   //id of game

    public BoardItem(int i, int d, String n, String sn, boolean m, int g) {
        id = i; diff = d; name = n; shortname = sn; marked = m; game = g;
    }
}