package ir.maktab.onlineexam.base.repositories.Impl;

import ir.maktab.onlineexam.base.repositories.BasePersonRepository;
import ir.maktab.onlineexam.domains.Person;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class BasePersonRepositoryImpl<E extends Person> extends BaseRepositoryImpl<E, Long>
        implements BasePersonRepository<E> {

    public BasePersonRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }
}
