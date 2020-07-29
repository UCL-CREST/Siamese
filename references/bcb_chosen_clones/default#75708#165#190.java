    private void TrainAllFilesTask() {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            String s;
            if (ProgramFile.equals("")) {
                if (State == TRAIN) s = TrainingProgramName; else {
                    s = TestProgramName;
                }
            } else s = ProgramFile;
            p = r.exec(s);
            InputStream in = p.getInputStream();
            OutputStream out = p.getOutputStream();
            InputStream err = p.getErrorStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = input.readLine()) != null) {
                logger.info(line);
            }
            input.close();
            out.write(4);
        } catch (Exception e) {
            System.out.println("error===" + e.getMessage());
            e.printStackTrace();
        }
    }
