package com.example.favoriteschoolmeal.domain.file.exeption;

import com.example.favoriteschoolmeal.global.exception.BaseException;

public class FileException extends BaseException {

    private final FileExceptionType fileExceptionType;

    public FileException(final FileExceptionType fileExceptionType) {
        this.fileExceptionType = fileExceptionType;
    }

    @Override
    public FileExceptionType exceptionType() {
        return fileExceptionType;
    }

}
