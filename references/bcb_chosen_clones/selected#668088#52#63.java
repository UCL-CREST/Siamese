    @Override
    public void trainClassifier(File dir, String... args) throws Exception {
        String[] command = new String[args.length + 3];
        command[0] = this.getCommand();
        System.arraycopy(args, 0, command, 1, args.length);
        command[command.length - 2] = new File(dir, "training-data.libsvm").getPath();
        command[command.length - 1] = new File(dir, this.getModelName()).getPath();
        Process process = Runtime.getRuntime().exec(command);
        IOUtils.copy(process.getInputStream(), System.out);
        IOUtils.copy(process.getErrorStream(), System.err);
        process.waitFor();
    }
