/**
 *
 */

/**
 *
 */
// MancalaTest.java
import javax.swing.*;
import java.awt.*;

public class MancalaTest {
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

            // Actions
            btnNew.addActionListener(e -> {
                MancalaInitDialog configDialog = MancalaInitDialog.showDialog(f);
                if (configDialog != null) {
                    view.setStyle(configDialog.getSelectedStyle());
                    model.newGame(configDialog.getStones());
                }
            });

            btnUndo.addActionListener(e -> {
                if (!model.undo()){
                    JOptionPane.showMessageDialog(f, "Cannot undo now (max 3 per turn, and not twice in a row).");
                }
            });

            btnClassic.addActionListener(e -> view.setStyle(classic));
            btnOcean.addActionListener(e -> view.setStyle(ocean));

            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

