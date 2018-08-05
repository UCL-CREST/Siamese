    static void copyRec(File source, File target, byte[] sharedBuffer) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdirs();
            }
            if (target.isDirectory()) {
                File[] files = source.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    File targetFile = SmbFileUtils.getFile(target, file.getName());
                    if (file.isFile()) {
                        if (targetFile.exists()) {
                            targetFile.delete();
                        }
                        copy(file, targetFile, sharedBuffer);
                    } else {
                        targetFile.mkdirs();
                        copyRec(file, targetFile);
                    }
                }
            }
        } else {
            if (!target.isDirectory()) {
                if (!target.exists()) {
                    File dir = target.getParentFile();
                    if (!dir.exists() && !dir.mkdirs()) {
                        throw new IOException("Could not create target directory: " + dir);
                    }
                    if (!target.createNewFile()) {
                        throw new IOException("Could not create target file: " + target);
                    }
                }
                copy(source, target, sharedBuffer);
            }
        }
    }
