package cosmin.server;

import java.io.*;
import java.net.*;

public class EmojiServer {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started, waiting for client connection...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received: " + clientMessage);

                File emojiFile = null;
                switch (clientMessage) {
                    case ":)":
                        emojiFile = new File("src/cosmin/server/smile.png");
                        break;
                    case ":(":
                        emojiFile = new File("src/cosmin/server/sad.png");
                        break;
                    default:
                        String errorMessage = "ERROR: Unknown command\n";
                        out.write(errorMessage.getBytes());
                        out.flush();
                        continue;
                }

                if (emojiFile != null && emojiFile.exists()) {
                    out.write("OK\n".getBytes()); //Start of sending file

                    FileInputStream fileInputStream = new FileInputStream(emojiFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

                    fileInputStream.close();
                    out.flush();
                    socket.shutdownOutput(); // end of outpit0
                    System.out.println("Emoji sent.");
                } else {
                    String errorMessage = "ERROR: Emoji file not found\n";
                    out.write(errorMessage.getBytes());
                    out.flush();
                }
            }

            System.out.println("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
