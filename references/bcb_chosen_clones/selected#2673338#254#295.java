    private static String parsePFFSequenceString(String richStr, Map<Integer, JPLMass> masses, JPLMassAccuracy massType) throws JPLParseException {
        if (isSequenceWithMolecularFormulaTypeModif(richStr)) {
            richStr = modifyMolecFormula2DoubleMassModifInSequence(richStr, massType);
        }
        Pattern modPattern = Pattern.compile("\\(\\{-?[\\d.]+\\}\\)");
        Matcher matcher = modPattern.matcher(richStr);
        StringBuilder nakedSeqStr = new StringBuilder();
        int cumulatedPatternLength = 0;
        int richSeqFrom = 0;
        while (matcher.find()) {
            cumulatedPatternLength += matcher.end() - matcher.start();
            int pos = matcher.end() - 1 - cumulatedPatternLength;
            String massStr = richStr.substring(matcher.start() + 2, matcher.end() - 2);
            JPLMass mass = null;
            if (massType == JPLMassAccuracy.MONOISOTOPIC) {
                mass = JPLMass.MonoisotopicMass(Double.parseDouble(massStr));
            } else if (massType == JPLMassAccuracy.AVERAGE) {
                mass = JPLMass.AverageMass(Double.parseDouble(massStr));
            } else {
                throw new IllegalStateException("mass type is not defined");
            }
            if (pos == -1) {
                if (richStr.charAt(matcher.end()) == NTermModifDelimitor) {
                    cumulatedPatternLength++;
                    richSeqFrom = matcher.end() + 1;
                } else {
                    throw new JPLParseException("bad character : '" + richStr.charAt(matcher.end()) + "'", matcher.end());
                }
            } else if (richStr.charAt(matcher.start() - 1) == CTermModifDelimitor) {
                nakedSeqStr.append(richStr.substring(richSeqFrom, matcher.start() - 1));
                richSeqFrom = matcher.end();
            } else {
                nakedSeqStr.append(richStr.substring(richSeqFrom, matcher.start()));
                richSeqFrom = matcher.end();
            }
            masses.put(pos, mass);
        }
        if (richSeqFrom < richStr.length()) {
            nakedSeqStr.append(richStr.substring(richSeqFrom));
        }
        return nakedSeqStr.toString();
    }
