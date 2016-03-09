package test.com.squareup.retrofit2.utils;

import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.IOException;

import static okhttp3.MultipartBody.DIGEST;
import static okhttp3.RequestBody.create;

/**
 * Created by Administrator on 2016-03-09.
 */
public class StringRequestBodyConverter implements Converter<String, RequestBody> {
    @Override
    public RequestBody convert(String value) throws IOException {
        return asRequestBody(value);
    }

    public static RequestBody asRequestBody(String value) {
        return create(DIGEST, value);
    }
}
