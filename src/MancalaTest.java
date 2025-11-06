/**
 *
 */

/**
 *
 */
import javax.swing.*;
import java.awt.*;

public class MancalaTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create model, view, controller
            MancalaModel model = new MancalaModel();
            MancalaView view = new MancalaView();
            MancalaController controller = new MancalaController(model, view);
            
            // Display style selection first
            view.showStyleSelection();
        });
    }
}
