package com.autumn.tool;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpDigest
 * @author: 秋雨
 * 2021-04-09 15:33
 **/
public class OKHttp {

    /**
     * 计算http digest md5认证的response参数
     * @param username 用户名
     * @param password 密码
     * @param realm 用户域
     * @param nonce 随机数 服务器生成
     * @param qop 保护质量  auth：鉴权，不对消息体做完整性验证。 auth-int：鉴权并需要对消息体做摘要，保证消息完整性。
     * @param nonceCount nonce计数参数,以00000001开始，每次请求加1，目的是防止重放攻击
     * @param cnonce 客户端nonce
     * @param method 请求方式GET/POST
     * @param digestURI 请求的URI
     * @return
     */
    public static String getDigestResponseStr(String username,String password,String realm,String nonce,String qop,
                                              String nonceCount,String cnonce,String method,String digestURI){
        String HA1 = MD5Util.MD5(username+":"+realm+":"+password).toLowerCase();
        String HA2 = MD5Util.MD5(method+":"+digestURI).toLowerCase();
        String responseStr = MD5Util.MD5(HA1+":"+nonce+":"+nonceCount+":"+cnonce+":"+qop+":"+HA2).toLowerCase();
        return responseStr;
    }

    /**
     * 以application/json方式请求http digest,qop为auth模式
     * @param url 请求url
     * @param jsonBody json字符串(post的body部分)
     * @param username 用户名
     * @param password 密码
     * @param digestURI 请求的URI
     * @return 返回请求的主体值
     * @throws Exception
     */
    public static String postJsonByHttpMd5Digest(String url,String jsonBody,String username,String password,String digestURI) throws Exception {
        //1.直接请求获取nonce
        Map<String,String> responseHeader = HttpUtil.getResHeaderByUrl(url, "POST", null, null, null, null, null);
        //获取401的授权头WWW-Authenticate    Digest realm=realm@host.com,qop=auth,nonce=RL42*****lA,opaque=Dqm*****KEx6
        String authenticate = responseHeader.get("WWW-Authenticate");
        String[] authenticateArr = authenticate.replace("Digest", "").replace(" ", "").split(",");
        //获取授权头中的nonce、realm等值
        Map<String,String> reqMap = new HashMap();
        if (authenticateArr!=null && authenticateArr.length>0){
            for (String d:authenticateArr){
                String[] headerArr = d.split("=");
                reqMap.put(headerArr[0],headerArr[1]);
            }
        }
        //2.获取到nonce值成功
        //System.out.println("nonce值为::::   "+reqMap.get("nonce"));

        //3.计算获取response值
        String realm = reqMap.get("realm");
        String nonce = reqMap.get("nonce");
        String qop = "auth";
        String nonceCount = "";  //nc计数器
        String cnonce = "";  //随机字符串
        String method = "POST";

        String digestResponseStr = getDigestResponseStr(username, password, realm, nonce, qop, nonceCount, cnonce, method, digestURI);

        //4.请求http
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("authorization", "Digest username=\""+username+"\", realm=\""+realm+"\", " +
                        "nonce=\""+nonce+"\", uri=\""+digestURI+"\", " +
                        "qop=auth, nc=\"\", cnonce=\"\", " +
                        "response=\""+digestResponseStr+"\", opaque=\"\"")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();

        /*System.out.println("Response code: " + response.code());
        System.out.println("Response Header: " + response.headers());
        System.out.println("Response Body: " +response.body().string());*/

        String reulst = response.body().string();
        return reulst;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://IP:PORT/url";
        String jsonBody = "{}";
        String username = "username";
        String password = "password";
        String uri = "/url";

        String r = postJsonByHttpMd5Digest(url, jsonBody, username, password, uri);
        System.out.println(r);
    }
}
