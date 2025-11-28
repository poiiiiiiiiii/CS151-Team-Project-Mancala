/**
 * The MancalaModel class represents the game logic and state of a Mancala game.
 * It maintains the pit stone counts, tracks the current player's turn,
 * enforces legal moves, handles captures, free turns, undo functionality,
 * and determines when the game ends and who wins.
 * @author Kaydon Do, Rongjie Mai, Sarah Hoang
 * @version 1.0
 */
//MancalaModel
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
     * Constructs a new MancalaModel with an empty board and no stones distributed.
     * The model begins with Player 1's turn and no undos.
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
    
   /**
     * Starts a new Mancala game by distributing the specified number of stones
     * into each non-Mancala pit and resetting all turn and undo state.
     * stones the number of stones to place in each pit (typically 3 or 4)
     */
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
     * selected index of selected pit
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
    /**
     * Checks whether the game is over by determining if one player's side
     * of the board is completely empty.
     * return true if either side has no stones remaining; false otherwise
     */

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
 /**
     * Computes the final score, determines the winner, and clears the pits.
     * return a string describing the winning player or a draw
     */
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
     * Determines whether the selected pit is a valid choice for the current player.
     * index the pit index being checked
     * return true if the pick is legal; false otherwise
     */
    public Boolean isLegalPick(int index) {
        if ((pits[index] == 0) || (player1Turn && index > 5) || (!player1Turn && (index < 7 || index > 12))) {
            return false;
        }
        return true;
    }

    /**
     * Attempts to undo the previous move if the player has undos remaining.
     * Undo restores the board state, the active player, and the undo counter.
     * return true if the undo was successful; false otherwise
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

    /**
     * Returns a string describing whose turn it is.
     * return "Player 1's Turn" or "Player 2's Turn"
     */

    public String getTurn() {
        if(player1Turn) return "Player 1's Turn";
        else return "Player 2's Turn";
    }
     /**
     * Returns the full board array representing all pits and Mancalas.
     * return an array of pit stone counts
     */
    public int[] getBoard() {
        return pits;
    }

    /**
     * Returns the number of undos performed in this turn.
     * return the current undo count
     */
    public int getUndoCount() {
        return undoCount;
    }
    /**
     * return true if player 1's turn, false if player 2's turn
     */
    public boolean isPlayer1Turn() {
        return player1Turn;
    }


    // Observer pattern methods
    public void attach(ChangeListener listener) {
        listeners.add(listener);
    }
    /**
     * Notifies all registered ChangeListeners that the model state has changed.
     */
    private void notifyListeners(){
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}
