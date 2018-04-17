    private void zipFiles(File cpFile) {
        int byteCount;
        final int DATA_BLOCK_SIZE = 2048;
        FileInputStream cpFileInputStream;
        if (cpFile.isDirectory()) {
            if (cpFile.getName().equalsIgnoreCase(".metadata")) {
                return;
            }
            File[] fList = cpFile.listFiles();
            for (int i = 0; i < fList.length; i++) {
                zipFiles(fList[i]);
            }
        } else {
            try {
                if (cpFile.getAbsolutePath().equalsIgnoreCase(strTarget)) {
                    return;
                }
                System.out.println("Zipping " + cpFile);
                size += cpFile.length();
                numOfFiles++;
                String strAbsPath = cpFile.getPath();
                String strZipEntryName = strAbsPath.substring(strSource.length() + 1, strAbsPath.length());
                cpFileInputStream = new FileInputStream(cpFile);
                ZipEntry cpZipEntry = new ZipEntry(strZipEntryName);
                cpZipOutputStream.putNextEntry(cpZipEntry);
                byte[] b = new byte[DATA_BLOCK_SIZE];
                while ((byteCount = cpFileInputStream.read(b, 0, DATA_BLOCK_SIZE)) != -1) {
                    cpZipOutputStream.write(b, 0, byteCount);
                }
                cpZipOutputStream.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
