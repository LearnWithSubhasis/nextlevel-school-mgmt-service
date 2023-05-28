package org.nextlevel.attendance;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for Pagination.
 * It converts the Attendance Entity to the Attendance Model and has the code for it
 */
@Component
public class AttendanceModelAssembler extends RepresentationModelAssemblerSupport<Attendance, AttendanceModel> {
    public AttendanceModelAssembler() {
        super(AttendanceController.class, AttendanceModel.class);
    }

    @Override
    public AttendanceModel toModel(Attendance entity) {
        AttendanceModel model = new AttendanceModel();
        // Both AttendanceModel and Attendance have the same property names. So copy the values from the Entity to the Model
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
