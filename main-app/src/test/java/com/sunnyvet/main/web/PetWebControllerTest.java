package com.sunnyvet.main.web;

import com.sunnyvet.main.config.UiSecurityConfig;
import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.security.JwtAuthenticationFilter;
import com.sunnyvet.main.security.SecurityConfig;
import com.sunnyvet.main.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        value = PetWebController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {UiSecurityConfig.class, SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
public class PetWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Test
    public void testListPets() throws Exception {
        PetDto petDto = new PetDto();
        petDto.setId(UUID.randomUUID());
        petDto.setName("Buddy");
        petDto.setSpecies("Dog");
        petDto.setAge(3);
        petDto.setOwnerId(UUID.randomUUID());

        when(petService.getAllPets()).thenReturn(List.of(petDto));

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/list"))
                .andExpect(model().attributeExists("pets"));
    }

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/pets/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/new"))
                .andExpect(model().attributeExists("pet"));
    }

    @Test
    public void testCreatePetSuccess() throws Exception {
        mockMvc.perform(post("/pets")
                        .param("name", "Buddy")
                        .param("species", "Dog")
                        .param("age", "3")
                        .param("ownerId", UUID.randomUUID().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pets"));
    }

    @Test
    public void testCreatePetValidationErrors() throws Exception {
        mockMvc.perform(post("/pets")
                        .param("name", "")
                        .param("species", "")
                        .param("age", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/new"))
                .andExpect(model().attributeHasFieldErrors("pet", "name", "species", "age", "ownerId"));
    }
}