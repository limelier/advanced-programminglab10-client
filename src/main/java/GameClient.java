import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    static final String SERVER_ADDRESS = "127.0.0.1";
    static final int PORT = 8531;
    final Socket socket;


    public GameClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, PORT);
    }

    public void run() {
        try (socket) {
            PrintWriter to = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader from = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner in = new Scanner(System.in);

            while (true) {
                String command = in.nextLine();

                if (command.equals("exit")) {
                    break;
                }

                to.println(command);
                String response = from.readLine();
                if (response == null) {
                    System.out.println("Sorry, it looks like the server has been closed.");
                    break;
                }

                System.out.println(response);

                if (response.equals("Server stopping.")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
