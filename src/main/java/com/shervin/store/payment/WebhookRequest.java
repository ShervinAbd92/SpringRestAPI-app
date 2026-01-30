package com.shervin.store.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class WebhookRequest {
    private Map<String, String> header;
    private String payload;
}

