package com.gradeBook.service;

import com.gradeBook.converter.ClazzConverter;
import com.gradeBook.converter.SubjectConverter;
import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.*;
import com.gradeBook.entity.bom.*;
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
        subjectBoms.add(new SubjectBom(null, "Free", "Free", new ArrayList<>(), null, null, null));
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
                    subjectBomArray[i] = new SubjectBom(null, "Free", UUID.randomUUID().toString(), new ArrayList<>(), null, null, null);
                } else {
                    Lesson lesson = optionalLesson.get();
                    SubjectBom subjectBom = subjectConverter.toBom(lesson.getSubject());
                    ArrayList<User> users = new ArrayList<>();
                    if (lesson.getTeacher() != null) users.add(lesson.getTeacher());
                    subjectBom.setTeachers(userConverter.toBom(users));
                    List<SubjectBom> conflicts = new ArrayList<>();

                    // TODO: 26.12.2022  change SubjectBom to LessonBom for conflicts
                    lessonRepo.findByTeacherAndDayOfWeekAndOrderNumber(lesson.getTeacher(), lesson.getDayOfWeek(), lesson.getOrderNumber()).forEach(lesson1 -> {
                        if (!Objects.equals(lesson1.getOID(), lesson.getOID())) {
                            SubjectBom subjectBom1 = subjectConverter.toBom(lesson1.getSubject());
                            subjectBom1.setClazzWithConflict(clazzConverter.toBom(lesson1.getClazz()));
                            conflicts.add(subjectBom1);
                        }
                    });
                    subjectBom.setConflicts(conflicts);
                    subjectBomArray[i] = subjectBom;
                }
            }
            map1.put("items", Arrays.stream(subjectBomArray).toList());
            list.add(map1);
        });

        schedulerBom.setDaySchedulerBomList(list);
        return schedulerBom;
    }

    public LinkedHashMap<String, List<SchedulerItemBom>> getSchedulerByTeacher(User user) {
        LinkedHashMap<String, List<SchedulerItemBom>> result = new LinkedHashMap<>();
        Teacher teacher = (Teacher) user;
        List<Lesson> lessons = lessonRepo.findByTeacher(teacher);
        Arrays.stream(Lesson.DAY_OF_WEEK.values()).toList().forEach(day_of_week -> {

            List<Lesson> filteredList = lessons.stream().filter(lesson -> lesson.getDayOfWeek().equals(day_of_week))
                    .sorted(Comparator.comparing(Lesson::getOrderNumber)).toList();
            SchedulerItemBom[] schedulerItems;
            schedulerItems = new SchedulerItemBom[filteredList.size() > 0 ? filteredList.get(filteredList.size() - 1).getOrderNumber() : 0];

            for (int i = 0; i < schedulerItems.length; i++) {

                int finalI = i;
                Optional<Lesson> optionalLesson = filteredList.stream().filter(lesson -> (lesson.getOrderNumber() - 1 == finalI)).findFirst();
                if (optionalLesson.isEmpty()) {
                    // TODO: 26.12.2022 Resolve conflicts
                    schedulerItems[i] = new SchedulerItemBom(new SubjectBom(null, "Free", UUID.randomUUID().toString(), new ArrayList<>(), null, null, null), null);
                } else {
                    Lesson lesson = optionalLesson.get();
                    SubjectBom subjectBom = subjectConverter.toBom(lesson.getSubject());
                    ClazzBom clazzBom = clazzConverter.toBom(lesson.getClazz());
                    schedulerItems[i] = new SchedulerItemBom(subjectBom, clazzBom);
                }
            }
            result.put(day_of_week.name(), Arrays.stream(schedulerItems).toList());
        });
        return result;
    }

    public List<FullSchedulerBom> getFullScheduler() {
        List<FullSchedulerBom> result = new ArrayList<>();
        List<Teacher> teachers = teacherService.findAll();

        teachers.forEach(teacher -> {
            FullSchedulerBom fullSchedulerBom = new FullSchedulerBom();
            fullSchedulerBom.setTeacherBom(userConverter.toBom(teacher));

            List<Lesson> lessons = teacher.getLessons();
            List<List<SchedulerItemBom>> listList = new ArrayList<>();
            Arrays.stream(Lesson.DAY_OF_WEEK.values()).toList().forEach(day_of_week -> {
                List<Lesson> filteredList = lessons.stream().filter(lesson -> lesson.getDayOfWeek().equals(day_of_week)).sorted(Comparator.comparing(Lesson::getOrderNumber)).toList();
                // TODO: 26.02.2023  7 - count of lessons per day should be configurable
                for (int i = 1; i < 8; i++) {
                    List<SchedulerItemBom> list = new ArrayList<>();
                    int finalI = i;
                    List<Lesson> l1 = filteredList.stream().filter(lesson -> lesson.getOrderNumber() == finalI).toList();
                    if (l1.isEmpty()) {
                        list.add(new SchedulerItemBom());
                    } else {
                        l1.forEach(lesson -> {
                            list.add(new SchedulerItemBom(
                                    subjectConverter.toBom(lesson.getSubject()),
                                    clazzConverter.toBom(lesson.getClazz())));
                        });

                    }
                    listList.add(list);
                }
            });

            fullSchedulerBom.setItemList(listList);
            result.add(fullSchedulerBom);
        });
        return result;
    }
}
