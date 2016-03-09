package test.com.squareup.retrofit2;

import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import test.com.squareup.retrofit2.supports.RequestSnapshot;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016-03-09.
 */
public class AsyncronousCallTest extends AbstractRetrofitTest implements Callback<ResponseBody> {

    private Client client;

    public static interface Client {

        @GET("/")
        @Streaming
        Call<ResponseBody> touch();
    }


    @Before
    public void setUp() throws Exception {
        client = aServiceOf(Client.class);
    }

    private BlockingQueue<String> responds = new ArrayBlockingQueue<String>(1);

    @Test
    public void asyncCallByEnqueue() throws Exception {
        whenServerRespondAs("retrofit");

        client.touch().enqueue(this);

        assertThat(responds.poll(1, TimeUnit.SECONDS), equalTo("retrofit"));
        assertArrivedRequestMatching(any(RequestSnapshot.class));
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            responds.add(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }
}
