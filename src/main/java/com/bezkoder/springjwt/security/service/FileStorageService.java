package com.bezkoder.springjwt.security.service;

import java.io.IOException;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.FileDB;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.FileDBRepository;

@Service
public class FileStorageService {

  @Autowired
  private FileDBRepository fileDBRepository;
  
  @Autowired
  private CourseRepository courseRepository;

  public FileDB store(MultipartFile file, Integer regId) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
    Course course = courseRepository.findById(regId).orElse(null);
    course.addFile(FileDB);
    courseRepository.save(course);
    return fileDBRepository.save(FileDB);
  }

  public FileDB getFile(String id) {
    return fileDBRepository.findById(id).get();
  }
  
  public Stream<FileDB> getAllFilesById(Integer regId) {
    return fileDBRepository.getAllFilesByCourseId(regId).stream();
  }
}