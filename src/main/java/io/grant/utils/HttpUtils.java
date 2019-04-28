package io.grant.utils;

/**
 * Created by grant on 2017/4/7.
 */

import com.xforceplus.apollo.utils.JacksonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Http 工具类
 */
public class HttpUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static int SOCKET_TIMEOUT = 10000;
    private static int CONNECT_TIMEOUT = 10000;
    //抛送异常
    private static volatile boolean IS_THROWS_ERROR = false;
    //显示请求-响应
    private static volatile boolean SHOW_REQRESP_LOGS = false;

    public static void conf(int socketTimeout, int connectTimeout,
                            boolean showReqRespLogs) {
        SOCKET_TIMEOUT = socketTimeout;
        CONNECT_TIMEOUT = connectTimeout;
        SHOW_REQRESP_LOGS = showReqRespLogs;
        init();
    }

    public static void conf(boolean showReqRespLogs) {
        SHOW_REQRESP_LOGS = showReqRespLogs;
    }

    public static void conf(int socketTimeout,
                            int connectTimeout) {
        conf(socketTimeout, connectTimeout, SHOW_REQRESP_LOGS);
    }

    public static void confThrowsError(boolean isThrowsError) {
        IS_THROWS_ERROR = isThrowsError;
    }

    public static Pair<String, String> doGet_1(String url, Map<String, Object> param, Header[] headers) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = getHttpClient();
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).build();
        String resultString = "";
        CloseableHttpResponse response = null;
        URI uri = null;
        int status = 0;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    Object obj = param.get(key);
                    if (obj instanceof String) {
                        builder.addParameter(key, (String) obj);
                    } else {
                        String json = JacksonUtil.getInstance().toJson(obj);
                        builder.addParameter(key, json);
                    }

                }
            }
            uri = builder.build();
            if (SHOW_REQRESP_LOGS) {
                logger.info("GET 请求 HTTP {} \n params：{}", Objects.toString(uri), param);
            }
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);
            if (headers != null){
                httpGet.setHeaders(headers);
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            status = response.getStatusLine().getStatusCode();
            // 判断返回状态是否为200
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (status == 200) {

            } else {
                logger.warn("接口调用：{} >> {}", url, status);
            }
        } catch (Exception e) {
            logger.error("Get请求调用异常：", e);
            if (IS_THROWS_ERROR) {
                throw new RuntimeException(e);
            }
        } finally {
            CommonTools.close(response);
        }
        if (SHOW_REQRESP_LOGS) {
            logger.info("GET 响应 HTTP {} \n {} >> {}", Objects.toString(uri), status, resultString);
        }
        return Pair.of(status + "", resultString);
    }

    public static String doGet(String url, Map<String, Object> param) {
        return doGet_1(url, param, null).getValue();
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static Pair<String, String> doPostForm(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        if (SHOW_REQRESP_LOGS) {
            logger.info("POST 请求 HTTP {} \n params：{}", url, param);
        }
        int status = 0;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
            // 判断返回状态是否为200
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (status == 200) {

            } else {
                logger.error("{} >> {} >> {}", status, url, resultString);
            }
        } catch (Exception e) {
            logger.error("Post请求调用异常：", e);
            if (IS_THROWS_ERROR) {
                throw new RuntimeException(e);
            }
        } finally {
            CommonTools.close(response);
        }

        return Pair.of(status + "", resultString);
    }

    public static String doPost(String url) {
        return doPostForm(url, null).getValue();
    }

    public static String doPostJson(String url, String json) {
        return doPostJson(url, json, null);
    }

    public static String doPostJson(String url, String json, Header[] headers) {
        return doPostJson_1(url, json, headers).getValue();
    }

    public static String doPostJson(String url, Map<String, String> param, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        if (SHOW_REQRESP_LOGS) {
            logger.info("POST 请求 HTTP {} \n params：{}", url, param);
        }
        int status = 0;
        URI uri;
        try {

            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            uri = builder.build();
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(uri);
            // 创建请求内容
            StringEntity entity1 = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity1);
            // 执行http请求
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
            // 判断返回状态是否为200
            if (status == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                logger.warn("{} >> {}", status, EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("Post请求调用异常：", e);
            if (IS_THROWS_ERROR) {
                throw new RuntimeException(e);
            }
        } finally {
            IOUtils.closeQuietly(response, httpClient);
        }
        if (SHOW_REQRESP_LOGS) {
            logger.info("POST 响应 HTTP {} \n {} >> {}", url, status, resultString);
        }

        return resultString;
    }

    /**
     * 使用SOAP1.2发送消息
     *
     * @param postUrl
     * @param soapXml
     * @param soapAction
     * @return
     */
    public static String doPostSoap1_2(String postUrl, String soapXml,
                                       String soapAction) {
        String retStr = "";
        // HttpClient
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(postUrl);
        CloseableHttpResponse response = null;
        if (SHOW_REQRESP_LOGS) {
            logger.info("SOAP1.2 请求 HTTP {} \n params：{}", postUrl, soapXml);
        }
        int status = 0;
        try {
            httpPost.setHeader("Content-Type",
                    "application/soap+xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", soapAction);
            StringEntity data = new StringEntity(soapXml,
                    Charset.forName("UTF-8"));
            httpPost.setEntity(data);
            response = httpClient
                    .execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            status = response.getStatusLine().getStatusCode();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }

        } catch (Exception e) {
            logger.error("exception in doPostSoap1_2", e);
            if (IS_THROWS_ERROR) {
                throw new RuntimeException(e);
            }
        } finally {
            CommonTools.close(response);
        }
        if (SHOW_REQRESP_LOGS) {
            logger.info("响应 HTTP {} \n {} >> {}", postUrl, status, retStr);
        }
        return retStr;
    }

    public static Pair<String, String> doPostJson_1(String url, String json, Header[] headers) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        if (SHOW_REQRESP_LOGS) {
            logger.info("POST 请求 HTTP {} \n params：{}", url, json);
        }
        int status = 0;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            if (headers != null) {
                httpPost.setHeaders(headers);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
            // 判断返回状态是否为200
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (status == 200) {

            } else {
                logger.warn("接口调用：{} >> {} >> {}", url, status, resultString);
            }
        } catch (Exception e) {
            logger.error("Post json请求调用异常：", e);
            if (IS_THROWS_ERROR) {
                throw new RuntimeException(e);
            }
        } finally {
            CommonTools.close(response);
        }
        if (SHOW_REQRESP_LOGS) {
            logger.info("POST 响应 HTTP {} \n {} >> {}", url, status, resultString);
        }
        return Pair.of(status + "", resultString);
    }

    private static CloseableHttpClient getHttpClient(){
        return HttpClients.custom().setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
    }

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;

    static {
        init();
    }


    private static synchronized void init(){
        if (connMgr != null) {
            connMgr.close();
            connMgr = null;
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", createSSLConnSocketFactory()).build();
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager(registry);
        // 设置连接池大小
        connMgr.setMaxTotal(1000);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();

        // 设置连接超时
        configBuilder.setConnectTimeout(CONNECT_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(SOCKET_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(CONNECT_TIMEOUT);
        requestConfig = configBuilder.build();
    }

    private static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            logger.error("创建SSL 失败", e);
        }
        return sslsf;
    }
}
