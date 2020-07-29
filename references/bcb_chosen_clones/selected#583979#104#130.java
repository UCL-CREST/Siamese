    public void patchFile(final File classFile) {
        if (!classFile.exists()) {
            myErrors.add(new FormErrorInfo(null, "Class to bind does not exist: " + myRootContainer.getClassToBind()));
            return;
        }
        FileInputStream fis;
        try {
            byte[] patchedData;
            fis = new FileInputStream(classFile);
            try {
                patchedData = patchClass(fis);
                if (patchedData == null) {
                    return;
                }
            } finally {
                fis.close();
            }
            FileOutputStream fos = new FileOutputStream(classFile);
            try {
                fos.write(patchedData);
            } finally {
                fos.close();
            }
        } catch (IOException e) {
            myErrors.add(new FormErrorInfo(null, "Cannot read or write class file " + classFile.getPath() + ": " + e.toString()));
        }
    }
