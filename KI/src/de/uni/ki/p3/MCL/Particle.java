package de.uni.ki.p3.MCL;

import de.uni.ki.p3.Drawing.Drawable;
import de.uni.ki.p3.Drawing.Line;
import de.uni.ki.p3.Drawing.MapObject;
import de.uni.ki.p3.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Particle implements Drawable, Cloneable {
	float x, y, heading, weight;
	private GraphicsContext gc;
	private MapObject map;

	public static final float WIDTH = 2;
    public static final float HEIGHT = 2;

    public Particle(final float x,
                    final float y,
                    final float heading,
                    final float weight,
                    final GraphicsContext gc,
                    final MapObject map) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.weight = weight;
        this.gc = gc;
        this.map = map;
    }
    
    public Particle clone() throws CloneNotSupportedException {
        return (Particle) super.clone();
}

    public float measure() {
        boolean infinity = true;

        float distance = -1 ;
        for(Line l : map.getLines())
        {
        	// ToDo: wir m�ssen von der Partikel-Perspektive aus gerade aus gucken. Wir gucken bisher nur nach oben
            if(x*Main.DrawFactor > l.getX1() && x*Main.DrawFactor < l.getX2())
            {
                distance = (float)(y*Main.DrawFactor - l.getY2());
                System.out.println("Particle-Distance: " + distance);
                infinity = false;
                break;
            }
        }

//        if(infinity)
//            System.out.println("Distance: infinity");

        return infinity ? -1 : distance;
    }

    @Override
    public void draw() {
        gc.setFill(Color.BLUE);
    	gc.setStroke(Color.YELLOW);
    	
        gc.fillOval(
            x, y, WIDTH * Main.DrawFactor, HEIGHT * Main.DrawFactor
        );
        float startX = (x + WIDTH * Main.DrawFactor/ 2);
        float startY = (y + HEIGHT * Main.DrawFactor / 2);
        float endX = (x - WIDTH * Main.DrawFactor / 2) ;
        float endY = (y + HEIGHT * Main.DrawFactor / 2) ;

        Rotate r = new Rotate(heading, startX, startY);
        Point2D p = r.transform(endX, endY);

        gc.strokeLine(startX, startY, p.getX(), p.getY());
    }
}