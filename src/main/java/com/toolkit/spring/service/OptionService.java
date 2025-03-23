package com.toolkit.spring.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.ValueObject;
import com.toolkit.spring.util.Data;

public interface OptionService<T extends ValueObject<E>, E>
{
    public JpaRepository<T, E> getReadRepository();

    public default Map<E, String> options()
    { return Data.asMap(getReadRepository().findAll(), ValueObject::getId, ValueObject::getLabel); }

    public default Map<E, T> indexed()
    { return Data.asMap(getReadRepository().findAll(), ValueObject::getId, Function.identity()); }
}
