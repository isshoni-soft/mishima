package tv.isshoni.mishima.http;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.event.ConnectionEvent;
import tv.isshoni.mishima.exception.HTTPProtocolException;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.transformer.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Injected
public class HTTPService {

    @Logger("HTTPService") private AraragiLogger logger;

    @Listener(ConnectionEvent.class)
    @Async
    public void handleNewConnection(@Event ConnectionEvent event) throws IOException {
        Socket client = event.getClient();

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        // PROCESS FIRST LINE
        String line = in.readLine();
        String[] tokens = line.split(" ");

        logger.info("Received first line: " + line);

        if (tokens.length != 3) {
            throw new HTTPProtocolException("malformed first line");
        }

        HTTPMethod method;
        try {
            method = HTTPMethod.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HTTPProtocolException(tokens[0] + " is not a valid HTTP method");
        }

        logger.info("Detected method: " + method);
    }
}
