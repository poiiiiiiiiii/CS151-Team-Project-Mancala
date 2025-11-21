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
            BoardStyle classic = new ClassicStyle();
            BoardStyle ocean   = new OceanStyle();

            MancalaView view = new MancalaView(model, classic);
            model.addPropertyChangeListener(view);

            JFrame f = new JFrame("Mancala — CS151");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());

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
                String s = JOptionPane.showInputDialog(f, "Stones per pit (3 or 4):", "3");
                if (s==null) return;
                try {
                    int v = Integer.parseInt(s.trim());
                    if (v==3 || v==4) model.newGame(v);
                    else JOptionPane.showMessageDialog(f, "Enter 3 or 4.");
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(f, "Enter 3 or 4.");
                }
            });

            btnUndo.addActionListener(e -> {
                if (!model.undo()){
                    JOptionPane.showMessageDialog(f, "Cannot undo now (max 3 per turn, and not twice in a row).");
                }
            });

            btnClassic.addActionListener(e -> view.setStyle(classic));
            btnOcean.addActionListener(e -> view.setStyle(ocean));

            // Start with a prompt
            model.newGame(3);

            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

