package com.batch.valuation.model;

import lombok.Data;

@Data
public class ServerResponse {
    public ServerResponse(String response) {
        this.response = response;
    }

    private String response;
}
