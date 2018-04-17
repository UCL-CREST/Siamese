    public void end() throws Exception {
        handle.waitFor();
        Calendar endTime = Calendar.getInstance();
        File resultsDir = new File(runDir, "results");
        if (!resultsDir.isDirectory()) throw new Exception("The results directory not found!");
        String resHtml = null;
        String resTxt = null;
        String[] resultFiles = resultsDir.list();
        for (String resultFile : resultFiles) {
            if (resultFile.indexOf("html") >= 0) resHtml = resultFile; else if (resultFile.indexOf("txt") >= 0) resTxt = resultFile;
        }
        if (resHtml == null) throw new IOException("SPECweb2005 output (html) file not found");
        if (resTxt == null) throw new IOException("SPECweb2005 output (txt) file not found");
        File resultHtml = new File(resultsDir, resHtml);
        copyFile(resultHtml.getAbsolutePath(), runDir + "SPECWeb-result.html", false);
        BufferedReader reader = new BufferedReader(new FileReader(new File(resultsDir, resTxt)));
        logger.fine("Text file: " + resultsDir + resTxt);
        Writer writer = new FileWriter(runDir + "summary.xml");
        SummaryParser parser = new SummaryParser(getRunId(), startTime, endTime, logger);
        parser.convert(reader, writer);
        writer.close();
        reader.close();
    }
