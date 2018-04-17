    @SuppressWarnings("unchecked")
    public void solve() throws Exception {
        System.out.println("Fast Solver triggered");
        ArrayList<Object> list;
        String train_str = "";
        String test_str = "";
        String landmarks_str = "";
        String cmd, stats_file, tmp;
        Process p;
        this.createWorkDirectory();
        File codeWorkDirFile = (new File(this.codeWorkDir)).getAbsoluteFile();
        list = (ArrayList<Object>) this.info.get("landmarks");
        for (Object obj : list) landmarks_str = landmarks_str + ((Landmark) obj).toString2() + "\n";
        PrintStream landmarks_f = new PrintStream(new File(this.dataWorkDir + "/" + landmarks_file));
        this.filesCreated = this.filesCreated + " " + landmarks_file;
        landmarks_f.print(landmarks_str);
        landmarks_f.close();
        list = (ArrayList<Object>) this.info.get("training");
        for (Object obj : list) train_str = train_str + obj.toString() + "\n";
        list = (ArrayList<Object>) this.info.get("testing");
        if (this.info.get("algorithm").equals("fs_m1")) {
            for (int i = 0; i < list.size(); i++) {
                test_str = list.get(i).toString();
                PrintStream model_f = new PrintStream(new File(this.dataWorkDir + "/" + model_file));
                this.filesCreated = this.filesCreated + " " + model_file;
                model_f.println(train_str + test_str);
                model_f.close();
                stats_file = dataWorkDir + "/" + i + STAT_FILE_SUFFIX;
                this.filesCreated = this.filesCreated + " " + stats_file;
                tmp = (this.info.containsKey("burin") ? this.info.get("burnin") : new Integer(1000)) + " " + (this.info.containsKey("iterations") ? this.info.get("iterations") : new Integer(5000)) + " " + dataWorkDir + "/" + model_file + " " + dataWorkDir + "/" + landmarks_file + " " + this.info.get("maxx") + " " + this.info.get("maxy");
                cmd = codeWorkDirFile.getPath() + "/M1 " + tmp + " slice1 -a " + stats_file + " -d";
                System.out.println("Solve:\n" + cmd);
                p = Runtime.getRuntime().exec(cmd);
                ProcessOutputHandler.create(p);
                while (true) {
                    try {
                        p.waitFor();
                        break;
                    } catch (InterruptedException e) {
                    } finally {
                        if (p != null) {
                            closehandle(p.getOutputStream());
                            closehandle(p.getInputStream());
                            closehandle(p.getErrorStream());
                            p.destroy();
                        }
                    }
                }
            }
        } else {
            test_str = "";
            for (int i = 0; i < list.size(); i++) test_str = test_str + list.get(i).toString() + "\n";
            PrintStream model_f = new PrintStream(new File(this.dataWorkDir + "/" + model_file));
            this.filesCreated = this.filesCreated + " " + model_file;
            model_f.println(train_str + test_str);
            model_f.close();
            stats_file = dataWorkDir + "/" + "all" + STAT_FILE_SUFFIX;
            this.filesCreated = this.filesCreated + " " + stats_file;
            tmp = (this.info.containsKey("burin") ? this.info.get("burnin") : new Integer(1000)) + " " + (this.info.containsKey("iterations") ? this.info.get("iterations") : new Integer(5000)) + " " + dataWorkDir + "/" + model_file + " " + dataWorkDir + "/" + landmarks_file + " " + this.info.get("maxx") + " " + this.info.get("maxy");
            if (this.info.get("algorithm").equals("fs_m2")) cmd = codeWorkDirFile.getPath() + "/M2 " + tmp + " slice1 -a " + stats_file + " -d"; else cmd = codeWorkDirFile.getPath() + "/M2 " + tmp + " slice1all -a " + stats_file + " -d";
            System.out.println("Solve:\n" + cmd);
            p = Runtime.getRuntime().exec(cmd);
            ProcessOutputHandler.create(p);
            while (true) {
                try {
                    p.waitFor();
                    break;
                } catch (InterruptedException e) {
                } finally {
                    if (p != null) {
                        closehandle(p.getOutputStream());
                        closehandle(p.getInputStream());
                        closehandle(p.getErrorStream());
                        p.destroy();
                    }
                }
            }
        }
        System.out.println("Done!");
    }
