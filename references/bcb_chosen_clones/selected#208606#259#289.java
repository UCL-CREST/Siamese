    private File downloadPDB(String pdbId) {
        File tempFile = new File(path + "/" + pdbId + ".pdb.gz");
        File pdbHome = new File(path);
        if (!pdbHome.canWrite()) {
            System.err.println("can not write to " + pdbHome);
            return null;
        }
        String ftp = String.format("ftp://ftp.ebi.ac.uk/pub/databases/msd/pdb_uncompressed/pdb%s.ent", pdbId.toLowerCase());
        System.out.println("Fetching " + ftp);
        try {
            URL url = new URL(ftp);
            InputStream conn = url.openStream();
            System.out.println("writing to " + tempFile);
            FileOutputStream outPut = new FileOutputStream(tempFile);
            GZIPOutputStream gzOutPut = new GZIPOutputStream(outPut);
            PrintWriter pw = new PrintWriter(gzOutPut);
            BufferedReader fileBuffer = new BufferedReader(new InputStreamReader(conn));
            String line;
            while ((line = fileBuffer.readLine()) != null) {
                pw.println(line);
            }
            pw.flush();
            pw.close();
            outPut.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return tempFile;
    }
