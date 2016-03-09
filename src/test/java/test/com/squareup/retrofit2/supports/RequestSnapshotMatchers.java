package test.com.squareup.retrofit2.supports;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import test.com.squareup.retrofit2.supports.RequestSnapshot;

import static org.hamcrest.Matchers.*;

/**
 * Created by Administrator on 2016-03-09.
 */
public class RequestSnapshotMatchers {
    public static Matcher<RequestSnapshot> hasNoQueryString() {
        return hasQueryString(nullValue(String.class));
    }

    public static Matcher<RequestSnapshot> hasQueryString(String queryString) {
        return hasQueryString(equalTo(queryString));
    }

    public static Matcher<RequestSnapshot> hasQueryString(Matcher<String> matcher) {
        return hasProperty("queryString", matcher);
    }

    public static Matcher<RequestSnapshot> uri(Object uri) {
        return hasProperty("uri", equalTo(uri));
    }

    public static Matcher<RequestSnapshot> param(final String name, String value) {
        return new FeatureMatcher<RequestSnapshot, String>(equalTo(value), graph("param", name) + " = ", graph("param", name)) {
            @Override
            protected String featureValueOf(RequestSnapshot actual) {
                return actual.getParameter(name);
            }
        };
    }

    private static String graph(String type, String property) {
        return type + "['" + property + "']";
    }

    public static Matcher<RequestSnapshot> requestAs(String method) {
        return hasProperty("method", equalTo(method));
    }

    public static Matcher<RequestSnapshot> header(final String name, String value) {
        return new FeatureMatcher<RequestSnapshot, String>(equalTo(value), graph("header", name) + " = ", graph("header", name)) {
            @Override
            protected String featureValueOf(RequestSnapshot actual) {
                return actual.getHeader(name);
            }
        };
    }

    public static Matcher<RequestSnapshot> bodyAsString(String body) {
        return new FeatureMatcher<RequestSnapshot, String>(equalTo(body), "body", "body") {
            @Override
            protected String featureValueOf(RequestSnapshot actual) {
                return actual.getBodyAsString();
            }
        };
    }
}
