package org.nextlevel.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DeleteResponse {
    private Long id;
    private String entityType;
    private DeleteStatus status;
    private String deleteMessage;

//
//    public DeleteResponse(Long id, String entityType, HttpStatus status) {
//        this.id = id;
//        this.entityType = entityType;
//        this.status = status;
//    }
}
