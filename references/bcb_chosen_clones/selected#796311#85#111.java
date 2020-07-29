    public static void main(String[] args) {
        try {
            JasperReport report = JasperCompileManager.compileReport("/home/jose/Projects/telmma/Documents/Code/testParameters.jrxml");
            System.out.println("Query en el jasper: " + report.getQuery().getText());
            for (JRParameter param : report.getParameters()) {
                if (!param.isSystemDefined()) System.out.println(param.getName());
            }
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("NombreCiudad", "Huelva");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            byte[] reportBin = JasperExportManager.exportReportToPdf(jasperPrint);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                File outputFile;
                try {
                    outputFile = File.createTempFile("simple_report", ".pdf");
                    FileUtils.writeByteArrayToFile(outputFile, reportBin);
                    System.out.println("OutputFile -> " + outputFile.getName() + " " + outputFile.getTotalSpace());
                    desktop.open(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
