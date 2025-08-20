package main.java;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.domain.model.Post;

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations/2.19.2
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core/2.19.2
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind/2.19.2

public class Application {
    public static void main(String[] args) throws IOException {
        URL url = URI.create("https://jsonplaceholder.typicode.com/posts/1").toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Jackson
        ObjectMapper mapper = new ObjectMapper();

        try (var in = con.getInputStream()) {
            Post post = mapper.readValue(in, Post.class);
            System.out.println(post);               
            System.out.println(post.getTitle());
        } finally {
            con.disconnect();
        }
    }
}
