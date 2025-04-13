package com.toolkit.spring.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationService<T, E>
{
    public JpaRepository<T, E> getOperationRepository();

    public default void save(T entity)
    { getOperationRepository().save(entity); }

    public default void deleteById(E id)
    { getOperationRepository().deleteById(id); }
}