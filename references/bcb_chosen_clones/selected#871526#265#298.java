    private void copy(final File src, final File dstDir) {
        dstDir.mkdirs();
        final File dst = new File(dstDir, src.getName());
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(src), "ISO-8859-1"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst), "ISO-8859-1"));
            String read;
            while ((read = reader.readLine()) != null) {
                Line line = new Line(read);
                if (line.isComment()) continue;
                writer.write(read);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
            }
            if (writer != null) {
                try {
                    writer.flush();
                } catch (IOException e) {
                }
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }
