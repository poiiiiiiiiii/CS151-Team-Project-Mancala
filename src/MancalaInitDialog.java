/**
 * The MancalaInitDialog class provides a modal dialog used to configure the 
 * initial settings of a new Mancala game. The dialog allows the user to select 
 * the number of stones per pit (3 or 4) and choose a board style 
 * (Classic or Ocean).These values can be retrieved by the caller to initialize
 * once the user confirms their choices, the game model.
 * @author Kaydon Do, Rongjie Mai, Sarah Hoang
 * @version 1.0
 */
import javax.swing.*;
import java.awt.*;

public class MancalaInitDialog extends JDialog {
    private int stones = 3;
    private MancalaBoardStyle selectedStyle = new ClassicStyle();
    private boolean confirmed = false;

/**
     * Constructs a new initialization dialog attached to the given parent frame.
     * The dialog provides radio button options for stone count and board style,
     * as well as OK/Cancel controls.
     * parent the parent JFrame that owns this dialog
     */   
    public MancalaInitDialog(JFrame parent) {
        super(parent, "New Game Setup", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Stones selection
        JPanel stonesPanel = new JPanel();
        stonesPanel.add(new JLabel("Stones per pit:"));
        ButtonGroup stonesGroup = new ButtonGroup();
        JRadioButton threeStonesBtn = new JRadioButton("3", true);
        JRadioButton fourStonesBtn = new JRadioButton("4", false);
        stonesGroup.add(threeStonesBtn);
        stonesGroup.add(fourStonesBtn);
        stonesPanel.add(threeStonesBtn);
        stonesPanel.add(fourStonesBtn);
        mainPanel.add(stonesPanel, BorderLayout.NORTH);

        // Style selection
        JPanel stylePanel = new JPanel();
        stylePanel.add(new JLabel("Board Style:"));
        ButtonGroup styleGroup = new ButtonGroup();
        JRadioButton classicBtn = new JRadioButton("Classic", true);
        JRadioButton oceanBtn = new JRadioButton("Ocean", false);
        styleGroup.add(classicBtn);
        styleGroup.add(oceanBtn);
        stylePanel.add(classicBtn);
        stylePanel.add(oceanBtn);
        mainPanel.add(stylePanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        okButton.addActionListener(e -> {
            if (threeStonesBtn.isSelected()) {
                stones = 3;
            } else {
                stones = 4;
            }

            if (classicBtn.isSelected()) {
                selectedStyle = new ClassicStyle();
            } else {
                selectedStyle = new OceanStyle();
            }
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        add(mainPanel);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Returns true if the user pressed the OK button to confirm their selections.
     * return true if the dialog was confirmed; false otherwise
     */
    public boolean isConfirmed() {
        return confirmed;
    }
/**
     * Returns the number of stones per pit selected by the user.
     * return the selected stone count (3 or 4)
     */
    public int getStones() {
        return stones;
    }

    public MancalaBoardStyle getSelectedStyle() {
        return selectedStyle;
    }
    /**
     * Displays the initialization dialog and returns it only if the user confirms 
     * their choices. If the user cancels, null is returned.
     * parent the parent JFrame
     * return the MancalaInitDialog instance if confirmed; otherwise null
     */

    public static MancalaInitDialog showDialog(JFrame parent) {
        MancalaInitDialog dialog = new MancalaInitDialog(parent);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            return dialog;
        }
        return null;
    }
}

