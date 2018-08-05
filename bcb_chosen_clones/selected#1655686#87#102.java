            public void run() {
                try {
                    File inf = new File(dest);
                    if (!inf.exists()) {
                        inf.getParentFile().mkdirs();
                    }
                    FileChannel in = new FileInputStream(src).getChannel();
                    FileChannel out = new FileOutputStream(dest).getChannel();
                    out.transferFrom(in, 0, in.size());
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error copying file \n" + src + "\n" + dest);
                }
            }
