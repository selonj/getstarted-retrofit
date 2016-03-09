package test.com.squareup.retrofit2;

import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.http.*;

import static test.com.squareup.retrofit2.supports.RequestSnapshotMatchers.*;

/**
 * Created by Administrator on 2016-03-09.
 */
public class QuickstartRetrofitTest extends AbstractRetrofitTest {

    public static final String METHOD_DETECT = "DETECT";
    public static final String TOUCH_URI = "/touch";
    public static final String SEARCH_URI = "/search";
    public static final String SAVE_URI = "/save";
    public static final String CONTENT_NAME = "content";
    public static final String ACCESS_URI = "/access";
    public static final String HEADER_NAME = "user";
    public static final String PING_URI = "/";
    private Client client;

    private interface Client {
        @GET(PING_URI)
        Call<Void> ping();

        @HTTP(method = METHOD_DETECT, path = TOUCH_URI)
        Call<Void> touch();


        @GET("/echo/{name}")
        Call<Void> echo(@Path("name") String name);

        @GET(SEARCH_URI)
        Call<Void> search(@Query("kw") String word);

        @POST(SAVE_URI)
        @FormUrlEncoded
        Call<Void> save(@Field(CONTENT_NAME) String content);

        @GET(ACCESS_URI)
        Call<Void> access(@Header(HEADER_NAME) String username);

    }

    @Before
    public void setUp() throws Exception {
        client = aServiceOf(Client.class);
    }

    @Test
    public void tinyRequest() throws Exception {
        client.ping().execute();

        assertArrivedRequestMatching(uri(PING_URI));
    }

    @Test
    public void customizedRequest() throws Exception {
        client.touch().execute();

        assertArrivedRequestMatching(uri(TOUCH_URI), requestAs(METHOD_DETECT));
    }


    @Test
    public void pathVariables() throws Exception {
        client.echo("jack").execute();

        assertArrivedRequestMatching(uri("/echo/jack"));
    }

    @Test
    public void queryString() throws Exception {
        client.search("foo").execute();

        assertArrivedRequestMatching(uri(SEARCH_URI), hasQueryString("kw=foo"));
    }

    @Test
    public void queryStringCanBeEncoded() throws Exception {
        client.search("#word#").execute();

        assertArrivedRequestMatching(uri(SEARCH_URI), hasQueryString("kw=%23word%23"));
    }

    @Test
    public void fields() throws Exception {
        client.save("something").execute();

        assertArrivedRequestMatching(
                uri(SAVE_URI), hasNoQueryString(),
                param(CONTENT_NAME, "something")
        );
    }

    @Test
    public void headers() throws Exception {
        client.access("admin").execute();

        assertArrivedRequestMatching(
                uri(ACCESS_URI), header(HEADER_NAME, "admin")
        );
    }


}
