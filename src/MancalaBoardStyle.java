// BoardStyle.java
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D;

public interface MancalaBoardStyle {
    Color boardColor();
    Color pitColor();
    Color storeColor();
    Color stoneColor();
    Font  labelFont();

    default Shape pitShape(int x, int y, int w, int h){
        return new Ellipse2D.Double(x, y, w, h);
    }
    default Shape storeShape(int x, int y, int w, int h){
        return new RoundRectangle2D.Double(x, y, w, h, 30, 30);
    }
}

// Two example styles
class ClassicStyle implements MancalaBoardStyle {
    public Color boardColor(){ return new Color(222, 201, 158); }
    public Color pitColor(){ return new Color(184, 138, 79); }
    public Color storeColor(){ return new Color(164, 118, 59); }
    public Color stoneColor(){ return new Color(50, 50, 50); }
    public Font  labelFont(){ return new Font("SansSerif", Font.BOLD, 14); }
}

class OceanStyle implements MancalaBoardStyle {
    public Color boardColor(){ return new Color(212, 235, 248); }
    public Color pitColor(){ return new Color(88, 156, 215); }
    public Color storeColor(){ return new Color(60, 123, 190); }
    public Color stoneColor(){ return new Color(245, 245, 245); }
    public Font  labelFont(){ return new Font("SansSerif", Font.BOLD, 14); }
}
