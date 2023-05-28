package org.nextlevel.org;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for Pagination.
 * It converts the Attendance Entity to the Attendance Model and has the code for it
 */
@Component
public class OrgModelAssembler extends RepresentationModelAssemblerSupport<Organisation, OrgModel> {
    public OrgModelAssembler() {
        super(OrgController.class, OrgModel.class);
    }

    @Override
    public OrgModel toModel(Organisation entity) {
        OrgModel model = new OrgModel();
        // Both OrgModel and Organisation have the same property names. So copy the values from the Entity to the Model
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
