    private void addReportToZip(ZipOutputStream out, ReportData report) throws IOException {
        ZipEntry entry = new ZipEntry(FDDPMA.REPORT_FILE_PREFIX + report.getId());
        out.putNextEntry(entry);
        out.write(report.getXmlData().getBytes());
    }
