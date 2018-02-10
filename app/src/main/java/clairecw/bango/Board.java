package clairecw.bango;

/**
 * Created by admin on 2/9/18.
 */



public class Board {

    int id;
    //WinConfig wc;
    //int size;       // board has size x size dimensions
    BoardItem[][] items;

    public Board() {
        items = new BoardItem[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                items[i][j] = new BoardItem(0, 0, "", "", false, 0);
            }
        }
    }
}

