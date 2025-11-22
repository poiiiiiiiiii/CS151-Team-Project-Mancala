/**
 *
 */

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class MancalaModel {
    private boolean player1Turn;
    private boolean lastTurn;
    private static final int MAX_UNDOS = 3;
    private int undoCount;
    private int lastUndoCount;
    private int[] pits;
    private int[] lastPitState;
    private ArrayList<ChangeListener> listeners;

    public static final int A_STORE = 6;
    public static final int B_STORE = 13;

    /**
     *
     */
    public MancalaModel () {
        player1Turn = true;
        undoCount = 0;
        pits = new int[14];
        listeners = new ArrayList<>();
    }

    public void newGame(int stones) {
        pits[6] = 0; //Player 1 Mancala 0 stones
        pits[13] = 0; // Player 2 Mancala 0 stones
        for (int i = 0; i < 6; i++) {
            pits[i] = stones; // add stones to Player 1 pit
        }
        for (int i = 7; i < 13; i++) {
            pits[i] = stones; // add stones to Player 2 pit
        }
        player1Turn = true;
        undoCount = 0;
        lastUndoCount = 0;
        lastPitState = null;
        lastTurn = false;
        notifyListeners();
    }

    /**
     * Makes a move from the selected pit.
     * @param selected index of selected pit
     */
    public void move(int selected) {
        lastTurn = player1Turn;
        lastUndoCount = undoCount;
        lastPitState = pits.clone(); //saves the state of game before move is made
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
        // Captures opposite side if lands on empty pit on player side
        if(player1Turn && index >= 0 && index <= 5 && pits[index]==1 && pits[12-index] > 0) {
            pits[6] += pits[12-index] + 1;
            pits[index] = 0;
            pits[12-index] = 0;
        } else if(!player1Turn && index >= 7 && index <= 12 && pits[index]==1 && pits[12-index] > 0) {
            pits[13] += pits[12-index] + 1;
            pits[index] = 0;
            pits[12-index] = 0;
        }
        // Free turn if last stone is in player Mancala otherwise change turns
        if((player1Turn && index == 6) || (!player1Turn && index ==  13)) {
            notifyListeners();
        } else {
            undoCount = 0;
            player1Turn = !player1Turn;
            notifyListeners();
        }
    }

    public boolean isGameOver() {
        boolean player1Side = true;
        boolean player2Side = true;
        // If one side is not empty, the game continues
        for(int i = 0; i < 6; i++) {
            if(pits[i] != 0) {
                player1Side = false;
            }
        }
        for(int i = 7; i < 13; i++) {
            if(pits[i] != 0) {
                player2Side = false;
            }
        }
        return player1Side||player2Side;
    }

    public String getWinner() {
        //adds up all stones on player 1 side
        for (int i = 0; i < 6; i++) {
            pits[6] += pits[i];
        }
        // adds up all stones on player 2 side
        for (int i = 7; i < 13; i++) {
            pits[13] += pits[i];
        }
        // empties all pits
        for (int i = 0; i < 14; i++) {
            if (i != 6 && i != 13) {
                pits[i] = 0;
            }
        }
        if (pits[6] > pits[13]) {
            return "Player 1 is the winner";
        } else if (pits[13] > pits[6]) {
            return "Player 2 is the winner";
        } else {
            return "It is a draw";
        }
    }

    /**
     *
     * @param index
     * @return
     */
    public Boolean isLegalPick(int index) {
        if ((pits[index] == 0) || (player1Turn && index > 5) || (!player1Turn && (index < 7 || index > 12))) {
            return false;
        }
        return true;
    }

    /**
     * undoes the players move if the player has undos left on that turn
     */
    public boolean undo() {
        if ((lastUndoCount < MAX_UNDOS) && (!Arrays.equals(pits, lastPitState)) && (lastPitState != null)) {
            undoCount = lastUndoCount;
            player1Turn = lastTurn;
            pits = lastPitState.clone();
            undoCount++;
            notifyListeners();
            return true;
        }
        return false;
    }

    // Accessors

    public String getTurn() {
        if(player1Turn) return "Player 1's Turn";
        else return "Player 2's Turn";
    }
    /**
     *
     * @return
     */
    public int[] getBoard() {
        return pits;
    }

    /**
     *
     * @return
     */
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