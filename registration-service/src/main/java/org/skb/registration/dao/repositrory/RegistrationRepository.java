package org.skb.registration.dao.repositrory;

import org.skb.registration.dao.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
