package com.docmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocManagerApplication {

  public static void main(String[] args) {
    System.out.println("ENV >> " + System.getenv("JASYPT_ENCRYPTOR"));
    SpringApplication.run(DocManagerApplication.class, args);
  }

}
