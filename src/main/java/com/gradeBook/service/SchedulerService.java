package com.gradeBook.service;

import com.gradeBook.converter.ClazzConverter;
import com.gradeBook.converter.SubjectConverter;
import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.Lesson;
import com.gradeBook.entity.User;
import com.gradeBook.entity.bom.SchedulerBom;
import com.gradeBook.entity.bom.SubjectBom;
import com.gradeBook.repository.LessonRepo;
import com.gradeBook.service.impl.ClazzServiceImpl;
import com.gradeBook.service.impl.SubjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SubjectServiceImpl subjectService;

    private final ClazzServiceImpl clazzService;

    private final ClazzConverter clazzConverter;

    private final SubjectConverter subjectConverter;
    private final UserConverter userConverter;

    private final LessonRepo lessonRepo;
    private final TeacherService teacherService;

    public void save(SchedulerBom schedulerBom) {
        lessonRepo.deleteByClazz(clazzConverter.fromBom(schedulerBom.getClazz()));
        schedulerBom.getDaySchedulerBomList().forEach(map -> {
            if (map.get("name").equals("Subjects")) return;

            final Integer[] orderNumber = {0};
            ((ArrayList<LinkedHashMap<String, Integer>>) map.get("items")).forEach(entry -> {
                Long subjectOid = entry.get("subjectOid") != null ? entry.get("subjectOid").longValue() : null;
                Long teacherOid = entry.get("selectedTeacherOid") != null ? entry.get("selectedTeacherOid").longValue() : null;

                orderNumber[0] = orderNumber[0] + 1;
                if (subjectOid == null) return;
                Lesson lesson = new Lesson();
                lesson.setDayOfWeek(Lesson.DAY_OF_WEEK.valueOf(((String) map.get("name")).toUpperCase()));
                lesson.setOrderNumber(orderNumber[0]);
                lesson.setClazz(clazzConverter.fromBom(schedulerBom.getClazz()));
                lesson.setSubject(subjectService.findById(subjectOid));
                // TODO: 18.12.2022 Need to set Teacher
                lesson.setTeacher(teacherService.findById(teacherOid));
                lessonRepo.save(lesson);
            });
        });
    }

    public SchedulerBom getScheduler(Long classOid) {
        Clazz clazz = clazzService.findClazzById(classOid);
        List<Lesson> lessons = lessonRepo.findByClazz(clazz);
        SchedulerBom schedulerBom = new SchedulerBom();
        schedulerBom.setClazz(clazzConverter.toBom(clazz));
        ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("name", "Subjects");
        List<SubjectBom> subjectBoms = subjectService.findAll(true, "");
        subjectBoms.forEach(subjectBom -> {
            subjectBom.setTeachers(teacherService.findBySubjectBom(subjectBom));
        });
        subjectBoms.add(new SubjectBom(null, "Free", "Free", new ArrayList<>(), null));
        map.put("items", subjectBoms);
        list.add(map);
        Arrays.stream(Lesson.DAY_OF_WEEK.values()).toList().forEach(day_of_week -> {
            LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
            map1.put("name", day_of_week.name());
            List<Lesson> filteredList = lessons.stream().filter(lesson -> lesson.getDayOfWeek().equals(day_of_week))
                    .sorted(Comparator.comparing(Lesson::getOrderNumber)).toList();
            SubjectBom[] subjectBomArray;
            subjectBomArray = new SubjectBom[filteredList.size() > 0 ? filteredList.get(filteredList.size() - 1).getOrderNumber() : 0];

            for (int i = 0; i < subjectBomArray.length; i++) {

                int finalI = i;
                Optional<Lesson> optionalLesson = filteredList.stream().filter(lesson -> (lesson.getOrderNumber() - 1 == finalI)).findFirst();
                if (optionalLesson.isEmpty()) {
                    subjectBomArray[i] = new SubjectBom(null, "Free", UUID.randomUUID().toString(), new ArrayList<>(), null);
                } else {
                    Lesson lesson = optionalLesson.get();
                    SubjectBom subjectBom = subjectConverter.toBom(lesson.getSubject());
                    ArrayList<User> users = new ArrayList<>();
                    if (lesson.getTeacher() != null) users.add(lesson.getTeacher());
                    subjectBom.setTeachers(userConverter.toBom(users));
                    subjectBomArray[i] = subjectBom;
                }
            }
            map1.put("items", Arrays.stream(subjectBomArray).toList());
            list.add(map1);
        });

        schedulerBom.setDaySchedulerBomList(list);
        return schedulerBom;
    }

}