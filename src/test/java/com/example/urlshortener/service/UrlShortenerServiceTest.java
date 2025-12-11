package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Test
    void shortenUrl_ShouldReturnShortCode() {
        // Arrange
        String originalUrl = "https://www.google.com";
        Url savedUrl = Url.builder().id(10000L).originalUrl(originalUrl).build();

        when(urlRepository.save(any(Url.class))).thenReturn(savedUrl);

        // Act
        String shortCode = urlShortenerService.shortenUrl(originalUrl);

        // Assert
        assertNotNull(shortCode);
        assertEquals("2bI", shortCode); // 10000 in Base62 is 2bI
        verify(urlRepository, times(2)).save(any(Url.class)); // Saved once for ID, once for shortCode
    }

    @Test
    void shortenUrl_ShouldAddProtocol_WhenMissing() {
        // Arrange
        String originalUrl = "www.google.com";
        String expectedUrl = "http://www.google.com";
        Url savedUrl = Url.builder().id(10000L).originalUrl(expectedUrl).build();

        when(urlRepository.save(any(Url.class))).thenReturn(savedUrl);

        // Act
        String shortCode = urlShortenerService.shortenUrl(originalUrl);

        // Assert
        assertNotNull(shortCode);
        verify(urlRepository, atLeastOnce()).save(argThat(url -> url.getOriginalUrl().equals(expectedUrl)));
    }

    @Test
    void getOriginalUrl_ShouldReturnUrl_WhenExists() {
        // Arrange
        String shortCode = "2Bi";
        String originalUrl = "https://www.google.com";
        Url url = Url.builder().id(10000L).originalUrl(originalUrl).shortCode(shortCode).build();

        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));

        // Act
        String result = urlShortenerService.getOriginalUrl(shortCode);

        // Assert
        assertEquals(originalUrl, result);
    }

    @Test
    void getOriginalUrl_ShouldThrowException_WhenNotFound() {
        // Arrange
        String shortCode = "invalid";
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> urlShortenerService.getOriginalUrl(shortCode));
    }
}
