package edu.alexu.cse.dripmeup.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;

@Service
public class DatabaseService {

    @Autowired
    private EntityManager entityManager;

    public <T> T saveOrUpdate(T entity) {
        entityManager.merge(entity);
        return entity;
    }

}
