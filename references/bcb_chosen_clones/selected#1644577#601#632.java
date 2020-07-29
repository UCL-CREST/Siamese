        public void copyTo(File folder) {
            if (!isNewFile()) {
                return;
            }
            if (!folder.exists()) {
                folder.mkdir();
            }
            File dest = new File(folder, name);
            try {
                FileInputStream in = new FileInputStream(currentPath);
                FileOutputStream out = new FileOutputStream(dest);
                byte[] readBuf = new byte[1024 * 512];
                int readLength;
                long totalCopiedSize = 0;
                boolean canceled = false;
                while ((readLength = in.read(readBuf)) != -1) {
                    out.write(readBuf, 0, readLength);
                }
                in.close();
                out.close();
                if (canceled) {
                    dest.delete();
                } else {
                    currentPath = dest;
                    newFile = false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
