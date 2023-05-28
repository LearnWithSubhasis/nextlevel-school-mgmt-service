//package org.nextlevel.training;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.util.Date;
//import java.util.List;
//
//public interface TrainingRepository extends JpaRepository<Training, Long> {
//    @Query("from Trainings e where not(e.trainingEndTime < :from or e.trainingStartTime > :to)")
//    public List<Training> findBetween(@Param("from") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date start, @Param("to") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date end);
//
//}
