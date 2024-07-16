package com.github.aclijpio.bloghub.servlets;

import com.github.aclijpio.bloghub.services.PostService;
import com.github.aclijpio.bloghub.services.PostServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/posts/*")
public class PostServlet extends HttpServlet {

    private final PostService service = new PostServiceImpl();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        super.doGet(req, resp);
    }

    private void newPosts(HttpServletResponse resp){
        service.getPostsFromToday();
    }
    private void weekPosts(HttpServletResponse resp){
        service.getPostsFromWeek();
    }
    private void monthPosts(HttpServletResponse resp){
        service.getPostsFromMonth();
    }
    private void allPosts(HttpServletResponse resp){
        service.getAllPosts();
    }
    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}