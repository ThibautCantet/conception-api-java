package fr.soat.training.api.superhero.web.responses;

public class ExceptionResponse {

    private final String message;

    private final Object details;

    public ExceptionResponse(final String message, final Object details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public Object getDetails() {
        return details;
    }
}
