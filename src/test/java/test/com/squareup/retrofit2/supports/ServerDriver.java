package test.com.squareup.retrofit2.supports;

import org.hamcrest.Matcher;

/**
 * Created by Administrator on 2016-03-09.
 */
public interface ServerDriver {
    void start() throws Exception;

    void stop() throws Exception;

    void stopAtShutdown();

    void assertArrivedRequestMatching(Matcher<RequestSnapshot>... matchers) throws InterruptedException;

    String baseUrl();

    void respondAs(String content);
}
