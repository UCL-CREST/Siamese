     *
     *
     *
     *@param  from     Դ·��,��c:\zxt\feedback\
     *@param  zout     ѹ�������

     *@param  filters  Description of Parameter
     *@param  baseDir  ѹ��Ļ�׼Ŀ¼

     */
    private static void zipFile(String baseDir, String from, String filters, ZipOutputStream zout) {
        String temp_to = null;
        String temp_from = null;
        String entryFileName = null;
        String entryName = "";
        if (baseDir == null) {
            int idx = from.lastIndexOf(File.separator);
            if (idx == -1) {
                throw new IllegalArgumentException("Argument Error:[from] is not a valid filename");
            }
            baseDir = from.substring(0, idx);
            System.err.println("from=" + from);
            System.err.println("baseDir=" + baseDir);
        }
        try {
            File f = new File(from);
            int filenumber;
            String[] strFile;
            File fTemp;
            if (f.isDirectory()) {
                strFile = f.list();
                for (int i = 0; i < strFile.length; i++) {
                    if (from.endsWith(File.separator)) {
                        temp_from = from + strFile[i];
                    } else {
                        temp_from = from + File.separator + strFile[i];
                    }
                    fTemp = new File(temp_from);
                    if (fTemp.isDirectory()) {
                        zipFile(baseDir, temp_from, filters, zout);
                    } else {
                        if (!ManageFile.checkFileNames(temp_from, filters)) {
                            continue;
                        }
                        byte[] s = ManageFile.loadFromFile(temp_from);
                        entryName = temp_from.substring(baseDir.length() + 1, temp_from.length());
                        System.err.println("entrName=" + entryName);
                        ZipEntry zefile = new ZipEntry(entryName);
                        zefile.setSize(s.length);
                        zout.putNextEntry(zefile);
                        zout.write(s);
                        zout.closeEntry();
                    }
                }
            } else {
                if (!ManageFile.checkFileNames(from, filters)) {
                    return;
                }
                byte[] s = ManageFile.loadFromFile(from);
                entryName = from.substring(baseDir.length() + 1, from.length());
                System.err.println("entrName=" + entryName);
                ZipEntry zefile = new ZipEntry(entryName);
