    public static void zipFiles(List files, File destinationZipArchive, boolean moveFiles) throws Exception {
        int nFiles, nnFiles;
        String fileName;
        File file;
        FileOutputStream fos;
        ZipOutputStream zos;
        ZipEntry ze;
        FileInputStream fis;
        BufferedInputStream bis;
        byte buffer[] = new byte[1000];
        int bufferSize = buffer.length;
        int bytesRead;
        try {
            if (destinationZipArchive.exists()) {
                if (destinationZipArchive.isDirectory()) throw new Exception("The specified zip archive '" + destinationZipArchive + "' already exists, and it is a folder."); else destinationZipArchive.delete();
            } else {
                File destinationFolder = destinationZipArchive.getParentFile();
                if (!destinationFolder.exists()) if (!destinationFolder.mkdirs()) throw new Exception("Could not create the folder for '" + destinationZipArchive.getAbsolutePath() + "'.");
            }
            fos = new FileOutputStream(destinationZipArchive);
            zos = new ZipOutputStream(fos);
            nnFiles = files.size();
            for (nFiles = 0; nFiles < nnFiles; nFiles++) {
                Object o = files.get(nFiles);
                if (o instanceof String) {
                    fileName = (String) o;
                    file = new File(fileName);
                } else if (o instanceof File) {
                    file = (File) o;
                } else {
                    throw new Exception("Only 'String' and 'File' instances can be specified in the files list.");
                }
                if (!file.exists()) throw new Exception("File '" + file.getAbsolutePath() + "', specified in the files list, doesn't exist.");
                if (file.isDirectory()) throw new Exception("File '" + file.getAbsolutePath() + "', specified in the files list, is actually a folder.");
                ze = new ZipEntry(file.getName());
                try {
                    zos.putNextEntry(ze);
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    for (; ; ) {
                        bytesRead = bis.read(buffer, 0, bufferSize);
                        if (bytesRead <= 0) break;
                        zos.write(buffer, 0, bytesRead);
                    }
                    fis.close();
                    bis.close();
                    zos.closeEntry();
                } catch (ZipException e) {
                    if (!e.getMessage().startsWith("duplicate entry")) throw new Exception(ParseError.parseError("Error zipping '" + file.getAbsolutePath() + "':", e));
                }
            }
            zos.close();
            fos.close();
            if (moveFiles) {
                nnFiles = files.size();
                for (nFiles = 0; nFiles < nnFiles; nFiles++) {
                    Object o = files.get(nFiles);
                    if (o instanceof String) {
                        fileName = (String) o;
                        file = new File(fileName);
                    } else if (o instanceof File) {
                        file = (File) o;
                    } else {
                        throw new Exception("Only 'String' and 'File' instances can be specified in the files list.");
                    }
                    if (file.exists()) if (file.isFile()) {
                        if (file.delete()) System.out.println("Deleted " + file.getAbsolutePath() + "."); else System.out.println("Could not delete " + file.getAbsolutePath() + ".");
                    }
                }
            }
        } catch (Exception e) {
            String sFiles = "null";
            if (files != null) sFiles = files.size() + " elements.";
            String sDestinationZipArchive = "null";
            if (destinationZipArchive != null) sDestinationZipArchive = destinationZipArchive.getAbsolutePath();
            throw new Exception(ParseError.parseError("ZipHelper.zipFiles('" + sFiles + "','" + sDestinationZipArchive + "'," + moveFiles, e));
        }
    }
