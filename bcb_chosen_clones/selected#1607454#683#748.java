        byte[][] genotypeData() {

            // parse the genotype information in scores strings
            List<String> scores = new ArrayList<>();

            // get the genotype text
            String genotypeText = getGenotypeText();

            // parse genotype text for scores
            char pos18;
            String genotypeScore = null;

            for (String line : genotypeText.split("\n")) {

                line = line.trim();

                if (line.length() > 18) {

                    pos18 = line.charAt(17);

                    if (pos18 == 'M' || pos18 == 'O' || pos18 == '-') {

                        // genes and scores
                        genotypeScore = line.substring(17).replace("M", "1").
                                replace("O", "0").replace("|", " ").replace("-", "" + MISSING_VALUE_CODE);

                        processScores(line.substring(0, 17), genotypeScore, scores);

                    } else {
                        // only genes (line length > 18)
                        processScores(line, genotypeScore, scores);
                    }

                } else {
                    // only genes (line length <= 18)
                    processScores(line, genotypeScore, scores);

                }

            }

            // turn the score strings into int array
            byte[][] data1 = new byte[scores.size()][];
            for (int k = 0; k < scores.size(); k++) {

                String[] vals = scores.get(k).split("\\s");
                byte[] row = new byte[vals.length];
                for (int i = 0; i < vals.length; i++) {
                    row[i] = Byte.valueOf(vals[i]).byteValue();
                }

                data1[k] = row;

            }

            // transpose the array row: samples col: genes (original had row: gene, col: samples)
            byte[][] data2 = new byte[data1[0].length][data1.length];
            for (int i = 0; i < data1.length; i++) {
                for (int j = 0; j < data1[0].length; j++) {
                    data2[j][i] = data1[i][j];

                }
            }

            return data2;
        }
