    private static void fork(ZipOutputStream out, File tmpFile, String path) throws IOException {
        System.out.println(tmpFile);
        if (tmpFile.isDirectory()) {
            System.out.println(tmpFile + " is DIR");
            for (String tmpStr : tmpFile.list()) {
                System.out.println(tmpStr + " is Child of " + tmpFile);
                fork(out, new File(tmpFile, tmpStr), path + Const.FILE_SEPARATOR + tmpStr);
            }
        } else if (tmpFile.isFile()) {
            System.out.println(tmpFile + " is File");
            byte[] buffer = new byte[Const.BUFFER_SIZE];
            FileInputStream in = new FileInputStream(tmpFile);
            out.putNextEntry(new ZipEntry(path));
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.closeEntry();
            in.close();
            return;
        } else {
            return;
        }
    }
