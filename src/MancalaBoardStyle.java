/**
 * The MancalaBoardStyle interface defines a strategy for rendering the Mancala board
 * using different colors, fonts, and geometric shapes. Implementations of this interface
 * supply the appearance of pits, stores, stones, and labels so the board can be displayed
 * in various visual themes.
 * 
 * This interface supports the Strategy Pattern, allowing the game to plug in new styles
 * without modifying the drawing logic in the view.
 * 
 * @author Kaydon Do, Rongjie Mai, Sarah Hoang
 * @version 1.0
 */
// BoardStyle.java
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D;

public interface MancalaBoardStyle {
    /**
     * Returns the background color of the Mancala board.
     */
    Color boardColor();
    Color pitColor();
    Color storeColor();
    Color stoneColor();
    Font  labelFont();
/**
     * Returns the geometric shape representing a pit.
     * The default implementation draws a circular pit using an ellipse.
     */
    default Shape pitShape(int x, int y, int w, int h){
        return new Ellipse2D.Double(x, y, w, h);
    }
    /**
     * Returns the geometric shape representing a Mancala store.
     * The default implementation draws a rounded rectangle.
     */
    default Shape storeShape(int x, int y, int w, int h){
        return new RoundRectangle2D.Double(x, y, w, h, 30, 30);
    }
}
/**
 * The ClassicStyle class implements the MancalaBoardStyle interface and
 * provides a traditional wooden color scheme for the Mancala board.
 * Muted brown tones are used for pits and stores, with dark stones and
 * bold font styling for labels.
 */
// Two example styles
class ClassicStyle implements MancalaBoardStyle {
    public Color boardColor(){ return new Color(222, 201, 158); }
    public Color pitColor(){ return new Color(184, 138, 79); }
    public Color storeColor(){ return new Color(164, 118, 59); }
    public Color stoneColor(){ return new Color(50, 50, 50); }
    public Font  labelFont(){ return new Font("SansSerif", Font.BOLD, 14); }
}
/**
 * The OceanStyle class implements the MancalaBoardStyle interface and 
 * provides a lighter, ocean-themed appearance. Shades of blue are used 
 * for the board, pits, and stores, while stones and labels draw with 
 * contrasting bright tones.
 */

class OceanStyle implements MancalaBoardStyle {
    public Color boardColor(){ return new Color(212, 235, 248); }
    public Color pitColor(){ return new Color(88, 156, 215); }
    public Color storeColor(){ return new Color(60, 123, 190); }
    public Color stoneColor(){ return new Color(245, 245, 245); }
    public Font  labelFont(){ return new Font("SansSerif", Font.BOLD, 14); }
}
