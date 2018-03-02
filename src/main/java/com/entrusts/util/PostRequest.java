/**
 * 
 */
package com.entrusts.util;

import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <p>
 * Post请求
 * </p>
 * 
 * <p>
 * Post请求
 * </p>
 * 
 ******************************************************** 
 * Date Author Changes 2011-11-23 Eric Cao 创建
 ******************************************************** 
 */
public class PostRequest {

	private static Logger log = Logger.getLogger(PostRequest.class);
	private static final int DEFAULT_TIMEOUT = 45000;
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * Post请求(默认使用UTF-8)
	 * 
	 * @param uri
	 *            请求的URL
	 * @param data
	 *            请求参数,待请求的参数采用name=value&name=value模式集成.
	 * @return 应答报文
	 * @throws IOException
	 *             如果请求失败，抛出该异常
	 */
	public static String postRequest(String uri, String data,Map<String,String> headers,Integer timeout) throws IOException {

		log.info("POST:"+uri+", msg:"+data);
		trustAllHosts();// 信任所有HTTPS主机.
		URL url = new URL(uri);
		HttpURLConnection conn = null;
		StringBuffer resp = new StringBuffer();
		try {
			conn = (HttpURLConnection) url.openConnection();
			if ( url.getProtocol().equalsIgnoreCase("HTTPS") ) {// 为HTTPS地址，添加域名验证策略
				((HttpsURLConnection) conn).setHostnameVerifier(verifier);
			}

			//添加请求头
			if (headers!=null && headers.size()>0){
				for (Map.Entry<String,String> header: headers.entrySet()) {
					conn.setRequestProperty(header.getKey(),header.getValue());
				}
			}

			if(timeout == null || timeout == 0){
				timeout = DEFAULT_TIMEOUT;
			}
			conn.setConnectTimeout(timeout);// 连接超时
			conn.setReadTimeout(timeout);// 超时时限

			conn.setRequestMethod("POST");
			// 这里是关键，表示我们要向链接里输出内容
			conn.setDoOutput(true);
			// 获得连接输出流
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			// 把数据写入
			StringBuffer param = new StringBuffer(data);
			if ( param.length() > 0 && param.charAt(0) == '&' ) {// Post请求，删除第一个'&符号'
				out.write(param.substring(1));
			} else {
				out.write(param.toString());
			}
			out.flush();
			out.close();
			// 到这里已经完成了，不过我们还是看看返回信息吧，他的注册返回信息也在此页面
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),DEFAULT_CHARSET));

			String inputLine = reader.readLine();
			while (inputLine != null) {
				resp.append(inputLine);
				inputLine = reader.readLine();
			}
			reader.close();
		} finally {
			if ( conn != null ) {
				conn.disconnect();
			}
		}
		log.info("POST RESULT:"+resp.toString());
		return resp.toString();
	}

	/**
	 * Post请求(使用默认编码UTF-8,默认超时时间)
	 * @param uri
	 *            请求的URL
	 * @param params
	 *            请求参数
	 * @return 应答报文
	 * @throws IOException
	 *             如果请求失败，抛出该异常
	 */
	public static String postRequest(String uri, Map<String, String> params) throws IOException {
		return postRequest(uri, params,DEFAULT_CHARSET , DEFAULT_TIMEOUT);
	}

	public static String postRequest(String uri, Map<String, String> params, String charSet) throws IOException {
		return postRequest(uri, params,charSet,DEFAULT_TIMEOUT);
	}

	public static String postRequest(String uri, Map<String, String> params, Integer timeout) throws IOException {
		return postRequest(uri, params,DEFAULT_CHARSET,timeout);
	}

	/**
	 * Post请求(使用默认编码UTF-8,默认超时时间)
	 * @param uri 请求的URL
	 * @param data 请求数据
	 * @param headers 头信息
	 * @return 应答报文
	 * @throws IOException 如果请求失败，抛出该异常
	 */
	public static String postRequest(String uri, String data, Map<String, String> headers) throws IOException {
		return postRequest(uri, data, headers, DEFAULT_TIMEOUT);
	}

	/**
	 * 按照指定的编码发起Post请求.
	 * 
	 * @param uri
	 *            请求的URL
	 * @param params
	 *            请求参数
	 * @return 应答报文
	 * @throws IOException
	 *             如果请求失败，抛出该异常
	 */
	public static String postRequest(String uri, Map<String, String> params, String charSet, Integer timeout) throws
			IOException {

		StringBuffer param = new StringBuffer();
		Set<String> keys = params.keySet();
		for ( String key : keys ) {
			// 将请求参数进行URL编码
			String value = URLEncoder.encode(params.get(key), charSet);
			param.append("&" + key + "=" + value);
		}
		return postRequest(uri, param.toString(), null,timeout);
	}

	/**
	 * HTTS域名验证
	 */
	private static final HostnameVerifier verifier = new HostnameVerifier() {

		public boolean verify(String hostname, SSLSession session) {

			return true;
		}
	};

	/**
	 * HTTPS信任所有主机 trustAllHosts:
	 * 
	 */
	private static final void trustAllHosts() {

		TrustManager[] trustEverythingTrustManager = new TrustManager[] { new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

				// TODO Auto-generated method stub
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

				// TODO Auto-generated method stub

			}

			public X509Certificate[] getAcceptedIssuers() {

				// TODO Auto-generated method stub
				return null;
			}

		} };

		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustEverythingTrustManager, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}

	public static void main(String[] arg) throws IOException {
		
	    Map<String,String> params = new HashMap<String,String>();
	    params.put("client_id", "S_admin");
	    params.put("valid_type", "secret");
	    params.put("valid_token", "11111111");
	    params.put("mobile", "15618550929");
	    params.put("template_name", "PINCODE");
	    params.put("template_data", "captcha:123456");
	    params.put("template_tags", "CN");
		String str = PostRequest.postRequest("http://localhost/snc/sms/sendSms", params);
		System.out.println(str);
	}


}
