package  home.test.application.fileserver;

import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
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

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void addFile() throws IOException {
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            String fileName = fileNode.getRootDir().getCanonicalPath()+"/testdir.txt";
            File file = new File(fileName);
            if(!file.exists()){
                boolean result = file.createNewFile();
                if(result){
                    init();
                } else {
                    //TODO: alert the user
                }
            }
        }
    }

    public void createDirectory(){
        File file = new File(String.format("%s/%s", parentPath, directoryName));
        System.out.println(String.format("%s/%s", parentPath, directoryName));
        if(!file.exists()){
            boolean result = file.mkdir();
        } else {
            //TODO: alert the user
        }
        init();
    }

    public void showAddDirectoryPanel() throws IOException {
        if(currentSelection!=null && currentSelection instanceof FileNode){
            FileNode fileNode = (FileNode) currentSelection;
            parentPath = fileNode.getRootDir().getCanonicalPath();
        }
    }

    public void actionListener(){
        if(currentSelection!=null && currentSelection instanceof FileNode){
            nodes.iterator().next().addFile(new File("test.txt"));
        }
    }

    public Object getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(Object currentSelection) {
        this.currentSelection = currentSelection;
    }

    public void selectionChanged(TreeSelectionChangeEvent selectionChangeEvent) {
        // considering only single selection
        List<Object> selection = new ArrayList<Object>(selectionChangeEvent.getNewSelection());
        Object currentSelectionKey = selection.get(0);
        UITree tree = (UITree) selectionChangeEvent.getSource();
        Object storedKey = tree.getRowKey();
        tree.setRowKey(currentSelectionKey);
        setCurrentSelection(tree.getRowData());
        tree.setRowKey(storedKey);
    }

    public Set<FileNode> getFileNode(){
        FileNode root = new FileNode(rootPath);
        nodes.add(root);
        return nodes;
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
}

