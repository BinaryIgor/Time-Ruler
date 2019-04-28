package com.iprogrammerr.time.ruler.model.date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.Instant;

public class ServerClientDates {

    private final String offsetKey;

    public ServerClientDates(String offsetKey) {
        this.offsetKey = offsetKey;
    }

    public ServerClientDates() {
        this("utcOffset");
    }

    public Instant serverDate(HttpServletRequest request, Instant clientDate) {
        return clientDate.plusSeconds(clientUtcOffset(request));
    }

    public int clientUtcOffset(HttpServletRequest request) {
        int offset = 0;
        for (Cookie c : request.getCookies()) {
            if (offsetKey.equals(c.getName())) {
                try {
                    offset = Integer.parseInt(c.getValue());
                    break;
                } catch (Exception e) {
                    offset = 0;
                }
            }
        }
        return offset;
    }

    public Instant clientDate(HttpServletRequest request, Instant serverDate) {
        return serverDate.minusSeconds(clientUtcOffset(request));
    }

    public Instant clientDate(HttpServletRequest request, long serverSeconds) {
        return clientDate(request, Instant.ofEpochSecond(serverSeconds));
    }

    public Instant clientDate(HttpServletRequest request) {
        return clientDate(request, Instant.now(Clock.systemUTC()));
    }
}
