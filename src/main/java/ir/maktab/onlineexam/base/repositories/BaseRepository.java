package ir.maktab.onlineexam.base.repositories;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<E, PK extends Serializable> {

    void closeEntityManger();

    E saveOne(E e);

    boolean saveMany(List<E> e);

    E updateOne(E e);

    List<E> updateMany(List<E> e);

    boolean deleteById(Class<E> e, PK id);

    boolean deleteAll(Iterable<? extends E> e);

    boolean deleteOne(E e);

    boolean deleteMany(List<E> e);

    Optional<E> findById(Class<E> e, PK id);

    List<E> findAll(Class<E> e);

    List<E> findAllByIdsIn(Class<E> e, Collection<PK> ids);
}
