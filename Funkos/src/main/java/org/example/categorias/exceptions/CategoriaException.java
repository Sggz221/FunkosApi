package org.example.categorias.exceptions;

public sealed class CategoriaException extends RuntimeException {

    public CategoriaException(String message) { super(message); }

    public static final class NotFoundException extends CategoriaException {
        public NotFoundException(String message) { super(message); }
    }

    public static final class ConflictException extends CategoriaException {
        public ConflictException(String message) { super(message); }
    }
}
