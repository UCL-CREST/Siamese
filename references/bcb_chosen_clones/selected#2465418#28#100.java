    public static boolean cmprsFile(String source, String target) throws Exception {
        boolean result = false;
        int cnt = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream finput = null;
        FileOutputStream foutput = null;
        ZipOutputStream zoutput = null;
        String source1 = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
        String target1 = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
        File srcFile = new File(source1);
        if (srcFile.exists()) {
            if (srcFile.isFile()) {
                String target2 = EgovFileTool.createNewFile(target1);
                File tarFile = new File(target2);
                foutput = null;
                zoutput = null;
                ZipEntry zentry = null;
                try {
                    foutput = new FileOutputStream(tarFile);
                    zoutput = new ZipOutputStream((OutputStream) foutput);
                    finput = new FileInputStream(srcFile);
                    zentry = new ZipEntry(srcFile.getName());
                    zoutput.putNextEntry(zentry);
                    zoutput.setLevel(COMPRESSION_LEVEL);
                    cnt = 0;
                    while ((cnt = finput.read(buffer)) != -1) {
                        zoutput.write(buffer, 0, cnt);
                    }
                    zoutput.closeEntry();
                    result = true;
                } catch (Exception e) {
                    tarFile.delete();
                    throw e;
                } finally {
                    if (finput != null) finput.close();
                    if (zoutput != null) zoutput.close();
                    if (foutput != null) foutput.close();
                }
            } else if (srcFile.isDirectory()) {
                String target2 = EgovFileTool.createNewFile(target1);
                File tarFile = new File(target2);
                ZipEntry zentry = null;
                try {
                    foutput = new FileOutputStream(tarFile);
                    zoutput = new ZipOutputStream((OutputStream) foutput);
                    File[] fileArr = srcFile.listFiles();
                    ArrayList list = EgovFileTool.getSubFilesByAll(fileArr);
                    for (int i = 0; i < list.size(); i++) {
                        File sfile = new File((String) list.get(i));
                        finput = new FileInputStream(sfile);
                        zentry = new ZipEntry(sfile.getAbsolutePath().replace('\\', '/').replaceAll(srcFile.getAbsolutePath().replace('\\', '/'), ""));
                        zoutput.putNextEntry(zentry);
                        zoutput.setLevel(COMPRESSION_LEVEL);
                        cnt = 0;
                        while ((cnt = finput.read(buffer)) != -1) {
                            zoutput.write(buffer, 0, cnt);
                        }
                        finput.close();
                        result = true;
                    }
                    zoutput.closeEntry();
                } catch (Exception e) {
                    tarFile.delete();
                    throw e;
                } finally {
                    if (finput != null) finput.close();
                    if (zoutput != null) zoutput.close();
                    if (foutput != null) foutput.close();
                }
            }
        }
        return result;
    }
