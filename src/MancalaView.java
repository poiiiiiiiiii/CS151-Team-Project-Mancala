/**
 * The MancalaView class serves as the visual component of the Mancala game,
 * responsible for drawing the board, pits, stores, stones, and status messages.
 * It observes changes in the MancalaModel and updates the display accordingly.
 * @author Kaydon Do, Rongjie Mai, Sarah Hoang
 * @version 1.0
 */
// MancalaView.java
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public class MancalaView extends JPanel implements ChangeListener {
    private final MancalaModel model;
    private MancalaBoardStyle style;

    private final List<Rectangle> pitBounds = new ArrayList<>(14); // click regions

    // Layout constants
    private static final int PAD = 24;
    private static final int PIT_W = 70, PIT_H = 70, STORE_W = 70, STORE_H = 160;
    private static final int GAP = 16;
    /**
     * Constructs a new MancalaView for the given model and style.
     * The constructor computes click regions, sets the background color,
     * and installs a mouse listener to handle pit selection by the user.
     * model the MancalaModel providing game data
     * style the MancalaBoardStyle determining visual appearance
     */
    public MancalaView(MancalaModel model, MancalaBoardStyle style) {
        this.model = model;
        this.style = style;
        setBackground(style.boardColor());
        setPreferredSize(new Dimension(2 * PAD + STORE_W + GAP + 6 * (PIT_W + GAP) + STORE_W, 260));

        // Build clickable rects in index order: 0..5(A pits), 6(A store), 7..12(B pits), 13(B store)
        recomputeBounds();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int idx = hitTest(e.getX(), e.getY());
                if (idx >= 0 && model.isLegalPick(idx)) {
                    model.move(idx);
                }
            }
        });
    }
     /**
     * Sets the board's visual style and repaints the view.
     * style the MancalaBoardStyle to apply
     */
    public void setStyle(MancalaBoardStyle style) {
        this.style = style;
        setBackground(style.boardColor());
        repaint();
    }
     /**
     * Recomputes the rectangular hit-boxes for pits and stores
     * based on the layout constants. The indices match the model's
     * pit ordering: 0–5 Player A pits, 6 A store, 7–12 Player B pits, 13 B store.
     */
    private void recomputeBounds() {
        pitBounds.clear();
        int x0 = PAD + STORE_W + GAP;
        int yTop = PAD, yBottom = PAD + STORE_H - PIT_H;

        // A pits (0..5) left->right along bottom row
        for (int i = 0; i < 6; i++) {
            int x = x0 + i * (PIT_W + GAP);
            pitBounds.add(new Rectangle(x, yBottom, PIT_W, PIT_H));
        }
        // A store (6) at left
        int rightX = x0 + 6 * (PIT_W + GAP);
        pitBounds.add(new Rectangle(rightX, PAD, STORE_W, STORE_H));

        // B pits (7..12) right->left along top row (note index order is 7..12)
        for (int i = 0; i < 6; i++) {
            int x = x0 + (5 - i) * (PIT_W + GAP);
            pitBounds.add(new Rectangle(x, yTop, PIT_W, PIT_H));
        }
        // B store (13) at left
        pitBounds.add(new Rectangle(PAD, PAD, STORE_W, STORE_H));
    }
    /**
     * Determines which pit or store a user clicked by checking mouse coordinates
     * against the list of rectangular pit bounds.
     * mx the x-coordinate of the mouse click
     * my the y-coordinate of the mouse click
     * return the index of the pit/store clicked, or -1 if none were hit
     */
    private int hitTest(int mx, int my) {
        for (int i = 0; i < pitBounds.size(); i++) {
            if (pitBounds.get(i).contains(mx, my)) return i;
        }
        return -1;
    }
    /**
     * Updates the view when the model state changes. If the game is over,
     * displays the winner using a message dialog.
     * evt the ChangeEvent fired by the model
     */
    @Override
    public void stateChanged(ChangeEvent evt) {
        repaint();
        if (model.isGameOver()) {
            JOptionPane.showMessageDialog(this, model.getWinner(),
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     * Paints the Mancala board, including pits, stores, stones, labels,
     * and the active player's turn indicator.
     * g the Graphics context used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] b = model.getBoard();

        // Draw stores
        drawStore(g2, pitBounds.get(MancalaModel.A_STORE), 'A', b[MancalaModel.A_STORE]);
        drawStore(g2, pitBounds.get(MancalaModel.B_STORE), 'B', b[MancalaModel.B_STORE]);

        // Draw pits + labels (A1..A6 bottom; B1..B6 top)
        // A pits indices 0..5 bottom
        for (int i = 0; i < 6; i++) {
            String label = "A" + (i + 1);
            drawPit(g2, pitBounds.get(i), label, b[i]);
        }
        // B pits indices 7..12 top BUT label order is B1 on the right visually;
        // we keep simple A-left->right and B-left->right labels (matches our rectangles).
        for (int i = 7; i <= 12; i++) {
            String label = "B" + (i - 6);
            drawPit(g2, pitBounds.get(i), label, b[i]);
        }

        // Turn indicator
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(style.labelFont());
        g2.drawString("Turn: " + model.getTurn() + "   (Undo: press button)", PAD, getHeight() - 10);

        g2.dispose();
    }
    /**
     * Draws a single pit at the specified bounds with its label and number of stones.
     * g2 the Graphics2D context
     * r the rectangle representing the pit region
     * label the text label (A1..A6 or B1..B6)
     * stones the number of stones to draw inside the pit
     */
    private void drawPit(Graphics2D g2, Rectangle r, String label, int stones){
        g2.setColor(style.pitColor());
        Shape s = style.pitShape(r.x, r.y, r.width, r.height);
        g2.fill(s);
        g2.setColor(Color.BLACK);
        g2.draw(s);

        // label (centered)
        g2.setFont(style.labelFont());
        int labelWidth = g2.getFontMetrics().stringWidth(label);
        int labelX = r.x + (r.width - labelWidth) / 2;
        g2.drawString(label, labelX, r.y + 18);

        // stones (simple small circles)
        drawStones(g2, r, stones);
    }
    /**
     * Draws a Mancala store with its label ('A' or 'B') and stone count.
     * g2 the Graphics2D context
     * r the rectangle representing the store region
     * who the store label ('A' for Player 1, 'B' for Player 2)
     * stones number of stones in the store
     */
    private void drawStore(Graphics2D g2, Rectangle r, char who, int stones){
        g2.setColor(style.storeColor());
        Shape s = style.storeShape(r.x, r.y, r.width, r.height);
        g2.fill(s);
        g2.setColor(Color.BLACK);
        g2.draw(s);

        g2.setFont(style.labelFont());
        g2.drawString(String.valueOf(who), r.x + r.width/2 - 4, r.y + 16);

        drawStones(g2, r, stones);
    }
    /**
     * Draws stones inside a pit or store. For small counts (≤ 6), stones are drawn 
     * as circular markers arranged in rows. For larger counts, the total number is 
     * displayed as text instead to avoid cluttering the pit visually.
     * g2 the Graphics2D context
     * r  the rectangle representing the pit or store region
     * n  the number of stones to draw
     */
    private void drawStones(Graphics2D g2, Rectangle r, int n){
        g2.setColor(style.stoneColor());
        int d = 12, pad = 5;
        if(n <= 6) {
            // draw small circles for each stone
            int cols = Math.max(1, (r.width - pad)/(d+pad));
            int x = r.x + pad, y = r.y + 24;
            for (int i=0;i<n;i++){
                g2.fillOval(x, y, d, d);
                x += d + pad;
                if (x + d > r.x + r.width - pad){
                    x = r.x + pad;
                    y += d + pad;
                    if (y + d > r.y + r.height - pad) break; // clip if too many
                }
            }
        } else {
            // too many stones — draw just the count
            g2.setFont(style.labelFont());
            g2.drawString(String.valueOf(n), r.x + r.width/2 - 4, r.y + 40);
        }

    }
}
