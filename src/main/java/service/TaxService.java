package service;

import modelDTO.Tax;

public interface TaxService {
    Tax getTaxByState(String state);
}