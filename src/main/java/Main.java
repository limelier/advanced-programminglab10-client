import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static final String SERVER_ADDRESS = "127.0.0.1";
    static final int PORT = 8531;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
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
                    response = "Server stopping.";
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
