    private void encryptChkFile(ProjectMember member, File chkFile) throws Exception {
        final java.io.FileReader reader = new java.io.FileReader(chkFile);
        final File encryptedChkFile = new File(member.createOutputFileName(outputPath, "chk"));
        FileOutputStream outfile = null;
        ObjectOutputStream outstream = null;
        Utilities.discardBooleanResult(encryptedChkFile.getParentFile().mkdirs());
        outfile = new FileOutputStream(encryptedChkFile);
        outstream = new ObjectOutputStream(outfile);
        outstream.writeObject(new Format().parse(reader));
        reader.close();
        outfile.close();
        outstream.close();
    }
