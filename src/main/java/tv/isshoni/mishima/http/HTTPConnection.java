package tv.isshoni.mishima.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HTTPConnection implements Closeable {
    
    private final Socket socket;
    
    private final BufferedReader clientReader;
    
    private final BufferedWriter clientWriter;
    
    public HTTPConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void write(String line) {
        try {
            this.clientWriter.write(line + "\n");
        } catch (IOException e) { /* Swallowed */ }
    }

    public String readLine() {
        try {
            return this.clientReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        this.clientWriter.close();
        this.socket.close();
    }
}
