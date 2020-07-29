    private void copy(File source, File destination) throws PackageException {
        try {
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(destination);
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0) out.write(buff, 0, len);
            in.close();
            out.close();
        } catch (IOException e) {
            throw new PackageException("Unable to copy " + source.getPath() + " to " + destination.getPath() + " :: " + e.toString());
        }
    }
