package test.com.squareup.retrofit2;

import okhttp3.ResponseBody;
import org.junit.Test;
import org.mortbay.util.ajax.JSON;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import test.com.squareup.retrofit2.supports.RequestSnapshot;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016-03-09.
 */
public class ResponseAdapterTest extends AbstractRetrofitTest {

    public static final int USER_ID = 1;

    @Override
    protected Retrofit init(Retrofit.Builder builder) {
        builder.addConverterFactory(new Converter.Factory() {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new ResponseBodyAsUser();
            }
        });
        return super.init(builder);
    }

    @Test
    public void convertResponseBodyToPojo() throws Exception {
        Client client = aServiceOf(Client.class);

        whenServerRespondAs("{\"name\":\"zhangsan\",\"password\":\"12345\"}");

        User user = client.get(USER_ID).execute().body();

        assertArrivedRequestMatching(any(RequestSnapshot.class));

        assertThat(user.name, equalTo("zhangsan"));
        assertThat(user.password, equalTo("12345"));
    }

    public static interface Client {
        @GET("/user/{id}")
        public Call<User> get(@Path("id") Integer id);
    }


    private static class User {
        public String name;
        public String password;

        public User(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }

    private static class ResponseBodyAsUser implements Converter<ResponseBody, Object> {
        @Override
        public Object convert(ResponseBody value) throws IOException {
            Map json = (Map) JSON.parse(value.string());
            return new User((String) json.get("name"), (String) json.get("password"));
        }
    }
}
