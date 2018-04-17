    public String closeOutput(String rootDirectory, String workDirectory) throws IOException {
        agenciesOut.close();
        stopsOut.close();
        routesOut.close();
        tripsOut.close();
        calendarsOut.close();
        if (calendarDatesOut != null) calendarDatesOut.close();
        agenciesOut = null;
        stopsOut = null;
        routesOut = null;
        tripsOut = null;
        calendarsOut = null;
        calendarDatesOut = null;
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outdir + "/" + gtfsZipfileName));
        byte[] buf = new byte[1024];
        for (int i = 0; i < filenames.size(); i++) {
            FileInputStream in = new FileInputStream(outdir + "/" + (String) filenames.get(i));
            zipOut.putNextEntry(new ZipEntry((String) filenames.get(i)));
            int len;
            while ((len = in.read(buf)) > 0) {
                zipOut.write(buf, 0, len);
            }
            zipOut.closeEntry();
            in.close();
        }
        zipOut.close();
        return workDirectory + "/" + "google_transit.zip";
    }
