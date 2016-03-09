package test.com.squareup.retrofit2.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016-03-09.
 */
public class IO {

    private static final int BUFF_SIZE = 1024;
    private static final int EOF = -1;

    public static byte[] getBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(in, out);
        return out.toByteArray();
    }

    public static void writeTo(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[BUFF_SIZE];
        while (true) {
            int n = in.read(buff);
            if (n == EOF) {
                break;
            }
            out.write(buff, 0, n);
        }
    }
}
