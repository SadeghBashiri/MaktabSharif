package ir.maktab.onlineexam.domains;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("MultiChoiceQuestion")
public class MultiChoiceQuestion extends Question {

    @Column(nullable = false)
    private String Definition;

    @OneToMany
    private List<Choice> choiceList;

    //Getter and Setter

    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String definition) {
        Definition = definition;
    }

    public List<Choice> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<Choice> choiceList) {
        this.choiceList = choiceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiChoiceQuestion that = (MultiChoiceQuestion) o;
        return Definition.equals(that.Definition) && choiceList.equals(that.choiceList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Definition, choiceList);
    }
}