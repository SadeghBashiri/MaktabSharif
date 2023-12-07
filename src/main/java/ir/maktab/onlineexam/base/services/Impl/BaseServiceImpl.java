package ir.maktab.onlineexam.base.services.Impl;

import ir.maktab.onlineexam.base.repositories.BaseRepository;
import ir.maktab.onlineexam.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BaseServiceImpl<E, PK extends Serializable, Repository extends BaseRepository<E, PK>> implements BaseService<E, PK> {

    @Autowired
   protected Repository baseRepository;


    @Override
    public void closeEntityManger() {
        baseRepository.closeEntityManger();
    }

    @Override
    public E saveOne(E e) {
        return baseRepository.saveOne(e);
    }

    @Override
    public boolean saveMany(List<E> e) {
        return baseRepository.saveMany(e);
    }

    @Override
    public E updateOne(E e) {
        return baseRepository.updateOne(e);
    }

    @Override
    public List<E> updateMany(List<E> e) {
        return baseRepository.updateMany(e);
    }

    @Override
    public boolean deleteById(Class<E> e, PK id) {
        return baseRepository.deleteById(e, id);
    }

    @Override
    public boolean deleteAll(Iterable<? extends E> e) {
        return baseRepository.deleteAll(e);
    }

    @Override
    public boolean deleteOne(E e) {
        return baseRepository.deleteOne(e);
    }

    @Override
    public boolean deleteMany(List<E> e) {
        return baseRepository.deleteMany(e);
    }

    @Override
    public Optional<E> findById(Class<E> e, PK id) {
        return baseRepository.findById(e, id);
    }

    @Override
    public List<E> findAll(Class<E> e) {
        return baseRepository.findAll(e);
    }

    @Override
    public List<E> findAllByIdsIn(Class<E> e, Collection<PK> ids) {
        return baseRepository.findAllByIdsIn(e, ids);
    }
}
