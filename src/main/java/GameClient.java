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

    public void play(PrintWriter to, BufferedReader from, Scanner in) throws IOException {
        while (true) {
            String response = from.readLine();
            if (printResponseNotGame(from, response)) {
                return;
            }

            while (true) {
                String command = in.nextLine();
                to.println(command);

                response = from.readLine();

                if (response.equals("Quitting game and returning to lobby.")) {
                    System.out.println(response);
                    return;
                }

                if (!response.equals("Invalid move, try again.")) {
                    break;
                }
                System.out.println(response);
            }

            if (printResponseNotGame(from, response)) {
                return;
            }
        }
    }

    private boolean printResponseNotGame(BufferedReader from, String response) throws IOException {
        if (response.equals("# GAME STATE #")) {
            System.out.println();
            for (int i = 0; i < 19; i++) {
                response = from.readLine();
                System.out.println(response);
            }
            System.out.println();
            return false;
        } else {
            System.out.println(response);
            return true;
        }
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
                } else if (command.equals("host")) {
                    to.println(command);
                    String response = from.readLine();

                    if (response == null) {
                        System.out.println("Sorry, it looks like the server has been closed.");
                        break;
                    }

                    System.out.println(response);
                    response = from.readLine();

                    if (response == null) {
                        System.out.println("Sorry, it looks like the server has been closed.");
                        break;
                    }
                    System.out.println(response);
                    play(to, from, in);
                } else if (command.startsWith("join")) {
                    String[] words = command.split(" ");
                    if (words.length != 2) {
                        System.out.println("Usage: 'join <code>'");
                        continue;
                    }
                    to.println(command);
                    String response = from.readLine();
                    System.out.println(response);

                    if (!response.equals("Invalid game code.")
                            && !response.equals("This game is already running!")
                            && !response.equals("This game has already ended!")) {
                        play(to, from, in);
                    }

                } else {
                    System.out.println("Invalid command. Valid commands: 'host', 'join <code>', or 'exit'.");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
