        @Override
        public byte[] toBytes() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[8192];
            int rd;
            try {
                while ((rd = fis.read(data)) != -1) {
                    baos.write(data, 0, rd);
                }
                return baos.toByteArray();
            } finally {
                fis.close();
            }
        }
