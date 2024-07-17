package com.github.aclijpio.bloghub.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.aclijpio.bloghub.exceptions.entity.PostNotFoundException;
import com.github.aclijpio.bloghub.services.PostService;
import com.github.aclijpio.bloghub.services.dtos.comment.CommentRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostUpdateRequest;
import com.github.aclijpio.bloghub.services.impl.PostServiceImpl;
import com.github.aclijpio.bloghub.services.dtos.post.PostDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "post", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {

    private final PostService service = new PostServiceImpl();
    private final static ObjectMapper mapper = createStandardObjectMapper();

    private static ObjectMapper createStandardObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureResponse(resp);

        String pathInfo = req.getPathInfo();
        if (isInvalidPath(pathInfo)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String action = extractAction(pathInfo);

        switch (action) {
            case "new" -> respondWithPosts(resp, service.getPostsFromToday());
            case "week" -> respondWithPosts(resp, service.getPostsFromWeek());
            case "month" -> respondWithPosts(resp, service.getPostsFromMonth());
            case "all" -> respondWithPosts(resp, service.getAllPosts());
            default -> handleGetPostById(resp, action);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureResponse(resp);

        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) {
            PostRequest newPost = mapper.readValue(req.getReader(), PostRequest.class);

            service.createPost(newPost);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            CommentRequest commentRequest = mapper.readValue(req.getReader(), CommentRequest.class);

            service.postComment(id, commentRequest);

        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        deletePost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updatePost(req, resp);
    }

    private void updatePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        configureResponse(resp);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PostUpdateRequest updatedPost = mapper.readValue(req.getReader(), PostUpdateRequest.class);
        try {
            service.updatePost(id, updatedPost);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (PostNotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(mapper.writeValueAsString(e.getMessage()));
        }
    }
    private void deletePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        configureResponse(resp);

        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean success = service.deletePost(id);
        resp.setStatus(success ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_FOUND);
    }

    private void respondWithPosts(HttpServletResponse resp, List<PostDto> postDtoList) throws IOException {
        mapper.writeValue(resp.getWriter(), postDtoList);
    }

    private void handleGetPostById(HttpServletResponse resp, String action) throws IOException {
        try {
            Long id = Long.parseLong(action);
            PostDto postDto = service.getPostById(id);
            if (postDto != null) {
                mapper.writeValue(resp.getWriter(), postDto);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (PostNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(mapper.writeValueAsString(e.getMessage()));
        }
    }



    private Long extractIdFromPath(String pathInfo) {
        if (isInvalidPath(pathInfo)) {
            return null;
        }
        try {
            return Long.parseLong(pathInfo.split("/")[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isInvalidPath(String pathInfo) {
        return pathInfo == null || pathInfo.split("/").length < 2;
    }

    private String extractAction(String pathInfo) {
        return pathInfo.split("/")[1];
    }

    private static void configureResponse(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}