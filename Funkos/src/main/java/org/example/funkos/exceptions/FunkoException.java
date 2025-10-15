package org.example.funkos.exceptions;

public sealed class FunkoException extends RuntimeException {

    public FunkoException(String message) { super(message); }

    public static final class NotFoundException extends FunkoException {
        public NotFoundException(String message) { super(message); }
    }

    public static final class InvalidException extends FunkoException {
        public InvalidException(String message) { super(message); }
    }

    public static final class DatabaseException extends FunkoException {
        public DatabaseException(String message) { super(message); }
    }

    public static final class ApiException extends FunkoException {
        public ApiException(String message) { super(message); }
    }
}
