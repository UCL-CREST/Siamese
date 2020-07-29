    private void deleteFiles(File directory) {
        delete_pressed = true;
        File files = (directory);
        File[] dir = (directory.listFiles());
        if (dir != null) {
            for (File f : dir) {
                if (f.isDirectory()) {
                    deleteFiles(f);
                } else {
                    if (f.isDirectory() == false) {
                        f.delete();
                    }
                }
            }
            File html_file = new File("\\wub\\cats\\" + GlobalVars.getSelected_node() + ".html");
            System.out.println("this is html_file:" + html_file);
            html_file.delete();
            directory.delete();
        } else {
            files.delete();
        }
        System.out.println(" is directory?: " + files.isDirectory());
        System.out.println("    files removed node: " + removed_node);
        System.out.println("    directory: " + directory);
        System.out.println("    files : " + files);
        System.out.println("    parent: " + removed_node.getParent());
        System.out.println("    previous sib: " + removed_node.getPreviousSibling());
        System.out.println("    previous node: " + removed_node.getPreviousNode());
        try {
            treeModel.removeNodeFromParent(removed_node);
        } catch (NullPointerException exc) {
        }
    }
