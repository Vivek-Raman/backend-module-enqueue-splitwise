package dev.vivekraman.enqueue.splitwise.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 utility for client-credentials and HMAC-SHA1 signing with Bearer tokens.
 */
public class OAuthUtil {
    private String apiKey;
    private String apiSecret;
    private String oauth2BearerToken;
    private List<Integer> httpErrorCodes = new ArrayList<Integer>() {{
        add(400);
        add(401);
        add(403);
        add(404);
        add(500);
        add(503);
    }};

    public OAuthUtil setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public OAuthUtil setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public String getBearerToken() {
        return this.oauth2BearerToken;
    }

    public Response makeRequest(String url, Verb httpMethod, Object... data) throws Exception {
        java.net.URI uri = java.net.URI.create(url);
        URLConnection conn = uri.toURL().openConnection();
        if (!(conn instanceof HttpURLConnection)) {
            throw new Exception("Invalid protocol for API endpoint");
        }
        HttpURLConnection http = (HttpURLConnection) conn;
        http.setRequestMethod(httpMethod == Verb.POST ? "POST" : "GET");
        http.setRequestProperty("Accept", "application/json");
        if (this.oauth2BearerToken != null) {
            http.setRequestProperty("Authorization", "Bearer " + this.oauth2BearerToken);
        }
        if (httpMethod == Verb.POST && data.length > 0 && data[0] instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> details = (Map<String, String>) data[0];
            StringBuilder form = new StringBuilder();
            for (Map.Entry<String, String> entry : details.entrySet()) {
                if (form.length() > 0) form.append('&');
                form.append(encode(entry.getKey())).append('=').append(encode(entry.getValue()));
            }
            byte[] out = form.toString().getBytes(StandardCharsets.UTF_8);
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.getOutputStream().write(out);
        }
        int code = http.getResponseCode();
        String body;
        try {
            body = new String((code >= 200 && code < 400 ? http.getInputStream() : http.getErrorStream()).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            body = "";
        }
        Response response = new Response(code, body, http.getResponseMessage());
        return parseResponse(response);
    }

    public void requestOAuth2ClientCredentialsToken() throws Exception {
        String body = "grant_type=client_credentials&client_id=" + encode(apiKey)
                + "&client_secret=" + encode(apiSecret);
        java.net.URI tokenUri = java.net.URI.create(dev.vivekraman.enqueue.splitwise.client.URL.OAUTH2_TOKEN);
        URLConnection conn = tokenUri.toURL().openConnection();
        if (!(conn instanceof HttpURLConnection)) {
            throw new Exception("Invalid protocol for token endpoint");
        }
        HttpURLConnection http = (HttpURLConnection) conn;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        byte[] out = body.getBytes(StandardCharsets.UTF_8);
        http.getOutputStream().write(out);
        int code = http.getResponseCode();
        if (code != 200) {
            throw new Exception("Failed to fetch OAuth2 token. HTTP status: " + code);
        }
        String response = new String(http.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String token = parseAccessTokenFromJson(response);
        if (token == null || token.isEmpty()) {
            throw new Exception("OAuth2 token response did not contain access_token");
        }
        this.oauth2BearerToken = token;
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String parseAccessTokenFromJson(String json) {
        int idx = json.indexOf("\"access_token\"");
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx);
        if (colon < 0) return null;
        int startQuote = json.indexOf('"', colon + 1);
        if (startQuote < 0) return null;
        int endQuote = json.indexOf('"', startQuote + 1);
        if (endQuote < 0) return null;
        return json.substring(startQuote + 1, endQuote);
    }

    private Response parseResponse(Response response) throws Exception {
        if (httpErrorCodes.contains(response.getCode())){
            throw new Exception(String.format(
                    "Invalid response received with body - %s, message - %s. Please check your credentials. Response code - %s",
                    response.getBody(),
                    response.getMessage(),
                    response.getCode()));
        }
        return response;
    }
}