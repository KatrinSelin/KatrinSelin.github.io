package Exercises.hide;

public class Fibonacci {

    private int current = 0;
    private int next = 1;


    public int nextValue() {

        int result = current;

        current = next;
        next += result;

        return result;
    }

}
