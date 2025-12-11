package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Transactional
    public String shortenUrl(String originalUrl) {
        // Normalize URL with protocol prefix
        final String normalizedUrl = (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://"))
                ? "http://" + originalUrl
                : originalUrl;

        // Check if URL already exists
        return urlRepository.findByOriginalUrl(normalizedUrl)
                .map(Url::getShortCode)
                .orElseGet(() -> {
                    // URL doesn't exist, create new entry
                    Url url = Url.builder()
                            .originalUrl(normalizedUrl)
                            .build();

                    // Save first to generate ID
                    Url savedUrl = urlRepository.save(url);

                    // Generate short code from ID
                    String shortCode = encodeBase62(savedUrl.getId());
                    savedUrl.setShortCode(shortCode);

                    // Update with short code
                    urlRepository.save(savedUrl);

                    return shortCode;
                });
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public String getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new RuntimeException("URL not found for short code: " + shortCode));
    }

    private String encodeBase62(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.reverse().toString();
    }
}
