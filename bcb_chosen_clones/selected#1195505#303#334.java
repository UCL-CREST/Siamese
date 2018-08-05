    protected int getFileSize(File file) {
        if (file == null) {
            return -1;
        }
        FileInputStream fileInputStream = null;
        int size = -1;
        try {
            fileInputStream = new FileInputStream(file);
            BufferedInputStream input = new BufferedInputStream(fileInputStream);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] b = new byte[512];
            int len = -1;
            while ((len = input.read(b)) != -1) {
                output.write(b, 0, len);
                output.flush();
            }
            input.close();
            fileInputStream.close();
            byte[] fileArray = output.toByteArray();
            if (fileArray != null) {
                size = fileArray.length;
            } else {
                size = -1;
            }
            output.close();
        } catch (FileNotFoundException e) {
            return -1;
        } catch (IOException ioe) {
            return -1;
        }
        return size;
    }
