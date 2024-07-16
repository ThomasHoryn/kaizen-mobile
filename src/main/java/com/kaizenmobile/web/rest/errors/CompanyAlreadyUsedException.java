package com.kaizenmobile.web.rest.errors;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class CompanyAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CompanyAlreadyUsedException() {
        super(ErrorConstants.COMPANY_ALREADY_USED_TYPE, "Company is already in use!", "userManagement", "companyexists");
    }
}
