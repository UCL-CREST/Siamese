                public void run() {
                    try {
                        FileChannel in = (new FileInputStream(file)).getChannel();
                        FileChannel out = (new FileOutputStream(updaterFile)).getChannel();
                        in.transferTo(0, file.length(), out);
                        updater.setProgress(50);
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startUpdater();
                }
