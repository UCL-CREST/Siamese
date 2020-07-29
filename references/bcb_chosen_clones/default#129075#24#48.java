    protected void flatten(File dir) {
        assert dir.isDirectory();
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File subfile = files[i];
            if (subfile.isFile()) {
                continue;
            } else {
                flatten(subfile);
                File[] subfiles = subfile.listFiles();
                for (int j = 0; j < subfiles.length; j++) {
                    File f = subfiles[j];
                    File temp = new File(subfile.getParent() + '\\' + subfile.getName() + '_' + f.getName());
                    if (f.renameTo(temp) == false) {
                        throw new RuntimeException("Cannot rename: " + f.toString());
                    } else {
                        System.out.println(f.toString() + ". Rename OK!");
                    }
                }
                if (subfile.delete() == false) {
                    throw new RuntimeException("Cannot delete: " + subfile.toString());
                }
            }
        }
    }
