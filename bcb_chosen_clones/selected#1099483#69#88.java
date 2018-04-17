    private void invokeTest(String queryfile, String target) {
        try {
            String query = IOUtils.toString(XPathMarkBenchmarkTest.class.getResourceAsStream(queryfile)).trim();
            String args = EXEC_CMD + " \"" + query + "\" \"" + target + '"';
            System.out.println("Invoke command: \n " + args);
            Process proc = Runtime.getRuntime().exec(args, null, benchmarkDir);
            InputStream is = proc.getInputStream();
            File outFile = new File(outDir, queryfile + ".result");
            IOUtils.copy(is, new FileOutputStream(outFile));
            is.close();
            int ret = proc.waitFor();
            if (ret != 0) {
                System.out.println("process exited with value : " + ret);
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        } catch (InterruptedException irre) {
            throw new IllegalStateException(irre);
        }
    }
