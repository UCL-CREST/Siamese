    private void transform(CommandLine commandLine) throws IOException {
        Reader reader;
        if (commandLine.hasOption('i')) {
            reader = createFileReader(commandLine.getOptionValue('i'));
        } else {
            reader = createStandardInputReader();
        }
        Writer writer;
        if (commandLine.hasOption('o')) {
            writer = createFileWriter(commandLine.getOptionValue('o'));
        } else {
            writer = createStandardOutputWriter();
        }
        String mapRule = commandLine.getOptionValue("m");
        try {
            SrxTransformer transformer = new SrxAnyTransformer();
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            if (mapRule != null) {
                parameterMap.put(Srx1Transformer.MAP_RULE_NAME, mapRule);
            }
            transformer.transform(reader, writer, parameterMap);
        } finally {
            cleanupReader(reader);
            cleanupWriter(writer);
        }
    }
