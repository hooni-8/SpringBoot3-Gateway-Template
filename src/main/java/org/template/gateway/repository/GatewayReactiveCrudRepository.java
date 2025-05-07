package org.template.gateway.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface GatewayReactiveCrudRepository<T, ID> extends ReactiveCrudRepository<T, ID> {
	public <S extends T> Mono<S> insert(S objectToSave);

	public <S extends T> Mono<S> update(S objectToSave);

}
