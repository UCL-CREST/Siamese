    private void backupDir(ZipOutputStream zos, String root, File dir) throws IOException {
        if (!root.equals(dir.toString())) {
            String relDir = dir.toString().substring(root.length() + 1) + '/';
            zos.putNextEntry(new ZipEntry(relDir));
        }
        File[] children = dir.listFiles();
        if (children == null) return;
        for (int ii = 0; ii < children.length; ++ii) {
            File child = children[ii];
            log(child.toString());
            if (child.isDirectory()) backupDir(zos, root, child); else {
                String relFile = child.toString().substring(root.length() + 1);
                ZipEntry ze = new ZipEntry(relFile);
                ze.setSize(child.length());
                ze.setTime(child.lastModified());
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(child);
                int len;
                while ((len = fis.read(fileBuffer)) != -1) {
                    crc32.update(fileBuffer, 0, len);
                    zos.write(fileBuffer, 0, len);
                }
                fis.close();
                ze.setCrc(crc32.getValue());
            }
        }
    }
