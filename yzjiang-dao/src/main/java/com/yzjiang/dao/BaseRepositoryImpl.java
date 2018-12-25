package com.yzjiang.dao;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * DAO基础
 * @author yzjiang
 * @description
 * @date 2018/10/11 0011 11:18
 */
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {
    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }
}
