package in.creativelizard.creativepixeldraw;

/**
 * Created by siddhartha on 14/9/17.
 */

public class EPoint {
    public int x, y;

    /**
     * Default constructor required for Jackson
     **/
    public EPoint() {
    }

    public EPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
