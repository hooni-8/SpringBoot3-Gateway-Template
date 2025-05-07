package org.template.gateway.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class GatewayReactiveCrudRepositoryImpl<T, ID> extends SimpleR2dbcRepository<T, ID> implements GatewayReactiveCrudRepository<T, ID> {
    private final R2dbcEntityOperations entityOperations;

	public GatewayReactiveCrudRepositoryImpl(RelationalEntityInformation<T, ID> entity, R2dbcEntityOperations entityOperations, R2dbcConverter converter) {
		super(entity, entityOperations, converter);

		this.entityOperations = entityOperations;
	}

    @Override
    @Transactional
    public <S extends T> Mono<S> insert(S objectToSave) {
        Assert.notNull(objectToSave, "Object to save must not be null!");

        return this.entityOperations.insert(objectToSave);
    }

    @Override
    @Transactional
    public <S extends T> Mono<S> update(S objectToSave) {
        Assert.notNull(objectToSave, "Object to save must not be null!");

        return this.entityOperations.update(objectToSave);
    }

}
