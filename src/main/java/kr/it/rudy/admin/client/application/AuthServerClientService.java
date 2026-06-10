package kr.it.rudy.admin.client.application;

import kr.it.rudy.admin.client.dto.AuthApiResponse;
import kr.it.rudy.admin.client.dto.ClientCreateRequest;
import kr.it.rudy.admin.client.dto.ClientDetail;
import kr.it.rudy.admin.client.dto.ClientSummary;
import kr.it.rudy.admin.client.dto.ClientUpdateRequest;
import kr.it.rudy.admin.client.dto.SecretRevealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServerClientService {

    private final RestClient authServerRestClient;

    public List<ClientSummary> findAll() {
        AuthApiResponse<List<ClientSummary>> response = authServerRestClient.get()
                .uri("/api/clients")
                .retrieve()
                .body(new ParameterizedTypeReference<AuthApiResponse<List<ClientSummary>>>() {});
        return response != null ? response.data() : List.of();
    }

    public ClientDetail getDetail(String id) {
        AuthApiResponse<ClientDetail> response = authServerRestClient.get()
                .uri("/api/clients/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<AuthApiResponse<ClientDetail>>() {});
        return response != null ? response.data() : null;
    }

    public SecretRevealResponse create(ClientCreateRequest request) {
        AuthApiResponse<SecretRevealResponse> response = authServerRestClient.post()
                .uri("/api/clients")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AuthApiResponse<SecretRevealResponse>>() {});
        return response != null ? response.data() : null;
    }

    public void update(String id, ClientUpdateRequest request) {
        authServerRestClient.put()
                .uri("/api/clients/{id}", id)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(String id) {
        authServerRestClient.delete()
                .uri("/api/clients/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    public SecretRevealResponse regenerateSecret(String id) {
        AuthApiResponse<SecretRevealResponse> response = authServerRestClient.post()
                .uri("/api/clients/{id}/regenerate-secret", id)
                .retrieve()
                .body(new ParameterizedTypeReference<AuthApiResponse<SecretRevealResponse>>() {});
        return response != null ? response.data() : null;
    }
}
