package  home.test.application.fileserver;

import org.richfaces.component.UITree;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: damir
 * Date: 02.06.13
 * Time: 21:54
 */
@Component
@Scope("session")
public class FileSystemBean {

    private String rootPath = "/1";
    private Object currentSelection = null;
    private Set<FileNode> nodes = new TreeSet<FileNode>();
    private String directoryName;
    private String parentPath;
    private String newName;
    private String oldName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void createDirectory() throws IOException{
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            parentPath = fileNode.getRootDir().getCanonicalPath();
            File file = new File(String.format("%s/%s", parentPath, directoryName));
            if(!file.exists()){
                boolean result = file.mkdir();
            } else {
                //TODO: alert the user
            }
            init();
        }
    }

    public void renameDirectory() throws IOException{
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            parentPath = fileNode.getRootDir().getCanonicalPath();
            File old = new File(String.format("%s", parentPath));
            if(old.exists()){
                boolean result = old.renameTo(new File(String.format("%s/%s", old.getParent(), newName)));
                if(result) oldName = newName;
            } else {
                //TODO: alert the user
            }
            init();
        }
        if(currentSelection!=null && currentSelection instanceof File){
            File file = (File) currentSelection;
            parentPath = file.getCanonicalPath();
            File old = new File(String.format("%s", parentPath));
            if(old.exists()){
                boolean result = old.renameTo(new File(String.format("%s/%s", old.getParent(), newName)));
                if(result) oldName = newName;
            } else {
                //TODO: alert the user
            }
            init();
        }
    }

    public void deleteDirectory() throws IOException{
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            parentPath = fileNode.getRootDir().getCanonicalPath();
            File old = new File(String.format("%s", parentPath));
            if(old.exists()){
                old.delete();
                oldName = newName = "";
            } else {
                //TODO: alert the user
            }
            init();
        }
        if(currentSelection!=null && currentSelection instanceof File){
            File fileNode = (File) currentSelection;
            parentPath = fileNode.getCanonicalPath();
            File old = new File(String.format("%s", parentPath));
            if(old.exists()){
                old.delete();
                oldName = newName = "";
            } else {
                //TODO: alert the user
            }
            init();
        }
    }

    public Object getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(Object currentSelection) {
        this.currentSelection = currentSelection;
    }

    public void selectionChanged(TreeSelectionChangeEvent selectionChangeEvent) throws IOException {
        // considering only single selection
        List<Object> selection = new ArrayList<Object>(selectionChangeEvent.getNewSelection());
        Object currentSelectionKey = selection.get(0);
        UITree tree = (UITree) selectionChangeEvent.getSource();
        Object storedKey = tree.getRowKey();
        tree.setRowKey(currentSelectionKey);
        setCurrentSelection(tree.getRowData());
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            parentPath = fileNode.getRootDir().getCanonicalPath();
            oldName = new File(parentPath).getName();
        } else {
            File file = (File) currentSelection;
            oldName = file.getName();
        }
        tree.setRowKey(storedKey);
    }

    public Set<FileNode> getFileNode(){
        FileNode root = new FileNode(rootPath);
        nodes.add(root);
        return nodes;
    }

    public void reload(){
        init();
        FileNode root = new FileNode(rootPath);
        nodes.add(root);
    }

    private void init(){
        nodes = new TreeSet<FileNode>();

    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getUploadedFile();
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            parentPath = fileNode.getRootDir().getCanonicalPath();
            PrintStream printWriter = new PrintStream(new File(String.format("%s/%s", parentPath, item.getName())));
            printWriter.write(item.getData());
            printWriter.close();
        }
        init();
    }

}

