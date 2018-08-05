    public String parseLink(File f) throws Exception {
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
        final int shell_offset = 0x4c;
        int shell_len = 0;
        if ((flags & 0x1) > 0) {
            shell_len = bytes2short(link, shell_offset) + 2;
        }
        int file_start = 0x4c + shell_len;
        int local_sys_off = link[file_start + 0x10] + file_start;
        String l = new String(link);
        return getNullDelimitedString(link, local_sys_off);
    }
