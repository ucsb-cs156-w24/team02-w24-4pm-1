package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItems;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemsRepository;

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

@WebMvcTest(controllers = UCSBDiningCommonsMenuItemsController.class)
@Import(TestConfig.class)
public class UCSBDiningCommonsMenuItemsControllerTests extends ControllerTestCase {

        @MockBean
        UCSBDiningCommonsMenuItemsRepository ucsbDiningCommonsMenuItemsRepository;

        @MockBean
        UserRepository userRepository;
        // Tests for GET /api/ucsbdiningcommonsmenuitem/all

        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem/all"))
                                .andExpect(status().is(200)); // logged
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_ucsbdiningcommonsmenuitem() throws Exception {

                // arrange

                UCSBDiningCommonsMenuItems carrillo = UCSBDiningCommonsMenuItems.builder()
                                .name("Carrillo")
                                .code("carrillo")
                                .station("carrillo")
                                .build();

                UCSBDiningCommonsMenuItems dlg = UCSBDiningCommonsMenuItems.builder()
                                .name("De La Guerra")
                                .code("de-la-guerra")
                                .station("dlg")
                                .build();

                ArrayList<UCSBDiningCommonsMenuItems> expectedCommonsMenuItems = new ArrayList<>();
                expectedCommonsMenuItems.addAll(Arrays.asList(carrillo, dlg));

                when(ucsbDiningCommonsMenuItemsRepository.findAll()).thenReturn(expectedCommonsMenuItems);

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedCommonsMenuItems);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // Tests for POST /api/ucsbdiningcommonsmenuitem...

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsbdiningcommonsmenuitem/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsbdiningcommonsmenuitem/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_commonsmenuitem() throws Exception {
                // arrange

                UCSBDiningCommonsMenuItems ortega = UCSBDiningCommonsMenuItems.builder()
                                .name("Ortega")
                                .code("ortega")
                                .station("ortega")
                                .build();

                when(ucsbDiningCommonsMenuItemsRepository.save(eq(ortega))).thenReturn(ortega);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/ucsbdiningcommonsmenuitem/post?name=Ortega&code=ortega&station=ortega")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).save(ortega);
                String expectedJson = mapper.writeValueAsString(ortega);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

/*
        // Tests for GET /api/ucsbdiningcommonsmenuitem?...

        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem?code=carrillo"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange

                UCSBDiningCommonsMenuItems commonsmenuitem = UCSBDiningCommonsMenuItems.builder()
                                .name("Carrillo")
                                .code("carrillo")
                                .hasSackMeal(false)
                                .hasTakeOutMeal(false)
                                .hasDiningCam(true)
                                .latitude(34.409953)
                                .longitude(-119.85277)
                                .build();

                when(ucsbDiningCommonsMenuItemsRepository.findById(eq("carrillo"))).thenReturn(Optional.of(commonsmenuitem));

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitem?code=carrillo"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).findById(eq("carrillo"));
                String expectedJson = mapper.writeValueAsString(commonsmenuitem);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // Tests for DELETE /api/ucsbdiningcommonsmenuitem?...

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_delete_a_date() throws Exception {
                // arrange

                UCSBDiningCommonsMenuItems portola = UCSBDiningCommonsMenuItems.builder()
                                .name("Portola")
                                .code("portola")
                                .hasSackMeal(true)
                                .hasTakeOutMeal(true)
                                .hasDiningCam(true)
                                .latitude(34.417723)
                                .longitude(-119.867427)
                                .build();

                when(ucsbDiningCommonsMenuItemsRepository.findById(eq("portola"))).thenReturn(Optional.of(portola));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbdiningcommonsmenuitem?code=portola")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).findById("portola");
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).delete(any());

                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItems with id portola deleted", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_tries_to_delete_non_existant_commonsmenuitem_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(ucsbDiningCommonsMenuItemsRepository.findById(eq("Entrees"))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbdiningcommonsmenuitem?code=1")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).findById("Entrees");
                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItems with id 1 not found", json.get("message"));
        }

        // Tests for PUT /api/ucsbdiningcommonsmenuitem?...

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_edit_an_existing_commonsmenuitem() throws Exception {
                // arrange

                UCSBDiningCommonsMenuItems carrilloOrig = UCSBDiningCommonsMenuItems.builder()
                                .name("Carrillo")
                                .code("carrillo")
                                .hasSackMeal(false)
                                .hasTakeOutMeal(false)
                                .hasDiningCam(true)
                                .latitude(34.409953)
                                .longitude(-119.85277)
                                .build();

                UCSBDiningCommonsMenuItems carrilloEdited = UCSBDiningCommonsMenuItems.builder()
                                .name("Carrillo Dining Hall")
                                .code("carrillo")
                                .hasSackMeal(true)
                                .hasTakeOutMeal(true)
                                .hasDiningCam(false)
                                .latitude(34.409954)
                                .longitude(-119.85278)
                                .build();

                String requestBody = mapper.writeValueAsString(carrilloEdited);

                when(ucsbDiningCommonsMenuItemsRepository.findById(eq("carrillo"))).thenReturn(Optional.of(carrilloOrig));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ucsbdiningcommonsmenuitem?code=carrillo")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).findById("carrillo");
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).save(carrilloEdited); // should be saved with updated info
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }


        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_cannot_edit_commonsmenuitem_that_does_not_exist() throws Exception {
                // arrange

                UCSBDiningCommonsMenuItems editedCommonsMenuItems = UCSBDiningCommonsMenuItems.builder()
                                .name("Munger Hall")
                                .code("Entrees")
                                .hasSackMeal(false)
                                .hasTakeOutMeal(false)
                                .hasDiningCam(true)
                                .latitude(34.420799)
                                .longitude(-119.852617)
                                .build();

                String requestBody = mapper.writeValueAsString(editedCommonsMenuItems);

                when(ucsbDiningCommonsMenuItemsRepository.findById(eq("Entrees"))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ucsbdiningcommonsmenuitem?code=1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemsRepository, times(1)).findById("Entrees");
                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItems with id 1 not found", json.get("message"));

        }*/
}
