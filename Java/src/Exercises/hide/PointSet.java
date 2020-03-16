package Exercises.hide;

import java.util.Objects;

public class PointSet {

    private Point[] points;

    public PointSet(int capacity) {
        this.points = new Point[capacity];
    }

    public PointSet() {
        this(10);
    }

    public void add(Point point) {

        if (!(contains(point))) {

            if (points[points.length - 1] == null) {

                for (int ind = 0; ind < points.length; ind++) {

                    if (points[ind] == null) {
                        points[ind] = point;
                        break;
                    }
                }
            } else {

                Point[] biggerArrayPoint = copyPointsFromOldArray();

                biggerArrayPoint[points.length] = point;

                points = biggerArrayPoint;
            }
        }
    }

    public Point[] copyPointsFromOldArray() {

        Point[] biggerArrayPoint = new Point[points.length * 2];

        for (int indexesOfOldArray = 0; indexesOfOldArray < points.length; indexesOfOldArray++) {

            biggerArrayPoint[indexesOfOldArray] = points[indexesOfOldArray];
        }
        return biggerArrayPoint;

    }

    public int size() {

        int counterOfPoints = 0;

        for (Point point : points) {

            if (point != null) {
                counterOfPoints++;
            }
        }
        return counterOfPoints;
    }

    public boolean contains(Point point) {

        for (Point element : points) {

            if (Objects.equals(element, point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        StringBuilder returnString = new StringBuilder();

        for (int indexOfPoint = 0; indexOfPoint < size(); indexOfPoint++) {

            returnString.append(points[indexOfPoint].toString());

            if (indexOfPoint < size() - 1) {

                returnString.append(", ");
            }
        }
        return returnString.toString();
    }

    @Override
    public boolean equals(Object another) {

        // Object should be the type of Point set
        // lengths of our Points Set and other point set are equal
        // check if elements contains in other set using method contains

        if (!(another instanceof PointSet)) {

            return false;

        } else {

            PointSet other = (PointSet) another;

            if (other.size() != this.size()) {

                return false;

            } else {

                for (int indexOfArray = 0; indexOfArray < this.size(); indexOfArray++) {
                    if (!(other.contains(points[indexOfArray]))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public PointSet subtract(PointSet other) {

        PointSet subtractedSet = new PointSet();

        for (Point element : points) {

            if (!(other.contains(element))) {
                subtractedSet.add(element);
            }
        }
        return subtractedSet;
    }

    public PointSet intersect(PointSet other) {

        PointSet intersectedSet = new PointSet();

        for (Point element : points) {

            if (other.contains(element)) {
                intersectedSet.add(element);
            }
        }
        return intersectedSet;
    }
}
