    public void save(String zipFileName, int y) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            out.putNextEntry(new ZipEntry(Integer.toString(y) + ".txt"));
            int n = vector.size();
            String s = "";
            byte[] buf;
            for (int i = 0; i < n; i++) {
                s = vector.elementAt(i).toString() + "\n";
                recordSize = s.length();
                buf = s.getBytes();
                out.write(buf, 0, recordSize);
            }
            out.closeEntry();
            out.close();
        } catch (Exception e) {
            System.out.println("Error while zipping:/t" + zipFileName);
            e.printStackTrace();
        }
    }
