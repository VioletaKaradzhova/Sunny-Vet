package com.sunnyvet.main.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunnyvet.main.domain.dto.PetDto;
import com.sunnyvet.main.exception.ResourceNotFoundException;
import com.sunnyvet.main.service.PetService;
import com.sunnyvet.main.security.JwtService;
import com.sunnyvet.main.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createPet_ValidInput_ReturnsCreatedWithHateoasLinks() throws Exception {
        UUID petId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        PetDto inputDto = new PetDto();
        inputDto.setName("Bella");
        inputDto.setSpecies("Dog");
        inputDto.setAge(3);
        inputDto.setOwnerId(ownerId);

        PetDto outputDto = new PetDto();
        outputDto.setId(petId);
        outputDto.setName("Bella");
        outputDto.setSpecies("Dog");
        outputDto.setAge(3);
        outputDto.setOwnerId(ownerId);

        when(petService.createPet(any(PetDto.class))).thenReturn(outputDto);

        MvcResult result = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertEquals(petId.toString(), jsonNode.get("id").asText());
        assertEquals("Bella", jsonNode.get("name").asText());
        assertNotNull(jsonNode.get("_links").get("self").get("href").asText());
    }

    @Test
    void getPet_NotFound_ReturnsProblemDetail() throws Exception {
        UUID petId = UUID.randomUUID();
        when(petService.getPetById(petId)).thenThrow(new ResourceNotFoundException("Pet not found with ID: " + petId));

        MvcResult result = mockMvc.perform(get("/api/pets/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertEquals("https://api.sunnyvet.com/errors/not-found", jsonNode.get("type").asText());
        assertEquals("Resource Not Found", jsonNode.get("title").asText());
        assertNotNull(jsonNode.get("detail").asText());
        assertNotNull(jsonNode.get("timestamp").asText());
    }

    @Test
    void createPet_InvalidInput_ReturnsValidationProblemDetail() throws Exception {
        PetDto invalidDto = new PetDto();
        invalidDto.setName("");

        MvcResult result = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertEquals("https://api.sunnyvet.com/errors/validation-failed", jsonNode.get("type").asText());
        assertEquals("Validation Error", jsonNode.get("title").asText());
        assertNotNull(jsonNode.get("invalid_fields"));
    }
}