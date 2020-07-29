    public void testDirectory() {
        File dirUpload = new File(this.profile.getUpload_GlobalPath());
        if (!dirUpload.exists()) {
            dirUpload.mkdir();
        }
        File files[] = dirUpload.listFiles();
        File dirs[];
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    dirs = files[i].listFiles();
                    if (dirs != null) {
                        for (int j = 0; j < dirs.length; j++) {
                            dirs[j].delete();
                        }
                    }
                }
                files[i].delete();
            }
        }
        File dirDownload = new File(this.profile.getDownload_GlobalPath());
        if (!dirDownload.exists()) {
            dirDownload.mkdir();
        }
        files = dirDownload.listFiles();
        boolean delete_dir = true;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delete_dir = true;
                    dirs = files[i].listFiles();
                    if (dirs != null) {
                        for (int j = 0; j < dirs.length; j++) {
                            try {
                                if (this.poolDown.isUsed(dirs[j].getPath())) {
                                    delete_dir = false;
                                }
                            } catch (Exception e) {
                                delete_dir = false;
                            }
                        }
                        if (delete_dir) {
                            for (int j = 0; j < dirs.length; j++) {
                                dirs[j].delete();
                            }
                        }
                    }
                }
                files[i].delete();
            }
        }
    }
