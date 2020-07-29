    public static int unzipFile(File file_input, File dir_output) {
        ZipInputStream zip_in_stream;
        try {
            FileInputStream in = new FileInputStream(file_input);
            BufferedInputStream source = new BufferedInputStream(in);
            zip_in_stream = new ZipInputStream(source);
        } catch (IOException e) {
            return STATUS_IN_FAIL;
        }
        byte[] input_buffer = new byte[BUF_SIZE];
        int len = 0;
        do {
            try {
                ZipEntry zip_entry = zip_in_stream.getNextEntry();
                if (zip_entry == null) break;
                File output_file = new File(dir_output, zip_entry.getName());
                FileOutputStream out = new FileOutputStream(output_file);
                BufferedOutputStream destination = new BufferedOutputStream(out, BUF_SIZE);
                while ((len = zip_in_stream.read(input_buffer, 0, BUF_SIZE)) != -1) destination.write(input_buffer, 0, len);
                destination.flush();
                out.close();
            } catch (IOException e) {
                return STATUS_GUNZIP_FAIL;
            }
        } while (true);
        try {
            zip_in_stream.close();
        } catch (IOException e) {
        }
        return STATUS_OK;
    }
