package ir.maktab.onlineexam.services.impl;

import ir.maktab.onlineexam.base.repositories.BasePersonRepository;
import ir.maktab.onlineexam.base.services.Impl.BaseServiceImpl;
import ir.maktab.onlineexam.domains.Person;
import ir.maktab.onlineexam.services.BasePersonService;

public class BasePersonServiceImpl extends BaseServiceImpl<Person, Long, BasePersonRepository<Person>> implements BasePersonService {
    @Override
    public Person saveOne(Person person) {
        return super.saveOne(person);
    }
}
