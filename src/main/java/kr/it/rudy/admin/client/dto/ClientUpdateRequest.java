package kr.it.rudy.admin.client.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ClientUpdateRequest {

    @NotBlank
    private String clientName;

    private String loginPageUri = "";

    private Set<String> grantTypes = new HashSet<>();

    private Set<String> scopes = new HashSet<>();

    private String redirectUrisRaw = "";

    private String postLogoutRedirectUrisRaw = "";

    private boolean requirePkce;

    @Min(1)
    @Max(1440)
    private int accessTokenTtlMinutes = 60;

    @Min(1)
    @Max(365)
    private int refreshTokenTtlDays = 30;
}
