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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Injected
public class ConnectionService {

    @Logger("ConnectionService") private AraragiLogger logger;

    @Inject private IWinryContext context;

    private Thread thread;

    private ServerSocket socket;

    public void init(MishimaConfigEvent event) {
        if (!event.isValid()) {
            throw new IllegalArgumentException("MishimaConfigEvent does ");
        }

        this.thread = new Thread(() -> {
            try {
                this.socket = new ServerSocket(event.getPort());
                logger.info("Listening for connections on port: " + event.getPort());

                while (!this.socket.isClosed()) {
                    Socket client = this.socket.accept();

                    this.logger.debug("Accepted new connection!");

                    try {
                        this.context.getEventBus().fire(new ConnectionEvent(client));
                    } catch (Throwable e) {
                        this.context.getExceptionManager().recover(e);
                    }
                }
            } catch (IOException e) {
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