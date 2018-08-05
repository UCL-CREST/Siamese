    @NotNull
    public static Map<CompilerMessageCategory, List<JFlexMessage>> compile(VirtualFile file, Sdk projectSdk) throws IOException, CantRunException {
        JFlexSettings settings = JFlexSettings.getInstance();
        JavaParameters javaParameters = new JavaParameters();
        javaParameters.setJdk(projectSdk);
        javaParameters.setMainClass(JFLEX_MAIN_CLASS);
        javaParameters.getClassPath().add(JFLEX_JAR_PATH);
        StringBuilder command = new StringBuilder(CommandLineBuilder.createFromJavaParameters(javaParameters).getCommandLineString());
        String options = MessageFormat.format(" {0} ", settings.COMMAND_LINE_OPTIONS);
        if (!StringUtil.isEmptyOrSpaces(options)) {
            command.append(options);
        }
        if (!StringUtil.isEmptyOrSpaces(settings.SKELETON_PATH) && options.indexOf(OPTION_SKEL) == -1) {
            command.append(OPTION_SKEL).append(QUOT).append(settings.SKELETON_PATH).append(QUOT);
        }
        if (options.indexOf(OPTION_Q) == -1 && options.indexOf(OPTION_QUIET) == -1) {
            command.append(OPTION_QUIET);
        }
        VirtualFile parent = file.getParent();
        if (parent != null) {
            command.append(OPTION_D).append(QUOT).append(parent.getPath()).append(QUOT);
        }
        command.append(SPACE).append(QUOT).append(file.getPath()).append(QUOT);
        String shell = SystemInfo.isWindows ? SystemInfo.isWindows9x ? COMMAND_COM : CMD_EXE : BIN_BASH;
        String[] commands;
        if (SystemInfo.isWindows) {
            commands = new String[] { shell, SLASH_C, QUOT + command.toString() + QUOT };
        } else {
            commands = new String[] { shell, HYPHEN_C, command.toString() };
        }
        Process process = Runtime.getRuntime().exec(commands, null, new File(settings.JFLEX_HOME));
        try {
            InputStream out = process.getInputStream();
            try {
                InputStream err = process.getErrorStream();
                try {
                    List<JFlexMessage> information = new ArrayList<JFlexMessage>();
                    List<JFlexMessage> error = new ArrayList<JFlexMessage>();
                    filter(StreamUtil.readText(out), information, error);
                    filter(StreamUtil.readText(err), information, error);
                    Map<CompilerMessageCategory, List<JFlexMessage>> messages = new HashMap<CompilerMessageCategory, List<JFlexMessage>>();
                    messages.put(CompilerMessageCategory.ERROR, error);
                    messages.put(CompilerMessageCategory.INFORMATION, information);
                    int code = 0;
                    try {
                        code = process.waitFor();
                    } catch (InterruptedException e) {
                        List<JFlexMessage> warnings = new ArrayList<JFlexMessage>();
                        warnings.add(new JFlexMessage("Interrupted while waiting for Jflex to complete"));
                        messages.put(CompilerMessageCategory.WARNING, warnings);
                    }
                    if (code == 0) {
                        return messages;
                    } else {
                        if (messages.get(CompilerMessageCategory.ERROR).size() > 0) {
                            return messages;
                        }
                        throw new IOException(JFlexBundle.message("command.0.execution.failed.with.exit.code.1", command, code));
                    }
                } finally {
                    err.close();
                }
            } finally {
                out.close();
            }
        } finally {
            process.destroy();
        }
    }
