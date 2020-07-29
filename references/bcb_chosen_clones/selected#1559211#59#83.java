    private boolean addToZip(String addFileFromThisDir, File addThis, ZipOutputStream zos) {
        String entryPath;
        String addThisPath = addThis.getAbsolutePath();
        if (!addThisPath.startsWith(addFileFromThisDir)) {
            System.out.println("CatalinaDeleteEvent.addToZip() --- parameter error !");
            return false;
        } else {
            entryPath = addThisPath.substring(addFileFromThisDir.length());
        }
        try {
            zos.putNextEntry(new ZipEntry(entryPath));
            FileInputStream input = new FileInputStream(addThis);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            return true;
        } catch (Exception e) {
            System.out.println("CatalinaDeleteEvent.addToZip()");
            e.printStackTrace();
            return false;
        }
    }
