package org.bf2.srs.fleetmanager.rest.publicapi.impl;

import lombok.SneakyThrows;
import org.bf2.srs.fleetmanager.rest.publicapi.ApiResource;
import org.bf2.srs.fleetmanager.rest.publicapi.beans.RegistryCreateRest;
import org.bf2.srs.fleetmanager.rest.publicapi.beans.RegistryListRest;
import org.bf2.srs.fleetmanager.rest.publicapi.beans.RegistryRest;
import org.bf2.srs.fleetmanager.rest.service.RegistryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Jakub Senko <jsenko@redhat.com>
 */
@ApplicationScoped
public class ApiResourceImpl implements ApiResource {

    private static final String SCHEMA;

    static {
        try {
            SCHEMA = new String(ApiResourceImpl.class.getResourceAsStream("/srs-fleet-manager.json").readAllBytes());
        } catch (Exception e) {
            throw new IllegalStateException("Could not load Open API schema for the v1 REST interface.");
        }
    }

    @Inject
    RegistryService registryService;

    @Inject
    Convert convert;

    @Override
    public RegistryListRest getRegistries(@Min(0) Integer page,
                                          @Min(1) @Max(1000) Integer size,
                                          String orderBy, String search) {
        return convert.convert(registryService.getRegistries(page, size, orderBy, search));
    }

    @SneakyThrows
    @Override
    public RegistryRest createRegistry(RegistryCreateRest data) {
        return convert.convert(registryService.createRegistry(convert.convert(data)));
    }

    @SneakyThrows
    @Override
    public RegistryRest getRegistry(String id) {
        return convert.convert(registryService.getRegistry(id));
    }

    @SneakyThrows
    @Override
    public void deleteRegistry(String id) {
        registryService.deleteRegistry(id);
    }

    @Override
    public String getSchema() {
        return SCHEMA;
    }
}
