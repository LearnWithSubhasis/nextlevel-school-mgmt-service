package org.nextlevel.school;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for Pagination.
 * It converts the Attendance Entity to the Attendance Model and has the code for it
 */
@Component
public class SchoolModelAssembler extends RepresentationModelAssemblerSupport<School, SchoolModel> {
    public SchoolModelAssembler() {
        super(SchoolController.class, SchoolModel.class);
    }

    @Override
    public SchoolModel toModel(School entity) {
        SchoolModel model = new SchoolModel();
        // Both OrgModel and Organisation have the same property names. So copy the values from the Entity to the Model
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
