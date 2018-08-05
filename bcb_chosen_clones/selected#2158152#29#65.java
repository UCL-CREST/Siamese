    private void parse(File f) throws IOException {
        FileInputStream fin = new FileInputStream(f);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buff = new byte[256];
        while (true) {
            int n = fin.read(buff);
            if (n == -1) {
                break;
            }
            bout.write(buff, 0, n);
        }
        fin.close();
        byte[] link = bout.toByteArray();
        byte flags = link[0x14];
        final int file_atts_offset = 0x18;
        byte file_atts = link[file_atts_offset];
        byte is_dir_mask = (byte) 0x10;
        if ((file_atts & is_dir_mask) > 0) {
            is_dir = true;
        } else {
            is_dir = false;
        }
        final int shell_offset = 0x4c;
        final byte has_shell_mask = (byte) 0x01;
        int shell_len = 0;
        if ((flags & has_shell_mask) > 0) {
            shell_len = bytes2short(link, shell_offset) + 2;
        }
        int file_start = 0x4c + shell_len;
        final int basename_offset_offset = 0x10;
        final int finalname_offset_offset = 0x18;
        int basename_offset = link[file_start + basename_offset_offset] + file_start;
        int finalname_offset = link[file_start + finalname_offset_offset] + file_start;
        String basename = getNullDelimitedString(link, basename_offset);
        String finalname = getNullDelimitedString(link, finalname_offset);
        real_file = basename + finalname;
    }
