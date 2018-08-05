    static File getTestArchiveFile() throws IOException {
        if (TarTestUtils.testArchiveFile == null) {
            TarTestUtils.testArchiveFile = File.createTempFile("jaxlib.arc.tar.TarTestUtils.archiveFile", ".tar").getCanonicalFile();
        }
        if (!TarTestUtils.testArchiveFile.isFile() || (TarTestUtils.testArchiveFile.length() == 0)) {
            TarTestUtils.filesInArchive.clear();
            TarTestUtils.testArchiveRoot = File.createTempFile("jaxlib.arc.tar.TarTestUtils", null);
            TarTestUtils.testArchiveRoot.delete();
            TarTestUtils.testArchiveRoot.mkdirs();
            TarTestUtils.testArchiveRoot.deleteOnExit();
            TarTestUtils.testArchiveRoot = TarTestUtils.testArchiveRoot.getCanonicalFile();
            StringBuilder sb = new StringBuilder();
            TarTestUtils.entryCount = 0;
            for (int i = 0; i < COUNT_FILES; i++) {
                sb.setLength(0);
                File f;
                if (LONG_NAMES && ((i & 1) == 0)) {
                    while (sb.length() < 100) {
                        sb.append('d').append(i).append('/');
                        TarTestUtils.entryCount++;
                    }
                    f = new File(TarTestUtils.testArchiveRoot, sb.toString());
                    f.mkdirs();
                    sb.setLength(0);
                    f = new File(f, sb.append("file").append(i).toString());
                } else {
                    f = new File(TarTestUtils.testArchiveRoot, sb.append("file").append(i).toString());
                }
                f.deleteOnExit();
                TarTestUtils.entryCount++;
                TarTestUtils.filesInArchive.add(f);
            }
            sb = null;
            for (final File f : TarTestUtils.filesInArchive) {
                f.delete();
                final FileOutputStream out = new FileOutputStream(f);
                final long len = writeTestData(out, -1);
                out.flush();
                out.getChannel().force(true);
                out.getFD().sync();
                out.close();
            }
            TarTestUtils.testArchiveFile.delete();
            Process tarProc = Runtime.getRuntime().exec(new String[] { "tar", "-cf", TarTestUtils.testArchiveFile.getPath(), TarTestUtils.testArchiveRoot.getPath() });
            try {
                int tarExit = tarProc.waitFor();
                if (tarExit != 0) {
                    InputStream err = tarProc.getErrorStream();
                    for (int b; (b = err.read()) >= 0; ) System.out.write(b);
                    throw new RuntimeException("tar exited with error " + tarExit);
                }
                InputStream in = tarProc.getInputStream();
                for (int b; (b = in.read()) >= 0; ) ;
            } catch (final InterruptedException ex) {
                throw (InterruptedIOException) new InterruptedIOException().initCause(ex);
            } finally {
                tarProc.destroy();
            }
            TarTestUtils.filesInArchive.add(TarTestUtils.testArchiveRoot);
            testArchiveUsingTar(TarTestUtils.testArchiveFile);
        }
        return TarTestUtils.testArchiveFile;
    }
