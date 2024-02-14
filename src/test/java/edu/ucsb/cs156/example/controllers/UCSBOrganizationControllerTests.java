package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBOrganization;
import edu.ucsb.cs156.example.repositories.UCSBOrganizationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBOrganizationController.class)
@Import(TestConfig.class)
public class UCSBOrganizationControllerTests extends ControllerTestCase { 

        @MockBean
        UCSBOrganizationRepository ucsbOrgsRepository;

        @MockBean
        UserRepository userRepository;

        // Tests for GET /api/UCSBOrganization/all
        
        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsborganization/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsborganization/all"))
                                .andExpect(status().is(200)); // logged
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_ucsborgs() throws Exception {

                // arrange

                UCSBOrganization TEST = UCSBOrganization.builder()
                                .orgCode("TEST")
                                .orgTranslationShort("TEST")
                                .orgTranslation("TEST")
                                .inactive(false)
                                .build();


                UCSBOrganization TEST2 = UCSBOrganization.builder()
                .orgCode("TEST2")
                .orgTranslationShort("TEST2")
                .orgTranslation("TEST2")
                .inactive(true)
                .build();

                ArrayList<UCSBOrganization> expectedOrgs = new ArrayList<>();
                expectedOrgs.addAll(Arrays.asList(TEST, TEST2));

                when(ucsbOrgsRepository.findAll()).thenReturn(expectedOrgs);

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsborganization/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbOrgsRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedOrgs);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // Tests for POST /api/UCSBOrganization/post...

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsborganization/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsborganization/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_ucsborg() throws Exception {
                // arrange

                UCSBOrganization TEST2 = UCSBOrganization.builder()
                                .orgCode("TEST2")
                                .orgTranslationShort("TEST2")
                                .orgTranslation("TEST2")
                                .inactive(true)
                                .build();

                when(ucsbOrgsRepository.save(eq(TEST2))).thenReturn(TEST2);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/ucsborganization/post?orgCode=TEST2&orgTranslationShort=TEST2&orgTranslation=TEST2&inactive=true")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbOrgsRepository, times(1)).save(TEST2);
                String expectedJson = mapper.writeValueAsString(TEST2);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
                //test2
        }

        // Tests for GET /api/ucsborganization?...

        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ucsborganization?orgCode=test5"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange

                UCSBOrganization org = UCSBOrganization.builder()
                        .orgCode("test5")
                        .orgTranslationShort("test5")
                        .orgTranslation("test5")
                        .inactive(true)
                        .build();

                when(ucsbOrgsRepository.findById(eq("test5"))).thenReturn(Optional.of(org));

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsborganization?orgCode=test5"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbOrgsRepository, times(1)).findById(eq("test5"));
                String expectedJson = mapper.writeValueAsString(org);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(ucsbOrgsRepository.findById(eq("test"))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsborganization?orgCode=test"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(ucsbOrgsRepository, times(1)).findById(eq("test"));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("UCSBOrganization with id test not found", json.get("message"));
        }

        // Tests for DELETE /api/ucsborganization?...

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_delete_a_org() throws Exception {
                // arrange

                UCSBOrganization test = UCSBOrganization.builder()
                        .orgCode("test5")
                        .orgTranslationShort("test5")
                        .orgTranslation("test5")
                        .inactive(true)
                        .build();

                when(ucsbOrgsRepository.findById(eq("test5"))).thenReturn(Optional.of(test));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsborganization?orgCode=test5")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbOrgsRepository, times(1)).findById("test5");
                verify(ucsbOrgsRepository, times(1)).delete(any());

                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBOrganization with id test5 deleted", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_tries_to_delete_non_existant_commons_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(ucsbOrgsRepository.findById(eq("test"))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsborganization?orgCode=test")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(ucsbOrgsRepository, times(1)).findById("test");
                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBOrganization with id test not found", json.get("message"));
        }


        //  // Tests for PUT /api/ucsborganization?...

        //  @WithMockUser(roles = { "ADMIN", "USER" })
        //  @Test
        //  public void admin_can_edit_an_existing_commons() throws Exception {
        //          // arrange
 
        //          UCSBOrganization test5Orig = UCSBOrganization.builder()
        //                         .orgCode("test5")
        //                         .orgTranslationShort("test5")
        //                         .orgTranslation("test5")
        //                         .inactive(true)
        //                         .build();
 
        //          UCSBOrganization test5Edited = UCSBOrganization.builder()
        //                         .orgCode("test5-e")
        //                         .orgTranslationShort("test5-e")
        //                         .orgTranslation("test5-e")
        //                         .inactive(false)
        //                         .build();
 
        //          String requestBody = mapper.writeValueAsString(test5Edited);
 
        //          when(ucsbOrgsRepository.findById(eq("test5"))).thenReturn(Optional.of(test5Orig));
 
        //          // act
        //          MvcResult response = mockMvc.perform(
        //                          put("/api/ucsborganization?orgCode=test5")
        //                                          .contentType(MediaType.APPLICATION_JSON)
        //                                          .characterEncoding("utf-8")
        //                                          .content(requestBody)
        //                                          .with(csrf()))
        //                          .andExpect(status().isOk()).andReturn();
 
        //          // assert
        //          verify(ucsbOrgsRepository, times(1)).findById("test5");
        //          verify(ucsbOrgsRepository, times(1)).save(test5Edited); // should be saved with updated info
        //          String responseString = response.getResponse().getContentAsString();
        //          assertEquals(requestBody, responseString);
        //  }
 
 
        //  @WithMockUser(roles = { "ADMIN", "USER" })
        //  @Test
        //  public void admin_cannot_edit_commons_that_does_not_exist() throws Exception {
        //          // arrange
 
        //          UCSBOrganization editedOrg = UCSBOrganization.builder()
        //                         .orgCode("test6")
        //                         .orgTranslationShort("test6")
        //                         .orgTranslation("test6")
        //                         .inactive(true)
        //                         .build();
 
        //          String requestBody = mapper.writeValueAsString(editedOrg);
 
        //          when(ucsbOrgsRepository.findById(eq("test6"))).thenReturn(Optional.empty());
 
        //          // act
        //          MvcResult response = mockMvc.perform(
        //                          put("/api/ucsborganization?orgCode=test6")
        //                                          .contentType(MediaType.APPLICATION_JSON)
        //                                          .characterEncoding("utf-8")
        //                                          .content(requestBody)
        //                                          .with(csrf()))
        //                          .andExpect(status().isNotFound()).andReturn();
 
        //          // assert
        //          verify(ucsbOrgsRepository, times(1)).findById("test6");
        //          Map<String, Object> json = responseToJson(response);
        //          assertEquals("UCSBOrganization with id test6 not found", json.get("message"));
 
        // }

}