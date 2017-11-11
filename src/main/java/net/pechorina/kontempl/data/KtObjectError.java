package net.pechorina.kontempl.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KtObjectError {

    private String[] codes;
    private String defaultMessage;
    private String objectName;
    private String field;
    private Object rejectedValue;
    private Boolean bindingFailure;
    private String code;

    public String[] getCodes() {
        return codes;
    }

    public KtObjectError setCodes(String[] codes) {
        this.codes = codes;
        return this;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public KtObjectError setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public String getObjectName() {
        return objectName;
    }

    public KtObjectError setObjectName(String objectName) {
        this.objectName = objectName;
        return this;
    }

    public String getField() {
        return field;
    }

    public KtObjectError setField(String field) {
        this.field = field;
        return this;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public KtObjectError setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
        return this;
    }

    public Boolean getBindingFailure() {
        return bindingFailure;
    }

    public KtObjectError setBindingFailure(Boolean bindingFailure) {
        this.bindingFailure = bindingFailure;
        return this;
    }

    public String getCode() {
        return code;
    }

    public KtObjectError setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return objectName + "." + field + ": " + code + ", rejected:{" + rejectedValue + "}";
    }
}
