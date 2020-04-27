import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            GameClient client = new GameClient();
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
