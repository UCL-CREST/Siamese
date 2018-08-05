    private void deleteFiles(File root) {
        String fileMasks = DataStore.getInstance().getProperty("filebrowser.masks");
        File[] files = root.listFiles(new FileTypeFilter(fileMasks));
        if (files == null) return;
        for (int x = 0; x < files.length; x++) {
            if (files[x].isDirectory() && files[x].isHidden() == false) deleteFiles(files[x]);
            if (files[x].isHidden() == false) {
                System.out.println("Deleting File : " + files[x].getAbsolutePath());
                files[x].delete();
            }
        }
    }
