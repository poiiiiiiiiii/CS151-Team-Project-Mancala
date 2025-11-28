/**
 * The MancalaTest class serves as the entry point for launching the Mancala game
 * application. It initializes the MVC components — the model, view, and UI controls —
 * and displays the game window. This class also handles style selection, starting
 * new games, and coordinating user interactions such as undoing moves.
 * @author Kaydon Do, Rongjie Mai, Sarah Hoang
 * @version 1.0
 */
// MancalaTest.java
import javax.swing.*;
import java.awt.*;

public class MancalaTest {
    /**
     * Launches the Mancala game UI. This method constructs the game model,
     * initializes the view using the user's selected style and stone count,
     * and sets up toolbar controls for starting a new game, undoing moves,
     * and switching board styles.
     * args command-line arguments (unused)
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            MancalaModel model = new MancalaModel();

            // Choose style (Strategy)
            MancalaBoardStyle classic = new ClassicStyle();
            MancalaBoardStyle ocean   = new OceanStyle();

            JFrame f = new JFrame("Mancala — CS151");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());

            // Show initialization dialog on startup
            MancalaInitDialog initialDialog = MancalaInitDialog.showDialog(f);
            if (initialDialog == null) {
                // User cancelled, exit
                System.exit(0);
                return;
            }

            MancalaView view = new MancalaView(model, initialDialog.getSelectedStyle());
            model.attach(view);
            model.newGame(initialDialog.getStones());

            // Toolbar
            JToolBar tb = new JToolBar(); tb.setFloatable(false);

            JButton btnNew = new JButton("New");
            JButton btnUndo = new JButton("Undo");
            JButton btnClassic = new JButton("Classic");
            JButton btnOcean = new JButton("Ocean");

            tb.add(btnNew); tb.add(btnUndo);
            tb.addSeparator();
            tb.add(new JLabel("Style: "));
            tb.add(btnClassic); tb.add(btnOcean);

            f.add(tb, BorderLayout.NORTH);
            f.add(view, BorderLayout.CENTER);
            // New game action
            btnNew.addActionListener(e -> {
                MancalaInitDialog configDialog = MancalaInitDialog.showDialog(f);
                if (configDialog != null) {
                    view.setStyle(configDialog.getSelectedStyle());
                    model.newGame(configDialog.getStones());
                }
            });
            // Undo action
            btnUndo.addActionListener(e -> {
                if (!model.undo()){
                    JOptionPane.showMessageDialog(f, "Cannot undo now (max 3 per turn, and not twice in a row).");
                }
            });
            // Change board style dynamically
            btnClassic.addActionListener(e -> view.setStyle(classic));
            btnOcean.addActionListener(e -> view.setStyle(ocean));
            // Display window
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

