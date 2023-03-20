package tv.isshoni.mishima.http;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.winry.api.context.ILoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HTTPConnection implements Closeable {

    private final AraragiLogger logger;

    private final Socket socket;
    
    private final BufferedReader clientReader;
    
    private final BufferedWriter clientWriter;
    
    public HTTPConnection(Socket socket, ILoggerFactory loggerFactory) throws IOException {
        this.logger = loggerFactory.createLogger("Conn" + socket.getRemoteSocketAddress().toString());
        this.socket = socket;
        this.clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void write(String line) {
        logger.debug("-> " + line);
        try {
            this.clientWriter.write(line + "\n");
        } catch (IOException e) { /* Swallowed */ }
    }

    public String readLine() {
        try {
            String line = this.clientReader.readLine();
            logger.debug("<- " + line);
            return line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasLine() throws IOException {
        return this.clientReader.ready();
    }

    @Override
    public void close() throws IOException {
        this.clientWriter.close();
        this.socket.close();
    }
}
