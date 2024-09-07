package com.tta.geumgiri.common.dto.response;


import lombok.Builder;

public record MessageResponse(
    String message
) {

  @Builder
  public MessageResponse(String message) {
    this.message = message; 
  }
}
