package org.stockify.Model.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.Model.Repositories.ProviderRepo;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepo providerRepo;
}
