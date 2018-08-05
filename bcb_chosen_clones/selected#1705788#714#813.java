    private DistanceMatrix readDistancesBlock(List<Taxon> taxonList) throws ImportException, IOException {
        if (taxonList == null) {
            throw new ImportException.BadFormatException("Missing Taxa for reading distances");
        }
        Triangle triangle = Triangle.LOWER;
        boolean diagonal = true;
        boolean labels = false;
        boolean ttl = false, fmt = false;
        String token = helper.readToken();
        while (!token.equalsIgnoreCase("MATRIX")) {
            if (token.equalsIgnoreCase("TITLE")) {
                if (ttl) {
                    throw new ImportException.DuplicateFieldException("TITLE");
                }
                ttl = true;
            } else if (token.equalsIgnoreCase("FORMAT")) {
                if (fmt) {
                    throw new ImportException.DuplicateFieldException("FORMAT");
                }
                sequenceType = null;
                do {
                    String token2 = helper.readToken("=;");
                    if (token2.equalsIgnoreCase("TRIANGLE")) {
                        if (helper.getLastDelimiter() != '=') {
                            throw new ImportException.BadFormatException("Expecting '=' after TRIANGLE subcommand in FORMAT command");
                        }
                        String token3 = helper.readToken(";");
                        if (token3.equalsIgnoreCase("LOWER")) {
                            triangle = Triangle.LOWER;
                        } else if (token3.equalsIgnoreCase("UPPER")) {
                            triangle = Triangle.UPPER;
                        } else if (token3.equalsIgnoreCase("BOTH")) {
                            triangle = Triangle.BOTH;
                        }
                    } else if (token2.equalsIgnoreCase("NODIAGONAL")) {
                        diagonal = false;
                    } else if (token2.equalsIgnoreCase("LABELS")) {
                        labels = true;
                    }
                } while (helper.getLastDelimiter() != ';');
                fmt = true;
            }
            token = helper.readToken();
        }
        double[][] distances = new double[taxonList.size()][taxonList.size()];
        for (int i = 0; i < taxonList.size(); i++) {
            token = helper.readToken();
            Taxon taxon = Taxon.getTaxon(token);
            int index = taxonList.indexOf(taxon);
            if (index < 0) {
                StringBuilder message = new StringBuilder("Expected: ").append(token).append("\nActual taxa:\n");
                for (Taxon taxon1 : taxonList) {
                    message.append(taxon1).append("\n");
                }
                throw new ImportException.UnknownTaxonException(message.toString());
            }
            if (index != i) {
                throw new ImportException.BadFormatException("The taxon labels are in a different order to those in the TAXA block");
            }
            if (triangle == Triangle.LOWER) {
                for (int j = 0; j < i + 1; j++) {
                    if (i != j) {
                        distances[i][j] = helper.readDouble();
                        distances[j][i] = distances[i][j];
                    } else {
                        if (diagonal) {
                            distances[i][j] = helper.readDouble();
                        }
                    }
                }
            } else if (triangle == Triangle.UPPER) {
                for (int j = i; j < taxonList.size(); j++) {
                    if (i != j) {
                        distances[i][j] = helper.readDouble();
                        distances[j][i] = distances[i][j];
                    } else {
                        if (diagonal) {
                            distances[i][j] = helper.readDouble();
                        }
                    }
                }
            } else {
                for (int j = 0; j < taxonList.size(); j++) {
                    if (i != j || diagonal) {
                        distances[i][j] = helper.readDouble();
                    } else {
                        distances[i][j] = 0.0;
                    }
                }
            }
            if (helper.getLastDelimiter() == ';' && i < taxonList.size() - 1) {
                throw new ImportException.TooFewTaxaException();
            }
        }
        if (helper.nextCharacter() != ';') {
            throw new ImportException.BadFormatException("Expecting ';' after sequences data");
        }
        findEndBlock();
        return new BasicDistanceMatrix(taxonList, distances);
    }
