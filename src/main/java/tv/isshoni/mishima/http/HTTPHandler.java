package tv.isshoni.mishima.http;

import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.internal.model.meta.IAnnotatedMethod;

import java.util.Map;

public class HTTPHandler {

    private final IWinryContext context;

    private final IAnnotatedMethod method;

    private final MIMEType mimeType;

    private final Object target;

    public HTTPHandler(IWinryContext context, MIMEType mimeType, IAnnotatedMethod method, Object target) {
        this.context = context;
        this.mimeType = mimeType;
        this.method = method;
        this.target = target;
    }

    public MIMEType getMIMEType() {
        return this.mimeType;
    }

    public <R> R execute(Map<String, Object> provided) {
        return this.context.getMetaManager().execute(this.method, this.target, provided);
    }

    public IAnnotatedMethod getMethod() {
        return this.method;
    }
}
