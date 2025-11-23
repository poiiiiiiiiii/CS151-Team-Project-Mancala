import javax.swing.*;
import java.awt.*;

public class MancalaInitDialog extends JDialog {
    private int stones = 3;
    private MancalaBoardStyle selectedStyle = new ClassicStyle();
    private boolean confirmed = false;

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

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getStones() {
        return stones;
    }

    public MancalaBoardStyle getSelectedStyle() {
        return selectedStyle;
    }

    public static MancalaInitDialog showDialog(JFrame parent) {
        MancalaInitDialog dialog = new MancalaInitDialog(parent);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            return dialog;
        }
        return null;
    }
}

