package test.com.squareup.retrofit2.supports;

import org.hamcrest.Matcher;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016-03-09.
 */
public class JettyServerDriver implements ServerDriver {
    public static final int serverPort = 80;
    private ServletDriver servletDriver = new ServletDriver();
    private Server server;

    public JettyServerDriver() {
        server = new Server(serverPort);
        server.setHandler(new ServletHandler() {{
            addServletWithMapping(new ServletHolder(servletDriver), "/*");
        }});
        stopAtShutdown();
    }

    @Override
    public void start() throws Exception {
        server.start();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    @Override
    public void stopAtShutdown() {
        server.setStopAtShutdown(true);
    }


    @Override
    public void assertArrivedRequestMatching(Matcher<RequestSnapshot>... matchers) throws InterruptedException {
        servletDriver.assertArrivedRequestMatching(matchers);
    }

    @Override
    public String baseUrl() {
        return String.format("http://localhost:%d", serverPort);
    }

    @Override
    public void respondAs(String content) {
        servletDriver.respondToAllRequestWith(content);
    }

    public class ServletDriver extends HttpServlet {
        private BlockingQueue<RequestSnapshot> requestSnapshots = new ArrayBlockingQueue<RequestSnapshot>(1);
        private String content = "";

        @Override
        protected void service(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                requestSnapshots.put(new RequestSnapshot(req));
            } catch (InterruptedException ignored) {
            }
            respond(req,resp);
        }

        private void respond(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            PrintWriter out = resp.getWriter();
            out.write(content);
            out.close();
        }

        private void assertArrivedRequestMatching(Matcher<RequestSnapshot>... matchers) throws InterruptedException {
            RequestSnapshot snapshot = requestSnapshots.poll(1, TimeUnit.SECONDS);
            assertThat(snapshot, allOf(matchers));
        }

        public void respondToAllRequestWith(String content) {
            this.content = content;
        }
    }

}
