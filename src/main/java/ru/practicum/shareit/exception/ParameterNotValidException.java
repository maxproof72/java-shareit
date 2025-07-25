package ru.practicum.shareit.exception;

public class ParameterNotValidException extends IllegalArgumentException {

    private final String parameter;
    private final String reason;

    public ParameterNotValidException(String parameter, String reason) {
        super();
        this.parameter = parameter;
        this.reason = reason;
    }

    public String getParameter() {
        return parameter;
    }

    public String getReason() {
        return reason;
    }
}
