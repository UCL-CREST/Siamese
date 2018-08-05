    protected void removeFile(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File children[] = file.listFiles();
                for (int i = 0; i < children.length; i++) {
                    File child = children[i];
                    if (!(child.getName().equals(".") || child.getName().equals(".."))) {
                        removeFile(child);
                    }
                }
            }
            file.delete();
        }
    }
