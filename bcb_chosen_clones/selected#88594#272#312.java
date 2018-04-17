    private void transferFiles(String stream_path, File tr_file, String name, java.util.zip.ZipOutputStream zos) throws IOException {
        if (tr_file.isDirectory()) {
            File[] files = tr_file.listFiles();
            for (int i = 0; i < files.length; i++) {
                transferFiles(stream_path + name + "/", files[i], files[i].getName(), zos);
            }
        } else {
            ZipEntry file = new ZipEntry(stream_path + name);
            file.setSize(tr_file.length());
            byte[] mode = new byte[1];
            mode[0] = 0;
            if (tr_file.canRead()) mode[0] += 4;
            if (tr_file.canWrite()) mode[0] += 2;
            file.setExtra(mode);
            CLogger.status("Streaming: " + file + " as " + stream_path + name);
            zos.putNextEntry(file);
            FileInputStream fis = new FileInputStream(tr_file);
            long first = System.currentTimeMillis();
            long second = System.currentTimeMillis();
            long overhead = second - first;
            long middle = System.currentTimeMillis();
            int read = fis.read(buffer);
            long read_end = System.currentTimeMillis();
            long read_time = (read_end - middle - overhead);
            long write_time = 0;
            int total = 0;
            while (read >= 0) {
                total += read;
                long write_start = System.currentTimeMillis();
                zos.write(buffer, 0, read);
                middle = System.currentTimeMillis();
                read = fis.read(buffer);
                read_end = System.currentTimeMillis();
                write_time += (middle - write_start - overhead);
                read_time += (read_end - middle - overhead);
            }
            fis.close();
            zos.closeEntry();
            CLogger.status("Streamed <" + total + "> bytes of: " + file + "in <" + write_time / 1000.0 + "><" + read_time / 1000.0 + ">");
        }
    }
