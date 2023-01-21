import org.junit.Test;
import tv.isshoni.winry.api.Winry;

import java.util.concurrent.ExecutionException;

public class TestRunner {

    @Test
    public void testServer() throws ExecutionException, InterruptedException {
        Winry.bootstrap(TestServer.class);
    }
}
