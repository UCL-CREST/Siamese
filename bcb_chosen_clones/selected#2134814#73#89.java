    private boolean copyFile(BackupItem item) {
        try {
            FileChannel src = new FileInputStream(item.getDrive() + ":" + item.getPath()).getChannel();
            createFolderStructure(this.task.getDestinationPath() + "\\" + item.getDrive() + item.getPath());
            FileChannel dest = new FileOutputStream(this.task.getDestinationPath() + "\\" + item.getDrive() + item.getPath()).getChannel();
            dest.transferFrom(src, 0, src.size());
            src.close();
            dest.close();
            Logging.logMessage("file " + item.getDrive() + ":" + item.getPath() + " was backuped");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
