    private void CopyTo(File dest) throws IOException {
        FileReader in = null;
        FileWriter out = null;
        int c;
        try {
            in = new FileReader(image);
            out = new FileWriter(dest);
            while ((c = in.read()) != -1) out.write(c);
        } finally {
            if (in != null) try {
                in.close();
            } catch (Exception e) {
            }
            if (out != null) try {
                out.close();
            } catch (Exception e) {
            }
        }
    }
