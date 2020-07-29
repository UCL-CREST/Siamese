    public void execute() throws BuildException {
        Enumeration list = getIndividualTests();
        String srcRptFile = "";
        String destRptFile = "";
        while (list.hasMoreElements()) {
            J3DPerfUnitTest test = (J3DPerfUnitTest) list.nextElement();
            if (test.shouldRun(getProject())) {
                try {
                    execute(test);
                    try {
                        srcRptFile = getProject().resolveFile(".").getAbsolutePath() + "\\" + test.getName() + ".txt";
                        destRptFile = getProject().resolveFile(test.getName() + ".txt", new File(test.getTodir())).getAbsolutePath();
                        FileChannel srcChannel = new FileInputStream(srcRptFile).getChannel();
                        FileChannel dstChannel = new FileOutputStream(destRptFile).getChannel();
                        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                        srcChannel.close();
                        dstChannel.close();
                        if (!srcRptFile.equals(destRptFile)) {
                            new File(srcRptFile).delete();
                        }
                    } catch (IOException e) {
                    }
                } catch (BuildException e) {
                    e.printStackTrace();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
