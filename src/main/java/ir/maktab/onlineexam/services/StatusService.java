package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Status;
import ir.maktab.onlineexam.repositories.StatusRepository;
import ir.maktab.onlineexam.utility.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    private StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status findStatusByTitle(StatusTitle statusTitle) {
        return statusRepository.findStatusByTitle(statusTitle);
    }

    public List<Status> findAllStatus(){
        return statusRepository.findAll();
    }
}
