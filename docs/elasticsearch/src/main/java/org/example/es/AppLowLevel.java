package org.example.es;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * Elasticsearch
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/5.6
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.10.1
 */
public class AppLowLevel {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        // 5.6.4
        // Response response = restClient.performRequest("GET", "/");
        // 7.10.1
        Request request = new Request("GET", "/");
        Response response = restClient.performRequest(request);

        RequestLine requestLine = response.getRequestLine();
        System.out.println(requestLine);

        HttpHost host = response.getHost();
        System.out.println(host);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        Header[] headers = response.getHeaders();
        for (Header header: headers) {
            System.out.println(header);
        }

        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);

        restClient.close();
    }
}
