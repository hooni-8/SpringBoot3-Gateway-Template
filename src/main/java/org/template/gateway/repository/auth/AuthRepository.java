package org.template.gateway.repository.auth;

import org.springframework.stereotype.Repository;
import org.template.gateway.entity.user.UserDetail;
import org.template.gateway.repository.GatewayReactiveCrudRepository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthRepository extends GatewayReactiveCrudRepository<UserDetail, Long> {

    Mono<UserDetail> findByUserId(String userId);

    Mono<UserDetail> findByUserCode(String userCode);

    Mono<Boolean> existsByUserId(String userId);
}
