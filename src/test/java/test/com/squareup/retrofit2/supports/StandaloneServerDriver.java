package test.com.squareup.retrofit2.supports;

import org.hamcrest.Matcher;

/**
 * Created by Administrator on 2016-03-09.
 */
public class StandaloneServerDriver implements ServerDriver {
    public static final StandaloneServerDriver INSTANCE = new StandaloneServerDriver(new JettyServerDriver());
    private ServerDriver target;
    private boolean started;

    public StandaloneServerDriver(ServerDriver target) {
        this.target = target;
    }

    @Override
    public void start() throws Exception {
        if (started) {
            return;
        }
        started = true;
        target.stopAtShutdown();
        target.start();

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void stopAtShutdown() {
        target.stopAtShutdown();
    }

    @Override
    public void assertArrivedRequestMatching(Matcher<RequestSnapshot>... matchers) throws InterruptedException {
        target.assertArrivedRequestMatching(matchers);
    }

    @Override
    public String baseUrl() {
        return target.baseUrl();
    }

    @Override
    public void respondAs(String content) {
        target.respondAs(content);
    }
}
