package br.com.ufrpe.foodguru.pagseguro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;



import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.GUI.RequestPGAuthActivity;

/**
 * código para acessar a api de requisição do pag seguro
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <authorizationRequest>
 *     <permissions>
 *         <code>CREATE_CHECKOUTS</code>
 *         <code>RECEIVE_TRANSACTION_NOTIFICATIONS</code>
 *         <code>SEARCH_TRANSACTIONS</code>
 *         <code>MANAGE_PAYMENT_PRE_APPROVALS</code>
 *         <code>DIRECT_PAYMENT</code>
 *     </permissions>
 * </authorizationRequest>
 *
 * caso a gente pegue os dados do estabelecimento para facilitar, temos que adicionar:
 *
 * <email>usuario\@seusite.com.br</email>
 * <type>COMPANY</type>
 * <company>
 * <name>Seu Site</name>
 * <documents>
 * <document>
 * <type>CNPJ</type>
 * <value>17302417000101</value>
 * </document>
 * </documents>
 * <displayName>Seu Site</displayName>
 * <websiteURL>http://www.seusite.com.br</websiteURL>
 * <partner>
 * <name>Antonio Carlos</name>
 * <documents>
 * <document>
 * <type>CPF</type>
 * <value>34163749160</value>
 * </document>
 * </documents>
 * <birthDate>1982-02-05</birthDate>
 * </partner>
 * <phones>
 * <phone>
 * <type>BUSINESS</type>
 * <areaCode>11</areaCode>
 * <number>30302323</number>
 * </phone>
 * <phone>
 * <type>BUSINESS</type>
 * <areaCode>11</areaCode>
 * <number>976302323</number>
 * </phone>
 * </phones>
 * <address>
 * <postalCode>01452002</postalCode>
 * <street>Av. Brig. Faria Lima</street>
 * <number>1384</number>
 * <complement>5o andar</complement>
 * <district>Jardim Paulistano</district>
 * <city>Sao Paulo</city>
 * <state>SP</state>
 * <country>BRA</country>
 * </address>
 * </company>
 * </account>
 * </authorizationRequest>
 **/

public class PagSeguroRequest {

    private Activity activity;
    private ProgressDialog progressDialog;
    //referencia da autorização que será usada para pegar o código de autorização;
    private String reference;

    public static final String PAG_SEGURO_EXTRA = "PAG_REQUEST_EXTRA";
    public static final int PAG_SEGURO_REQUEST_CODE = 555;
    public static final int PAG_SEGURO_REQUEST_SUCCESS_CODE = 666;
    public static final int PAG_SEGURO_REQUEST_FAILURE_CODE = 777;
    public static final int PAG_SEGURO_REQUEST_CANCELLED_CODE = 888;

    public PagSeguroRequest(Activity activity) {
        this.reference = activity.getString(R.string.pagseguro_reference_code);
        this.activity = activity;
        this.progressDialog =  new ProgressDialog(activity);
    }

    public void request(String checkoutXml) {
        progressDialog.setTitle(this.activity.getString(R.string.pagseguro));
        progressDialog.setMessage(this.activity.getString(R.string.waiting_for_answer));
        progressDialog.show();
        StringEntity checkoutEntity = null;
        try {
            checkoutEntity = new StringEntity(checkoutXml);
            //Log.d("WEB", "checkoutXml:" + checkoutXml);
            // very important step, if you don`t set this, the request will fail
            checkoutEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/xml"));
            checkoutEntity.setContentEncoding("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            Log.d("WEB", e.getMessage());
        }
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        final String appId = activity.getString(R.string.pagseguro_app_id);
        final String appKey = activity.getString(R.string.pagseguro_app_key);
        final String webService = activity.getString(R.string.pagseguro_webservice_request_address);
        final String pagseguroWsRequestAddress = String.format(webService, appId, appKey);

        client.post(activity, pagseguroWsRequestAddress, checkoutEntity, "application/xml", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "ISO-8859-1");
                    Log.d("PAG", "response:" + response);

                } catch (UnsupportedEncodingException e) {
                    Log.d("PAG", e.getMessage());
                }
                try {
                    // read checkout code
                    final XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(new StringReader(response));
                    int eventType = parser.getEventType();
                    String requestCode="";
                    String requestDate="";
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if(eventType == XmlPullParser.START_TAG) {
                            if(parser.getName().equalsIgnoreCase("code")){
                                parser.next();
                                requestCode = parser.getText();
                                parser.next();
                            }
                        }
                        if(eventType == XmlPullParser.START_TAG) {
                            if(parser.getName().equalsIgnoreCase("requestDate")){
                                parser.next();
                                requestDate = parser.getText();
                                parser.next();
                            }
                        }
                        eventType = parser.next();
                    }
                    final String autorizationAdress = activity.getString(R.string.pagseguro_autorization_page);
                    final String autorizationPage = String.format(autorizationAdress, requestCode);

                    final Intent pagseguro = new Intent(activity, RequestPGAuthActivity.class);
                    pagseguro.putExtra("uri", autorizationPage);
                    pagseguro.putExtra("reference", reference);
                    activity.startActivityForResult(pagseguro, PAG_SEGURO_REQUEST_CODE);
                    progressDialog.hide();

                }catch (XmlPullParserException e) {
                    Log.d("PAG_SEGURO", e.getMessage());
                } catch (IOException e) {
                    Log.d("PAG_SEGURO", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.hide();
                StringBuilder errors = new StringBuilder();
                errors.append("List of errors\n");
                errors.append("Statuscode: "+ statusCode + "\n");
                String response = null;
                try {
                    response = new String(responseBody, "ISO-8859-1");
                    Log.d("PAG", "errorsResponse:" + response);
                } catch (UnsupportedEncodingException e) {
                    Log.d("PAG_SEGURO", e.getMessage());
                }
                try {
                    final XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(new StringReader(response));
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if(eventType == XmlPullParser.START_TAG) {
                            if(parser.getName().equalsIgnoreCase("error")){
                                parser.next();
                                errors.append(parser.getText()+"\n");
                                parser.next();
                            }
                        }
                        eventType = parser.next();
                    }
                    Toast.makeText(activity, errors.toString(), Toast.LENGTH_LONG).show();

                }catch (XmlPullParserException e) {
                    Log.d("PAG_SEGURO", e.getMessage());
                } catch (IOException e) {
                    Log.d("PAG_SEGURO", e.getMessage());
                }
            }
        });
    }

    public String buildRequestXml(){
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>\n" +
                "<authorizationRequest>\n" +
                "    <reference>%s</reference>\n" +
                "    <permissions>\n"+
                "        <code>CREATE_CHECKOUTS</code>\n" +
                "        <code>RECEIVE_TRANSACTION_NOTIFICATIONS</code>\n" +
                "        <code>SEARCH_TRANSACTIONS</code>\n" +
                "        <code>MANAGE_PAYMENT_PRE_APPROVALS</code>\n" +
                "        <code>DIRECT_PAYMENT</code>\n" +
                "    </permissions>\n"+
                "    <redirectURL>http://www.google.com</redirectURL>\n" +
                "</authorizationRequest>";

        return String.format(xml, this.reference );
    }

}
