package src.khai;

public class KhaiMain {
    public static void main(String[] args) {
        Game game = new Game();
        Draw draw = new Draw(game);
        draw.run();
    }
}
