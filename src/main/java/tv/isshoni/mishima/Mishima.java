package tv.isshoni.mishima;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.event.MishimaConfigEvent;
import tv.isshoni.mishima.event.MishimaInitEvent;
import tv.isshoni.mishima.service.ConnectionService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.exception.EventExecutionException;
import tv.isshoni.winry.api.service.VersionService;

import java.util.regex.Pattern;

@Loader(
        loadPackage = "tv.isshoni.mishima"
)
public class Mishima {

    public static final Pattern PATH_LEGAL = Pattern.compile("[/a-z0-9]+", Pattern.CASE_INSENSITIVE);

    @Logger("Mishima") private AraragiLogger logger;

    @Inject private IWinryContext context;

    @Listener(MishimaInitEvent.class)
    public void init(@Inject VersionService service, @Inject ConnectionService connection) {
        logger.info("Initializing Mishima v" + service.getVersion("mishima").get());

        logger.info("Suppressing Winry shutdown signal...");
        this.context.suppressShutdown();

        MishimaConfigEvent config;
        try {
            config = this.context.getEventBus().fire(MishimaConfigEvent.class);
            connection.init(config);
        } catch (EventExecutionException e) { }
    }
}
