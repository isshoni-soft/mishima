package tv.isshoni.mishima.service;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.event.ConnectionEvent;
import tv.isshoni.mishima.event.MishimaConfigEvent;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.event.WinryShutdownEvent;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

@Injected
public class ConnectionService {

    @Logger("ConnectionService") private AraragiLogger logger;

    @Inject private IWinryContext context;

    private Thread thread;

    private ServerSocket socket;

    public void init(final MishimaConfigEvent event) {
        if (!event.isValid()) {
            throw new IllegalArgumentException("MishimaConfigEvent does ");
        }

        this.thread = new Thread(() -> {
            try {
                if (event.isTLS()) {
                    SSLContext sslContext = SSLContext.getInstance("TLS");
//                    sslContext.init();

//                    this.socket =
                } else {
                    this.socket = new ServerSocket(event.getPort());
                }
                logger.info("Listening for connections on port: " + event.getPort());

                while (!this.socket.isClosed()) {
                    Socket client = this.socket.accept();

                    this.logger.debug("Accepted new connection!");

                    try {
                        this.context.getEventBus().fire(new ConnectionEvent(client, this.context.getLoggerFactory()));
                    } catch (Throwable e) {
                        this.context.getExceptionManager().recover(e);
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                this.context.getExceptionManager().toss(e);
            }
        }, "ConnectionService");

        this.thread.start();
    }

    @Listener(WinryShutdownEvent.class)
    public void shutdown() throws IOException, InterruptedException {
        this.socket.close();
        this.thread.join();
    }
}
