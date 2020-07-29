    public CCompoundLocation convertSecondaryStructure(String secondary) {
        CCompoundLocation location = new CCompoundLocation();
        CCompoundLocation.NamedLocation hloc = new CCompoundLocation.NamedLocation(H);
        CCompoundLocation.NamedLocation eloc = new CCompoundLocation.NamedLocation(E);
        location.add(hloc);
        location.add(eloc);
        String regex = "H+|E+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(secondary);
        while (matcher.find()) {
            String value = matcher.group().substring(0, 1);
            int start = matcher.start() + 1;
            int end = matcher.end();
            if (H.equals(value)) hloc.add(start, end); else if (E.equals(value)) eloc.add(start, end);
        }
        return location;
    }
