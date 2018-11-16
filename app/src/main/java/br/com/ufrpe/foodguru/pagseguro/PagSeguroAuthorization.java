package br.com.ufrpe.foodguru.pagseguro;

import android.app.Activity;
import android.util.Log;
import android.util.Xml;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.negocio.EstabelecimentoServices;

public class PagSeguroAuthorization {

    private Activity activity;

    public PagSeguroAuthorization(Activity activity) {
        this.activity = activity;
    }


     public void setAuthCodeByRefCode(String referenceCode){
         AsyncHttpClient client = new AsyncHttpClient();
         final String appId = activity.getString(R.string.pagseguro_app_id);
         final String appKey = activity.getString(R.string.pagseguro_app_key);
         final String webService = activity.getString(R.string.pagseguro_webservice_auth_request_address);
         final String pagseguroWsRequestAddress = String.format(webService,appId, appKey, referenceCode);
         client.get(activity, pagseguroWsRequestAddress, new AsyncHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                 String response = null;
                 String authCode;
                 try {
                     response = new String(responseBody, "ISO-8859-1");
                     Log.d("PAG", "responseGet:" + response);
                 } catch (UnsupportedEncodingException e) {
                     Log.d("PAG", e.getMessage());
                 }
                 try {
                     // read auth code
                     final XmlPullParser parser = Xml.newPullParser();
                     parser.setInput(new StringReader(response));
                     int eventType = parser.getEventType();
                     //String authCode;
                     while (eventType != XmlPullParser.END_DOCUMENT) {
                         if(eventType == XmlPullParser.START_TAG) {
                             if(parser.getName().equalsIgnoreCase("code")){
                                 parser.next();
                                 authCode = parser.getText();
                                 if (authCode != null){
                                     Log.d("PAG", "authCode:"+authCode);
                                     //adicionando ao estabelecimento para autorizar futuras transações!!!
                                     addPagAuthCode(authCode);
                                     break;

                                 }
                                 parser.next();
                             }
                         }
                         eventType = parser.next();
                     }
                 }catch (XmlPullParserException e) {
                     Log.d("PAG", e.getMessage());
                 } catch (IOException e) {
                     Log.d("PAG", e.getMessage());
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                 String response = null;
                 try {
                     response = new String(responseBody, "ISO-8859-1");
                     Log.d("PAG", "errorsResponse:" + response);
                 } catch (UnsupportedEncodingException e) {
                     Log.d("PAG", e.getMessage());
                 }
             }
         });
     }
     private void addPagAuthCode(String authCode){
         EstabelecimentoServices estabelecimentoServices = new EstabelecimentoServices();
         estabelecimentoServices.addPagAuthCode(authCode);
     }

}
