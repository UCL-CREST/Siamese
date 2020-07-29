    private boolean deleteAll(File file) {
        boolean bDeleted = true;
        if (file.isDirectory()) {
            File[] afile = file.listFiles();
            for (int iFile = 0; bDeleted && (iFile < afile.length); iFile++) bDeleted = deleteAll(afile[iFile]);
        }
        if (bDeleted) bDeleted = file.delete();
        return bDeleted;
    }
