    public DataSet guessAtUnknowns(String filename) {
        TasselFileType guess = TasselFileType.Sequence;
        DataSet tds = null;
        try {
            BufferedReader br = null;
            if (filename.startsWith("http")) {
                URL url = new URL(filename);
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            } else {
                br = new BufferedReader(new FileReader(filename));
            }
            String line1 = br.readLine().trim();
            String[] sval1 = line1.split("\\s");
            String line2 = br.readLine().trim();
            String[] sval2 = line2.split("\\s");
            boolean lociMatchNumber = false;
            if (!sval1[0].startsWith("<") && (sval1.length == 2) && (line1.indexOf(':') < 0)) {
                int countLoci = Integer.parseInt(sval1[1]);
                if (countLoci == sval2.length) {
                    lociMatchNumber = true;
                }
            }
            if (sval1[0].equalsIgnoreCase("<Annotated>")) {
                guess = TasselFileType.Annotated;
            } else if (line1.startsWith("<") || line1.startsWith("#")) {
                boolean isTrait = false;
                boolean isMarker = false;
                boolean isNumeric = false;
                boolean isMap = false;
                Pattern tagPattern = Pattern.compile("[<>\\s]+");
                String[] info1 = tagPattern.split(line1);
                String[] info2 = tagPattern.split(line2);
                if (info1.length > 1) {
                    if (info1[1].toUpperCase().startsWith("MARKER")) {
                        isMarker = true;
                    } else if (info1[1].toUpperCase().startsWith("TRAIT")) {
                        isTrait = true;
                    } else if (info1[1].toUpperCase().startsWith("NUMER")) {
                        isNumeric = true;
                    } else if (info1[1].toUpperCase().startsWith("MAP")) {
                        isMap = true;
                    }
                }
                if (info2.length > 1) {
                    if (info2[1].toUpperCase().startsWith("MARKER")) {
                        isMarker = true;
                    } else if (info2[1].toUpperCase().startsWith("TRAIT")) {
                        isTrait = true;
                    } else if (info2[1].toUpperCase().startsWith("NUMER")) {
                        isNumeric = true;
                    } else if (info2[1].toUpperCase().startsWith("MAP")) {
                        isMap = true;
                    }
                } else {
                    guess = null;
                    String inline = br.readLine();
                    while (guess == null && inline != null && (inline.startsWith("#") || inline.startsWith("<"))) {
                        if (inline.startsWith("<")) {
                            String[] info = tagPattern.split(inline);
                            if (info[1].toUpperCase().startsWith("MARKER")) {
                                isMarker = true;
                            } else if (info[1].toUpperCase().startsWith("TRAIT")) {
                                isTrait = true;
                            } else if (info[1].toUpperCase().startsWith("NUMER")) {
                                isNumeric = true;
                            } else if (info[1].toUpperCase().startsWith("MAP")) {
                                isMap = true;
                            }
                        }
                    }
                }
                if (isTrait || (isMarker && isNumeric)) {
                    guess = TasselFileType.Phenotype;
                } else if (isMarker) {
                    guess = TasselFileType.Polymorphism;
                } else if (isMap) {
                    guess = TasselFileType.GeneticMap;
                } else {
                    throw new IOException("Improperly formatted header. Data will not be imported.");
                }
            } else if ((line1.startsWith(">")) || (line1.startsWith(";"))) {
                guess = TasselFileType.Fasta;
            } else if (sval1.length == 1) {
                guess = TasselFileType.SqrMatrix;
            } else if (line1.indexOf(':') > 0) {
                guess = TasselFileType.Polymorphism;
            } else if ((sval1.length == 2) && (lociMatchNumber)) {
                guess = TasselFileType.Polymorphism;
            } else if ((line1.startsWith("#Nexus")) || (line1.startsWith("#NEXUS")) || (line1.startsWith("CLUSTAL")) || ((sval1.length == 2) && (sval2.length == 2))) {
                guess = TasselFileType.Sequence;
            } else if (sval1.length == 3) {
                guess = TasselFileType.Numerical;
            }
            myLogger.info("guessAtUnknowns: type: " + guess);
            tds = processDatum(filename, guess);
            br.close();
        } catch (Exception e) {
        }
        return tds;
    }
