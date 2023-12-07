package ir.maktab.onlineexam.domains;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@DiscriminatorValue("DetailedQuestion")
public class DetailedQuestion extends Question {

    @Column(nullable = false)
    private String Definition;

    // Getter and Setter
    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String definition) {
        Definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailedQuestion that = (DetailedQuestion) o;
        return Definition.equals(that.Definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Definition);
    }
}
