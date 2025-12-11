package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenRequest;
import com.example.urlshortener.dto.ShortenResponse;
import com.example.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "URL Shortener", description = "API for shortening URLs and redirecting")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/api/v1/shorten")
    @Operation(summary = "Shorten a URL", description = "Accepts a long URL and returns a short URL")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenRequest request) {
        String shortCode = urlShortenerService.shortenUrl(request.getUrl());

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String shortUrl = baseUrl + "/" + shortCode;

        return ResponseEntity.ok(new ShortenResponse(shortUrl));
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Redirect to original URL", description = "Redirects the user to the original URL associated with the short code")
    public void redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);
        response.sendRedirect(originalUrl);
    }
}
