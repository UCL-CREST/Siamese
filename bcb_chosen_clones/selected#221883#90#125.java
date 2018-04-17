    static void moveRec(File source, File target, byte[] sharedBuffer) throws IOException {
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
                        if (!file.renameTo(targetFile)) {
                            copy(file, targetFile, sharedBuffer);
                            file.delete();
                        }
                    } else {
                        if (!targetFile.exists()) {
                            if (!targetFile.mkdirs()) {
                                throw new IOException("Could not create target directory: " + targetFile);
                            }
                        }
                        moveRec(file, targetFile);
                    }
                }
                source.delete();
            }
        } else {
            if (!target.isDirectory()) {
                copy(source, target, sharedBuffer);
                source.delete();
            }
        }
    }
