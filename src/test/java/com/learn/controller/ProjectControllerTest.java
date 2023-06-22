package com.learn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learn.dto.*;
import com.learn.enums.Gender;
import com.learn.enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static String token;
    static UserDTO manager;
    static ProjectDTO project;

    @BeforeAll
    static void setUp(){

        //token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUQ2tvTl9QQWxYbHNwZ2w1bUdqeW1CWUNDV1BsX1kyenN4b0g4M2tXaXlnIn0.eyJleHAiOjE2ODc0MDMxMDIsImlhdCI6MTY4NzM4NTEwMiwianRpIjoiMjgxZWJjMWItYmFmYi00YzM1LWI2MTEtOTg5ZGY1YjQ1NjYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJmNTVkOWE4MC1hNGFlLTRlMWItYjY0NS1lMmFmYjYzNjM3ZGIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjM4YjhhODk4LTkzMTgtNDZhYi1hMWFkLThiMDQyN2MxZmNjOCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgxIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1jeWRlby1kZXYiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYXBwIjp7InJvbGVzIjpbIk1hbmFnZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiIzOGI4YTg5OC05MzE4LTQ2YWItYTFhZC04YjA0MjdjMWZjYzgiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoib3p6eSJ9.ELZcZN3RgZWtY9U0cHcglec6UkIP4DXJFJ3YWjyKKh_5RiyQbfCvArm296iM6MORlE-u4sk86kGmTEUPraCmMg5FTIzHia_IypS8jnTQS7FSNv5EqHNN-nbaHHB88i4f1dhX3BSxxXrYQY-tIFD6ET6WJYobUdNCVQNjmOK9EUITYMcdBXmxAF9-SCvMwYAbxz5qwQj0044hnw87BLtLe6SeNaBiRchSOZXduD9iiFcrziEC532_XmLdapop3pIhU64bmU5KgmShz6zbxvyt_7hzC6_5kHvk6ognsDGp09uG6k8315gMbxayrHgDOjBhfNmEVR6OMpPcVGBRjek2bQ";

        token = "Bearer " + getToken();


        manager = new UserDTO(2L,
                "",
                "",
                "ozzy",
                "abc1",
                "",
                true,
                "",
                new RoleDTO(2L, "Manager"),
                Gender.MALE);

        project = new ProjectDTO(
                "API Project",
                "PR001",
                manager,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Some Details",
                Status.OPEN);

    }

    @Test
    void givenNoToken_getProjects() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenToken_getProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").value("ozzy"));

    }

    @Test
    void givenToken_createProject() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/project")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJsonString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project is successfully created"));

    }

    @Test
    void givenToken_updateProject() throws Exception{

        project.setProjectName("API Project-2");

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project is successfully updated"));

    }

    @Test
    void givenToken_deleteProject() throws Exception{

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/project/" + project.getProjectCode())
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project is successfully deleted"));

    }

    private String toJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }

    private static String getToken() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("grant_type", "password");
        map.add("client_id", "ticketing-app");
        map.add("client_secret", "B8WFjmIcHh0Vy7DRnx993HsFFgJNRFBJ");
        map.add("username", "ozzy");
        map.add("password", "abc1");
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<TestResponseDTO> response =
                restTemplate.exchange("http://localhost:8080/auth/realms/cydeo-dev/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        TestResponseDTO.class);

        if (response.getBody() != null) {
            return response.getBody().getAccess_token();
        }

        return "";

    }


}