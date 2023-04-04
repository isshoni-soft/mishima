package tv.isshoni.mishima.exception.parameter;

import tv.isshoni.mishima.annotation.exception.StatusCode;
import tv.isshoni.mishima.protocol.http.HTTPStatus;
import tv.isshoni.mishima.protocol.http.MIMEType;

@StatusCode(HTTPStatus.BAD_REQUEST)
public class NeedsContentTypeException extends RuntimeException {

    private final MIMEType mimeType;

    public NeedsContentTypeException(MIMEType mimeType) {
        super();

        this.mimeType = mimeType;
    }

    public MIMEType getMimeType() {
        return this.mimeType;
    }
}
