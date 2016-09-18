package hbie.vip.parking.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import hbie.vip.parking.utils.LogUtils;

/**
 * Created by mac on 16/5/13.
 */

public class NetUtil {
    private static final String TAG = "NetUtil";

    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        LogUtils.e(TAG, "the net is ok");
                        return true;
                    }
                }
            }
        }
//		Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 网络可用状态下，通过get方式向server端发送请求，并返回响应数据
     *
     * @param strUrl
     *            请求网址
     * @param context
     *            上下文
     * @return 响应数据
     */
    public static JSONObject getResponseForGet(String strUrl, Context context) {
        if (isConnnected(context)) {
            return getResponseForGet(strUrl);
        }
        return null;
    }

    /**
     * 通过Get方式处理请求，并返回相应数据
     *
     * @param strUrl
     *            请求网址
     * @return 响应的JSON数据
     */
    public static JSONObject getResponseForGet(String strUrl) {
        HttpGet httpRequest = new HttpGet(strUrl);
        return getRespose(httpRequest);
    }

    /**
     * 网络可用状态下，通过get方式向server端发送请求，并返回响应数据
     *
     * @param strUrl
     *            请求网址
     * @param context
     *            上下文
     * @return 响应数据
     */
    public static JSONArray getResponseArrayForGet(String strUrl, Context context) {
        if (isConnnected(context)) {
            return getResponseArrayForGet(strUrl);
        }
        return null;
    }

    /**
     * 通过Get方式处理请求，并返回相应数据
     *
     * @param strUrl
     *            请求网址
     * @return 响应的JSON数据
     */
    public static JSONArray getResponseArrayForGet(String strUrl) {
        HttpGet httpRequest = new HttpGet(strUrl);
        return getResposeArray(httpRequest);
    }

    /**
     * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据
     *
     * @param market_uri
     *            请求网址
     * @param nameValuePairs
     *            参数信息
     * @param context
     *            上下文
     * @return 响应数据
     */
    public static JSONObject getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs, Context context) {
        if (isConnnected(context)) {
            // System.out.println("market_uri="+market_uri);
            // System.out.println("nameValuePairs="+nameValuePairs);
            return getResponseForPost(market_uri, nameValuePairs);
        }
        return null;
    }

    /**
     * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据
     *
     * @param market_uri
     *            请求网址
     * @param nameValuePairs
     *            参数信息
     * @param context
     *            上下文
     * @return 响应数据
     */
    public static JSONArray getResponseArrayForPost(String market_uri, List<NameValuePair> nameValuePairs, Context context) {
        if (isConnnected(context)) {
            // System.out.println("market_uri="+market_uri);
            // System.out.println("nameValuePairs="+nameValuePairs);
            return getResponseArrayForPost(market_uri, nameValuePairs);
        }
        return null;
    }

    /**
     * 通过post方式向服务器发送请求，并返回响应数据
     *
     * @param market_uri
     *            请求网址
     * @param nameValuePairs
     *            参数信息
     * @return 响应数据
     */
    public static JSONObject getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs) {
        if (null == market_uri || "" == market_uri) {
            return null;
        }
        HttpPost request = new HttpPost(market_uri);
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            return getRespose(request);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 通过post方式向服务器发送请求，并返回响应数据
     *
     * @param market_uri
     *            请求网址
     * @param nameValuePairs
     *            参数信息
     * @return 响应数据
     */
    public static JSONArray getResponseArrayForPost(String market_uri, List<NameValuePair> nameValuePairs) {
        if (null == market_uri || "" == market_uri) {
            return null;
        }
        HttpPost request = new HttpPost(market_uri);
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            return getResposeArray(request);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 响应客户端请求
     *
     * @param request
     *            客户端请求get/post
     * @return 响应数据
     */
    public static JSONObject getRespose(HttpUriRequest request) {
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                // System.out.println("[result]="+result);
                if (result.equals("")) {
                    return null;
                }
                return new JSONObject(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 响应客户端请求
     *
     * @param request
     *            客户端请求get/post
     * @return 响应数据
     */
    public static JSONArray getResposeArray(HttpUriRequest request) {
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                // System.out.println("[result]="+result);
                if (result.equals("")) {
                    return null;
                }
                return new JSONArray(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
