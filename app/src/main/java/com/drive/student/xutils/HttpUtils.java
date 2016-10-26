/**
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.drive.student.xutils;

import android.text.TextUtils;

import com.drive.student.util.LogUtil;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class HttpUtils {

    private static HttpUtils httpUtils = null;

    public final static com.drive.student.xutils.http.HttpCache sHttpCache = new com.drive.student.xutils.http.HttpCache();

    private final DefaultHttpClient httpClient;

    private com.drive.student.xutils.http.callback.HttpRedirectHandler httpRedirectHandler;

    /**
     * 当前活动的HttpContext上下文
     */
    public static HttpContext httpContext = new BasicHttpContext();

    /************************************ default settings & fields ****************************/

    /**
     * response返回编 码
     */
    private String responseTextCharset = HTTP.UTF_8;

    private long currentRequestExpiry = com.drive.student.xutils.http.HttpCache.getDefaultExpiryTime();
    /**
     * 默认请求超时时间
     */
    private final static int DEFAULT_CONN_TIMEOUT = 1000 * 15;
    /**
     * 默认读取超时时间
     */
    private final static int DEFAULT_SO_TIMEOUT = 1000 * 10;
    /**
     * 默认重新请求次数
     */
    private final static int DEFAULT_RETRY_TIMES = 2;
    /**
     * 声明浏览器支持的编码类型
     */
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    /**
     * 编码格式
     */
    private static final String ENCODING_GZIP = "gzip";
    /**
     * 默认线程数
     */
    private final static int DEFAULT_POOL_SIZE = 3;

    private final static com.drive.student.xutils.task.PriorityExecutor EXECUTOR = new com.drive.student.xutils.task.PriorityExecutor(DEFAULT_POOL_SIZE);

    /**
     * 客户端版本
     */
    /*
	private static final String VERSION_CODE = MainApplication.getInstance().getAPKVersionCode();
	*//** 客户端系统版本 **/
	/*
	private static final String OS_VERSION = Build.VERSION.RELEASE;
	*//** 客户端操作系统类型 **/
	/*
	private static final String OS = "android";
	*//** 用户名 **/

	/*
	private static final String USER_NAME = MainApplication.getInstance().getUserName();
	*/

    /**
     *
     * 获取类实例,非单例模式.
     *
     * @return
     * @author 韩新凯
     * @update 2014年5月30日 上午10:53:21
     */
    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
