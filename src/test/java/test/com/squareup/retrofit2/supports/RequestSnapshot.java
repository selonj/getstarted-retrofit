package test.com.squareup.retrofit2.supports;

import test.com.squareup.retrofit2.utils.IO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-03-09.
 */
public class RequestSnapshot {

    private Map<String, String> headers = new HashMap<>();
    private String method;
    private Map parameters;
    private String uri;
    private String queryString;
    private byte[] body;

    public RequestSnapshot(HttpServletRequest request) throws IOException {
        uri = request.getRequestURI();
        queryString = request.getQueryString();
        parameters = request.getParameterMap();
        method = request.getMethod();
        headers = getHeaders(request);
        body = IO.getBytes(request.getInputStream());
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        for (Object name : Collections.list(request.getHeaderNames())) {
            headers.put((String) name, request.getHeader((String) name));
        }
        return headers;
    }

    public Map getParameters() {
        return parameters;
    }

    public String getUri() {
        return uri;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        String[] values = (String[]) parameters.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getMethod() {
        return method;
    }

    public String getBodyAsString() {
        return new String(body);
    }
}
