    public void copyContent(long mailId1, long mailId2) throws Exception {
        File file1 = new File(this.getMailDir(mailId1) + "/");
        File file2 = new File(this.getMailDir(mailId2) + "/");
        this.recursiveDir(file2);
        if (file1.isDirectory()) {
            File[] files = file1.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        File file2s = new File(file2.getAbsolutePath() + "/" + files[i].getName());
                        if (!file2s.exists()) {
                            file2s.createNewFile();
                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file2s));
                            BufferedInputStream in = new BufferedInputStream(new FileInputStream(files[i]));
                            int read;
                            while ((read = in.read()) != -1) {
                                out.write(read);
                            }
                            out.flush();
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException ex1) {
                                    ex1.printStackTrace();
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
