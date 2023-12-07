package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Choice;
import ir.maktab.onlineexam.repositories.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceService {

    private ChoiceRepository choiceRepository;

    @Autowired
    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public Choice save(Choice choice){
        return choiceRepository.save(choice);
    }

    public Choice findChoiceById(Long id){
        return choiceRepository.findById(id).get();
    }

    public void deleteChoiceById(Long id){
        choiceRepository.deleteById(id);
    }
}
