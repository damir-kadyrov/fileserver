package home.test.application.fileserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: damir
 * Date: 02.06.13
 * Time: 21:52
 */
public class FileNode implements Comparable<FileNode> {
    private List<File> files = new ArrayList<File>();
    private List<FileNode> dirs = new ArrayList<FileNode>();
    private File rootDir;

    public FileNode(String path) {
        rootDir = new File(path);
        if(rootDir.listFiles()!= null){
            for (File file: rootDir.listFiles()){
                if(file.isDirectory()){
                    addDir(file.getPath());
                } else {
                    addFile(file);
                }
            }
        }
    }

    public File getRootDir() {
        return rootDir;
    }

    public String getPath() {
        return rootDir.getName();
    }

    public void addDir(String dir){
        dirs.add(new FileNode(dir));
    }

    public void addFile(File file){
        files.add(file);
    }

    public List<File> getFiles() {
        return files;
    }

    public List<FileNode> getDirs() {
        return dirs;
    }

    @Override
    public String toString() {
        return rootDir.getName();
    }

    @Override
    public int compareTo(FileNode o) {
        return rootDir.getAbsolutePath().compareTo(o.rootDir.getAbsolutePath());
    }
}
