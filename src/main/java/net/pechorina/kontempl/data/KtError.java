package net.pechorina.kontempl.data;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KtError {

    private String timestamp;

    private Integer status;

    private String error;

    private List<KtObjectError> errors;

    private String exception;

    private String message;

    private String path;

    public KtError() {
        setTimestamp(ZonedDateTime.now().toString());
    }

    private KtError(Builder builder) {
        this();
        if (builder.timestamp != null)
            setTimestamp(builder.timestamp);
        setStatus(builder.status);
        setError(builder.error);
        setErrors(builder.errors);
        setException(builder.exception);
        setMessage(builder.message);
        setPath(builder.path);
    }

    public KtError addError(String objectName, String fieldName, String rejectedValue, String... errorCodes) {
        if (errors == null) errors = new ArrayList<>();
        errors.add(toObjectErrorDTO(new FieldError(objectName, fieldName, rejectedValue, true, errorCodes, null, null)));
        return this;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public KtError setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public KtError setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getError() {
        return error;
    }

    public KtError setError(String error) {
        this.error = error;
        return this;
    }

    public List<KtObjectError> getErrors() {
        return errors;
    }

    public KtError setErrors(List<KtObjectError> errors) {
        this.errors = errors;
        return this;
    }

    public String getException() {
        return exception;
    }

    public KtError setException(String exception) {
        this.exception = exception;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public KtError setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getPath() {
        return path;
    }

    public KtError setPath(String path) {
        this.path = path;
        return this;
    }

    public static final class Builder {
        private String timestamp;
        private Integer status;
        private String error;
        private List<KtObjectError> errors = new ArrayList<>();
        private String exception;
        private String message;
        private String path;

        private Builder() {
        }

        public Builder withTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withTimestamp(ZonedDateTime timestamp) {
            this.timestamp = timestamp.toString();
            return this;
        }

        public Builder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public Builder withFieldErrors(List<FieldError> errors) {
            if (errors != null)
                this.errors.addAll(errors.stream().map(KtError::toObjectErrorDTO).collect(Collectors.toList()));
            return this;
        }

        public Builder withGlobalErrors(List<ObjectError> errors) {
            if (errors != null)
                this.errors.addAll(errors.stream().map(KtError::toObjectErrorDTO).collect(Collectors.toList()));
            return this;
        }

        public Builder withException(String exception) {
            this.exception = exception;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public KtError build() {
            return new KtError(this);
        }
    }

    @Override
    public String toString() {
        return "KtError{" +
            "timestamp=" + timestamp +
            ", status=" + status +
            ", error='" + error + '\'' +
            ", errors=" + errors +
            ", exception='" + exception + '\'' +
            ", message='" + message + '\'' +
            ", path='" + path + '\'' +
            '}';
    }

    public String toErrorString() {
        String msg = status + " " + error + " " + path;
        if (errors != null && !errors.isEmpty())
            msg += " ~> " + errors.stream().map(error -> error.toString()).collect(Collectors.joining(", "));
        return msg;
    }

    public static KtObjectError toObjectErrorDTO(FieldError fieldError) {
        return new KtObjectError()
            .setBindingFailure(fieldError.isBindingFailure())
            .setCode(fieldError.getCode())
            .setCodes(fieldError.getCodes())
            .setDefaultMessage(fieldError.getDefaultMessage())
            .setField(fieldError.getField())
            .setRejectedValue(fieldError.getRejectedValue());
    }

    public static KtObjectError toObjectErrorDTO(ObjectError objectError) {
        return new KtObjectError()
            .setCode(objectError.getCode())
            .setCodes(objectError.getCodes())
            .setDefaultMessage(objectError.getDefaultMessage());
    }

}
