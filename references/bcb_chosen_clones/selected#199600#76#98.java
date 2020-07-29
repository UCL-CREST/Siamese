    private static void addTargetFile(ZipOutputStream zos, File file) throws FileNotFoundException, ZipException, IOException {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            String file_path = file.getPath();
            if (file_path.startsWith(OctopusApplication.PATH_EXPORT_FOLDER)) {
                file_path = file_path.substring(OctopusApplication.PATH_EXPORT_FOLDER.length(), file_path.length());
            }
            ZipEntry target = new ZipEntry(file_path);
            zos.putNextEntry(target);
            int c;
            while ((c = bis.read()) != -1) {
                zos.write((byte) c);
            }
            bis.close();
            zos.closeEntry();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (ZipException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
