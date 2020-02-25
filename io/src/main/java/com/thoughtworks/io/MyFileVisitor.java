package com.thoughtworks.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisitor extends SimpleFileVisitor<Path> {
  private Path source;
  private Path target;
  public MyFileVisitor(File from, File to) {
    source = from.toPath();
    target = to.toPath();
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
//    System.out.println("preDirectory: " + dir);
      try {
        Files.copy(dir, target.resolve(source.relativize(dir)));
      } catch (IOException e) {
        System.out.println("ERROR: " +e.getMessage());
      }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
//    System.out.println("visitFile: " + file);
    try{
      Files.copy(file, target.resolve(source.relativize(file)));
    } catch (IOException e) {
      System.out.println("Error: " +e.getMessage());
    }
    return FileVisitResult.CONTINUE;
  }

}
