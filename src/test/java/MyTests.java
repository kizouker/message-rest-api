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

    private String buildJsonRequestBody(long id, String text) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("text", text);

        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonRequestBody = out.toString();

        System.out.println("----- JSON Request Body ----");
        System.out.println(jsonRequestBody);
        System.out.println("---- ---- ---- ---- --- ----");
        return jsonRequestBody;
    }

    //TODO: there is no validation in json raw format sent in body messag or message is the ok
    private HttpResponse doPostWithText(String text, String url, String http_verb) throws URISyntaxException, IOException, InterruptedException{
        String url__ = url + http_verb;

        System.out.println("-----  " + "HTTP VERB :" + http_verb + " -----");
        System.out.println("-----  "+ url__ + " -----");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url__))
                //.header("content-type", "application/json")
                .header("content-type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(text))
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP StatusCode: " + response.statusCode());
        System.out.println("HTTP Response Body: " + response.body());
        System.out.println("----- "+ " ------"  + " -----");

        return response;
    }



    private HttpResponse<String> doHttpGet(HttpResponse response, String url, String http_verb, long id) throws URISyntaxException, IOException, InterruptedException {

        String url__ = url + http_verb + "?id="+id;
        System.out.println("-----  " + "HTTP VERB :" + http_verb + " -----");
        System.out.println("-----  "+ url__ + " -----");

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(new URI(url__))
                .headers("content-type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        HttpResponse<String> responseGet = httpclient
                .send(requestGet, BodyHandlers.ofString());

        System.out.println("HTTP StatusCode: " + responseGet.statusCode());
        System.out.println("HTTP Response Body: " + responseGet.body());
        return responseGet;
    }

    private HttpResponse<String> doHttpDelete(HttpResponse response, String url, String http_verb, long id) throws URISyntaxException, IOException, InterruptedException {
        String url__ = url + http_verb + "?id="+id;
        System.out.println("-----  " + "HTTP VERB :" + http_verb + " -----");
        System.out.println("-----  "+ url__ + " -----");

        HttpRequest requestDel = HttpRequest.newBuilder()
                .uri(new URI(url__))
                .headers("content-type", "application/x-www-form-urlencoded")
                .DELETE()
                .build();

        HttpResponse<String> responseDelete = httpclient
                .send(requestDel, BodyHandlers.ofString());

        System.out.println("HTTP StatusCode: " + responseDelete.statusCode());
        System.out.println("HTTP Response Body: " + responseDelete.body());
        return responseDelete;
    }

    private HttpResponse<String> doHttpPut(String jsonRequestBody, String url, String http_verb, long id) throws URISyntaxException, IOException, InterruptedException {
        String url__ = url + http_verb;
        System.out.println("-----  " + "HTTP VERB :" + http_verb + " -----");
        System.out.println("-----  "+ url__ + " -----");

        HttpRequest requestPut = HttpRequest.newBuilder()
                .uri(new URI(url__))
                .headers("content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        HttpResponse<String> responsePut = httpclient
                .send(requestPut, BodyHandlers.ofString());

        System.out.println("HTTP StatusCode: " + responsePut.statusCode());
        System.out.println("HTTP Response Body: " + responsePut.body());
        return responsePut;
    }

    private long getId(HttpResponse response) throws ParseException {
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(response.body().toString());
        JSONObject jsonObject = (JSONObject) jsonObj;

        long id = (long) jsonObject.get("id");
        return id;
    }
    @Test
    public void testPosFlowFlowPost()  throws URISyntaxException, IOException, InterruptedException{
        String http_verb ="post";

        HttpResponse response = doPostWithText("David I cannot do that!", url_, http_verb);

        assertThat(response.statusCode(), equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void testNotFoundPost()  throws URISyntaxException, IOException, InterruptedException{
        String http_verb ="postt";

        HttpResponse response = doPostWithText("goliat I cannot do that!", url_, http_verb);

        assertThat(response.statusCode(), equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void testPositiveFlowPostGet() throws URISyntaxException, IOException, InterruptedException, ParseException {

        //do a http post
        String http_verb ="post";
        HttpResponse responsePost = doPostWithText("David I cannot do that!", url_, http_verb);

        //get the id from the post, to use for the get
        long id = getId(responsePost);

        // http get
        http_verb ="get";
        HttpResponse<String> responseGet = doHttpGet(responsePost, url_, http_verb, id);

        assertThat(responseGet.statusCode(), equalTo(HttpStatus.OK.value()));
    }
    @Test
    public void testPositiveFlowPostDel() throws URISyntaxException, IOException, InterruptedException, ParseException {
        //do a http post
        String http_verb ="post";
        HttpResponse responsePost = doPostWithText("David I cannot do that!", url_, http_verb);

        //get the id from the post, to use for the get
        long id = getId(responsePost);

        // http delete
        http_verb ="delete";
        HttpResponse<String> responseDelete = doHttpDelete(responsePost, url_, http_verb, id);

        assertThat(responseDelete.statusCode(), equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void testPositiveFlowPostPut() throws URISyntaxException, IOException, InterruptedException, ParseException {
        //do a http post
        String http_verb ="post";

        HttpResponse responsePost = doPostWithText("Rickard I cannot do that!", url_, http_verb);

        //get the id from the post, to use for the put
        long id = getId(responsePost);

        // http put
        http_verb ="put";

        String jsonRequestBodyPut = buildJsonRequestBody(id, "Goliat I cannot do that!");
        HttpResponse<String> responsePut = doHttpPut(jsonRequestBodyPut, url_, http_verb, id);

        assertThat(responsePut.statusCode(), equalTo(HttpStatus.OK.value()));
    }


    //TODO: is it always 200 ? or sometimes 201?
    //TODO: put,
    //TODO: list tests
    //TODO: break out code that is redudant, V
    // TODO: more asserts?
    //TODO: negative flows
    //TODO: Readme + assumptions + requriements
    //TODO: github V


     /**
         * http://localhost:8080/restapi/put?id=1&message=changed

      **/

}
