package tv.isshoni.mishima.service;

import tv.isshoni.araragi.data.Constant;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.protocol.IProtocol;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.service.ObjectFactory;

@Injected
public class ProtocolService {

    @Logger("ProtocolService") private AraragiLogger LOGGER;

    private final AraragiLogger logger;

    private final Constant<IProtocol> protocol;

    private final ObjectFactory objectFactory;

    public ProtocolService(@Context IWinryContext context, @Inject ObjectFactory objectFactory) {
        this.protocol = new Constant<>();
        this.objectFactory = objectFactory;
        this.logger = context.getLoggerFactory().createLogger("ProtocolService");
    }

    public void useProtocol(Class<? extends IProtocol> clazz) {
        this.protocol.set(this.objectFactory.construct(clazz));
    }


}
