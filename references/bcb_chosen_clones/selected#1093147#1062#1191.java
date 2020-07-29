        public void setResult(final PredictionResult result) {
            DefaultProteinsFastaWriter proteinsWriter = new DefaultProteinsFastaWriter();
            proteinsWriter.setDisplayNumber(true);
            proteinsWriter.setAAPerWord(10);
            proteinsWriter.setResidueAnnotator(new ResidueAnnotator() {

                public String annotate(Protein protein, int index) {
                    PredictionModel model = selectedModel();
                    String acc = protein.getAccession();
                    Map<Integer, Double> preds = result.getPredictedSites(model, acc);
                    String score = String.format("%." + scorePrecision + "f", preds.get(index));
                    String res = "[" + score + protein.getSequence().charAt(index) + "]";
                    if (groundTruth == null) {
                        return res;
                    }
                    Set<Integer> trueSites = new TreeSet();
                    Protein pro = groundTruth.getProtein(protein.getAccession());
                    if (pro != null) {
                        trueSites = musite.PTMAnnotationUtil.getSites(pro, model.getSupportedPTM(), model.getSupportedAminoAcid());
                    }
                    if (trueSites.isEmpty()) {
                        return res;
                    }
                    if (!preds.containsKey(index)) {
                        return res = "";
                    }
                    if (trueSites.contains(index)) {
                        res += "#";
                    }
                    return res;
                }

                public Set<Integer> indicesOfResidues(Protein protein) {
                    PredictionModel model = selectedModel();
                    Map<Integer, Double> preds = result.getPredictedSites(model, protein.getAccession());
                    if (preds == null) return new HashSet();
                    Set<Integer> sites = preds.keySet();
                    if (groundTruth != null) {
                        Protein pro = groundTruth.getProtein(protein.getAccession());
                        if (pro != null) {
                            sites.addAll(musite.PTMAnnotationUtil.getSites(pro, model.getSupportedPTM(), model.getSupportedAminoAcid()));
                        }
                    }
                    return sites;
                }
            });
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                proteinsWriter.write(baos, result);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            String str = baos.toString();
            str = str.replaceAll("\r\n", "\n");
            HashMap<Integer, Double> mapLocScore = new HashMap();
            HashSet<Integer> trueSites = new HashSet();
            int nRemoved = 0;
            Pattern p = Pattern.compile("\\[(-?[0-9]*\\.[0-9]{" + scorePrecision + "})?(p?).\\]");
            Matcher m = p.matcher(str);
            while (m.find()) {
                int start = m.start();
                int site = start - nRemoved;
                String s = m.group(1);
                if (s != null && s.length() > 0) {
                    nRemoved += s.length();
                    Double score = Double.valueOf(s);
                    mapLocScore.put(site, score);
                }
                s = m.group(2);
                if (s != null && s.length() == 1) {
                    nRemoved++;
                    trueSites.add(site);
                }
                nRemoved += 2;
            }
            str = str.replaceAll("\\[(-?[0-9]*\\.[0-9]{" + scorePrecision + "})?p?(.)\\]", "$2");
            List<int[]> headerLocs = new ArrayList();
            p = Pattern.compile(">[^\n]+");
            m = p.matcher(str);
            while (m.find()) {
                int[] locs = new int[2];
                locs[0] = m.start();
                locs[1] = m.end();
                headerLocs.add(locs);
            }
            List<int[]> numberLocs = new ArrayList();
            p = Pattern.compile("\n([0-9]+ *)");
            m = p.matcher(str);
            while (m.find()) {
                int[] locs = new int[2];
                locs[0] = m.start(1);
                locs[1] = m.end(1);
                numberLocs.add(locs);
            }
            suppressRepaint = true;
            setText(str);
            StyledDocument doc = getStyledDocument();
            {
                SimpleAttributeSet attrSet = new SimpleAttributeSet();
                StyleConstants.setBackground(attrSet, Color.LIGHT_GRAY);
                for (int[] header : headerLocs) {
                    doc.setCharacterAttributes(header[0], header[1] - header[0], attrSet, false);
                }
            }
            {
                SimpleAttributeSet attrSet = new SimpleAttributeSet();
                StyleConstants.setForeground(attrSet, Color.GRAY);
                for (int[] num : numberLocs) {
                    doc.setCharacterAttributes(num[0], num[1] - num[0], attrSet, false);
                }
            }
            {
                for (Map.Entry<Integer, Double> entry : mapLocScore.entrySet()) {
                    int site = entry.getKey();
                    double score = entry.getValue();
                    Color color = getColorOfSite(score);
                    SimpleAttributeSet attrSet = new SimpleAttributeSet();
                    StyleConstants.setBackground(attrSet, color);
                    StyleConstants.setForeground(attrSet, invertColor(color));
                    doc.setCharacterAttributes(site, 1, attrSet, false);
                }
                for (int site : trueSites) {
                    SimpleAttributeSet attrSet = new SimpleAttributeSet();
                    StyleConstants.setUnderline(attrSet, true);
                    doc.setCharacterAttributes(site, 1, attrSet, false);
                }
            }
            suppressRepaint = false;
            repaint();
        }
