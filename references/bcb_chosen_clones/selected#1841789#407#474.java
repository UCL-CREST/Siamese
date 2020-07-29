    private File getJStockZipFile() {
        final List<File> files = getUserDefinedDatabaseFiles();
        final List<FileEx> fileExs = new ArrayList<FileEx>();
        for (File file : files) {
            final String filename;
            try {
                filename = file.getCanonicalPath();
            } catch (IOException ex) {
                log.error(null, ex);
                continue;
            }
            final int index = filename.indexOf(Utils.getApplicationVersionString());
            if (index < 0) {
                continue;
            }
            final String output = filename.substring(index + Utils.getApplicationVersionString().length() + File.separator.length());
            fileExs.add(FileEx.newInstance(file, output));
        }
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        final JStockOptions insensitiveJStockOptions = jStockOptions.insensitiveClone();
        try {
            final File tempJStockOptions = File.createTempFile(Utils.getJStockUUID(), ".xml");
            tempJStockOptions.deleteOnExit();
            org.yccheok.jstock.gui.Utils.toXML(insensitiveJStockOptions, tempJStockOptions);
            fileExs.add(FileEx.newInstance(tempJStockOptions, "config" + File.separator + "options.xml"));
        } catch (IOException ex) {
            log.error(null, ex);
        }
        getFileEx(fileExs, "config");
        getFileEx(fileExs, "indicator");
        getFileEx(fileExs, "logos");
        for (Country country : Country.values()) {
            getFileEx(fileExs, country + File.separator + "portfolios");
            getFileEx(fileExs, country + File.separator + "config");
            getFileEx(fileExs, country + File.separator + "watchlist");
        }
        final byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        File temp = null;
        try {
            temp = File.createTempFile(Utils.getJStockUUID(), ".zip");
            temp.deleteOnExit();
            out = new ZipOutputStream(new FileOutputStream(temp));
            for (FileEx fileEx : fileExs) {
                FileInputStream in = null;
                try {
                    in = new FileInputStream(fileEx.input);
                    final String zipEntryName = fileEx.output;
                    out.putNextEntry(Utils.getZipEntry(zipEntryName));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (IOException exp) {
                    log.error(null, exp);
                    continue;
                } finally {
                    Utils.closeEntry(out);
                    Utils.close(in);
                }
            }
        } catch (IOException exp) {
            log.error(null, exp);
        } finally {
            Utils.close(out);
        }
        return temp;
    }
