/**
 *
 */

/**
 *
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

    public void setStyle(MancalaBoardStyle style) {
        this.style = style;
        setBackground(style.boardColor());
        repaint();
    }

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
        pitBounds.add(new Rectangle(PAD, PAD, STORE_W, STORE_H));

        // B pits (7..12) left->right along top row (note index order is 7..12)
        for (int i = 0; i < 6; i++) {
            int x = x0 + i * (PIT_W + GAP);
            pitBounds.add(new Rectangle(x, yTop, PIT_W, PIT_H));
        }
        // B store (13) at right
        int rightX = x0 + 6 * (PIT_W + GAP);
        pitBounds.add(new Rectangle(rightX, PAD, STORE_W, STORE_H));
    }

    private int hitTest(int mx, int my) {
        for (int i = 0; i < pitBounds.size(); i++) {
            if (pitBounds.get(i).contains(mx, my)) return i;
        }
        return -1;
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        repaint();
        if (model.isGameOver()) {
            JOptionPane.showMessageDialog(this, model.getWinner(),
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

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

    private void drawPit(Graphics2D g2, Rectangle r, String label, int stones){
        g2.setColor(style.pitColor());
        Shape s = style.pitShape(r.x, r.y, r.width, r.height);
        g2.fill(s);
        g2.setColor(Color.BLACK);
        g2.draw(s);

        // label
        g2.setFont(style.labelFont());
        g2.drawString(label, r.x + 6, r.y + 18);

        // stones (simple small circles)
        drawStones(g2, r, stones);
    }

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

    private void drawStones(Graphics2D g2, Rectangle r, int n){
        g2.setColor(style.stoneColor());
        int d = 12, pad = 5;
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
    }
}
