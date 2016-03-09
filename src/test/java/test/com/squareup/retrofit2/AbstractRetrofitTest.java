package test.com.squareup.retrofit2;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import retrofit2.Retrofit;
import test.com.squareup.retrofit2.supports.RequestSnapshot;
import test.com.squareup.retrofit2.supports.ServerDriver;
import test.com.squareup.retrofit2.supports.StandaloneServerDriver;

/**
 * Created by Administrator on 2016-03-09.
 */
abstract public class AbstractRetrofitTest {
    private final ServerDriver server = StandaloneServerDriver.INSTANCE;
    private Retrofit retrofit;

    @Before
    public final void start() throws Exception {
        server.start();
        retrofit = init(new Retrofit.Builder().baseUrl(server.baseUrl()));
    }

    protected Retrofit init(Retrofit.Builder builder) {
        return builder.build();
    }

    @After
    public final void stop() throws Exception {
        server.stop();
    }

    protected <T> T aServiceOf(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    protected void assertArrivedRequestMatching(Matcher<RequestSnapshot>... matchers) throws InterruptedException {
        server.assertArrivedRequestMatching(matchers);
    }

    protected void whenServerRespondAs(String content) {
        server.respondAs(content);
    }
}
