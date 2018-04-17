    protected void addFile(Menu menu, String filepath) {
        try {
            File currentFile = new File(filepath);
            InputStream is = new FileInputStream(currentFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            String decodedMenuPath = decodeMenuPath(menu.getMenuPath());
            decodedMenuPath = decodedMenuPath + "/" + currentFile.getName();
            decodedMenuPath = decodedMenuPath.replaceAll("//", "/");
            ZipEntry entry = new ZipEntry(decodedMenuPath);
            zos.putNextEntry(entry);
            int len;
            while ((len = bis.read()) != -1) {
                zos.write(len);
            }
            bis.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
