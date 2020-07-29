    public static BLOB zipBlob(BLOB iBlob, String iComment, int iLevel, String iEntryName) throws Exception {
        byte data[] = new byte[BUFFER];
        BLOB out = null;
        String lComment = null;
        int lLevel = DEFAULT_ZIP_COMPRESS_LEVEL;
        String lEntryName = null;
        if (iComment == null) {
            lComment = "Compressed by oracle-jutils JDBMS_COMPRESS on " + (new Date());
            System.out.println("WARNING : No comment has been provided. The following one has been auto-generated : <" + lComment + ">");
        } else {
            lComment = iComment;
        }
        if ((iLevel < 0) || (iLevel > 9)) {
            System.err.println("WARNING : Zip Compression Level values must be between 0 and 9 : [0..9]");
            lLevel = DEFAULT_ZIP_COMPRESS_LEVEL;
            System.err.println("WARNING : input Compression Level was <" + iLevel + "> and has been reset to <" + lLevel + ">");
        } else {
            lLevel = iLevel;
            System.out.println("Compression Level set to <" + lLevel + ">");
        }
        if (iEntryName == null) {
            lEntryName = DEFAULT_ENTRY_NAME_TEMPLATE + UUID.randomUUID() + "." + JdbmsLob.BLOB_TO_FILE_EXTENSION;
            System.out.println("WARNING : No Entry Name has been provided, generating one : <" + lEntryName + ">");
        } else {
            lEntryName = iEntryName;
        }
        try {
            System.out.println("Trying to get an internal Oracle Connection ...");
            openOracleConnection();
            System.out.println("Internally connected.");
            System.out.println("Internally connected.");
            System.out.println("Creating and opening session temporary BLOB in READ/WRITE mode ...");
            out = oracle.sql.BLOB.createTemporary(conn, false, oracle.sql.BLOB.DURATION_SESSION);
            out.open(BLOB.MODE_READWRITE);
            System.out.println("Session temporary BLOB opened.");
            System.out.println("Creating ZipOutputStream ...");
            ZipOutputStream zout = new ZipOutputStream(out.getBinaryOutputStream());
            System.out.println("ZipOutputStream created.");
            zout.setComment(lComment);
            zout.setMethod(ZipOutputStream.DEFLATED);
            zout.setLevel(lLevel);
            BufferedInputStream buffi = new BufferedInputStream(iBlob.getBinaryStream());
            ZipEntry entry = new ZipEntry(lEntryName);
            zout.putNextEntry(entry);
            System.out.println("Adding entry <" + lEntryName + "> to the zip file ...");
            int count;
            while ((count = buffi.read(data, 0, BUFFER)) != -1) {
                zout.write(data, 0, count);
            }
            zout.close();
            System.out.println("Closing ZipOutputStream ...");
            buffi.close();
            System.out.println("Sending back zipped BLOB ...");
            return out;
        } catch (SQLException ex) {
            System.err.println("ERROR : Error while working on BLOB : " + ex.getMessage());
            System.err.println("ERROR : throwing Exception ...");
            System.err.println("ERROR : Bye.");
            throw ex;
        } catch (IOException ex) {
            System.err.println("ERROR : I/O While dealing with binary streams on zip output : " + ex.getMessage());
            System.err.println("ERROR : throwing Exception ...");
            System.err.println("ERROR : Bye.");
            throw ex;
        }
    }
