package com.toolkit.spring.service.interfaces;

import java.util.Map;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.interfaces.IItem;
import com.toolkit.spring.util.Data;

public interface OptionService<T extends IItem<E>, E>
{
    public JpaRepository<T, E> getReadRepository();

    public default Map<E, String> options()
    { return Data.asMap(getReadRepository().findAll(), IItem::getId, IItem::getLabel); }

    public default Map<E, T> indexed()
    { return Data.asMap(getReadRepository().findAll(), IItem::getId, Function.identity()); }
}
