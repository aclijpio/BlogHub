package com.github.aclijpio.bloghub.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aclijpio.bloghub.exceptions.entity.PostNotFoundException;
import com.github.aclijpio.bloghub.services.UserService;
import com.github.aclijpio.bloghub.services.dtos.user.UserDto;
import com.github.aclijpio.bloghub.services.dtos.user.UserRequest;
import com.github.aclijpio.bloghub.services.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "user", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {


    private final UserService service = new UserServiceImpl();
    private final static ObjectMapper mapper = new ObjectMapper();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureResponse(resp);
        getAllUsers(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        createNewUser(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateUser(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        deleteUser(req, resp);
    }

    private void getAllUsers(HttpServletResponse resp) throws IOException {
        configureResponse(resp);
        List<UserDto> userDtoList = service.getAllUsers();
        mapper.writeValue(resp.getWriter(), userDtoList);
    }

    private void createNewUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserRequest userDto = mapper.readValue(req.getReader(), UserRequest.class);
        service.createUser(userDto);
    }
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureResponse(resp);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserRequest updatedPost = mapper.readValue(req.getReader(), UserRequest.class);
        try {
            service.updateUser(id, updatedPost);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (PostNotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(mapper.writeValueAsString(e.getMessage()));
        }
    }
    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        configureResponse(resp);

        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) {
            service.deleteUser(id);

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean success = service.deleteUser(id);
        resp.setStatus(success ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleGetPostById(HttpServletResponse resp, String action) throws IOException {
        try {
            Long id = Long.parseLong(action);
            UserDto postDto = service.getUserById(id);
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

    private static void configureResponse(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

}
