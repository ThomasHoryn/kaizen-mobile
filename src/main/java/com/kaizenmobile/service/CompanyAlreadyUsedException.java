package com.kaizenmobile.service;

public class CompanyAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CompanyAlreadyUsedException() {
        super("Company is already in use!");
    }
}
