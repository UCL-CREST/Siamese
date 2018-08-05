    private static boolean addToZip(String addFileFromThisDir, File addThis, ZipOutputStream zos) {
        if (addThis.isDirectory()) return false;
        String entryPath;
        String addThisPath = addThis.getAbsolutePath();
        if (!addThisPath.startsWith(addFileFromThisDir)) {
            System.out.println("MiscUtils.addToZip(..) -- BAJ!!");
            return false;
        } else {
            entryPath = addThisPath.substring(addFileFromThisDir.length() + 1);
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
            e.printStackTrace();
            return false;
        }
    }
