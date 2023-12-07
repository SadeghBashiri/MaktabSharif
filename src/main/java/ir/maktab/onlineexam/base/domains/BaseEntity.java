package ir.maktab.onlineexam.base.domains;

import java.io.Serializable;

public class BaseEntity<PK extends Serializable> implements Serializable {

    private PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
