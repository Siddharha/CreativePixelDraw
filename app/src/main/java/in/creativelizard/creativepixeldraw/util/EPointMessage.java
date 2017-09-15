package in.creativelizard.creativepixeldraw.util;

/**
 * Created by siddhartha on 14/9/17.
 */

public class EPointMessage extends Message {

    private EPoint point;
    private int color;

    public EPointMessage() {
    }

    public EPointMessage(EPoint point, int color) {
        this.point = point;
        this.color = color;
    }

    public EPoint getPoint() {
        return point;
    }

    public void setPoint(EPoint point) {
        this.point = point;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}