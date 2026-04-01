package com.sccon.geo.pessoa.adapter.in.web.config;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleRateLimiter {

    private final int capacity;
    private final int refillTokens;
    private final long refillIntervalMillis;

    private AtomicInteger tokens;
    private volatile long lastRefillTime;

    public SimpleRateLimiter(int capacity, int refillTokens, long refillIntervalMillis) {
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillIntervalMillis = refillIntervalMillis;
        this.tokens = new AtomicInteger(capacity);
        this.lastRefillTime = Instant.now().toEpochMilli();
    }

    public synchronized boolean tryConsume() {
        refill();

        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refill() {
        long now = Instant.now().toEpochMilli();
        long elapsed = now - lastRefillTime;

        if (elapsed > refillIntervalMillis) {
            tokens.set(Math.min(capacity, tokens.get() + refillTokens));
            lastRefillTime = now;
        }
    }
}