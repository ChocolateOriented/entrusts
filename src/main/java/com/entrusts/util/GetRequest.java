/**
 * 
 */
package com.entrusts.util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <p>
 * Get请求
 * </p>
 * 
 * <p>
 * Get请求
 * </p>
 * 
 ******************************************************** 
 * Date Author Changes 2012-03-20 Eric Cao 创建
 ******************************************************** 
 */
public class GetRequest
{
    /**
     * Post请求
     * 
     * @param uri
     *            请求的URL
     * @param params
     *            请求参数
     * @return 应答报文
     * @throws IOException
     *             如果请求失败，抛出该异常
     */
    public static String getRequest(String uri, Map<String, String> params) throws IOException
    {
    	return getRequest(uri, params, 30000);
    }
	
    /**
     * Post请求
     * 
     * @param uri
     *            请求的URL
     * @param params
     *            请求参数
     * @param timeout
     *            超时时间
     * @return 应答报文
     * @throws IOException
     *             如果请求失败，抛出该异常
     */
    public static String getRequest(String uri, Map<String, String> params, int timeout) throws IOException
    {
        // 拼接请求参数
        StringBuffer param = new StringBuffer();
        Set<String> keys = params.keySet();
        for (String key : keys)
        {
            // 将请求参数进行URL编码
            String value = URLEncoder.encode(params.get(key), "UTF-8");
            param.append("&" + key + "=" + value);
        }
        return getRequest(uri, param.toString(), timeout);
    }
    
    /**
     * Post请求
     * 
     * @param uri
     *            请求的URL
     * @param params
     *            请求参数
     * @return 应答报文
     * @throws IOException
     *             如果请求失败，抛出该异常
     */
    public static String getRequest(String uri, Map<String, String> params,String charset) throws IOException
    {
        // 拼接请求参数
        StringBuffer param = new StringBuffer();
        Set<String> keys = params.keySet();
        for (String key : keys)
        {
            // 将请求参数进行URL编码
            String value = URLEncoder.encode(params.get(key), charset);
            param.append("&" + key + "=" + value);
        }
        return getRequest(uri, param.toString());
    }
    
    /**
     * Post请求
     * 
     * @param uri
     *            请求的URL
     * @param data
     *            请求参数
     * @return 应答报文
     * @throws IOException
     *             如果请求失败，抛出该异常
     */
    public static String getRequest(String uri, String data) throws IOException
    {
        int timeout ;
        if(uri.contains("mo9.com"))
        {/**mo9类部地址,仅仅允许2秒超时.*/
            timeout = 30000;
        }
        else
        {
            timeout = 8000;
        }
    	return getRequest(uri, data, timeout);
    }
    
    /**
     * Post请求
     * 
     * @param uri
     *            请求的URL
     * @param data
     *            请求参数
     * @param timeout
     *            超时时间
     * @return 应答报文
     * @throws IOException
     *             如果请求失败，抛出该异常
     */
    public static String getRequest(String uri, String data, int timeout) throws IOException
    {
        trustAllHosts();// 信任所有主机
        // 拼接请求参数
        StringBuffer param = new StringBuffer(data);
        if (uri.indexOf("?") >= 0)
        {// URL中已经包含?
            uri = uri + "&" + param.toString();
        } else
        {
            uri = uri + "?" + param.toString();
        }
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (url.getProtocol().equalsIgnoreCase("HTTPS"))
        {// 设置证书验证.
            ((HttpsURLConnection) conn).setHostnameVerifier(verifier);
        }
        conn.setConnectTimeout(timeout);// 连接超时
        conn.setReadTimeout(timeout);// 超时时限
        conn.setRequestMethod("GET");

        conn.setDoOutput(false);
        // 到这里已经完成了，不过我们还是看看返回信息吧，他的注册返回信息也在此页面
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        String inputLine = reader.readLine().toString();
        reader.close();
        return inputLine;
    }
    
    /**
     * HTTS域名验证
     */
    public static final HostnameVerifier verifier = new HostnameVerifier()
                                                   {
                                                       
                                                       public boolean verify(String hostname,
                                                               SSLSession session)
                                                       {
                                                           return true;
                                                       }
                                                   };
    
    public static void trustAllHosts()
    {
        
        TrustManager[] trustEverythingTrustManager = new TrustManager[] { new X509TrustManager()
        {
            
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {
                // TODO Auto-generated method stub
            }
            
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {
                // TODO Auto-generated method stub
                
            }
            
            public X509Certificate[] getAcceptedIssuers()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
        } };
        
        SSLContext sc;
        try
        {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustEverythingTrustManager, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e)
        {
        }
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException
    {
        String data = "name=中国&file=abc&hello=abc.adf";
        System.out.println(URLEncoder.encode(data, "UTF-8"));
    }
}
