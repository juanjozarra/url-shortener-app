package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenRequest;
import com.example.urlshortener.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shortenUrl_ShouldReturnShortUrl() throws Exception {
        // Arrange
        String originalUrl = "https://www.google.com";
        String shortCode = "2Bi";
        ShortenRequest request = new ShortenRequest(originalUrl);

        when(urlShortenerService.shortenUrl(originalUrl)).thenReturn(shortCode);

        // Act & Assert
        mockMvc.perform(post("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(org.hamcrest.Matchers.containsString(shortCode)));
    }

    @Test
    void redirectToOriginalUrl_ShouldRedirect_WhenFound() throws Exception {
        // Arrange
        String shortCode = "2Bi";
        String originalUrl = "https://www.google.com";

        when(urlShortenerService.getOriginalUrl(shortCode)).thenReturn(originalUrl);

        // Act & Assert
        mockMvc.perform(get("/{shortCode}", shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }
}
