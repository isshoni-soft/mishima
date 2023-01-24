package tv.isshoni.mishima.http;

import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.internal.model.meta.IAnnotatedMethod;

import java.util.HashMap;
import java.util.Map;

public class HTTPHandler {

    private final IWinryContext context;

    private final IAnnotatedMethod method;

    private final Object target;

    public HTTPHandler(IWinryContext context, IAnnotatedMethod method, Object target) {
        this.context = context;
        this.method = method;
        this.target = target;
    }

    public void execute(Map<String, Object> provided) {
        this.context.getMetaManager().execute(this.method, this.target, provided);
    }

    public IAnnotatedMethod getMethod() {
        return this.method;
    }
}
