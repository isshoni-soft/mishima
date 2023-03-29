package tv.isshoni.mishima;

import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.data.collection.map.Maps;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.HTTPSerializer;
import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.annotation.http.parameter.Path;
import tv.isshoni.mishima.annotation.http.parameter.Query;
import tv.isshoni.mishima.event.MishimaInitEvent;
import tv.isshoni.mishima.event.config.MishimaServerConfigEvent;
import tv.isshoni.mishima.http.handler.HTTPService;
import tv.isshoni.mishima.http.protocol.HTTP1;
import tv.isshoni.mishima.http.protocol.ProtocolService;
import tv.isshoni.mishima.service.ConnectionService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.event.WinryPreInitEvent;
import tv.isshoni.winry.api.service.VersionService;

import java.util.regex.Pattern;

@Loader(
        loadPackage = {
                "tv.isshoni.mishima.event",
                "tv.isshoni.mishima.service" // only load events and connection indiscriminately
        }
)
public class Mishima {

    public static final Pattern PATH_LEGAL = Pattern.compile("[/a-z0-9]+", Pattern.CASE_INSENSITIVE);

    @Logger("Mishima") private AraragiLogger logger;

    @Inject private IWinryContext context;

    private MishimaServerConfigEvent serverConfig;

    @Listener(WinryPreInitEvent.class)
    public void winryPreInit(@Inject IWinryContext context, @Inject VersionService service) throws Throwable {
        logger.info("Loading Mishima v${version}...", Maps.ofPairs(
                Pair.of("version", () -> service.getVersion("mishima").get())));

        this.serverConfig = new MishimaServerConfigEvent();

        context.getEventBus().fire(this.serverConfig);

        if (this.serverConfig.isHTTP()) {
            context.addSingleton(ProtocolService.class);
            context.addSingleton(HTTPService.class);
            context.getAnnotationManager().discoverAnnotation(Protocol.class);
            context.getAnnotationManager().discoverAnnotation(HTTPSerializer.class);
            context.getAnnotationManager().discoverAnnotation(GET.class);
            context.getAnnotationManager().discoverAnnotation(POST.class);
            context.getAnnotationManager().discoverAnnotation(Body.class);
            context.getAnnotationManager().discoverAnnotation(Path.class);
            context.getAnnotationManager().discoverAnnotation(Query.class);
            context.addSingleton(HTTP1.class);
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
