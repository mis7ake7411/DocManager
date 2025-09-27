package com.docmanager.model.dto;

public record LoginReqDTO(String account, String password) {
  @Override
  public String toString() {
    return "LoginReqDTO[account=%s, password=****]".formatted(account);
  }
}
