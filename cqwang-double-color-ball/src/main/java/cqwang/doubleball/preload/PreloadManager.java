package cqwang.doubleball.preload;

public class PreloadManager {
    public static void execute() {
        DoubleColorBallDataPreload.execute();
        RedBallDataPreload.execute();
    }
}
