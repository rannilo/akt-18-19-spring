package week1.animals;

public class Animal {
    private static int idx = 1;
    private final int myId = idx++;

    public void makeNoise() {
        System.out.println("Loom #" + myId + ": " + this);
    }
}
