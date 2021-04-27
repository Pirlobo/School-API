package com.bezkoder.springjwt.models;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentCourse {
	@EmbeddedId
	private StudentCourseId userCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("id")
	@JsonIgnore
	private User user;

	// Current Grade of the class
	@Enumerated(EnumType.STRING)
	private Grade grade;
	
	// Final Grade of the class
	@Enumerated(EnumType.STRING)
	private Grade finalGrade;

	private Double percentage;

	private Integer waitlistedRank;

	public Integer getWaitlistedRank() {
		return waitlistedRank;
	}

	public Grade getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(Grade finalGrade) {
		this.finalGrade = finalGrade;
	}

	public void setWaitlistedRank(Integer waitlistedRank) {
		this.waitlistedRank = waitlistedRank;
	}

	@Enumerated(EnumType.STRING)
	private IsPassed isPassed;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("regId")
	private Course course;

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public IsPassed getIsPassed() {
		return isPassed;
	}

	public void setIsPassed(IsPassed isPassed) {
		this.isPassed = isPassed;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public StudentCourse() {
		super();
	}

	@Enumerated(EnumType.STRING)
	private StudentCourseStatus userCourseStatus;

	public StudentCourseId getUserCourseId() {
		return userCourseId;
	}

	public void setUserCourseId(StudentCourseId userCourseId) {
		this.userCourseId = userCourseId;
	}

	public StudentCourseStatus getUserCourseStatus() {
		return userCourseStatus;
	}

	public void setUserCourseStatus(StudentCourseStatus userCourseStatus) {
		this.userCourseStatus = userCourseStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public StudentCourse(User user, Course course) {
		super();
		this.user = user;
		this.course = course;
		this.userCourseId = new StudentCourseId(user.getId(), course.getRegId());
	}

}