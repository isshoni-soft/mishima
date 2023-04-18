package tv.isshoni.mishima.protocol.http;

import tv.isshoni.mishima.annotation.http.Overseer;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.meta.IAnnotatedClass;

import java.util.HashMap;
import java.util.Map;

@Injected
public class OverseerService {

    private final Map<IAnnotatedClass, Overseer> paths;

    public OverseerService() {
        this.paths = new HashMap<>();
    }

    public void register(IAnnotatedClass clazz, Overseer overseer) {
        if (this.paths.containsKey(clazz)) {
            Overseer previous = this.paths.get(clazz);

            if (previous.weight() - overseer.weight() < 0) {
                return;
            }
        }

        this.paths.put(clazz, overseer);
    }

    public Overseer getPath(IAnnotatedClass clazz) {
        return this.paths.get(clazz);
    }

    public boolean contains(IAnnotatedClass clazz) {
        return this.paths.containsKey(clazz);
    }
}
