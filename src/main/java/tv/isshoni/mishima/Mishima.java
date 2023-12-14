package tv.isshoni.mishima;

import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.data.collection.map.Maps;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.event.MishimaInitEvent;
import tv.isshoni.mishima.event.config.MishimaServerConfigEvent;
import tv.isshoni.mishima.protocol.http.HTTP;
import tv.isshoni.mishima.protocol.http.PathService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.mishima.service.ConnectionService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.event.WinryPreInitEvent;
import tv.isshoni.winry.api.service.VersionService;

import java.util.regex.Pattern;

@Loader(
        manualLoad = ConnectionService.class,
        loadPackage = {
                "tv.isshoni.mishima.event",
        }
)
public class Mishima {

    public static final Pattern PATH_LEGAL = Pattern.compile("[/a-z0-9]+", Pattern.CASE_INSENSITIVE);

    @Logger("Mishima") private AraragiLogger logger;

    @Context private IWinryContext context;

    private MishimaServerConfigEvent serverConfig;

    @Listener(WinryPreInitEvent.class)
    public void winryPreInit(@Context IWinryContext context, @Inject VersionService service) throws Throwable {
        logger.info("Loading Mishima v${version}...", Maps.ofPairs(
                Pair.of("version", () -> service.getVersion("mishima").get())));

        this.serverConfig = new MishimaServerConfigEvent();

        context.getEventBus().fire(this.serverConfig);

        if (this.serverConfig.isHTTP()) {
            context.addSingleton(HTTPService.class);
            context.addSingleton(PathService.class);
            context.addSingleton(HTTP.class);

            context.getInstanceManager().getSingletonInjection(HTTP.class).init();
        }
    }

    @Listener(MishimaInitEvent.class)
    public void init(@Inject VersionService service, @Inject ConnectionService connection) throws Throwable {
        logger.info("Initializing Mishima v" + service.getVersion("mishima").get());

        logger.info("Suppressing Winry shutdown signal...");
        this.context.suppressShutdown();
        connection.init(this.serverConfig);
    }
}
