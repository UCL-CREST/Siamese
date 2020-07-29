    public void processFile(File file, String outputType, boolean hasQuery) {
        InputStream reportStream = null;
        try {
            reportStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = null;
            if (hasQuery) jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn); else jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, service);
            byte[] reportBin = null;
            if ("pdf".equals(outputType)) {
                reportBin = JasperExportManager.exportReportToPdf(jasperPrint);
            } else if ("xls".equals(outputType)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                JRExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                exporter.exportReport();
                reportBin = baos.toByteArray();
            }
            setRawData(reportBin);
        } catch (JRException e) {
            System.out.println("Error processing report: " + e);
            e.printStackTrace();
        }
    }
