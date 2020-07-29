    private List<Element> ReadAtomListFromCommentedLines(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        List<Element> eltList = new ArrayList<Element>();
        String line;
        Matcher matcher;
        Pattern namePattern = Pattern.compile("^([A-Z]+)\\s+");
        Pattern isotopePattern = Pattern.compile("^([A-Z][a-z]?)\\((\\d+)\\)\\s+(\\S+)\\s+(\\S+)\\s*");
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null && !line.equals("//START")) ;
        if (line == null) throw new Exception("Unable to find the START tag");
        while ((line = br.readLine()) != null && !line.equals("//END")) {
            sb.setLength(0);
            sb.append(line);
            matcher = namePattern.matcher(sb);
            if (!matcher.find()) throw new Exception("The atom commented line should start by the name in uppercase");
            Element elt = new Element(matcher.group(1));
            sb.delete(matcher.start(), matcher.end());
            matcher = isotopePattern.matcher(sb);
            while (matcher.find()) {
                String atomLabel = matcher.group(1);
                Integer isotopeIdx = new Integer(matcher.group(2));
                String strMass = matcher.group(3);
                Double mass = new Double(strMass);
                Double abundance = new Double(matcher.group(4));
                if (elt.label == null) elt.label = atomLabel; else if (!elt.label.equals(atomLabel)) throw new Exception("The isotopes of the atom " + elt.name + "should have the same label");
                elt.addIsotope(new Isotope(isotopeIdx, strMass, mass, abundance));
                sb.delete(matcher.start(), matcher.end());
                matcher = isotopePattern.matcher(sb);
            }
            elt.init();
            eltList.add(elt);
        }
        if (line == null) throw new Exception("Reached EOF before '//END' tag");
        br.close();
        return eltList;
    }
