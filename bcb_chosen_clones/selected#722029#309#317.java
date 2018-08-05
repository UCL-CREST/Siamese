        private long getCRC(final File f) throws IOException {
            CRC32 c = new CRC32();
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte data[] = new byte[1024];
            while (bis.read(data) != -1) c.update(data);
            bis.close();
            return (c.getValue());
        }
