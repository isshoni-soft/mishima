import org.junit.Ignore;
import org.junit.Test;
import tv.isshoni.winry.api.Winry;

import java.util.concurrent.ExecutionException;

public class TestRunner {

    @Test
    @Ignore

    public void testServer() throws ExecutionException, InterruptedException {
        Winry.bootstrap(TestServer.class);
    }
}
