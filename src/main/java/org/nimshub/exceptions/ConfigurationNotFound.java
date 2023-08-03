package org.nimshub.exceptions;

public class ConfigurationNotFound extends RuntimeException {
    public ConfigurationNotFound() {
        super();
    }

    public ConfigurationNotFound(String message) {
        super(message);
    }
}
