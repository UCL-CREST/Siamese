    public void appendFetch(IProgress progress, PrintWriter pw, String list, int from, int to) throws IOException {
        progress.start();
        try {
            File storage = new File(cacheDirectory.getValue(), "mboxes");
            storage.mkdirs();
            File mbox = new File(storage, list + "-" + from + "-" + to + ".mbox");
            if (mbox.exists()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(new ProgressInputStream(new FileInputStream(mbox), progress, 10000)));
                String line;
                while ((line = in.readLine()) != null) {
                    pw.write(line);
                    pw.write('\n');
                }
                in.close();
                return;
            }
            progress.setScale(100);
            IProgress subProgress1 = progress.getSub(75);
            URL url = getGmaneURL(list, from, to);
            BufferedReader in = new BufferedReader(new InputStreamReader(new ProgressInputStream(url.openStream(), subProgress1, 10000)));
            PrintWriter writeToMbox = new PrintWriter(mbox);
            int lines = 0;
            String line;
            while ((line = in.readLine()) != null) {
                lines++;
                if (line.matches("^From .*$") && !line.matches("^From .*? .*[0-9][0-9]:[0-9][0-9]:[0-9][0-9].*$")) {
                    line = ">" + line;
                }
                writeToMbox.write(line);
                writeToMbox.write('\n');
            }
            in.close();
            writeToMbox.close();
            appendFetch(progress.getSub(25), pw, list, from, to);
        } finally {
            progress.done();
        }
    }
