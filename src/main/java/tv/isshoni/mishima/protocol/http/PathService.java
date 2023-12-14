package tv.isshoni.mishima.protocol.http;

import tv.isshoni.mishima.annotation.http.Path;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.meta.IAnnotatedClass;

import java.util.HashMap;
import java.util.Map;

@Injected
public class PathService {

    private final Map<IAnnotatedClass, Path> paths;

    public PathService() {
        this.paths = new HashMap<>();
    }

    public void register(IAnnotatedClass clazz, Path path) {
        if (this.paths.containsKey(clazz)) {
            Path previous = this.paths.get(clazz);

            if (previous.weight() - path.weight() < 0) {
                return;
            }
        }

        this.paths.put(clazz, path);
    }

    public Path getPath(IAnnotatedClass clazz) {
        return this.paths.get(clazz);
    }

    public boolean contains(IAnnotatedClass clazz) {
        return this.paths.containsKey(clazz);
    }
}
