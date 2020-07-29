        public static void copyFile(File from, File to) {
            try {
                FileInputStream in = new FileInputStream(from);
                FileOutputStream out = new FileOutputStream(to);
                byte[] buffer = new byte[1024 * 16];
                int read = 0;
                while ((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
