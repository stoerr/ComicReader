package com.blogspot.applications4android.comicreader.core;

import com.blogspot.applications4android.comicreader.ComicUpdater;
import com.blogspot.applications4android.comicreader.exceptions.ComicSDCardFull;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

/**
 * Class containing utility functions for downloading the URI's
 */
public class Downloader {
    /**
     * buffer size for downloading the string (in B)
     */
    private static final int DNLD_BUFF_SIZE = 8192;
    /**
     * string builder capacity (in B)
     */
    private static final int SB_CAPACITY = 8192;


    /**
     * Private constructor!
     */
    private Downloader() {
    }

    /**
     * Helper function to download a URI from the given url and save it to a string.
     * Uses URLConnection in order to set up a connection with the server and download
     * the URI requested.
     *
     * @param uri The url of interest.
     * @return the contents of the url in a string
     * @throws IOException
     * @throws URISyntaxException
     * @throws ClientProtocolException
     */
    public static String downloadToString(URI uri) throws ClientProtocolException, URISyntaxException, IOException {
        BufferedReader br = openConnection(uri);
        try {
            StringBuilder sb = new StringBuilder(SB_CAPACITY);
            char[] buff = new char[SB_CAPACITY];
            int len;
            while ((len = br.read(buff)) > 0) {
                sb.append(buff, 0, len);
            }
            return sb.toString();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                ComicUpdater.addOtherException(e);
            }
        }
    }

    /**
     * Open a http connection with the given url
     *
     * @param uri desired url
     * @return buffered reader for this url connection
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static BufferedReader openConnection(URI uri) throws URISyntaxException, ClientProtocolException, IOException {
        HttpClient client = getHttpClient();
        HttpGet http = new HttpGet(uri);
        HttpResponse resp = (HttpResponse) client.execute(http);
        HttpEntity entity = resp.getEntity();
        InputStreamReader isr = new InputStreamReader(entity.getContent());
        BufferedReader br = new BufferedReader(isr, DNLD_BUFF_SIZE);
        return br;
    }

    private static HttpClient httpClient;

    private static HttpClient getHttpClient() {
        if (null == httpClient) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                HttpParams params = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params, 2000);
                HttpConnectionParams.setSoTimeout(params, 4000);
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                httpClient = new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                ComicUpdater.addOtherException(e);
            }
        }
        return httpClient;
    }

    /**
     * Open a http connection with the given url
     *
     * @param uri desired url
     * @return buffered reader stream for this url connection
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static BufferedInputStream openConnectionStream(URI uri) throws URISyntaxException, ClientProtocolException, IOException {
        HttpClient client = getHttpClient();
        HttpGet http = new HttpGet(uri);
        HttpResponse resp = (HttpResponse) client.execute(http);
        InputStream is = resp.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        return bis;
    }

    /**
     * Helper function to download the image from the given uri into a local file
     *
     * @param file local file to store the image
     * @param uri  url from which to download the image
     * @throws ComicSDCardFull
     * @throws ClientProtocolException
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void dnldImage(String file, URI uri) throws ComicSDCardFull, ClientProtocolException, URISyntaxException, IOException {
        FileUtils.checkFreeSpaceSdcard();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream reader = openConnectionStream(uri);
        try {
            byte[] buff = new byte[DNLD_BUFF_SIZE];
            int len;
            while ((len = reader.read(buff, 0, DNLD_BUFF_SIZE)) > 0) {
                fos.write(buff, 0, len);
            }
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                ComicUpdater.addOtherException(e);
            }
            try {
                reader.close();
            } catch (Exception e) {
                ComicUpdater.addOtherException(e);
            }
        }
    }

    /**
     * Replaces all sort of '&#39;' kind of occurences in HTML strings with their corresponding ASCII chars.
     *
     * @param in the input HTML string
     * @return the ASCII-converted string
     */
    public static String decodeHtml(String in) {
        if (in == null) {
            return in;
        }
        String out = in;
        String expr, repl;
        // HACK HACK HACK!!!
        // need to look for a sane way of doing this...
        for (int i = 33; i <= 47; i++) {
            char a = (char) i;
            expr = "&#" + i + ";";
            repl = "" + a;
            out = out.replaceAll(expr, repl);
        }
        out = out.replaceAll("&#8208;", "-");
        out = out.replaceAll("&#8211;", "-");
        out = out.replaceAll("&#8216;", "'");
        out = out.replaceAll("&#8217;", "'");
        out = out.replaceAll("&#8218;", ",");
        out = out.replaceAll("&#8219;", "'");
        out = out.replaceAll("&#8220;", "\"");
        out = out.replaceAll("&#8221;", "\"");
        out = out.replaceAll("&#8223;", "\"");
        out = out.replaceAll("&#8230;", "...");
        out = out.replaceAll("&quot;", "\"");
        out = out.replaceAll("&lt;", "<");
        out = out.replaceAll("&gt;", ">");
        out = out.replaceAll("&#amp;", "&");
        return out;
    }

    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                    // trust all
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                    // trust all
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
