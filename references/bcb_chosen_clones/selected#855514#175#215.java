    private void zipProjectDir(File saveFile, File saveDir) {
        ZipOutputStream zos = null;
        BufferedInputStream bis = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(saveFile));
            List<File> allFile = new ArrayList<File>();
            getAllProjectFile(saveDir, allFile);
            for (File file : allFile) {
                ZipEntry entry = new ZipEntry(file.getPath());
                zos.putNextEntry(entry);
                bis = new BufferedInputStream(new FileInputStream(file));
                int count;
                byte buf[] = new byte[1024];
                while ((count = bis.read(buf, 0, 104)) != EOF) {
                    zos.write(buf, 0, count);
                }
                bis.close();
                zos.closeEntry();
                file.delete();
            }
            for (File dir : saveDir.listFiles()) {
                dir.delete();
            }
            saveDir.delete();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
