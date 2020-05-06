import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;

import static java.lang.Math.min;

public class ImagePanel extends JPanel {

    private double angle;
    private int direcion;
    private Image sphere;

    public ImagePanel () {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        angle = 0;
        direcion = 1;
        sphere = Toolkit.getDefaultToolkit().getImage("D:\\Projects\\University\\UP_4sem\\Lab4\\exort.gif");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();
        int radius = min(width, height) / 3;
        g.drawImage(sphere, width/2 + (int)(radius * Math.cos(angle)) - sphere.getWidth(this)/2, height/2 + (int)(radius * Math.sin(angle)) - sphere.getHeight(this)/2, this);
        g.drawImage(sphere, width/2 + (int)(radius * Math.cos(angle + 2*Math.PI/3)) - sphere.getWidth(this)/2, height/2 + (int)(radius * Math.sin(angle + 2*Math.PI/3)) - sphere.getHeight(this)/2, this);
        g.drawImage(sphere, width/2 + (int)(radius * Math.cos(angle + 4*Math.PI/3)) - sphere.getWidth(this)/2, height/2 + (int)(radius * Math.sin(angle + 4*Math.PI/3)) - sphere.getHeight(this)/2, this);
    }

    public void changeDirection() {
        direcion *= -1;
    }

    public void addAngle(double add) {
        angle += direcion * add;
        if (angle >= 2 * Math.PI) {
            angle = 0;
        }
    }

}
