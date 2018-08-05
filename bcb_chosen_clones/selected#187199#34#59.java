    public void testImport() throws Exception {
        Init.importEverything();
        String filename = Framework.onlyInstance().getAppHomeDir() + "sts-sql/example.stz";
        ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(filename));
        StringWriter swt;
        byte[] data;
        swt = new StringWriter();
        ExportCSVRegattaWizard erw = new ExportCSVRegattaWizard(new JFrame());
        erw.doExport(swt);
        data = swt.toString().getBytes();
        zo.putNextEntry(new ZipEntry("regatta.csv"));
        zo.write(data);
        swt = new StringWriter();
        ExportCSVEntriesWizard eew = new ExportCSVEntriesWizard(new JFrame());
        eew.doExport(swt);
        data = swt.toString().getBytes();
        zo.putNextEntry(new ZipEntry("entries.csv"));
        zo.write(data);
        swt = new StringWriter();
        ExportCSVRaceDataWizard edw = new ExportCSVRaceDataWizard(new JFrame());
        edw.doExport(swt);
        data = swt.toString().getBytes();
        zo.putNextEntry(new ZipEntry("race-data.csv"));
        zo.write(data);
        zo.close();
    }
