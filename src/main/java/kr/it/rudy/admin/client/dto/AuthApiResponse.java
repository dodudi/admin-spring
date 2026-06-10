package kr.it.rudy.admin.client.dto;

public record AuthApiResponse<T>(String code, String message, T data) {
}
