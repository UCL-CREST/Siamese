    public void run() {
        if (saveAsDialog == null) {
            saveAsDialog = new FileDialog(window.getShell(), SWT.SAVE);
            saveAsDialog.setFilterExtensions(saveAsTypes);
        }
        String outputFile = saveAsDialog.open();
        if (outputFile != null) {
            Object inputFile = DataSourceSingleton.getInstance().getContainer().getWrapped();
            InputStream in;
            try {
                if (inputFile instanceof URL) in = ((URL) inputFile).openStream(); else in = new FileInputStream((File) inputFile);
                OutputStream out = new FileOutputStream(outputFile);
                if (outputFile.endsWith("xml")) {
                    int c;
                    while ((c = in.read()) != -1) out.write(c);
                } else {
                    PrintWriter pw = new PrintWriter(out);
                    Element data = DataSourceSingleton.getInstance().getRawData();
                    writeTextFile(data, pw, -1);
                    pw.close();
                }
                in.close();
                out.close();
            } catch (MalformedURLException e1) {
            } catch (IOException e) {
            }
        }
    }
