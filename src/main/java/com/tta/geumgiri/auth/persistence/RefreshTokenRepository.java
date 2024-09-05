package com.tta.geumgiri.auth.persistence;

import com.tta.geumgiri.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByRefreshToken(final String refreshToken);

  Optional<RefreshToken> findById(final Long id);
}
