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
     *
     * @param pit
     */
    public void move(int pit) {
        lastState = pits; //saves the state of game before move is made
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
