    public static void main(String args[]) throws IOException, TrimmerException, DataStoreException {
        Options options = new Options();
        options.addOption(new CommandLineOptionBuilder("ace", "path to ace file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("phd", "path to phd file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("out", "path to new ace file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("min_sanger", "min sanger end coveage default =" + DEFAULT_MIN_SANGER_END_CLONE_CVG).build());
        options.addOption(new CommandLineOptionBuilder("min_biDriection", "min bi directional end coveage default =" + DEFAULT_MIN_BI_DIRECTIONAL_END_COVERAGE).build());
        options.addOption(new CommandLineOptionBuilder("ignore_threshold", "min end coveage threshold to stop trying to trim default =" + DEFAULT_IGNORE_END_CVG_THRESHOLD).build());
        CommandLine commandLine;
        PhdDataStore phdDataStore = null;
        AceContigDataStore datastore = null;
        try {
            commandLine = CommandLineUtils.parseCommandLine(options, args);
            int minSangerEndCloneCoverage = commandLine.hasOption("min_sanger") ? Integer.parseInt(commandLine.getOptionValue("min_sanger")) : DEFAULT_MIN_SANGER_END_CLONE_CVG;
            int minBiDirectionalEndCoverage = commandLine.hasOption("min_biDriection") ? Integer.parseInt(commandLine.getOptionValue("min_biDriection")) : DEFAULT_MIN_BI_DIRECTIONAL_END_COVERAGE;
            int ignoreThresholdEndCoverage = commandLine.hasOption("ignore_threshold") ? Integer.parseInt(commandLine.getOptionValue("ignore_threshold")) : DEFAULT_IGNORE_END_CVG_THRESHOLD;
            AceContigTrimmer trimmer = new NextGenClosureAceContigTrimmer(minSangerEndCloneCoverage, minBiDirectionalEndCoverage, ignoreThresholdEndCoverage);
            File aceFile = new File(commandLine.getOptionValue("ace"));
            File phdFile = new File(commandLine.getOptionValue("phd"));
            phdDataStore = new DefaultPhdFileDataStore(phdFile);
            datastore = new IndexedAceFileDataStore(aceFile);
            File tempFile = File.createTempFile("nextGenClosureAceTrimmer", ".ace");
            tempFile.deleteOnExit();
            OutputStream tempOut = new FileOutputStream(tempFile);
            int numberOfContigs = 0;
            int numberOfTotalReads = 0;
            for (AceContig contig : datastore) {
                AceContig trimmedAceContig = trimmer.trimContig(contig);
                if (trimmedAceContig != null) {
                    numberOfContigs++;
                    numberOfTotalReads += trimmedAceContig.getNumberOfReads();
                    AceFileWriter.writeAceFile(trimmedAceContig, phdDataStore, tempOut);
                }
            }
            IOUtil.closeAndIgnoreErrors(tempOut);
            OutputStream masterAceOut = new FileOutputStream(new File(commandLine.getOptionValue("out")));
            masterAceOut.write(String.format("AS %d %d%n", numberOfContigs, numberOfTotalReads).getBytes());
            InputStream tempInput = new FileInputStream(tempFile);
            IOUtils.copy(tempInput, masterAceOut);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
        } finally {
            IOUtil.closeAndIgnoreErrors(phdDataStore, datastore);
        }
    }
