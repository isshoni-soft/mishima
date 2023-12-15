package tv.isshoni.mishima.protocol.http;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.Path;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.meta.IAnnotatedClass;

import java.util.HashMap;
import java.util.Map;

@Injected
public class PathService {

    private final AraragiLogger logger;

    private final Map<IAnnotatedClass, Path> paths;

    public PathService(@Logger("PathService") AraragiLogger logger) {
        this.paths = new HashMap<>();
        this.logger = logger;
    }

    public void register(IAnnotatedClass clazz, Path path) {
        if (this.paths.containsKey(clazz)) {
            Path previous = this.paths.get(clazz);

            if (previous.weight() - path.weight() < 0) {
                return;
            }
        }

        this.logger.debug("Registered path: ${0} for class ${1} (${2})", path.value(), clazz.getDisplay(), clazz.hashCode());
        this.paths.put(clazz, path);
    }

    public Path getPath(IAnnotatedClass clazz) {
        return this.paths.get(clazz);
    }

    public boolean contains(IAnnotatedClass clazz) {
        return this.paths.containsKey(clazz);
    }
}
