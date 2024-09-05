package com.tta.geumgiri.auth.presentation.dto.request;

public record MemberAuthSignInRequest(
    String userId,
    String password
) {

}
