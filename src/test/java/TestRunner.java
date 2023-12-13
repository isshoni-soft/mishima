import tv.isshoni.winry.api.Winry;

import java.util.concurrent.ExecutionException;

public class TestRunner {

    public static void main(String... args) throws ExecutionException, InterruptedException {
        Winry.bootstrap(TestServer.class);
    }
}
