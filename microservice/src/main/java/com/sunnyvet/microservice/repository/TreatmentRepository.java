package com.sunnyvet.microservice.repository;

import com.sunnyvet.microservice.domain.entity.Treatment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreatmentRepository extends MongoRepository<Treatment, UUID> {
}