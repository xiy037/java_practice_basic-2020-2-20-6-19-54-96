package com.thoughtworks.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

  /**
   * 完成复制文件夹方法:
   * 1. 把给定文件夹from下的所有文件(包括子文件夹)复制到to文件夹下
   * 2. 保证to文件夹为空文件夹，如果to文件夹不存在则自动创建
   * <p>
   * 例如把a文件夹(a文件夹下有1.txt和一个空文件夹c)复制到b文件夹，复制完成以后b文件夹下也有一个1.txt和空文件夹c
   */
  public static void copyDirectory(File from, File to) throws IOException {
    createIfNotExist(from, to);
    File[] files = from.listFiles();
    if (files != null) {
      for (File file : files) {
        copyDirectory(file, to.toPath().resolve(file.getName()).toFile());
      }
    } else {
      Files.copy(from.toPath(), to.toPath());
    }
  }


  private static void createIfNotExist(File from, File to) throws IOException {
    if (to.exists()) {
      deleteDir(to);
    } else {
      if (from.isDirectory()) {
        to.mkdir();
      }
    }

  }

  private static void deleteDir(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          deleteDir(file);
        } else {
          file.delete();
        }
      }
    }
  }

  /**
   * use Files.walk() to copy files. Files.walk() generates all paths under the source path, including both directory and file.
   * Files.walk() employs depth-first manner during traversal, so it will creates folder first then goes down to its files.
   */
  public static void walkFilesCopy(File from, File to) throws IOException {
    createIfNotExist(from, to);
    Files.walk(from.toPath())
            .filter(src -> src != from.toPath())
            .forEach(src -> {
              try {
                System.out.println(src);
                Files.copy(src, to.toPath().resolve(from.toPath().relativize(src)));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  /**
   * Use Files.walkFileTree() method to traverse `from` file/directory;
   * MyFileVisitor.java was used as FileVisitor, which overrides methods to achieve copy goal.
   */
  public static void walkFileTreeCopy(File from, File to) throws IOException {
    deleteDir(to);
    Files.walkFileTree(from.toPath(), new MyFileVisitor(from, to));
  }
}
