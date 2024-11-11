package utils;

import model.HttpModel;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static common.SimpleHttpServer.*;

public class HttpHandler extends HttpModel implements Runnable{
    public HttpHandler(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
             PrintWriter output = new PrintWriter(getSocket().getOutputStream(), true)) {

            String request = input.readLine();

            if (request != null) {
                String[] parts = request.split("\\s+");
                if (parts.length >= 2 && parts[0].equals("GET")) {
                    String path = parts[1];

                    if (path.equals("/") || path.equals("/index.html")) {
                        sendNewHtmlResponse(output);
                    }else if (path.equals("/style.css")) {  // Check for CSS request
                        sendNewCssResponse(output);
                    }else if (path.equals("/json")) {
                        sendNewJsonResponse(output);
                    } else {
                        sendNewNotFoundResponse(output);
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                getSocket().close();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendNewHtmlResponse(PrintWriter output)  throws IOException {
        File file = new File(HTML_FILE_PATHS);

        if (file.exists()) {
            sendResponse(output, file, "text/html");
        }else {
            sendNewNotFoundResponse(output);
        }
    }

    private void sendNewCssResponse(PrintWriter output) throws IOException {
        File file = new File(CSS_FILE_PATHS);

        if (file.exists()) {
            sendResponse(output, file, "text/css");
        } else {
            sendNewNotFoundResponse(output);
        }
    }


    private void sendNewJsonResponse(PrintWriter output) throws IOException {
        File file = new File(JSON_FILE_PATHS);

        if (file.exists()) {
            sendResponse(output, file, "application/json");
        }else {
            sendNewNotFoundResponse(output);
        }

    }


    private void sendNewNotFoundResponse(PrintWriter output) {
        output.println("HTTP/1.1 404 Not Found");
        output.println("Content-Type: text/plain");
        output.println();
        output.println("404 Not Found- The required resources was not found on this server");

    }

    private void sendResponse(PrintWriter output, File file, String contentType) throws IOException {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: " + contentType);
        output.println();

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;

            while ((line = reader.readLine()) != null) {
                output.println(line);
                System.out.println(line);
            }
        }
    }



}
