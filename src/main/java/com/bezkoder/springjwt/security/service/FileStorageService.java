package com.bezkoder.springjwt.security.service;

import java.io.IOException;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.FileDB;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.FileDBRepository;
import com.bezkoder.springjwt.persistence.UserRepository;

@Service
public class FileStorageService {

  @Autowired
  private FileDBRepository fileDBRepository;
  
  @Autowired
  private UserRepository userRepository;

  public FileDB store(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
    User user = userRepository.findByEmail("bonguyens2001@gmail.com").orElse(null); 
    FileDB.setUser(user);
    return fileDBRepository.save(FileDB);
  }

  public FileDB getFile(String id) {
    return fileDBRepository.findById(id).get();
  }
  
  public Stream<FileDB> getAllFiles() {
    return fileDBRepository.findAll().stream();
  }
}
