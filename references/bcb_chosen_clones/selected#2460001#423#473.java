    private void processAlignmentsFromAlignmentSource(String name, Alignment reference, String alignmentSource) throws AlignmentParserException, IllegalArgumentException, KADMOSCMDException, IOException {
        if (alignmentSource == null) throw new IllegalArgumentException("alignmentSource is null");
        URL url;
        String st;
        BufferedReader reader;
        Alignment alignment;
        try {
            try {
                alignment = parseAlignment(alignmentSource);
                addAlignmentWrapper(new AlignmentWrapper(name, reference, alignmentSource, alignment));
            } catch (AlignmentParserException e1) {
                url = new URL(alignmentSource);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                st = "";
                while (((st = reader.readLine()) != null)) {
                    alignment = parseAlignment(st);
                    addAlignmentWrapper(new AlignmentWrapper(name, reference, alignmentSource, alignment));
                }
            }
        } catch (Exception e1) {
            File itemFile = new File(alignmentSource);
            if (itemFile.exists()) {
                if (itemFile.isDirectory() && !itemFile.isHidden()) {
                    File[] files = itemFile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile() && !files[i].isHidden()) {
                            processAlignmentsFromAlignmentSource(name, reference, files[i].getPath());
                        } else if (files[i].isDirectory() && !files[i].isHidden() && deepScan) {
                            processAlignmentsFromAlignmentSource(name, reference, files[i].getPath());
                        }
                    }
                } else if (itemFile.isFile() && !itemFile.isHidden()) {
                    try {
                        alignment = parseAlignment(alignmentSource);
                        addAlignmentWrapper(new AlignmentWrapper(name, reference, alignmentSource, alignment));
                    } catch (Exception e2) {
                        reader = new BufferedReader(new FileReader(alignmentSource));
                        st = "";
                        while (((st = reader.readLine()) != null)) {
                            alignment = parseAlignment(st);
                            addAlignmentWrapper(new AlignmentWrapper(name, reference, st, alignment));
                        }
                    }
                } else {
                    throw new FileNotFoundException("File " + alignmentSource + " is neither directory nor file, or it is hidden.");
                }
            } else {
                throw new FileNotFoundException("File " + alignmentSource + " does not exists.");
            }
        }
    }
