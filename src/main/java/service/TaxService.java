package service;

import modelDTO.Tax;

public interface TaxService {

    public Tax getTaxByState(String state);
}