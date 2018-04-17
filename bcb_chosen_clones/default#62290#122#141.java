        public void run() {
            try {
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec("wget -c --directory-prefix=Downloads --no-cookies --header \"Cookie: " + cookie + "\" --append-output=wget-log.txt " + url);
                InputStream in = proc.getInputStream();
                proc.waitFor();
                in.close();
            } catch (Exception e) {
                System.out.print(e.getLocalizedMessage());
            } finally {
                synchronized (lock) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                    curSimultaneousDownloads--;
                    if (parentThreadSleeping) parentThread.interrupt();
                }
            }
        }
