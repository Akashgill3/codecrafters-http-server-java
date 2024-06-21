import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = reader.readLine();
            String[] httpRequest = line.split(" ", 0);

            if (httpRequest[1].equals("/")) {
                clientSocket.getOutputStream().write(
                        "HTTP/1.1 200 OK\r\n\r\n".getBytes()
                );
            } else if (httpRequest[1].startsWith("/echo/")) {
                String responseBody = httpRequest[1].substring("/echo/".length());
                String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + responseBody.length() + "\r\n\r\n" + responseBody;
                clientSocket.getOutputStream().write(
                        response.getBytes()
                );
            } else clientSocket.getOutputStream().write(
                    "HTTP/1.1 404 Not Found\r\n\r\n".getBytes()
            );
            System.out.println("accepted new connection");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
