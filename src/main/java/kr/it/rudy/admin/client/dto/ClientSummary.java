package kr.it.rudy.admin.client.dto;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public record ClientSummary(
        String id,
        String clientId,
        String clientName,
        String authorizationGrantTypes,
        String scopes,
        Instant issuedAt
) {
    public List<String> grantTypeList() {
        return splitToList(authorizationGrantTypes);
    }

    public List<String> scopeList() {
        return splitToList(scopes);
    }

    private static List<String> splitToList(String value) {
        if (value == null || value.isBlank()) return List.of();
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
