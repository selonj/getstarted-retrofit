package test.com.squareup.retrofit2;

import okhttp3.RequestBody;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import test.com.squareup.retrofit2.utils.StringRequestBodyConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static okhttp3.RequestBody.create;
import static test.com.squareup.retrofit2.supports.RequestSnapshotMatchers.bodyAsString;
import static test.com.squareup.retrofit2.utils.StringRequestBodyConverter.asRequestBody;

/**
 * Created by Administrator on 2016-03-09.
 */
public class RequestBodyTest extends AbstractRetrofitTest {

    private Client client;
    private Map<Type, Converter<?, RequestBody>> converters = new HashMap<>();

    private interface Client {
        @POST("/upload")
        Call<Void> upload(@Body RequestBody body);

        @POST("/upload")
        Call<Void> upload(@Body String body);
    }

    @Before
    public void setUp() throws Exception {
        client = aServiceOf(Client.class);
    }

    @Override
    protected Retrofit init(Retrofit.Builder builder) {
        builder.addConverterFactory(new Converter.Factory() {
            @Override
            public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
                return converters.get(type);
            }
        });
        return super.init(builder);
    }

    @Test
    public void annotatedRequestBody() throws Exception {
        client.upload(asRequestBody("<file>")).execute();

        assertArrivedRequestMatching(bodyAsString("<file>"));
    }


    @Test
    public void annotatedOtherTypesMustRegisterRequestBodyConverter() throws Exception {
        converters.put(String.class, new StringRequestBodyConverter());

        client.upload("<file>").execute();

        assertArrivedRequestMatching(bodyAsString("<file>"));
    }

}
