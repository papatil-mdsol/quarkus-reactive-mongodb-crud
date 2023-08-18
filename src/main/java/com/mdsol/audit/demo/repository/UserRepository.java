package com.mdsol.audit.demo.repository;

import com.mdsol.audit.demo.entity.User;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements ReactivePanacheMongoRepository<User> {
}
