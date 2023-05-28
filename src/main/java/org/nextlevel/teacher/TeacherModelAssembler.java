package org.nextlevel.teacher;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for Pagination.
 * It converts the Attendance Entity to the Attendance Model and has the code for it
 */
@Component
public class TeacherModelAssembler extends RepresentationModelAssemblerSupport<Teacher, TeacherModel> {
    public TeacherModelAssembler() {
        super(TeacherController.class, TeacherModel.class);
    }

    @Override
    public TeacherModel toModel(Teacher entity) {
        TeacherModel model = new TeacherModel();
        // Both AttendanceModel and Attendance have the same property names. So copy the values from the Entity to the Model
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
