import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storytel.App;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;



public class MyTests {

    private final String url_ = "http://localhost:8080/restapi/";

    @Test
    public void testPositiveFlowPost() throws URISyntaxException, IOException, InterruptedException {
       HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url_+"post?message=test"))
                //.headers("message", "test")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("StatusCode (POST): " + response.statusCode());
        System.out.println("Response Body (POST): " + response.body());

        assertThat(response.statusCode(), equalTo(200));
    }


    @Test
    public void testPositiveFlowPostGet() throws URISyntaxException, IOException, InterruptedException {
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
        System.out.println("Response Body (POST): " + json);

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(new URI(url_+"get?id="+id))
                //.headers("message", "test")
                .GET()
                .build();

        HttpResponse<String> responseGet = HttpClient.newBuilder()
                .build()
                .send(requestGet, HttpResponse.BodyHandlers.ofString());

        System.out.println("StatusCode (GET): " + response.statusCode());
        System.out.println("Response Body (GET): " + response.body());

        assertThat(responseGet.statusCode(), equalTo(200));
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

    // String[] args = {"2", "5", "5", "2,1", "1,2", "2,2", "3,2", "1,3", "3,3", "2,4"};
    // SpringApplication.run(App.class, args);
    // assertEquals();

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
