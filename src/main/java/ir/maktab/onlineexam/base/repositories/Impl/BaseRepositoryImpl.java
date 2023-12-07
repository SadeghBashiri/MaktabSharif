package ir.maktab.onlineexam.base.repositories.Impl;

import ir.maktab.onlineexam.base.domains.BaseEntity;
import ir.maktab.onlineexam.base.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BaseRepositoryImpl<E extends BaseEntity<PK>, PK extends Serializable> implements BaseRepository<E, PK> {

    @PersistenceContext
    private final EntityManager entityManager;


    public BaseRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void closeEntityManger() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Override
    public E saveOne(E e) {
        try {
            entityManager.persist(e);
            return e;
        } catch (EntityExistsException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return null;
    }

    @Override
    public boolean saveMany(List<E> e) {
        try {
            for (E item : e)
                entityManager.persist(item);
            return true;
        } catch (EntityExistsException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public E updateOne(E e) {
        try {
            if (!entityManager.contains(e))
                entityManager.merge(e);
            return e;
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return null;
    }

    @Override
    public List<E> updateMany(List<E> e) {
        List<E> list = new ArrayList<>();
        try {
            for (E item : e) {
                if (!entityManager.contains(item)) {
                    entityManager.merge(item);
                    list.add(item);
                }
            }
            return list;
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return null;
    }

    @Override
    public Optional<E> findById(Class<E> e, PK id) {
//        return entityManager.getReference(e, id);
//        return entityManager.find(e, id);
        E entity = entityManager.find(e, id);
        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public boolean deleteById(Class<E> e, PK id) {
        try {
            entityManager.remove(entityManager.getReference(e, id));
            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean deleteAll(Iterable<? extends E> e) {
        try {
            for (E entity : e) {
                /*entityManager.createQuery("delete from " + e);*/
                entityManager.remove(entity);
            }
            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean deleteOne(E e) {
        try {
            entityManager.remove(e);
            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean deleteMany(List<E> e) {
        try {
            for (E item : e) {
                entityManager.remove(item);
            }
            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public List<E> findAll(Class<E> e) {
        try {
            TypedQuery<E> query = entityManager.createQuery(
                    "select e from " + e.getSimpleName() + " e", e
            );
            return query.getResultList();
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return null;
    }

    @Override
    public List<E> findAllByIdsIn(Class<E> e, Collection<PK> ids) {
        try {
            TypedQuery<E> query = entityManager.createQuery(
                    "select e from " + e.getSimpleName() + " e where e.id in :ids", e
            );
            return query.getResultList();
        } catch (IllegalArgumentException | RollbackException ex) {
            entityManager.getTransaction().rollback();
        }
        return null;
    }
}
