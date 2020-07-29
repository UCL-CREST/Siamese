    @Override
    public void Test(String filename, String modelfile, String perdictFilename) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            String s = this.TestCommand + "  " + filename;
            if (modelfile != null) s += "  -model " + modelfile;
            if (perdictFilename != null) s += "  -per " + perdictFilename;
            s += "  " + this.OptionString;
            logger.info(s);
            p = r.exec(s);
            InputStream in = p.getInputStream();
            OutputStream out = p.getOutputStream();
            InputStream err = p.getErrorStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            BufferedReader inputerr = new BufferedReader(new InputStreamReader(err));
            String line;
            boolean flag = false;
            int count1 = 0;
            int count2 = 0;
            String TheSummary = "[Summary]";
            while ((line = input.readLine()) != null) {
                logger.info(line);
                if (TheSummary.equals(line)) {
                    flag = !flag;
                }
                if (flag) {
                    logCompare.info(line);
                }
            }
            while ((line = inputerr.readLine()) != null) {
                logger.error(line);
            }
            input.close();
            out.write(4);
        } catch (Exception e) {
            logger.error("error===" + e.getMessage());
            e.printStackTrace();
        }
    }
