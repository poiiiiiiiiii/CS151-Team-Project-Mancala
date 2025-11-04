/**
 *
 */

/**
 *
 */
public class MancalaModel {
    private boolean player1Turn = true;
    private int undos = 3;
    private int[] pits = new int[14];
    private int[] lastState;

    /**
     *
     */
    public MancalaModel () {
        pits[6] = 0; //Player 1 Mancala 0 stones
        pits[13] = 0; // Player 2 Mancala 0 stones
        for (int i = 0; i < 6; i++) {
            pits[i] = 4; //Player 1 pits 4 stones
        }
        for (int i = 7; i < 13; i++) {
            pits[i] = 4; // Player 2 pits 4 stones
        }
    }

    /**
     * Makes a move from the selected pit.
     * @param selected index of selected pit
     */
    public void selectPit(int selected) {
        lastState = pits; //saves the state of game before move is made
        undos = 3;
        int count = pits[selected];
        pits[selected] = 0;
        int index = selected;

        for(int i = count; i > 0; i--) {
            index = (index +1) % 14;
            if((player1Turn && index == 13) || (!player1Turn && index == 6)) {
                continue;
            }
            //adds stone to pit if not opponent mancala
            pits[index]++;
            count--;
        }
    }

    /**
     *
     * @return true if player 1's turn, false if player 2's turn
     */
    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    /**
     * undoes the players move if the player has undos left on that turn
     */
    public void undo() {
        if (undos > 0) {
            pits = lastState;
            undos--;
        }
    }
}
