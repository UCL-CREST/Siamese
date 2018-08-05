    public void run() {
        try {
            ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(getFilename()));
            StringWriter swt;
            byte[] data;
            appendProgressMessage("Exporting regatta...");
            swt = new StringWriter();
            ExportCSVRegattaWizard erw = new ExportCSVRegattaWizard(new JFrame());
            erw.addProgressMessageListener(this);
            erw.doExport(swt);
            data = swt.toString().getBytes();
            zo.putNextEntry(new ZipEntry("regatta.csv"));
            zo.write(data);
            appendProgressMessage("Exporting entries...");
            swt = new StringWriter();
            ExportCSVEntriesWizard eew = new ExportCSVEntriesWizard(new JFrame());
            eew.addProgressMessageListener(this);
            eew.doExport(swt);
            data = swt.toString().getBytes();
            zo.putNextEntry(new ZipEntry("entries.csv"));
            zo.write(data);
            appendProgressMessage("Exporting race data...");
            swt = new StringWriter();
            ExportCSVRaceDataWizard edw = new ExportCSVRaceDataWizard(new JFrame());
            edw.addProgressMessageListener(this);
            edw.doExport(swt);
            data = swt.toString().getBytes();
            zo.putNextEntry(new ZipEntry("race-data.csv"));
            zo.write(data);
            if (firstState.panel.getIncludeCourses()) {
                appendProgressMessage("Exporting courses...");
                swt = new StringWriter();
                ExportCSVCoursesWizard ecw = new ExportCSVCoursesWizard(new JFrame());
                ecw.addProgressMessageListener(this);
                ecw.doExport(swt);
                data = swt.toString().getBytes();
                zo.putNextEntry(new ZipEntry("courses.csv"));
                zo.write(data);
            }
            zo.close();
            appendProgressMessage("Done...");
            appendProgressMessage(null);
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
    }
