import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import org.springframework.http.HttpStatus;


public class MyTests {

    private final String url_ = "http://localhost:8080/rest/messageservice/1.0/";

    private HttpClient httpclient = HttpClient.newBuilder().build();

    private String buildJsonRequestBody(String msg) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("message", msg);

        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonRequestBody = out.toString();

        System.out.println("----- JSON Request Body ----");
        System.out.println(jsonRequestBody);
        System.out.println("---- ---- ---- ---- --- ----");
        return jsonRequestBody;
    }


    //TODO: there is no validation in json raw format sent in body messag or message is the ok
    private HttpResponse doPostWithJson(String json, String url, String http_verb) throws URISyntaxException, IOException, InterruptedException{

        System.out.println("-----  " + "HTTP VERB :" + http_verb + " -----");
        System.out.println("-----  "+ url + " -----");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url+http_verb))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP StatusCode: " + response.statusCode());
        System.out.println("HTTP Response Body: " + response.body());
        System.out.println("----- "+ " ------"  + " -----");

        return response;
    }

    @Test
    public void testPosFlowFlowPost()  throws URISyntaxException, IOException, InterruptedException{
        String http_verb ="post";

        String jsonRequestBody = buildJsonRequestBody("David I cannot do that!");
        HttpResponse response = doPostWithJson(jsonRequestBody, url_, http_verb);

        assertThat(response.statusCode(), equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void testNotFoundPost()  throws URISyntaxException, IOException, InterruptedException{
        String http_verb ="postt";

        String jsonRequestBody = buildJsonRequestBody("David I cannot do that!");
        HttpResponse response = doPostWithJson(jsonRequestBody, url_, http_verb);

        assertThat(response.statusCode(), equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void testPositiveFlowPostGet() throws URISyntaxException, IOException, InterruptedException, ParseException {

        //do a http post
        String http_verb ="post";
        String jsonRequestBody = buildJsonRequestBody("David I cannot do that!");
        HttpResponse responsePost = doPostWithJson(jsonRequestBody, url_, http_verb);

        //get the id from the post, to use for the get
        long id = getId(responsePost);

        // http get
        http_verb ="get";
        HttpResponse<String> responseGet = doHttpGet(responsePost, url_, http_verb, id);

        assertThat(responseGet.statusCode(), equalTo(HttpStatus.OK.value())); // TODO:test 201 too
    }

    private HttpResponse<String> doHttpGet(HttpResponse response, String url, String http_verb, long id) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("-----  " + "HTTP VERB :" + http_verb + " -----");
        System.out.println("-----  "+ url + " -----");

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(new URI(url+http_verb + "?id="+id))
                .headers("content-type", "x-www-form-urlencoded")
                .GET()
                .build();

        HttpResponse<String> responseGet = httpclient
                .send(requestGet, BodyHandlers.ofString());

        System.out.println("HTTP StatusCode: " + response.statusCode());
        System.out.println("HTTP Response Body: " + response.body());
        return responseGet;
    }

    private long getId(HttpResponse response) throws ParseException {
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(response.body().toString());
        JSONObject jsonObject = (JSONObject) jsonObj;

        long id = (long) jsonObject.get("id");
        return id;
    }

    @Test
    public void testPositiveFlowPostDel() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url_+"post?message=test"))
                //.headers("message", "test")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(json);
        long id = jsonNode.get("id").asLong();

        System.out.println("StatusCode (POST): " + response.statusCode());
        System.out.println("Response Body (POST): " + response.body());



        HttpRequest requestDel = HttpRequest.newBuilder()
                .uri(new URI(url_+"delete?id="+id))
                //.headers("message", "test")
                .DELETE()
                .build();

        HttpResponse<String> responseDel = HttpClient.newBuilder()
                .build()
                .send(requestDel, HttpResponse.BodyHandlers.ofString());

        System.out.println("StatusCode (DEL): " + response.statusCode());
        System.out.println("Response Body (DEL): " + response.body());

        assertThat(responseDel.statusCode(), equalTo(200));
    }

    //TODO: put, list tests, break out code that is redudant, more asserts?
    //TODO: negative flows
    //TODO: Readme + assumptions + requriements
    //TODO: github


        /**
         *
         *
         * "http://localhost:8080/restapi/list"
         * http://localhost:8080/restapi/post?message=test
         * http://localhost:8080/restapi/delete?id=1
         * http://localhost:8080/restapi/put?id=1&message=changed
         */
       /** List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "John"));
        params.add(new BasicNameValuePair("password", "pass"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
**/

}
