package com.zoo.ninestar.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class HttpUtils {
	
	private static final int HTTP_TIME_OUT = 5000;
	private static Logger loger=LoggerFactory.getLogger(HttpUtils.class);
    
    /**
     * 
     * @param urlStr urlStr
     * @throws MalformedURLException MalformedURLException
     * @throws IOException IOException
     */
    public  static void doHttpGetNoReturn(String urlStr) throws MalformedURLException, IOException {
        URL url = new URL(urlStr);
        URLConnection c = url.openConnection();

        if (c instanceof HttpURLConnection) {
            HttpURLConnection httpConn = (HttpURLConnection) c;
            httpConn.setRequestMethod("GET");
            httpConn.setUseCaches(false);
            httpConn.setDoInput(true);
            HttpURLConnection.setFollowRedirects(true);
          
        } else {
            throw new MalformedURLException("Only HTTP/HTTPs is valid.");
        }
    }
    /**
     * 
     * @param urlStr urlStr
     * @return String
     * @throws MalformedURLException MalformedURLException
     * @throws IOException IOException
     */
    public static String doHttpGet (String urlStr) throws MalformedURLException, IOException {
        URL url = new URL(urlStr);
        URLConnection c = url.openConnection();

        if (c instanceof HttpURLConnection) {
            HttpURLConnection httpConn = (HttpURLConnection) c;
            httpConn.setRequestMethod("GET");
            httpConn.setUseCaches(false);
            httpConn.setDoInput(true);
            HttpURLConnection.setFollowRedirects(true);

            // get response.
            InputStream is = httpConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            httpConn.disconnect();

            return response.toString();
        } else {
            throw new MalformedURLException("Only HTTP/HTTPs is valid.");
        }
    }
    
 
    
    public static String doHttpClientGet(String url){
     	 try {
              DefaultHttpClient client = new DefaultHttpClient();
              HttpGet request = new HttpGet(url);
              HttpResponse response = client.execute(request);
              int code = response.getStatusLine().getStatusCode();
              if (code == HttpStatus.SC_OK) {
                  String strResult = EntityUtils.toString(response.getEntity());
                  return strResult;
              }else{
           	   loger.info("[http request error] url=" + url + "  error code=" + code);
              }
          } catch (IOException e) {
       	   loger.info("[http request error] url=" + url + "   IOException");
          }
     	 return null;
     }
    
    public static InputStream doPost(String url){
      	 try {
               DefaultHttpClient client = new DefaultHttpClient();
               HttpGet request = new HttpGet(url);
//               HttpPost request = new HttpPost(url);
               HttpResponse response = client.execute(request);
               int code = response.getStatusLine().getStatusCode();
               if (code == HttpStatus.SC_OK) {
                   
                   return response.getEntity().getContent();
               }else{
            	   loger.info("[http request error] url=" + url + "  error code=" + code);
               }
           } catch (IOException e) {
        	   loger.info("[http request error] url=" + url + "   IOException");
           }
      	 return null;
      }
      public static String  doPost(String url ,List<NameValuePair> params){
      	DefaultHttpClient client = new DefaultHttpClient();
          HttpPost request = new HttpPost(url);
   		try {
   			UrlEncodedFormEntity  entity=new UrlEncodedFormEntity(params,"UTF-8");
   			request.setEntity(entity);
   			HttpResponse response=client.execute(request);
   			return EntityUtils.toString(response.getEntity(),  "UTF-8");
   		} catch (Exception e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   		return null;
      }
      public static String pushImgJpgDoPost(String url ,byte[] data,String partKey){
      	String responseContent =null;
      	DefaultHttpClient client = new DefaultHttpClient();
      	 HttpPost request = new HttpPost(url);
      	 MultipartEntity reqEntity=new MultipartEntity();
      	
      	 ByteArrayBody contentBody=new ByteArrayBody(data, "image/png", "file.png");
      	 
      	 reqEntity.addPart(partKey, contentBody);
      	 request.setEntity(reqEntity);
      	 try {
   			HttpResponse response=client.execute(request);
   			HttpEntity resEntity=response.getEntity();
   			responseContent = EntityUtils.toString(resEntity,  "UTF-8");
              EntityUtils.consume(resEntity);
   			
   		} catch (ClientProtocolException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}finally{
   			  client.getConnectionManager().shutdown();
   		}
      	return responseContent; 
      }





    /**
     * POST请求   携带Json格式的参数
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String postJson(String url, Object param) {
        String CONTENT_TYPE_TEXT_JSON = "text/json";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setConfig(RequestConfig.custom()
                .setConnectTimeout(2000)
                .setSocketTimeout(10000).build());
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        String parameter = JSON.toJSONString(param);
        StringEntity se = null;
        try {
            System.out.println(parameter);
            se = new StringEntity(parameter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        httpPost.setEntity(se);
        // TODO: post 的参数拼接

        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(httpEntity, "UTF-8");
            }else {
                loger.error("post fail result={}", result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
