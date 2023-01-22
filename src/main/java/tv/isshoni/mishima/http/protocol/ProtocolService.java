package tv.isshoni.mishima.http.protocol;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Injected
public class ProtocolService {

    private final AraragiLogger logger;

    private final Map<String, IProtocol> protocol;

    public ProtocolService(@Context IWinryContext context) {
        this.protocol = new HashMap<>();
        this.logger = context.getLoggerFactory().createLogger("ProtocolService");
    }

    void registerProtocol(String version, IProtocol protocol) {
        this.logger.info("Registering protocol: " + version + "...");
        this.protocol.put(version, protocol);
    }

    public Optional<IProtocol> getProtocol(String version) {
        return Optional.ofNullable(this.protocol.get(version));
    }
}
