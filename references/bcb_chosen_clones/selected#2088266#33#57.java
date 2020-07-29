    public void testAddFiles() throws Exception {
        File original = ZipPlugin.getFileInPlugin(new Path("testresources/test.zip"));
        File copy = new File(original.getParentFile(), "1test.zip");
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(original);
            out = new FileOutputStream(copy);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        } finally {
            Util.close(in);
            Util.close(out);
        }
        ArchiveFile archive = new ArchiveFile(ZipPlugin.createArchive(copy.getPath()));
        archive.addFiles(new String[] { ZipPlugin.getFileInPlugin(new Path("testresources/add.txt")).getPath() }, new NullProgressMonitor());
        IArchive[] children = archive.getChildren();
        boolean found = false;
        for (IArchive child : children) {
            if (child.getLabel(IArchive.NAME).equals("add.txt")) found = true;
        }
        assertTrue(found);
        copy.delete();
    }
