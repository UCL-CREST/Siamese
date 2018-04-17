    protected static IFile createTempFile(CodeFile codeFile) {
        IPath path = Util.getAbsolutePathFromCodeFile(codeFile);
        File file = new File(path.toOSString());
        String[] parts = codeFile.getName().split("\\.");
        String extension = parts[parts.length - 1];
        IPath ext = path.addFileExtension(extension);
        File tempFile = new File(ext.toOSString());
        if (tempFile.exists()) {
            boolean deleted = tempFile.delete();
            System.out.println("deleted: " + deleted);
        }
        try {
            boolean created = tempFile.createNewFile();
            if (created) {
                FileOutputStream fos = new FileOutputStream(tempFile);
                FileInputStream fis = new FileInputStream(file);
                while (fis.available() > 0) {
                    fos.write(fis.read());
                }
                fis.close();
                fos.close();
                IFile iFile = Util.getFileFromPath(ext);
                return iFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
