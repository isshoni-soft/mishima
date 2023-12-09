package tv.isshoni.mishima.service;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.util.FileUtil;
import tv.isshoni.mishima.event.ConnectionEvent;
import tv.isshoni.mishima.event.config.MishimaServerConfigEvent;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.event.WinryShutdownEvent;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Injected
public class ConnectionService {

    @Logger("ConnectionService") private AraragiLogger logger;

    @Context private IWinryContext context;

    private Thread thread;

    private ServerSocket socket;

    public void init(final MishimaServerConfigEvent event) {
        if (!event.isValid()) {
            throw new IllegalArgumentException("MishimaConfigEvent is not valid!");
        }

        this.thread = new Thread(() -> {
            try {
                if (event.isTLS()) {
                    char[] password = event.getKeystorePassword().toCharArray();

                    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    keyStore.load(FileUtil.getResource(event.getKeystorePath()), password);
                    logger.info("Successfully loaded keyfile!");

                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(keyStore);

                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
                    keyManagerFactory.init(keyStore, password);

                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

                    SSLServerSocketFactory socketFactory = sslContext.getServerSocketFactory();

                    logger.info("Creating SSL server socket...");

                    this.socket = socketFactory.createServerSocket(event.getPort());
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
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException |
                     UnrecoverableKeyException | KeyManagementException e) {
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
