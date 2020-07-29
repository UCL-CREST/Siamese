    private static void zipFileOrDirectory(ZipOutputStream zipOut, File file, String curPath) {
        if (file == null || zipOut == null) {
            return;
        }
        String fileName = file.getName();
        FileInputStream in = null;
        try {
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(curPath + fileName);
                zipOut.putNextEntry(zipEntry);
                in = new FileInputStream(file);
                int bytes;
                byte[] b = new byte[1024];
                while ((bytes = in.read(b)) != -1) {
                    zipOut.write(b, 0, bytes);
                }
                zipOut.closeEntry();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        zipFileOrDirectory(zipOut, f, curPath + file.getName() + "/");
                    }
                } else if (files != null && files.length == 0) {
                    ZipEntry zipEntry = new ZipEntry(curPath + fileName + "/");
                    zipOut.putNextEntry(zipEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
