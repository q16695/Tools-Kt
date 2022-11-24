package com.github.q16695.notifications;

import javax.imageio.stream.ImageInputStream;
import java.io.*;

@SuppressWarnings({"unused", "Duplicates"})
public
class IO {
    /**
     * Convenient close for a Closeable.
     */
    @SuppressWarnings("Duplicates")
    public static
    void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                System.err.println("Error closing : " + closeable);
                e.printStackTrace();
            }
        }
    }

    /**
     * Convenient close for a Closeable.
     */
    @SuppressWarnings("Duplicates")
    public static
    void close(final Closeable closeable, final org.slf4j.Logger logger) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.error("Error closing: {}", closeable, e);
            }
        }
    }

    /**
     * Convenient close for a Closeable.
     */
    @SuppressWarnings("Duplicates")
    public static
    void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Copy the contents of the input stream to the output stream.
     * <p>
     * DOES NOT CLOSE THE STEAMS!
     */
    public static
    <T extends OutputStream> T copyStream(final InputStream inputStream, final T outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;

        while ((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();

        return outputStream;
    }

    /**
     * Copy the contents of the input stream to the output stream.
     * <p>
     * DOES NOT CLOSE THE STEAMS!
     */
    public static
    <T extends OutputStream> T copyStream(final ImageInputStream inputStream, final T outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;

        while ((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();

        return outputStream;
    }

    /**
     * Copy the contents of the input stream to a new output stream.
     * <p>
     * DOES NOT CLOSE THE STEAMS!
     */
    public static ByteArrayOutputStream copyStream(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int read;

        while ((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();

        return outputStream;
    }

    /**
     * Copy the contents of the input stream to a new output stream.
     * <p>
     * DOES NOT CLOSE THE STEAMS!
     */
    public static
    ByteArrayOutputStream copyStream(final ImageInputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int read;

        while ((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();

        return outputStream;
    }
}

