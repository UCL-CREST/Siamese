        private void update() throws IOException {
            FileOutputStream out = new FileOutputStream(combined);
            try {
                File[] _files = listJavascript();
                List<File> files = new ArrayList<File>(Arrays.asList(_files));
                files.add(0, new File(jsdir.getAbsolutePath() + "/leemba.js"));
                files.add(0, new File(jsdir.getAbsolutePath() + "/jquery.min.js"));
                for (File js : files) {
                    FileInputStream fin = null;
                    try {
                        int count = 0;
                        byte buf[] = new byte[16384];
                        fin = new FileInputStream(js);
                        while ((count = fin.read(buf)) > 0) out.write(buf, 0, count);
                    } catch (Throwable t) {
                        log.error("Failed to read file: " + js.getAbsolutePath(), t);
                    } finally {
                        if (fin != null) fin.close();
                    }
                }
            } finally {
                out.close();
            }
        }
