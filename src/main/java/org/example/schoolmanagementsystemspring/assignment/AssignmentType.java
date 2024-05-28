package org.example.schoolmanagementsystemspring.assignment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
public enum AssignmentType {
    HOMEWORK("30%"),
    QUIZ("20%"),
    TEST("50%")
    ;

    private final String weight;

    AssignmentType(String weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return Double.parseDouble(weight.replace("%", "")) / 100;
    }
}
// nota curso: nota = (soma das notas do assigment / quantidade de assigments) x peso + ....