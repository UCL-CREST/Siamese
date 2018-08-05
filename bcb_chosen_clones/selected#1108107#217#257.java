    private static void performAction(SearchElement elt, Desktop.Action action) {
        try {
            if (!Desktop.isDesktopSupported()) return;
            File file = null;
            if (elt.isFile()) {
                file = new File(elt.toString());
            } else if (elt.isArchiveEntry()) {
                List<SearchElement> parents = elt.getPath();
                InputStream rootIs = new BufferedInputStream(new FileInputStream(parents.get(0).getName()));
                ZipInputStream currentZis = new ZipInputStream(rootIs);
                for (int i = 1; i < parents.size(); i++) {
                    SearchElement p = parents.get(i);
                    byte[] bytes = getEntryAsBytes(currentZis, p.getName());
                    if (bytes == null) return;
                    currentZis = new ZipInputStream(new ByteArrayInputStream(bytes));
                }
                byte[] bytes = getEntryAsBytes(currentZis, elt.getName());
                if (bytes == null) return;
                String name = getFileShortName(elt.getName());
                String tmpDir = System.getProperty("java.io.tmpdir");
                file = new File(tmpDir + File.separatorChar + name);
                file.deleteOnExit();
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                os.write(bytes);
                os.flush();
                os.close();
            }
            if ((file != null) && file.exists()) {
                switch(action) {
                    case OPEN:
                        Desktop.getDesktop().open(file);
                        break;
                    case EDIT:
                        Desktop.getDesktop().edit(file);
                        break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
