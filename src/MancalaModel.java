/**
 *
 */

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

/**
 *
 */
public class MancalaModel {
    private boolean player1Turn;
    private static final int MAX_UNDOS = 3;
    private int undoCount;
    private int[] pits;
    private int[] lastState;
    private ArrayList<ChangeListener> listeners;

    /**
     *
     */
    public MancalaModel () {
        player1Turn = true;
        undoCount = 0;
        pits = new int[14];
        listeners = new ArrayList<>();
    }

    public void initialize(int stones) {
        pits[6] = 0; //Player 1 Mancala 0 stones
        pits[13] = 0; // Player 2 Mancala 0 stones
        for (int i = 0; i < 6; i++) {
            pits[i] = stones; // add stones to Player 1 pit
        }
        for (int i = 7; i < 13; i++) {
            pits[i] = stones; // add stones to Player 2 pit
        }
        notifyListeners();
    }

    /**
     * Makes a move from the selected pit.
     * @param selected index of selected pit
     */
    public void selectPit(int selected) {
        lastState = pits; //saves the state of game before move is made
        undoCount = 0;
        int count = pits[selected];
        // pits[selected] = 0;
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
     * undoes the players move if the player has undos left on that turn
     */
    public void undo() {
        if (undoCount < MAX_UNDOS) {
            pits = lastState;
            undoCount++;
        }
    }

    // Accessors

    public int[] getPits() {
        return pits;
    }

    public int getUndoCount() {
        return undoCount;
    }
    /**
     * @return true if player 1's turn, false if player 2's turn
     */
    public boolean isPlayer1Turn() {
        return player1Turn;
    }


    // Observer pattern methods
    public void attach(ChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(){
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}
