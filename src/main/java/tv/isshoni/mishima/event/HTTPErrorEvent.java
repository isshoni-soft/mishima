package tv.isshoni.mishima.event;

import tv.isshoni.mishima.http.HTTPErrorType;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.winry.api.event.ICancellable;

public class HTTPErrorEvent implements ICancellable {

    private boolean cancelled;

    private final HTTPErrorType type;

    private final HTTPRequest request;

    public HTTPErrorEvent(HTTPErrorType type, HTTPRequest request) {
        this.type = type;
        this.request = request;
    }

    public HTTPRequest getRequest() {
        return this.request;
    }

    public HTTPErrorType getType() {
        return this.type;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
