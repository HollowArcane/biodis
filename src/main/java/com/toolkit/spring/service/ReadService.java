package com.toolkit.spring.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadService<T, E>
{
    public JpaRepository<T, E> getReadRepository();

    public default Page<T> findAll(Pageable pageable)
    { return getReadRepository().findAll(pageable); }

    public default List<T> findAll()
    { return getReadRepository().findAll(); }
}
