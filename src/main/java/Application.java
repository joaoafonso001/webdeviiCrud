package main.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.domain.model.Post;

public class Application {
    ObjectMapper mapper = new ObjectMapper();

   
    public String writeObjectAsStringJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

  
    public Post fetchPost(Integer id) throws IOException {
        URL url = URI.create("https://jsonplaceholder.typicode.com/posts/" + id).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (var in = con.getInputStream()) {
            return mapper.readValue(in, Post.class);
        } finally {
            con.disconnect();
        }
    }

  
    public int createPost(Post post) throws IOException {
        URL url = URI.create("https://jsonplaceholder.typicode.com/posts").toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = con.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
            writer.write(mapper.writeValueAsString(post));
            writer.flush();
        }

        con.connect();
        return con.getResponseCode();
    }


    public int updatePost(int postId, Post post) throws IOException {
        URL url = URI.create("https://jsonplaceholder.typicode.com/posts/" + postId).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = con.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
            writer.write(mapper.writeValueAsString(post));
            writer.flush();
        }

        con.connect();
        return con.getResponseCode();
    }


    public int deletePost(int postId) throws IOException {
        URL url = URI.create("https://jsonplaceholder.typicode.com/posts/" + postId).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

        con.connect();
        return con.getResponseCode();
    }

    public static void main(String[] args) throws IOException {
        Application app = new Application();

       
        Post post = app.fetchPost(10);
        System.out.println("GET Post:");
        System.out.println(app.writeObjectAsStringJson(post));

       
        Post novoPost = new Post(101, 12345, "Teste title", "teste body");
        int statusCodeCreate = app.createPost(novoPost);
        System.out.println("Status code (Create): " + statusCodeCreate);

        
        novoPost.setTitle("Título atualizado");
        int statusCodeUpdate = app.updatePost(101, novoPost);
        System.out.println("Status code (Update): " + statusCodeUpdate);

        
        int statusCodeDelete = app.deletePost(101);
        System.out.println("Status code (Delete): " + statusCodeDelete);
    }
}
