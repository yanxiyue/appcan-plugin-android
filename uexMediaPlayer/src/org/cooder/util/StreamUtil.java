package org.cooder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Common copy stream utility.
 */
public class StreamUtil {

    /**
     * Default buffer size.
     */
    public final static int DEFAULT_BUFFER_SIZE = 2048;

    /**
     * Copies the contents of an InputStream to an OutputStream using a copy
     * buffer of a given size. The contents of the InputStream are read until
     * the end of the stream is reached, but neither the source nor the
     * destination are closed. You must do this yourself outside of the method
     * call.
     * <p>
     * 
     * @param source
     *            The source InputStream.
     * @param dest
     *            The destination OutputStream.
     * @param bufferSize
     *            The number of bytes to buffer during the copy.
     * @throws IOException
     *             If an error occurs during copying
     */
    public static void copyStream(InputStream source, OutputStream dest, int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        int bytesToWrite;
        byte[] buffer = new byte[bufferSize];

        try {
            while ((bytesToWrite = source.read(buffer)) != -1) {
                // Technically, some read(byte[]) methods may return 0 and we
                // cannot accept that as an indication of EOF.
                if (bytesToWrite == 0) {
                    bytesToWrite = source.read();
                    if (bytesToWrite < 0)
                        break;
                    dest.write(bytesToWrite);
                    continue;
                }
                dest.write(buffer, 0, bytesToWrite);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                dest.flush();
            } catch (IOException e) {
                // ignore
            }
            try {
                source.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                dest.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static void copyStreamNoClose(InputStream source, OutputStream dest, int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        int bytesToWrite;
        byte[] buffer = new byte[bufferSize];

        try {
            while ((bytesToWrite = source.read(buffer)) != -1) {
                // Technically, some read(byte[]) methods may return 0 and we
                // cannot accept that as an indication of EOF.
                if (bytesToWrite == 0) {
                    bytesToWrite = source.read();
                    if (bytesToWrite < 0)
                        break;
                    dest.write(bytesToWrite);
                    continue;
                }
                dest.write(buffer, 0, bytesToWrite);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                dest.flush();
            } catch (IOException e) {
                // ignore
            }
            try {
                source.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Copies the contents of an reader to an writer.
     * 
     * @param reader
     *            the source input reader.
     * @param writer
     *            the output writer.
     * @param bufferSize
     *            the byte array of buffer size
     * @throws IOException
     *             If an error occurs during copying
     */
    public static void copyStream(Reader reader, Writer writer, int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        int bytesToWrite;
        char[] buffer = new char[bufferSize];

        try {
            while ((bytesToWrite = reader.read(buffer)) != -1) {
                // Technically, some read(char[]) methods may return 0 and we
                // cannot accept that as an indication of EOF.
                if (bytesToWrite == 0) {
                    bytesToWrite = reader.read();
                    if (bytesToWrite < 0)
                        break;
                    writer.write(bytesToWrite);
                    writer.flush();
                    continue;
                }
                writer.write(buffer, 0, bytesToWrite);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                // ignore
            }
            try {
                reader.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                writer.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Copies the contents of an reader to an writer.
     * 
     * @param reader
     *            the source input reader.
     * @param writer
     *            the output writer.
     * @throws IOException
     *             If an error occurs during copying
     */
    public static void copyStream(Reader reader, Writer writer) throws IOException {
        copyStream(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies the contents of an InputStream to an OutputStream. The contents of
     * the InputStream are read until the end of the stream is reached, but
     * neither the source nor the destination are closed. You must do this
     * yourself outside of the method call.
     * 
     * @param source
     *            the source input InputStream.
     * @param dest
     *            the output OutputStream.
     * @throws IOException
     *             If an error occurs during copying
     */
    public static void copyStream(InputStream source, OutputStream dest) throws IOException {
        copyStream(source, dest, DEFAULT_BUFFER_SIZE);
    }

    /**
     * backup the contents of an InputStream to an InputStream. The contents of
     * the InputStream are read until the end of the stream is reached, but
     * neither the source nor the destination are closed.
     * 
     * @param source
     *            the source input InputStream
     * @return InputStream the new InputStream.
     * @throws IOException
     *             If an error occurs during copying
     */
    public static InputStream backupStream(InputStream source) throws IOException {
        byte[] b = StreamUtil.writeToByteArray(source);

        return new ByteArrayInputStream(b);
    }

    /**
     * convert ByteArray from InputStream.
     * 
     * @param in
     *            ByteArrayOutputStream
     * @return ByteArray
     * @throws IOException
     *             If an error occurs during copying
     * 
     */
    public static byte[] writeToByteArray(InputStream in) throws IOException {
        return writeToByteArray(in, DEFAULT_BUFFER_SIZE);
    }

    /**
     * convert ByteArray from InputStream.
     * 
     * @param in
     *            ByteArrayOutputStream
     * @param bufferSize
     *            the byte array of buffer size
     * @return ByteArray
     * @throws IOException
     *             If an error occurs during copying
     * 
     */
    public static byte[] writeToByteArray(InputStream in, int bufferSize) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copyStream(in, baos, bufferSize);
        return baos.toByteArray();
    }

    /**
     * convert String from Reader to outputCharset.
     * 
     * @param reader
     *            Reader
     * @return String
     * @throws IOException
     *             If an error occurs during copying
     */
    public static String writeToString(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        copyStream(reader, writer);
        return writer.toString();
    }
}
