package tv.isshoni.mishima;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.event.MishimaConfigEvent;
import tv.isshoni.mishima.event.MishimaInitEvent;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.event.WinryPreInitEvent;
import tv.isshoni.winry.api.service.VersionService;

@Loader
public class Mishima {

    @Logger("Mishima") private AraragiLogger logger;

    @Inject private IWinryContext context;

    @Listener(WinryPreInitEvent.class)
    public void onPreInit() {
        logger.info("Suppressing Winry shutdown signal...");
        this.context.suppressShutdown();
    }

    @Listener(MishimaInitEvent.class)
    public void init(@Inject VersionService service) {
        logger.info("Initializing Mishima v" + service.getVersion("mishima"));

        MishimaConfigEvent config = context.getEventBus().fire(MishimaConfigEvent.class);

        // TODO: Create connection listener thread.
    }
}
