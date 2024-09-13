package com.megpbr.security;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Service
public class RateLimitingService {
    private final ConcurrentMap<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Bucket> usernameBuckets = new ConcurrentHashMap<>();

    // Create a bucket with a rate limit of 5 requests per minute
    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1)); // 5 requests per minute
        return Bucket4j.builder().addLimit(limit).build();
    }

    // Get or create a bucket for a specific IP address
    public Bucket getBucketForIpAddress(String ipAddress) {
        return ipBuckets.computeIfAbsent(ipAddress, k -> createBucket());
    }

    // Get or create a bucket for a specific username
    public Bucket getBucketForUsername(String username) {
        return usernameBuckets.computeIfAbsent(username, k -> createBucket());
    }

    // Get available tokens for an IP address
    public long getAvailableTokensForIp(String ipAddress) {
        return getBucketForIpAddress(ipAddress).getAvailableTokens();
    }

    // Get available tokens for a username
    public long getAvailableTokensForUsername(String username) {
        return getBucketForUsername(username).getAvailableTokens();
    }
}