//		validateCookie();
        return httpUtils;
    }

    /**
     *
     * 校验cookie是否完整.
     * @return true没失效，false失效
     * @author 韩新凯
     * @update 2014年7月23日 上午11:52:31
     */
	/*private synchronized static boolean validateCookie() {
		CookieStore cookies = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
		if (cookies == null || cookies.getCookies() == null) {
			httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.COOKIE_STORE, MainApplication.getInstance().getCookieStore());
			return false;
		}
	
		try {
			HashMap<String, String> cookieMap = new HashMap<String, String>();
			for (Cookie cookie : cookies.getCookies()) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
	
			boolean mfgMemberSginBool = (cookieMap.containsKey("mfg_membersgin") && !StringUtil.isEmpty(cookieMap.get("mfg_membersgin")));
			boolean mfgMemberIdBool = (cookieMap.containsKey("mfg_memberid") && !StringUtil.isEmpty(cookieMap.get("mfg_memberid")));
	
			if (mfgMemberSginBool && mfgMemberIdBool) {
				return true;
			} else {
				httpContext = new BasicHttpContext();
				httpContext.setAttribute(ClientContext.COOKIE_STORE, MainApplication.getInstance().getCookieStore());
			}
		} catch (Exception e) {
			// NOTE: handle exception
		}
		return false;
	}*/

    /**
     * 获得当前上下文cookie
     */
	/*public static String getCookieStr() {
		String cookieStr = "";
		try {
			CookieStore cookieStore = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
			if (cookieStore == null || cookieStore.getCookies() == null || cookieStore.getCookies().size() == 0) {
				return cookieStr;
			}
			for (Cookie c : cookieStore.getCookies()) {
				cookieStr += "; " + c.getName() + "=" + c.getValue() + "##" + c.getDomain();
			}
		} catch (Exception e) {
			return "";
		}
		return null == cookieStr ? "" : cookieStr;
	}*/
    public HttpUtils() {
        this(HttpUtils.DEFAULT_CONN_TIMEOUT, null);
    }

    public HttpUtils(int connTimeout) {
        this(connTimeout, null);
    }

    public HttpUtils(String userAgent) {
        this(HttpUtils.DEFAULT_CONN_TIMEOUT, userAgent);
    }

    public HttpUtils(int connTimeout, String userAgent) {
        HttpParams params = new BasicHttpParams();

        // configCookieStore(cookieStore); //配置好全局cookies
        ConnManagerParams.setTimeout(params, connTimeout);
        HttpConnectionParams.setSoTimeout(params, connTimeout);
        HttpConnectionParams.setConnectionTimeout(params, connTimeout);

        if (TextUtils.isEmpty(userAgent)) {
            userAgent = com.drive.student.xutils.util.OtherUtils.getUserAgent(null);
        }
        HttpProtocolParams.setUserAgent(params, userAgent);

        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(params, 10);

        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 1024 * 8);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", com.drive.student.xutils.http.client.DefaultSSLSocketFactory.getSocketFactory(), 443));

        httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);

        httpClient.setHttpRequestRetryHandler(new com.drive.student.xutils.http.client.RetryHandler(DEFAULT_RETRY_TIMES));

        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(org.apache.http.HttpRequest httpRequest, HttpContext httpContext) throws org.apache.http.HttpException, IOException {
                if (!httpRequest.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    httpRequest.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext httpContext) throws org.apache.http.HttpException, IOException {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new com.drive.student.xutils.http.client.entity.GZipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }
        });
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    /***************************************** config *******************************************/

    public HttpUtils configResponseTextCharset(String charSet) {
        if (!TextUtils.isEmpty(charSet)) {
            this.responseTextCharset = charSet;
        }
        return this;
    }

    public HttpUtils configHttpRedirectHandler(com.drive.student.xutils.http.callback.HttpRedirectHandler httpRedirectHandler) {
        this.httpRedirectHandler = httpRedirectHandler;
        return this;
    }

    public HttpUtils configHttpCacheSize(int httpCacheSize) {
        sHttpCache.setCacheSize(httpCacheSize);
        return this;
    }

    public HttpUtils configDefaultHttpCacheExpiry(long defaultExpiry) {
        com.drive.student.xutils.http.HttpCache.setDefaultExpiryTime(defaultExpiry);
        currentRequestExpiry = com.drive.student.xutils.http.HttpCache.getDefaultExpiryTime();
        return this;
    }

    public HttpUtils configCurrentHttpCacheExpiry(long currRequestExpiry) {
        this.currentRequestExpiry = currRequestExpiry;
        return this;
    }

    public HttpUtils configCookieStore(CookieStore cookieStore) {
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        return this;
    }

    public HttpUtils configUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
        return this;
    }

    public HttpUtils configTimeout(int timeout) {
        final HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        return this;
    }

    public HttpUtils configSoTimeout(int timeout) {
        final HttpParams httpParams = this.httpClient.getParams();
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        return this;
    }

    public HttpUtils configRegisterScheme(Scheme scheme) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return this;
    }

    public HttpUtils configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        Scheme scheme = new Scheme("https", sslSocketFactory, 443);
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return this;
    }

    public HttpUtils configRequestRetryCount(int count) {
        this.httpClient.setHttpRequestRetryHandler(new com.drive.student.xutils.http.client.RetryHandler(count));
        return this;
    }

    public HttpUtils configRequestThreadPoolSize(int threadPoolSize) {
        HttpUtils.EXECUTOR.setPoolSize(threadPoolSize);
        return this;
    }

    /***************************************** send request *******************************************/

    public <T> com.drive.student.xutils.http.HttpHandler<T> send(HttpRequest.HttpMethod method, String url, com.drive.student.xutils.http.callback.RequestCallBack<T> callBack) {
        return send(method, url, null, callBack);
    }

    public <T> com.drive.student.xutils.http.HttpHandler<T> send(HttpRequest.HttpMethod method, String url, com.drive.student.xutils.http.RequestParams params, RequestCallBack<T> callBack) {
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        if (params == null) {
            params = new com.drive.student.xutils.http.RequestParams();
        }
        buildHttpHeaderForHP(params);// 添加请求头
        if (method == HttpMethod.GET) {// 随机数参数过滤缓存
            params.addQueryStringParameter("t", UUID.randomUUID().toString());
        }
        HttpRequest request = new HttpRequest(method, url);
        return sendRequest(request, params, callBack);
    }

    public com.drive.student.xutils.http.ResponseStream sendSync(HttpRequest.HttpMethod method, String url) throws HttpException {
        return sendSync(method, url, null);
    }

    public com.drive.student.xutils.http.ResponseStream sendSync(HttpRequest.HttpMethod method, String url, com.drive.student.xutils.http.RequestParams params) throws HttpException {
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        if (params == null) {
            params = new com.drive.student.xutils.http.RequestParams();
        }
        buildHttpHeaderForHP(params);// 添加请求头
        if (method == HttpMethod.GET) {// 随机数参数过滤缓存
            params.addQueryStringParameter("t", UUID.randomUUID().toString());
        }
        HttpRequest request = new HttpRequest(method, url);
        return sendSyncRequest(request, params);
    }

    /***************************************** download *******************************************/

    public com.drive.student.xutils.http.HttpHandler<File> download(String url, String target, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(com.drive.student.xutils.http.client.HttpRequest.HttpMethod.GET, url, target, null, false, false, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(String url, String target, boolean autoResume, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(com.drive.student.xutils.http.client.HttpRequest.HttpMethod.GET, url, target, null, autoResume, false, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(String url, String target, boolean autoResume, boolean autoRename, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(com.drive.student.xutils.http.client.HttpRequest.HttpMethod.GET, url, target, null, autoResume, autoRename, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(String url, String target, com.drive.student.xutils.http.RequestParams params, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(com.drive.student.xutils.http.client.HttpRequest.HttpMethod.GET, url, target, params, false, false, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(String url, String target, com.drive.student.xutils.http.RequestParams params, boolean autoResume, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(com.drive.student.xutils.http.client.HttpRequest.HttpMethod.GET, url, target, params, autoResume, false, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(String url, String target, com.drive.student.xutils.http.RequestParams params, boolean autoResume, boolean autoRename, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(com.drive.student.xutils.http.client.HttpRequest.HttpMethod.GET, url, target, params, autoResume, autoRename, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target, com.drive.student.xutils.http.RequestParams params, RequestCallBack<File> callback) {
        return download(method, url, target, params, false, false, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target, com.drive.student.xutils.http.RequestParams params, boolean autoResume, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {
        return download(method, url, target, params, autoResume, false, callback);
    }

    public com.drive.student.xutils.http.HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target, com.drive.student.xutils.http.RequestParams params, boolean autoResume, boolean autoRename, com.drive.student.xutils.http.callback.RequestCallBack<File> callback) {

        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target may not be null");
        }

        HttpRequest request = new HttpRequest(method, url);

        com.drive.student.xutils.http.HttpHandler<File> handler = new com.drive.student.xutils.http.HttpHandler<File>(httpClient, httpContext, responseTextCharset, callback);

        handler.setExpiry(currentRequestExpiry);
        handler.setHttpRedirectHandler(httpRedirectHandler);

        if (params != null) {
            request.setRequestParams(params, handler);
            handler.setPriority(params.getPriority());
        }
        handler.executeOnExecutor(EXECUTOR, request, target, autoResume, autoRename);
        return handler;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////
    private <T> com.drive.student.xutils.http.HttpHandler<T> sendRequest(HttpRequest request, com.drive.student.xutils.http.RequestParams params, RequestCallBack<T> callBack) {

        com.drive.student.xutils.http.HttpHandler<T> handler = new HttpHandler<T>(httpClient, httpContext, responseTextCharset, callBack);

        handler.setExpiry(currentRequestExpiry);
        handler.setHttpRedirectHandler(httpRedirectHandler);
        request.setRequestParams(params, handler);

        if (params != null) {
            handler.setPriority(params.getPriority());
        }
        handler.executeOnExecutor(EXECUTOR, request);
        return handler;
    }

    private com.drive.student.xutils.http.ResponseStream sendSyncRequest(HttpRequest request, com.drive.student.xutils.http.RequestParams params) throws HttpException {

        com.drive.student.xutils.http.SyncHttpHandler handler = new SyncHttpHandler(httpClient, httpContext, responseTextCharset);

        handler.setExpiry(currentRequestExpiry);
        handler.setHttpRedirectHandler(httpRedirectHandler);
        request.setRequestParams(params);

        return handler.sendRequest(request);
    }

    /**
     *
     * 使用HttpClient发送一个get方式的超链接请求.
     *
     * @param urlpath
     * @return
     * @author 韩新凯
     * @update 2012-6-29 上午11:58:14
     */
    public HttpResponse sendHttpGet(String urlpath, boolean isGzip) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet(urlpath);
            buildHttpHeaderForGet(request);// 请求头
            if (isGzip) {
                request.addHeader("accept-encoding", "gzip");
            }
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpUtils.DEFAULT_CONN_TIMEOUT); // 设置请求超时时间
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HttpUtils.DEFAULT_SO_TIMEOUT); // 读取超时
            HttpResponse response = httpclient.execute(request, httpContext);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用HttpClient发送一个get方式的超链接请求.
     *
     * @param url
     * @param params
     * @return
     */
    public HttpResponse sendHttpGet(String url, Map params, boolean isGzip) {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpUtils.DEFAULT_CONN_TIMEOUT); // 设置请求超时时间
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HttpUtils.DEFAULT_SO_TIMEOUT); // 读取超时

		/* 建立HTTPGet对象 */
        String paramStr = "";
        if (params != null) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                paramStr += paramStr = "&" + key + "=" + val;
            }
        }
        if (!paramStr.equals("")) {
            paramStr = paramStr.replaceFirst("&", "?");
            url += paramStr;
        }
        HttpGet request = new HttpGet(url);
        buildHttpHeaderForGet(request);// 请求头
        if (isGzip) {
            request.addHeader("accept-encoding", "gzip");
        }
        try {
			/* 发送请求并等待响应 */
            HttpResponse response = httpclient.execute(request, httpContext);
			/* 若状态码为200 ok */
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 使用HttpClient发送一个post方式的请求.
     *
     * @param url
     * @param params
     * @return
     * @author 韩新凯
     * @update 2012-6-29 上午11:58:30
     */
    public HttpResponse sendHttpPost(String url, Map<String, String> params, boolean isGzip) {
        try {
            List<NameValuePair> param = new ArrayList<NameValuePair>(); // 参数
            if (params != null) {
                Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    param.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            HttpPost request = new HttpPost(url);
            buildHttpHeaderForPost(request);// 请求头
            if (isGzip) {
                request.addHeader("accept-encoding", "gzip");
            }
            HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
            request.setEntity(entity);
            HttpClient client = new DefaultHttpClient();

            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpUtils.DEFAULT_CONN_TIMEOUT); // 设置请求超时时间
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HttpUtils.DEFAULT_SO_TIMEOUT); // 读取超时
            HttpResponse response = client.execute(request, httpContext);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                LogUtil.e("hxk", "upload file success -->>");
                return response;
            } else {
                LogUtil.e("hxk", "upload fail -->>" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("hxk", "upload fail -->>");
        return null;
    }

    /**
     * 上传文件至Server的方法
     *
     * @param urlStr
     *            服务器对应的路径
     * @param serverFileName
     *            上传服务器后在服务器上的文件名称 如：image.jpg
     * @param uploadFile
     *            要上传的文件路径 如：/sdcard/a.jpg
     */
    public void uploadFile(String urlStr, String serverFileName, File uploadFile) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(1024 * 1024);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;file=" + uploadFile.getName());
            conn.setRequestProperty("filename", uploadFile.getName());
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(new FileInputStream(uploadFile));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            out.flush();
            out.close();

            int response = conn.getResponseCode();

            if (response == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                LogUtil.e("hxk", "upload file success-->>");
            } else {
                LogUtil.e("hxk", "upload file fail-->> response = " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("hxk", "upload file fail-->>");
        }
    }

    /**
     *
     * 打印返回信息(仅用于测试).
     *
     * @param conn
     * @author 韩新凯
     * @update Feb 7, 2012 6:18:42 PM
     */
    private String printResponse(HttpURLConnection conn) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append("\n" + line);
            }
            // System.out.println("==>" + sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void buildHttpHeaderForHP(com.drive.student.xutils.http.RequestParams params) {
		/*params.addHeader("versionCode", VERSION_CODE);
		params.addHeader("os", OS);
		params.addHeader("osVersion", OS_VERSION);*/
    }

    private void buildHttpHeaderForGet(HttpGet httpGet) {
		/*httpGet.addHeader("versionCode", VERSION_CODE);
		httpGet.addHeader("os", OS);
		httpGet.addHeader("osVersion", OS_VERSION);*/
    }

    private void buildHttpHeaderForPost(HttpPost httpPost) {
		/*httpPost.addHeader("versionCode", VERSION_CODE);
		httpPost.addHeader("os", OS);
		httpPost.addHeader("osVersion", OS_VERSION);*/
    }
}
