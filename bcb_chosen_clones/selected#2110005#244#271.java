    private void compressEntry(File f, String dest, String appPath) {
        try {
            int buffer = 1024;
            byte data[] = new byte[buffer];
            BufferedInputStream origin = null;
            FileOutputStream destination = new FileOutputStream(dest);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(destination));
            FileInputStream fi = new FileInputStream(f);
            origin = new BufferedInputStream(fi, buffer);
            ZipEntry entry = new ZipEntry(f.getCanonicalPath().substring(appPath.length(), f.getCanonicalPath().length()));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, buffer)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                JOptionPane.showMessageDialog(null, "Update error! Could not compress file " + f.getCanonicalPath(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
