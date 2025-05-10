package com.ssi.ms.collecticase.factorybean;

public interface ResponseFactory<T> {
    ResponseTypes getType();
    T createResponse();
}
