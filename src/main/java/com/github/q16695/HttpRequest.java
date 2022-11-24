package com.github.q16695;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.Proxy.Type;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpRequest {
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String ENCODING_GZIP = "gzip";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_SERVER = "Server";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";
    public static final String PARAM_CHARSET = "charset";
    private static final String BOUNDARY = "00content0boundary00";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary=00content0boundary00";
    private static final String CRLF = "\r\n";
    private static final String[] EMPTY_STRINGS = new String[0];
    private static SSLSocketFactory TRUSTED_FACTORY;
    private static HostnameVerifier TRUSTED_VERIFIER;
    private static HttpRequest.ConnectionFactory CONNECTION_FACTORY;
    private HttpURLConnection connection = null;
    private final URL url;
    private final String requestMethod;
    private HttpRequest.RequestOutputStream output;
    private boolean multipart;
    private boolean form;
    private boolean ignoreCloseExceptions = true;
    private boolean uncompress = false;
    private int bufferSize = 8192;
    private long totalSize = -1L;
    private long totalWritten = 0L;
    private String httpProxyHost;
    private int httpProxyPort;
    private HttpRequest.UploadProgress progress;

    private static String getValidCharset(String charset) {
        return charset != null && charset.length() > 0 ? charset : "UTF-8";
    }

    private static SSLSocketFactory getTrustedFactory() throws HttpRequest.HttpRequestException {
        if (TRUSTED_FACTORY == null) {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            }};

            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init((KeyManager[])null, trustAllCerts, new SecureRandom());
                TRUSTED_FACTORY = context.getSocketFactory();
            } catch (GeneralSecurityException var3) {
                IOException ioException = new IOException("Security exception configuring SSL context");
                ioException.initCause(var3);
                throw new HttpRequest.HttpRequestException(ioException);
            }
        }

        return TRUSTED_FACTORY;
    }

    private static HostnameVerifier getTrustedVerifier() {
        if (TRUSTED_VERIFIER == null) {
            TRUSTED_VERIFIER = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }

        return TRUSTED_VERIFIER;
    }

    private static StringBuilder addPathSeparator(String baseUrl, StringBuilder result) {
        if (baseUrl.indexOf(58) + 2 == baseUrl.lastIndexOf(47)) {
            result.append('/');
        }

        return result;
    }

    private static StringBuilder addParamPrefix(String baseUrl, StringBuilder result) {
        int queryStart = baseUrl.indexOf(63);
        int lastChar = result.length() - 1;
        if (queryStart == -1) {
            result.append('?');
        } else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&') {
            result.append('&');
        }

        return result;
    }

    public static void setConnectionFactory(HttpRequest.ConnectionFactory connectionFactory) {
        if (connectionFactory == null) {
            CONNECTION_FACTORY = HttpRequest.ConnectionFactory.DEFAULT;
        } else {
            CONNECTION_FACTORY = connectionFactory;
        }

    }

    public static String encode(CharSequence url) throws HttpRequest.HttpRequestException {
        URL parsed;
        try {
            parsed = new URL(url.toString());
        } catch (IOException var7) {
            throw new HttpRequest.HttpRequestException(var7);
        }

        String host = parsed.getHost();
        int port = parsed.getPort();
        if (port != -1) {
            host = host + ':' + Integer.toString(port);
        }

        try {
            String encoded = (new URI(parsed.getProtocol(), host, parsed.getPath(), parsed.getQuery(), (String)null)).toASCIIString();
            int paramsStart = encoded.indexOf(63);
            if (paramsStart > 0 && paramsStart + 1 < encoded.length()) {
                encoded = encoded.substring(0, paramsStart + 1) + encoded.substring(paramsStart + 1).replace("+", "%2B");
            }

            return encoded;
        } catch (URISyntaxException var6) {
            IOException io = new IOException("Parsing URI failed");
            io.initCause(var6);
            throw new HttpRequest.HttpRequestException(io);
        }
    }

    public static String append(CharSequence url, Map<?, ?> params) {
        String baseUrl = url.toString();
        if (params != null && !params.isEmpty()) {
            StringBuilder result = new StringBuilder(baseUrl);
            addPathSeparator(baseUrl, result);
            addParamPrefix(baseUrl, result);
            Iterator<?> iterator = params.entrySet().iterator();
            Entry<?, ?> entry = (Entry)iterator.next();
            result.append(entry.getKey().toString());
            result.append('=');
            Object value = entry.getValue();
            if (value != null) {
                result.append(value);
            }

            while(iterator.hasNext()) {
                result.append('&');
                entry = (Entry)iterator.next();
                result.append(entry.getKey().toString());
                result.append('=');
                value = entry.getValue();
                if (value != null) {
                    result.append(value);
                }
            }

            return result.toString();
        } else {
            return baseUrl;
        }
    }

    public static String append(CharSequence url, Object... params) {
        String baseUrl = url.toString();
        if (params != null && params.length != 0) {
            if (params.length % 2 != 0) {
                throw new IllegalArgumentException("Must specify an even number of parameter names/values");
            } else {
                StringBuilder result = new StringBuilder(baseUrl);
                addPathSeparator(baseUrl, result);
                addParamPrefix(baseUrl, result);
                result.append(params[0]);
                result.append('=');
                Object value = params[1];
                if (value != null) {
                    result.append(value);
                }

                for(int i = 2; i < params.length; i += 2) {
                    result.append('&');
                    result.append(params[i]);
                    result.append('=');
                    value = params[i + 1];
                    if (value != null) {
                        result.append(value);
                    }
                }

                return result.toString();
            }
        } else {
            return baseUrl;
        }
    }

    public static HttpRequest get(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "GET");
    }

    public static HttpRequest get(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "GET");
    }

    public static HttpRequest get(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = append(baseUrl, params);
        return get((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest get(CharSequence baseUrl, boolean encode, Object... params) {
        String url = append(baseUrl, params);
        return get((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest post(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "POST");
    }

    public static HttpRequest post(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "POST");
    }

    public static HttpRequest post(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = append(baseUrl, params);
        return post((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest post(CharSequence baseUrl, boolean encode, Object... params) {
        String url = append(baseUrl, params);
        return post((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest put(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "PUT");
    }

    public static HttpRequest put(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "PUT");
    }

    public static HttpRequest put(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = append(baseUrl, params);
        return put((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest put(CharSequence baseUrl, boolean encode, Object... params) {
        String url = append(baseUrl, params);
        return put((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest delete(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "DELETE");
    }

    public static HttpRequest delete(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "DELETE");
    }

    public static HttpRequest delete(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = append(baseUrl, params);
        return delete((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest delete(CharSequence baseUrl, boolean encode, Object... params) {
        String url = append(baseUrl, params);
        return delete((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest head(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "HEAD");
    }

    public static HttpRequest head(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "HEAD");
    }

    public static HttpRequest head(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = append(baseUrl, params);
        return head((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest head(CharSequence baseUrl, boolean encode, Object... params) {
        String url = append(baseUrl, params);
        return head((CharSequence)(encode ? encode(url) : url));
    }

    public static HttpRequest options(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "OPTIONS");
    }

    public static HttpRequest options(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "OPTIONS");
    }

    public static HttpRequest trace(CharSequence url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "TRACE");
    }

    public static HttpRequest trace(URL url) throws HttpRequest.HttpRequestException {
        return new HttpRequest(url, "TRACE");
    }

    public static void keepAlive(boolean keepAlive) {
        setProperty("http.keepAlive", Boolean.toString(keepAlive));
    }

    public static void maxConnections(int maxConnections) {
        setProperty("http.maxConnections", Integer.toString(maxConnections));
    }

    public static void proxyHost(String host) {
        setProperty("http.proxyHost", host);
        setProperty("https.proxyHost", host);
    }

    public static void proxyPort(int port) {
        String portValue = Integer.toString(port);
        setProperty("http.proxyPort", portValue);
        setProperty("https.proxyPort", portValue);
    }

    public static void nonProxyHosts(String... hosts) {
        if (hosts != null && hosts.length > 0) {
            StringBuilder separated = new StringBuilder();
            int last = hosts.length - 1;

            for(int i = 0; i < last; ++i) {
                separated.append(hosts[i]).append('|');
            }

            separated.append(hosts[last]);
            setProperty("http.nonProxyHosts", separated.toString());
        } else {
            setProperty("http.nonProxyHosts", (String)null);
        }

    }

    private static String setProperty(final String name, final String value) {
        PrivilegedAction action;
        if (value != null) {
            action = new PrivilegedAction<String>() {
                public String run() {
                    return System.setProperty(name, value);
                }
            };
        } else {
            action = new PrivilegedAction<String>() {
                public String run() {
                    return System.clearProperty(name);
                }
            };
        }

        return (String)AccessController.doPrivileged(action);
    }

    public HttpRequest(CharSequence url, String method) throws HttpRequest.HttpRequestException {
        this.progress = HttpRequest.UploadProgress.DEFAULT;

        try {
            this.url = new URL(url.toString());
        } catch (MalformedURLException var4) {
            throw new HttpRequest.HttpRequestException(var4);
        }

        this.requestMethod = method;
    }

    public HttpRequest(URL url, String method) throws HttpRequest.HttpRequestException {
        this.progress = HttpRequest.UploadProgress.DEFAULT;
        this.url = url;
        this.requestMethod = method;
    }

    private Proxy createProxy() {
        return new Proxy(Type.HTTP, new InetSocketAddress(this.httpProxyHost, this.httpProxyPort));
    }

    private HttpURLConnection createConnection() {
        try {
            HttpURLConnection connection;
            if (this.httpProxyHost != null) {
                connection = CONNECTION_FACTORY.create(this.url, this.createProxy());
            } else {
                connection = CONNECTION_FACTORY.create(this.url);
            }

            connection.setRequestMethod(this.requestMethod);
            return connection;
        } catch (IOException var2) {
            throw new HttpRequest.HttpRequestException(var2);
        }
    }

    public String toString() {
        return this.method() + ' ' + this.url();
    }

    public HttpURLConnection getConnection() {
        if (this.connection == null) {
            this.connection = this.createConnection();
        }

        return this.connection;
    }

    public HttpRequest ignoreCloseExceptions(boolean ignore) {
        this.ignoreCloseExceptions = ignore;
        return this;
    }

    public boolean ignoreCloseExceptions() {
        return this.ignoreCloseExceptions;
    }

    public int code() throws HttpRequest.HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseCode();
        } catch (IOException var2) {
            throw new HttpRequest.HttpRequestException(var2);
        }
    }

    public HttpRequest code(AtomicInteger output) throws HttpRequest.HttpRequestException {
        output.set(this.code());
        return this;
    }

    public boolean ok() throws HttpRequest.HttpRequestException {
        return 200 == this.code();
    }

    public boolean created() throws HttpRequest.HttpRequestException {
        return 201 == this.code();
    }

    public boolean noContent() throws HttpRequest.HttpRequestException {
        return 204 == this.code();
    }

    public boolean serverError() throws HttpRequest.HttpRequestException {
        return 500 == this.code();
    }

    public boolean badRequest() throws HttpRequest.HttpRequestException {
        return 400 == this.code();
    }

    public boolean notFound() throws HttpRequest.HttpRequestException {
        return 404 == this.code();
    }

    public boolean notModified() throws HttpRequest.HttpRequestException {
        return 304 == this.code();
    }

    public String message() throws HttpRequest.HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseMessage();
        } catch (IOException var2) {
            throw new HttpRequest.HttpRequestException(var2);
        }
    }

    public HttpRequest disconnect() {
        this.getConnection().disconnect();
        return this;
    }

    public HttpRequest chunk(int size) {
        this.getConnection().setChunkedStreamingMode(size);
        return this;
    }

    public HttpRequest bufferSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than zero");
        } else {
            this.bufferSize = size;
            return this;
        }
    }

    public int bufferSize() {
        return this.bufferSize;
    }

    public HttpRequest uncompress(boolean uncompress) {
        this.uncompress = uncompress;
        return this;
    }

    protected ByteArrayOutputStream byteStream() {
        int size = this.contentLength();
        return size > 0 ? new ByteArrayOutputStream(size) : new ByteArrayOutputStream();
    }

    public String body(String charset) throws HttpRequest.HttpRequestException {
        ByteArrayOutputStream output = this.byteStream();

        try {
            this.copy((InputStream)this.buffer(), (OutputStream)output);
            return output.toString(getValidCharset(charset));
        } catch (IOException var4) {
            throw new HttpRequest.HttpRequestException(var4);
        }
    }

    public String body() throws HttpRequest.HttpRequestException {
        return this.body(this.charset());
    }

    public HttpRequest body(AtomicReference<String> output) throws HttpRequest.HttpRequestException {
        output.set(this.body());
        return this;
    }

    public HttpRequest body(AtomicReference<String> output, String charset) throws HttpRequest.HttpRequestException {
        output.set(this.body(charset));
        return this;
    }

    public boolean isBodyEmpty() throws HttpRequest.HttpRequestException {
        return this.contentLength() == 0;
    }

    public byte[] bytes() throws HttpRequest.HttpRequestException {
        ByteArrayOutputStream output = this.byteStream();

        try {
            this.copy((InputStream)this.buffer(), (OutputStream)output);
        } catch (IOException var3) {
            throw new HttpRequest.HttpRequestException(var3);
        }

        return output.toByteArray();
    }

    public BufferedInputStream buffer() throws HttpRequest.HttpRequestException {
        return new BufferedInputStream(this.stream(), this.bufferSize);
    }

    public InputStream stream() throws HttpRequest.HttpRequestException {
        Object stream;
        if (this.code() < 400) {
            try {
                stream = this.getConnection().getInputStream();
            } catch (IOException var4) {
                throw new HttpRequest.HttpRequestException(var4);
            }
        } else {
            stream = this.getConnection().getErrorStream();
            if (stream == null) {
                try {
                    stream = this.getConnection().getInputStream();
                } catch (IOException var5) {
                    if (this.contentLength() > 0) {
                        throw new HttpRequest.HttpRequestException(var5);
                    }

                    stream = new ByteArrayInputStream(new byte[0]);
                }
            }
        }

        if (this.uncompress && "gzip".equals(this.contentEncoding())) {
            try {
                return new GZIPInputStream((InputStream)stream);
            } catch (IOException var3) {
                throw new HttpRequest.HttpRequestException(var3);
            }
        } else {
            return (InputStream)stream;
        }
    }

    public InputStreamReader reader(String charset) throws HttpRequest.HttpRequestException {
        try {
            return new InputStreamReader(this.stream(), getValidCharset(charset));
        } catch (UnsupportedEncodingException var3) {
            throw new HttpRequest.HttpRequestException(var3);
        }
    }

    public InputStreamReader reader() throws HttpRequest.HttpRequestException {
        return this.reader(this.charset());
    }

    public BufferedReader bufferedReader(String charset) throws HttpRequest.HttpRequestException {
        return new BufferedReader(this.reader(charset), this.bufferSize);
    }

    public BufferedReader bufferedReader() throws HttpRequest.HttpRequestException {
        return this.bufferedReader(this.charset());
    }

    public HttpRequest receive(File file) throws HttpRequest.HttpRequestException {
        final BufferedOutputStream output;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file), this.bufferSize);
        } catch (FileNotFoundException var4) {
            throw new HttpRequest.HttpRequestException(var4);
        }

        return (HttpRequest)(new HttpRequest.CloseOperation<HttpRequest>(output, this.ignoreCloseExceptions) {
            protected HttpRequest run() throws HttpRequest.HttpRequestException, IOException {
                return HttpRequest.this.receive((OutputStream)output);
            }
        }).call();
    }

    public HttpRequest receive(OutputStream output) throws HttpRequest.HttpRequestException {
        try {
            return this.copy((InputStream)this.buffer(), (OutputStream)output);
        } catch (IOException var3) {
            throw new HttpRequest.HttpRequestException(var3);
        }
    }

    public HttpRequest receive(PrintStream output) throws HttpRequest.HttpRequestException {
        return this.receive((OutputStream)output);
    }

    public HttpRequest receive(final Appendable appendable) throws HttpRequest.HttpRequestException {
        final BufferedReader reader = this.bufferedReader();
        return (HttpRequest)(new HttpRequest.CloseOperation<HttpRequest>(reader, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                CharBuffer buffer = CharBuffer.allocate(HttpRequest.this.bufferSize);

                int read;
                while((read = reader.read(buffer)) != -1) {
                    buffer.rewind();
                    appendable.append(buffer, 0, read);
                    buffer.rewind();
                }

                return HttpRequest.this;
            }
        }).call();
    }

    public HttpRequest receive(final Writer writer) throws HttpRequest.HttpRequestException {
        final BufferedReader reader = this.bufferedReader();
        return (HttpRequest)(new HttpRequest.CloseOperation<HttpRequest>(reader, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                return HttpRequest.this.copy((Reader)reader, (Writer)writer);
            }
        }).call();
    }

    public HttpRequest readTimeout(int timeout) {
        this.getConnection().setReadTimeout(timeout);
        return this;
    }

    public HttpRequest connectTimeout(int timeout) {
        this.getConnection().setConnectTimeout(timeout);
        return this;
    }

    public HttpRequest header(String name, String value) {
        this.getConnection().setRequestProperty(name, value);
        return this;
    }

    public HttpRequest header(String name, Number value) {
        return this.header(name, value != null ? value.toString() : null);
    }

    public HttpRequest headers(Map<String, String> headers) {
        if (!headers.isEmpty()) {
            Iterator i$ = headers.entrySet().iterator();

            while(i$.hasNext()) {
                Entry<String, String> header = (Entry)i$.next();
                this.header(header);
            }
        }

        return this;
    }

    public HttpRequest header(Entry<String, String> header) {
        return this.header((String)header.getKey(), (String)header.getValue());
    }

    public String header(String name) throws HttpRequest.HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderField(name);
    }

    public Map<String, List<String>> headers() throws HttpRequest.HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFields();
    }

    public long dateHeader(String name) throws HttpRequest.HttpRequestException {
        return this.dateHeader(name, -1L);
    }

    public long dateHeader(String name, long defaultValue) throws HttpRequest.HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldDate(name, defaultValue);
    }

    public int intHeader(String name) throws HttpRequest.HttpRequestException {
        return this.intHeader(name, -1);
    }

    public int intHeader(String name, int defaultValue) throws HttpRequest.HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldInt(name, defaultValue);
    }

    public String[] headers(String name) {
        Map<String, List<String>> headers = this.headers();
        if (headers != null && !headers.isEmpty()) {
            List<String> values = (List)headers.get(name);
            return values != null && !values.isEmpty() ? (String[])values.toArray(new String[values.size()]) : EMPTY_STRINGS;
        } else {
            return EMPTY_STRINGS;
        }
    }

    public String parameter(String headerName, String paramName) {
        return this.getParam(this.header(headerName), paramName);
    }

    public Map<String, String> parameters(String headerName) {
        return this.getParams(this.header(headerName));
    }

    protected Map<String, String> getParams(String header) {
        if (header != null && header.length() != 0) {
            int headerLength = header.length();
            int start = header.indexOf(59) + 1;
            if (start != 0 && start != headerLength) {
                int end = header.indexOf(59, start);
                if (end == -1) {
                    end = headerLength;
                }

                LinkedHashMap params = new LinkedHashMap();

                while(start < end) {
                    int nameEnd = header.indexOf(61, start);
                    if (nameEnd != -1 && nameEnd < end) {
                        String name = header.substring(start, nameEnd).trim();
                        if (name.length() > 0) {
                            String value = header.substring(nameEnd + 1, end).trim();
                            int length = value.length();
                            if (length != 0) {
                                if (length > 2 && '"' == value.charAt(0) && '"' == value.charAt(length - 1)) {
                                    params.put(name, value.substring(1, length - 1));
                                } else {
                                    params.put(name, value);
                                }
                            }
                        }
                    }

                    start = end + 1;
                    end = header.indexOf(59, start);
                    if (end == -1) {
                        end = headerLength;
                    }
                }

                return params;
            } else {
                return Collections.emptyMap();
            }
        } else {
            return Collections.emptyMap();
        }
    }

    protected String getParam(String value, String paramName) {
        if (value != null && value.length() != 0) {
            int length = value.length();
            int start = value.indexOf(59) + 1;
            if (start != 0 && start != length) {
                int end = value.indexOf(59, start);
                if (end == -1) {
                    end = length;
                }

                while(start < end) {
                    int nameEnd = value.indexOf(61, start);
                    if (nameEnd != -1 && nameEnd < end && paramName.equals(value.substring(start, nameEnd).trim())) {
                        String paramValue = value.substring(nameEnd + 1, end).trim();
                        int valueLength = paramValue.length();
                        if (valueLength != 0) {
                            if (valueLength > 2 && '"' == paramValue.charAt(0) && '"' == paramValue.charAt(valueLength - 1)) {
                                return paramValue.substring(1, valueLength - 1);
                            }

                            return paramValue;
                        }
                    }

                    start = end + 1;
                    end = value.indexOf(59, start);
                    if (end == -1) {
                        end = length;
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String charset() {
        return this.parameter("Content-Type", "charset");
    }

    public HttpRequest userAgent(String userAgent) {
        return this.header("User-Agent", userAgent);
    }

    public HttpRequest referer(String referer) {
        return this.header("Referer", referer);
    }

    public HttpRequest useCaches(boolean useCaches) {
        this.getConnection().setUseCaches(useCaches);
        return this;
    }

    public HttpRequest acceptEncoding(String acceptEncoding) {
        return this.header("Accept-Encoding", acceptEncoding);
    }

    public HttpRequest acceptGzipEncoding() {
        return this.acceptEncoding("gzip");
    }

    public HttpRequest acceptCharset(String acceptCharset) {
        return this.header("Accept-Charset", acceptCharset);
    }

    public String contentEncoding() {
        return this.header("Content-Encoding");
    }

    public String server() {
        return this.header("Server");
    }

    public long date() {
        return this.dateHeader("Date");
    }

    public String cacheControl() {
        return this.header("Cache-Control");
    }

    public String eTag() {
        return this.header("ETag");
    }

    public long expires() {
        return this.dateHeader("Expires");
    }

    public long lastModified() {
        return this.dateHeader("Last-Modified");
    }

    public String location() {
        return this.header("Location");
    }

    public HttpRequest authorization(String authorization) {
        return this.header("Authorization", authorization);
    }

    public HttpRequest proxyAuthorization(String proxyAuthorization) {
        return this.header("Proxy-Authorization", proxyAuthorization);
    }

    public HttpRequest basic(String name, String password) {
        return this.authorization("Basic " + HttpRequest.Base64.encode(name + ':' + password));
    }

    public HttpRequest proxyBasic(String name, String password) {
        return this.proxyAuthorization("Basic " + HttpRequest.Base64.encode(name + ':' + password));
    }

    public HttpRequest ifModifiedSince(long ifModifiedSince) {
        this.getConnection().setIfModifiedSince(ifModifiedSince);
        return this;
    }

    public HttpRequest ifNoneMatch(String ifNoneMatch) {
        return this.header("If-None-Match", ifNoneMatch);
    }

    public HttpRequest contentType(String contentType) {
        return this.contentType(contentType, (String)null);
    }

    public HttpRequest contentType(String contentType, String charset) {
        if (charset != null && charset.length() > 0) {
            String separator = "; charset=";
            return this.header("Content-Type", contentType + "; charset=" + charset);
        } else {
            return this.header("Content-Type", contentType);
        }
    }

    public String contentType() {
        return this.header("Content-Type");
    }

    public int contentLength() {
        return this.intHeader("Content-Length");
    }

    public HttpRequest contentLength(String contentLength) {
        return this.contentLength(Integer.parseInt(contentLength));
    }

    public HttpRequest contentLength(int contentLength) {
        this.getConnection().setFixedLengthStreamingMode(contentLength);
        return this;
    }

    public HttpRequest accept(String accept) {
        return this.header("Accept", accept);
    }

    public HttpRequest acceptJson() {
        return this.accept("application/json");
    }

    protected HttpRequest copy(final InputStream input, final OutputStream output) throws IOException {
        return (HttpRequest)(new HttpRequest.CloseOperation<HttpRequest>(input, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                byte[] buffer = new byte[HttpRequest.this.bufferSize];

                int read;
                while((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    HttpRequest.this.totalWritten = (long)read;
                    HttpRequest.this.progress.onUpload(HttpRequest.this.totalWritten, HttpRequest.this.totalSize);
                }

                return HttpRequest.this;
            }
        }).call();
    }

    protected HttpRequest copy(final Reader input, final Writer output) throws IOException {
        return (HttpRequest)(new HttpRequest.CloseOperation<HttpRequest>(input, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                char[] buffer = new char[HttpRequest.this.bufferSize];

                int read;
                while((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    HttpRequest.this.totalWritten = (long)read;
                    HttpRequest.this.progress.onUpload(HttpRequest.this.totalWritten, -1L);
                }

                return HttpRequest.this;
            }
        }).call();
    }

    public HttpRequest progress(HttpRequest.UploadProgress callback) {
        if (callback == null) {
            this.progress = HttpRequest.UploadProgress.DEFAULT;
        } else {
            this.progress = callback;
        }

        return this;
    }

    private HttpRequest incrementTotalSize(long size) {
        if (this.totalSize == -1L) {
            this.totalSize = 0L;
        }

        this.totalSize += size;
        return this;
    }

    protected HttpRequest closeOutput() throws IOException {
        this.progress((HttpRequest.UploadProgress)null);
        if (this.output == null) {
            return this;
        } else {
            if (this.multipart) {
                this.output.write("\r\n--00content0boundary00--\r\n");
            }

            if (this.ignoreCloseExceptions) {
                try {
                    this.output.close();
                } catch (IOException var2) {
                }
            } else {
                this.output.close();
            }

            this.output = null;
            return this;
        }
    }

    protected HttpRequest closeOutputQuietly() throws HttpRequest.HttpRequestException {
        try {
            return this.closeOutput();
        } catch (IOException var2) {
            throw new HttpRequest.HttpRequestException(var2);
        }
    }

    protected HttpRequest openOutput() throws IOException {
        if (this.output != null) {
            return this;
        } else {
            this.getConnection().setDoOutput(true);
            String charset = this.getParam(this.getConnection().getRequestProperty("Content-Type"), "charset");
            this.output = new HttpRequest.RequestOutputStream(this.getConnection().getOutputStream(), charset, this.bufferSize);
            return this;
        }
    }

    protected HttpRequest startPart() throws IOException {
        if (!this.multipart) {
            this.multipart = true;
            this.contentType("multipart/form-data; boundary=00content0boundary00").openOutput();
            this.output.write("--00content0boundary00\r\n");
        } else {
            this.output.write("\r\n--00content0boundary00\r\n");
        }

        return this;
    }

    protected HttpRequest writePartHeader(String name, String filename) throws IOException {
        return this.writePartHeader(name, filename, (String)null);
    }

    protected HttpRequest writePartHeader(String name, String filename, String contentType) throws IOException {
        StringBuilder partBuffer = new StringBuilder();
        partBuffer.append("form-data; name=\"").append(name);
        if (filename != null) {
            partBuffer.append("\"; filename=\"").append(filename);
        }

        partBuffer.append('"');
        this.partHeader("Content-Disposition", partBuffer.toString());
        if (contentType != null) {
            this.partHeader("Content-Type", contentType);
        }

        return this.send((CharSequence)"\r\n");
    }

    public HttpRequest part(String name, String part) {
        return this.part(name, (String)null, (String)part);
    }

    public HttpRequest part(String name, String filename, String part) throws HttpRequest.HttpRequestException {
        return this.part(name, filename, (String)null, (String)part);
    }

    public HttpRequest part(String name, String filename, String contentType, String part) throws HttpRequest.HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(name, filename, contentType);
            this.output.write(part);
            return this;
        } catch (IOException var6) {
            throw new HttpRequest.HttpRequestException(var6);
        }
    }

    public HttpRequest part(String name, Number part) throws HttpRequest.HttpRequestException {
        return this.part(name, (String)null, (Number)part);
    }

    public HttpRequest part(String name, String filename, Number part) throws HttpRequest.HttpRequestException {
        return this.part(name, filename, part != null ? part.toString() : null);
    }

    public HttpRequest part(String name, File part) throws HttpRequest.HttpRequestException {
        return this.part(name, (String)null, (File)part);
    }

    public HttpRequest part(String name, String filename, File part) throws HttpRequest.HttpRequestException {
        return this.part(name, filename, (String)null, (File)part);
    }

    public HttpRequest part(String name, String filename, String contentType, File part) throws HttpRequest.HttpRequestException {
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(part));
            this.incrementTotalSize(part.length());
        } catch (IOException var7) {
            throw new HttpRequest.HttpRequestException(var7);
        }

        return this.part(name, filename, contentType, (InputStream)stream);
    }

    public HttpRequest part(String name, InputStream part) throws HttpRequest.HttpRequestException {
        return this.part(name, (String)null, (String)null, (InputStream)part);
    }

    public HttpRequest part(String name, String filename, String contentType, InputStream part) throws HttpRequest.HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(name, filename, contentType);
            this.copy((InputStream)part, (OutputStream)this.output);
            return this;
        } catch (IOException var6) {
            throw new HttpRequest.HttpRequestException(var6);
        }
    }

    public HttpRequest partHeader(String name, String value) throws HttpRequest.HttpRequestException {
        return this.send((CharSequence)name).send((CharSequence)": ").send((CharSequence)value).send((CharSequence)"\r\n");
    }

    public HttpRequest send(File input) throws HttpRequest.HttpRequestException {
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(input));
            this.incrementTotalSize(input.length());
        } catch (FileNotFoundException var4) {
            throw new HttpRequest.HttpRequestException(var4);
        }

        return this.send((InputStream)stream);
    }

    public HttpRequest send(byte[] input) throws HttpRequest.HttpRequestException {
        if (input != null) {
            this.incrementTotalSize((long)input.length);
        }

        return this.send((InputStream)(new ByteArrayInputStream(input)));
    }

    public HttpRequest send(InputStream input) throws HttpRequest.HttpRequestException {
        try {
            this.openOutput();
            this.copy((InputStream)input, (OutputStream)this.output);
            return this;
        } catch (IOException var3) {
            throw new HttpRequest.HttpRequestException(var3);
        }
    }

    public HttpRequest send(final Reader input) throws HttpRequest.HttpRequestException {
        try {
            this.openOutput();
        } catch (IOException var3) {
            throw new HttpRequest.HttpRequestException(var3);
        }

        final Writer writer = new OutputStreamWriter(this.output, this.output.encoder.charset());
        return (HttpRequest)(new HttpRequest.FlushOperation<HttpRequest>(writer) {
            protected HttpRequest run() throws IOException {
                return HttpRequest.this.copy((Reader)input, (Writer)writer);
            }
        }).call();
    }

    public HttpRequest send(CharSequence value) throws HttpRequest.HttpRequestException {
        try {
            this.openOutput();
            this.output.write(value.toString());
            return this;
        } catch (IOException var3) {
            throw new HttpRequest.HttpRequestException(var3);
        }
    }

    public OutputStreamWriter writer() throws HttpRequest.HttpRequestException {
        try {
            this.openOutput();
            return new OutputStreamWriter(this.output, this.output.encoder.charset());
        } catch (IOException var2) {
            throw new HttpRequest.HttpRequestException(var2);
        }
    }

    public HttpRequest form(Map<?, ?> values) throws HttpRequest.HttpRequestException {
        return this.form(values, "UTF-8");
    }

    public HttpRequest form(Entry<?, ?> entry) throws HttpRequest.HttpRequestException {
        return this.form(entry, "UTF-8");
    }

    public HttpRequest form(Entry<?, ?> entry, String charset) throws HttpRequest.HttpRequestException {
        return this.form(entry.getKey(), entry.getValue(), charset);
    }

    public HttpRequest form(Object name, Object value) throws HttpRequest.HttpRequestException {
        return this.form(name, value, "UTF-8");
    }

    public HttpRequest form(Object name, Object value, String charset) throws HttpRequest.HttpRequestException {
        boolean first = !this.form;
        if (first) {
            this.contentType("application/x-www-form-urlencoded", charset);
            this.form = true;
        }

        charset = getValidCharset(charset);

        try {
            this.openOutput();
            if (!first) {
                this.output.write(38);
            }

            this.output.write(URLEncoder.encode(name.toString(), charset));
            this.output.write(61);
            if (value != null) {
                this.output.write(URLEncoder.encode(value.toString(), charset));
            }

            return this;
        } catch (IOException var6) {
            throw new HttpRequest.HttpRequestException(var6);
        }
    }

    public HttpRequest form(Map<?, ?> values, String charset) throws HttpRequest.HttpRequestException {
        if (!values.isEmpty()) {
            Iterator i$ = values.entrySet().iterator();

            while(i$.hasNext()) {
                Entry<?, ?> entry = (Entry)i$.next();
                this.form(entry, charset);
            }
        }

        return this;
    }

    public HttpRequest trustAllCerts() throws HttpRequest.HttpRequestException {
        HttpURLConnection connection = this.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(getTrustedFactory());
        }

        return this;
    }

    public HttpRequest trustAllHosts() {
        HttpURLConnection connection = this.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setHostnameVerifier(getTrustedVerifier());
        }

        return this;
    }

    public URL url() {
        return this.getConnection().getURL();
    }

    public String method() {
        return this.getConnection().getRequestMethod();
    }

    public HttpRequest useProxy(String proxyHost, int proxyPort) {
        if (this.connection != null) {
            throw new IllegalStateException("The connection has already been created. This method must be called before reading or writing to the request.");
        } else {
            this.httpProxyHost = proxyHost;
            this.httpProxyPort = proxyPort;
            return this;
        }
    }

    public HttpRequest followRedirects(boolean followRedirects) {
        this.getConnection().setInstanceFollowRedirects(followRedirects);
        return this;
    }

    static {
        CONNECTION_FACTORY = HttpRequest.ConnectionFactory.DEFAULT;
    }

    public static class RequestOutputStream extends BufferedOutputStream {
        private final CharsetEncoder encoder;

        public RequestOutputStream(OutputStream stream, String charset, int bufferSize) {
            super(stream, bufferSize);
            this.encoder = Charset.forName(HttpRequest.getValidCharset(charset)).newEncoder();
        }

        public HttpRequest.RequestOutputStream write(String value) throws IOException {
            ByteBuffer bytes = this.encoder.encode(CharBuffer.wrap(value));
            super.write(bytes.array(), 0, bytes.limit());
            return this;
        }
    }

    protected abstract static class FlushOperation<V> extends HttpRequest.Operation<V> {
        private final Flushable flushable;

        protected FlushOperation(Flushable flushable) {
            this.flushable = flushable;
        }

        protected void done() throws IOException {
            this.flushable.flush();
        }
    }

    protected abstract static class CloseOperation<V> extends HttpRequest.Operation<V> {
        private final Closeable closeable;
        private final boolean ignoreCloseExceptions;

        protected CloseOperation(Closeable closeable, boolean ignoreCloseExceptions) {
            this.closeable = closeable;
            this.ignoreCloseExceptions = ignoreCloseExceptions;
        }

        protected void done() throws IOException {
            if (this.closeable instanceof Flushable) {
                ((Flushable)this.closeable).flush();
            }

            if (this.ignoreCloseExceptions) {
                try {
                    this.closeable.close();
                } catch (IOException var2) {
                }
            } else {
                this.closeable.close();
            }

        }
    }

    protected abstract static class Operation<V> implements Callable<Object> {
        protected Operation() {
        }

        protected abstract V run() throws HttpRequest.HttpRequestException, IOException;

        protected abstract void done() throws IOException;

        public Object call() throws HttpRequest.HttpRequestException {
            boolean thrown = false;

            Object var2;
            try {
                var2 = this.run();
            } catch (HttpRequest.HttpRequestException var11) {
                thrown = true;
                throw var11;
            } catch (IOException var12) {
                thrown = true;
                throw new HttpRequest.HttpRequestException(var12);
            } finally {
                try {
                    this.done();
                } catch (IOException var13) {
                    if (!thrown) {
                        throw new HttpRequest.HttpRequestException(var13);
                    }
                }

            }

            return var2;
        }
    }

    public static class HttpRequestException extends RuntimeException {
        private static final long serialVersionUID = -1170466989781746231L;

        public HttpRequestException(IOException cause) {
            super(cause);
        }

        public IOException getCause() {
            return (IOException)super.getCause();
        }
    }

    public static class Base64 {
        private static final byte EQUALS_SIGN = 61;
        private static final String PREFERRED_ENCODING = "US-ASCII";
        private static final byte[] _STANDARD_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

        private Base64() {
        }

        private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
            byte[] ALPHABET = _STANDARD_ALPHABET;
            int inBuff = (numSigBytes > 0 ? source[srcOffset] << 24 >>> 8 : 0) | (numSigBytes > 1 ? source[srcOffset + 1] << 24 >>> 16 : 0) | (numSigBytes > 2 ? source[srcOffset + 2] << 24 >>> 24 : 0);
            switch(numSigBytes) {
                case 1:
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                    destination[destOffset + 2] = 61;
                    destination[destOffset + 3] = 61;
                    return destination;
                case 2:
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                    destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                    destination[destOffset + 3] = 61;
                    return destination;
                case 3:
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                    destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                    destination[destOffset + 3] = ALPHABET[inBuff & 63];
                    return destination;
                default:
                    return destination;
            }
        }

        public static String encode(String string) {
            byte[] bytes;
            try {
                bytes = string.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException var3) {
                bytes = string.getBytes();
            }

            return encodeBytes(bytes);
        }

        public static String encodeBytes(byte[] source) {
            return encodeBytes(source, 0, source.length);
        }

        public static String encodeBytes(byte[] source, int off, int len) {
            byte[] encoded = encodeBytesToBytes(source, off, len);

            try {
                return new String(encoded, "US-ASCII");
            } catch (UnsupportedEncodingException var5) {
                return new String(encoded);
            }
        }

        public static byte[] encodeBytesToBytes(byte[] source, int off, int len) {
            if (source == null) {
                throw new NullPointerException("Cannot serialize a null array.");
            } else if (off < 0) {
                throw new IllegalArgumentException("Cannot have negative offset: " + off);
            } else if (len < 0) {
                throw new IllegalArgumentException("Cannot have length offset: " + len);
            } else if (off + len > source.length) {
                throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));
            } else {
                int encLen = len / 3 * 4 + (len % 3 > 0 ? 4 : 0);
                byte[] outBuff = new byte[encLen];
                int d = 0;
                int e = 0;

                for(int len2 = len - 2; d < len2; e += 4) {
                    encode3to4(source, d + off, 3, outBuff, e);
                    d += 3;
                }

                if (d < len) {
                    encode3to4(source, d + off, len - d, outBuff, e);
                    e += 4;
                }

                if (e <= outBuff.length - 1) {
                    byte[] finalOut = new byte[e];
                    System.arraycopy(outBuff, 0, finalOut, 0, e);
                    return finalOut;
                } else {
                    return outBuff;
                }
            }
        }
    }

    public interface UploadProgress {
        HttpRequest.UploadProgress DEFAULT = new HttpRequest.UploadProgress() {
            public void onUpload(long uploaded, long total) {
            }
        };

        void onUpload(long var1, long var3);
    }

    public interface ConnectionFactory {
        HttpRequest.ConnectionFactory DEFAULT = new HttpRequest.ConnectionFactory() {
            public HttpURLConnection create(URL url) throws IOException {
                return (HttpURLConnection)url.openConnection();
            }

            public HttpURLConnection create(URL url, Proxy proxy) throws IOException {
                return (HttpURLConnection)url.openConnection(proxy);
            }
        };

        HttpURLConnection create(URL var1) throws IOException;

        HttpURLConnection create(URL var1, Proxy var2) throws IOException;
    }
}

