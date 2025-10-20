package org.example.funkos.exceptions;

public sealed class FunkoException extends RuntimeException {

    public FunkoException(String message) { super(message); }

    public static final class NotFoundException extends FunkoException {
        public NotFoundException(String message) { super(message); }
    }

    public static final class ConflictException extends FunkoException {
        public ConflictException(String message) { super(message); }
    }
}
