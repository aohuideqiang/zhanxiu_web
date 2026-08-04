package com.example.demo.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SaResult handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return SaResult.error(message).setCode(400);
    }

    @ExceptionHandler(BindException.class)
    public SaResult handleBind(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return SaResult.error(message).setCode(400);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public SaResult handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return SaResult.error(message).setCode(400);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public SaResult handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        return SaResult.error(ex.getMessage()).setCode(400);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public SaResult handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return SaResult.error("请求体格式不正确").setCode(400);
    }

    @ExceptionHandler(SaTokenException.class)
    public SaResult handleSaToken(SaTokenException ex) {
        return SaResult.error(ex.getMessage()).setCode(401);
    }

    @ExceptionHandler(DataAccessException.class)
    public SaResult handleDataAccess(DataAccessException ex) {
        return SaResult.error("数据库操作失败").setCode(500);
    }

    @ExceptionHandler(Exception.class)
    public SaResult handleException(Exception ex) {
        return SaResult.error("系统异常，请稍后再试").setCode(500);
    }
}
