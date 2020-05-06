import javax.swing.*;
import java.awt.*;

public class ClockPanel extends JPanel {

    private double angle;

    public ClockPanel () {
        setLayout(new BorderLayout());
        angle = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCenteredCircle(g,300, 300, 300);
        drawLine(g, 300, 300, 300 + (int)(150 * Math.cos(angle)), 300 + (int)(150 * Math.sin(angle)));
    }

    public void addAngle(double add) {
        angle += add;
        if (angle >= 2 * Math.PI) {
            angle = 0;
        }
    }

    public void drawCenteredCircle(Graphics g, int x, int y, int r) {
        g.setColor(Color.BLUE);
        g.drawOval(x - (r / 2), y - (r / 2), r, r);
    }

    public void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.setColor(Color.BLUE);
        g.drawLine(x1, y1, x2, y2);
    }

}
