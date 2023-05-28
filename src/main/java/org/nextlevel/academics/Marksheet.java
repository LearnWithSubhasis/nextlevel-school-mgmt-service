package org.nextlevel.academics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Marksheet {
    private Long studentId;
    private Long sectionId;

    private int year;
    private String term;
    private float marks;

}
