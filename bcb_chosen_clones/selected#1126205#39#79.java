    public void encode(String path) throws YEncException {
        int c, w;
        long size = 0;
        CRC32 crc32 = new CRC32();
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path + File.separator + header.getName() + ".yenc")));
            while ((c = input.read()) != -1) {
                crc32.update(c);
                if (size % line == 0 && size != 0) {
                    baos.write((int) '\r');
                    baos.write((int) '\n');
                }
                w = (c + 42) % 256;
                if (w == 0x00 || w == 0x0A || w == 0x0D || w == 0x3D) {
                    baos.write((int) '=');
                    baos.write((w + 64) % 256);
                } else {
                    baos.write(w);
                }
                size++;
            }
            header.setSize(size);
            out.write(header.toString().getBytes());
            out.write((int) '\r');
            out.write((int) '\n');
            out.write(baos.toByteArray());
            out.write((int) '\r');
            out.write((int) '\n');
            trailer.setSize(size);
            trailer.setCrc32(Long.toHexString(crc32.getValue()).toUpperCase());
            out.write(trailer.toString().getBytes());
            out.write((int) '\r');
            out.write((int) '\n');
            baos.flush();
            baos.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new YEncException(e);
        }
    }
