package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.dto.GradeDto;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.Grade;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.Teacher;
import com.bezkoder.springjwt.models.Transcript;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AnnouncementRequest;
import com.bezkoder.springjwt.payload.request.GradeRequest;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;
import com.bezkoder.springjwt.persistence.TeacherRepository;
import com.bezkoder.springjwt.persistence.TranscriptRepository;
import com.bezkoder.springjwt.persistence.UserRepository;
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;

import io.jsonwebtoken.lang.Collections;

@Service
public class TeacherService implements ITeacherService {

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserCourseService userCourseService;

	@Autowired
	private StudentCourseRepository studentCourseRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private TranscriptRepository transcriptRepository;

	@Override
	public List<StudentCourse> dropClasses(List<User> users, Integer regId) {
		List<StudentCourseId> userCourseIds = new ArrayList<StudentCourseId>();
		List<StudentCourse> userCourses = new ArrayList<StudentCourse>();
		Course course = courseService.findCourseById(regId);
		users.forEach(user -> {
			StudentCourseId userCourseId = new StudentCourseId(user.getId(), regId);
			userCourseIds.add(userCourseId);
		});

		userCourseIds.forEach(e -> {
			StudentCourse userCourse = userCourseService.findById(e);
			userCourses.add(userCourse);
		});

		userCourses.forEach(e -> {
			if (e.getCourse().getWaitlist() <= 0) {
				List<StudentCourse> afterRankList = studentCourseRepository.findAfterRankOfClass(e.getWaitlistedRank(), e.getCourse().getRegId());
				afterRankList.forEach(studentCourse -> {
					studentCourse.setWaitlistedRank(studentCourse.getWaitlistedRank() - 1);
				});
				studentCourseRepository.saveAll(afterRankList);
				course.setAvailable(e.getCourse().getAvailable() + 1);
				courseService.save(course);
				userCourseService.delete(e);
			} else {
				List<StudentCourse> afterRankList = studentCourseRepository.findAfterRankOfClass(e.getWaitlistedRank(), e.getCourse().getRegId());
				afterRankList.forEach(studentCourse -> {
					studentCourse.setWaitlistedRank(studentCourse.getWaitlistedRank() - 1);
					if (studentCourse.getWaitlistedRank() <= e.getCourse().getCapacity()) {
						studentCourse.setUserCourseStatus(StudentCourseStatus.Successfull);
					}
				});
				studentCourseRepository.saveAll(afterRankList);
				course.setWaitlist(e.getCourse().getWaitlist() - 1);
				courseService.save(course);
				userCourseService.delete(e);

			}

		});
		return userCourses;

	}

	@Override
	public Teacher findTeacherByUser(User user) {
		Teacher teacher = teacherRepository.findTeacherByUser(user.getId());
		return teacher;
	}

	public Grade switchCase(Character letter) {
		Grade grade = null;
		switch (letter) {
		case 'A':
			grade = Grade.A;
			break;
		case 'B':
			grade = Grade.B;
			break;
		case 'C':
			grade = Grade.C;
			break;
		case 'D':
			grade = Grade.D;
			break;
		case 'F':
			grade = Grade.F;
			break;
		default:
			break;
		}
		return grade;
	}

	@Override
	public void grade(GradeRequest gradeRequest) {
		User user = userService.findByUsername(gradeRequest.getUserName());
		Course course = courseService.findCourseById(gradeRequest.getRegId());
		// current Grade
		Grade letter = switchCase(gradeRequest.getLetter());
		// final Grade
		Grade finalGrade;
		try {
			finalGrade = switchCase(gradeRequest.getFinalGrade());
		} catch (Exception e) {
			finalGrade = null;
		}
		if (finalGrade == null) {
			userCourseService.update(letter, gradeRequest.getPercentage(), user, course);
		} else {
			userCourseService.lastUpdate(letter, gradeRequest.getPercentage(), finalGrade, user, course);
			if (finalGrade == Grade.A || finalGrade == Grade.B || finalGrade == Grade.C) {
				userCourseService.setIsPassedStatus(IsPassed.TRUE, user, course);
			} else {
				userCourseService.setIsPassedStatus(IsPassed.FALSE, user, course);
			}
			int totalEarnedCredits = userService.getTotalEarnedCredits(gradeRequest.getUserName());
			if (totalEarnedCredits > 0) {
				user.setTranscript(null);
				userService.save(user);
				int totalGradePoints = userService.getCorrespondingTotalGradePoints(gradeRequest.getUserName());
				double cumulativegpa = (double) totalGradePoints / totalEarnedCredits;
				Transcript transcript = new Transcript(user, totalEarnedCredits, totalGradePoints, cumulativegpa);
				transcriptRepository.save(transcript);
			}
		}
	}

	@Override
	public List<GradeDto> getAllStudentsByCourseForGrading(Integer regId) {
		List<StudentCourse> gradedStudents = userCourseService.findAllStudentsByCourse(regId);
		List<GradeDto> gradeDtos = new ArrayList<>();
		gradedStudents.forEach(student -> {
			Character finalGrade;
			try {
				finalGrade = student.getFinalGrade().toString().charAt(0);
			} catch (Exception e) {
				finalGrade = null;
			}
			GradeDto gradeDto;
			try {
				gradeDto = new GradeDto(student.getUser().getUsername(), student.getGrade().toString().charAt(0),
						student.getPercentage(), finalGrade, student.getCourse().getTerm().getSemester().toString()
								+ " " + String.valueOf(student.getCourse().getTerm().getYear()));
			} catch (Exception e) {
				gradeDto = new GradeDto(student.getUser().getUsername(), null, null, finalGrade,
						student.getCourse().getTerm().getSemester().toString() + " "
								+ String.valueOf(student.getCourse().getTerm().getYear()));
			}
			gradeDtos.add(gradeDto);
		});
		return gradeDtos;
	}

	@Override
	public GradeDto findGradedStudentByName(Integer regId, String studentName) {
		User user = userService.findByUsername(studentName);
		if (user == null) {
			throw new ResourceNotFoundException("Error: Could not find any student by the given info !!!");
		}
		Course course = courseService.findCourseById(regId);
		StudentCourse student = userCourseService.findById(new StudentCourseId(user.getId(), course.getRegId()));
		Character finalGrade;
		try {
			finalGrade = student.getFinalGrade().toString().charAt(0);
		} catch (Exception e) {
			finalGrade = null;
		}
		GradeDto gradeDto;
		try {
			gradeDto = new GradeDto(student.getUser().getUsername(), student.getGrade().toString().charAt(0),
					student.getPercentage(), finalGrade, student.getCourse().getTerm().getSemester().toString() + " "
							+ String.valueOf(student.getCourse().getTerm().getYear()));
		} catch (Exception e) {
			gradeDto = new GradeDto(student.getUser().getUsername(), null, null, null,
					student.getCourse().getTerm().getSemester().toString() + " "
							+ String.valueOf(student.getCourse().getTerm().getYear()));
		}
		return gradeDto;
	}

	@Override
	public void sendAnnouncementToAllStudents(AnnouncementRequest announcementRequest) {
		User user = userService.getCurrentLoggedUser();
		List<User> users = userService.findAllUsers();
		List<String> emaiList = users.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		String[] receiptAdresses = emaiList.toArray(new String[emaiList.size()]);

		String content = announcementRequest.getContent();
		eventPublisher.publishEvent(new OnAnnouncementEvent(user, receiptAdresses, content));
	}

}
