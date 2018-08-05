    public static void concatFiles(final String as_base_file_name) throws IOException, FileNotFoundException {
        new File(as_base_file_name).createNewFile();
        final OutputStream lo_out = new FileOutputStream(as_base_file_name, true);
        int ln_part = 1, ln_readed = -1;
        final byte[] lh_buffer = new byte[32768];
        File lo_file = new File(as_base_file_name + "part1");
        while (lo_file.exists() && lo_file.isFile()) {
            final InputStream lo_input = new FileInputStream(lo_file);
            while ((ln_readed = lo_input.read(lh_buffer)) != -1) {
                lo_out.write(lh_buffer, 0, ln_readed);
            }
            ln_part++;
            lo_file = new File(as_base_file_name + "part" + ln_part);
        }
        lo_out.flush();
        lo_out.close();
    }
