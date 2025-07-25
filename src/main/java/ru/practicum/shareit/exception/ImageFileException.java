package ru.practicum.shareit.exception;

public class ImageFileException extends RuntimeException {

    public ImageFileException(String s) {
        super(s);
    }

    public ImageFileException(String s, Throwable e) {
        super(s, e);
    }
}
