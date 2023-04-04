package tv.isshoni.mishima.protocol.http.handler;

import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

import java.util.Map;

public class HTTPHandler {

    protected final IWinryContext context;

    protected final IAnnotatedMethod method;

    protected final MIMEType mimeType;

    protected final Object target;

    public HTTPHandler(IWinryContext context, MIMEType mimeType, IAnnotatedMethod method, Object target) {
        this.context = context;
        this.mimeType = mimeType;
        this.method = method;
        this.target = target;
    }

    public MIMEType getMIMEType() {
        return this.mimeType;
    }

    public Object execute(Map<String, Object> provided) {
        return this.context.getMetaManager().execute(this.method, this.target, provided);
    }

    public IAnnotatedMethod getMethod() {
        return this.method;
    }
}
