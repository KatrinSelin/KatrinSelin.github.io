package Exercises.hide;

import java.util.Objects;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Point)) {
            return false;
        }

        Point other = (Point) obj;

        return Objects.equals(x, other.x) && Objects.equals(y, other.y);
    }
}
