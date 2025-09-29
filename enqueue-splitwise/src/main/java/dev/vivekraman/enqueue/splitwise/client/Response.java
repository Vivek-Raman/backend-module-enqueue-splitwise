package dev.vivekraman.enqueue.splitwise.client;

public class Response {
    private final int code;
    private final String body;
    private final String message;

    public Response(int code, String body, String message) {
        this.code = code;
        this.body = body;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }
}


