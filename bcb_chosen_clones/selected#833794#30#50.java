    public static IChemModel readInChI(URL url) throws CDKException {
        IChemModel chemModel = new ChemModel();
        try {
            IMoleculeSet moleculeSet = new MoleculeSet();
            chemModel.setMoleculeSet(moleculeSet);
            StdInChIParser parser = new StdInChIParser();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.toLowerCase().startsWith("inchi=")) {
                    IAtomContainer atc = parser.parseInchi(line);
                    moleculeSet.addAtomContainer(atc);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CDKException(e.getMessage());
        }
        return chemModel;
    }
