    public void doIt() throws GlobalMapperTileTranslatorException {
        if (StringUtils.isEmpty(dstGuid)) throw new GlobalMapperTileTranslatorException("GUID of destination map is empty");
        if (srcDir == null || !srcDir.isDirectory() || !srcDir.exists()) throw new GlobalMapperTileTranslatorException("Source directory is invalid");
        try {
            int z;
            final XFile dstDir = new XFile(dstParentDir, dstGuid);
            dstDir.mkdir();
            int n = 1;
            if (srcDir.isDirectory() && srcDir.exists()) {
                for (int i = 0; i < 18; i++) {
                    XFile zDir = new XFile(srcDir, "z" + i);
                    if (!zDir.isDirectory() || !zDir.exists()) zDir = new XFile(srcDir, "Z" + i);
                    if (zDir.isDirectory() && zDir.exists()) {
                        for (String fileName : zDir.list()) {
                            XFile file = new XFile(zDir, fileName);
                            if (file.isFile() && file.exists() && file.canRead()) {
                                final String[] yx;
                                if (fileName.indexOf('.') > 0) {
                                    String[] fileExt = fileName.split("\\.");
                                    yx = fileExt[0].split("_");
                                } else yx = fileName.split("_");
                                if (yx.length > 1) {
                                    final int x = Integer.valueOf(yx[1]);
                                    final int y = Integer.valueOf(yx[0]);
                                    z = 17 - i;
                                    XFileOutputStream out = null;
                                    XFileInputStream in = null;
                                    try {
                                        final XFile outFile = new XFile(dstDir, x + "_" + y + "_" + z);
                                        if (override || !(isExist(outFile, file))) {
                                            out = new XFileOutputStream(outFile);
                                            in = new XFileInputStream(file);
                                            IOUtils.copy(in, out);
                                        }
                                        if (n % 999 == 0) {
                                            logger.info(i + " tiles were copied from 'incoming'");
                                            synchronized (GlobalMapperTileTranslator.class) {
                                                GlobalMapperTileTranslator.class.wait(300);
                                            }
                                        }
                                        n++;
                                    } finally {
                                        if (out != null) {
                                            out.flush();
                                            out.close();
                                        }
                                        if (in != null) {
                                            in.close();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            logger.error("map tile importing has failed: ", e);
            throw new GlobalMapperTileTranslatorException(e);
        }
    }
