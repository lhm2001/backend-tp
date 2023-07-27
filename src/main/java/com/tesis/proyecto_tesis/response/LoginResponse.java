package com.tesis.proyecto_tesis.response;

public class LoginResponse {
    String message;
    Boolean status;

    Long idUser;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getIdUser() {
        return idUser;
    }

    public LoginResponse setIdUser(Long idUser) {
        this.idUser = idUser;
        return this;
    }

    public LoginResponse(String message, Boolean status,Long idUser) {
        this.message = message;
        this.status = status;
        this.idUser=idUser;
    }
}
