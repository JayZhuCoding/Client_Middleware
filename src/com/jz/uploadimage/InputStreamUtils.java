package com.jz.uploadimage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.tukaani.xz.*;
import java.io.OutputStream;
import java.util.zip.*;


/**
 * Created by Jay.
 */
public class InputStreamUtils {
    final static int BUFFER_SIZE = 4096;

    // InputStream to String
    public static String InputStreamTOString(InputStream in) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[BUFFER_SIZE];

        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)

            outStream.write(data, 0, count);

        data = null;

        return new String(outStream.toByteArray(), "ISO-8859-1");

    }

    // InputStream to String with specified encoding
    public static String InputStreamTOString(InputStream in, String encoding)
            throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[BUFFER_SIZE];

        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)

            outStream.write(data, 0, count);

        data = null;

        return new String(outStream.toByteArray(), "ISO-8859-1");

    }

    // String to InputStream
    public static InputStream StringTOInputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));

        return is;

    }

    // InputStream to byte array
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[BUFFER_SIZE];

        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)

            outStream.write(data, 0, count);

        data = null;

        return outStream.toByteArray();

    }

    // byte array to InputStream
    public static InputStream byteTOInputStream(byte[] in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in);

        return is;

    }

    // byte array to String
    public static String byteTOString(byte[] in) throws Exception {

        InputStream is = byteTOInputStream(in);

        return InputStreamTOString(is);

    }

}


