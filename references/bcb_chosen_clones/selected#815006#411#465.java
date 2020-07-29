    public void touchSomeFiles(int percentage, int mode) {
        try {
            File[] files = dir.listFiles(mff);
            Random rnd = new Random();
            for (int i = files.length - 1; i >= 0; i--) {
                int j = rnd.nextInt(i + 1);
                File swap = files[i];
                files[i] = files[j];
                files[j] = swap;
            }
            int howfar = files.length * percentage / 100;
            if (mode == TOUCH_MODE_RECREATE || mode == TOUCH_MODE_COPY_TWICE) {
                for (int i = 0; i < howfar; i++) {
                    File f = files[i];
                    if (mode == TOUCH_MODE_COPY_TWICE) {
                        File bf = new File(f.getParent(), "recreate--" + f.getName());
                        recreateFile(f, bf);
                    } else {
                        recreateFile(f, f);
                    }
                }
                if (mode == TOUCH_MODE_COPY_TWICE) {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    for (int i = 0; i < howfar; i++) {
                        File f = files[i];
                        File bf = new File(f.getParent(), "recreate--" + f.getName());
                        recreateFile(bf, f);
                    }
                }
            } else if (mode == TOUCH_MODE_ACCESS || mode == TOUCH_MODE_MODIFY) {
                for (int i = 0; i < howfar; i++) {
                    RandomAccessFile raf = new RandomAccessFile(files[i], mode == TOUCH_MODE_MODIFY ? "rw" : "r");
                    if (raf.length() > 0) {
                        int pos = rnd.nextInt(Math.max(0, (int) raf.length()));
                        raf.seek(pos);
                        byte byte1 = raf.readByte();
                        if (mode == TOUCH_MODE_MODIFY) {
                            raf.seek(pos);
                            raf.write(byte1);
                        }
                    }
                    raf.close();
                    files[i].setLastModified(LAST_MODIFIED_DATE);
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
