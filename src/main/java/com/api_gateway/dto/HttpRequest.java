package com.api_gateway.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest {
    public String method;
    public String path;
    public String version;
    public Map<String, String> headers = new LinkedHashMap<>();
}
