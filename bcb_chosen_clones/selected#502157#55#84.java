    public static Checksum packTask(Task t, String targetFile) {
        ZipOutputStream out = null;
        CheckedOutputStream cos = null;
        try {
            File file = new File(targetFile);
            FileOutputStream fos = new FileOutputStream(file);
            cos = new CheckedOutputStream(fos, new Adler32());
            out = new ZipOutputStream(new BufferedOutputStream(cos));
            TaskDescription td = t.getDescription();
            if (td != null) {
                out.putNextEntry(new ZipEntry("description.ini"));
                out.write(td.toString().getBytes());
                output(td.getExefile(), out);
                output(td.getDataFile(), out);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return cos.getChecksum();
    }
