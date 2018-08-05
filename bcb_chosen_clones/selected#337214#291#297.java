    protected Gene createGene(String a_geneClassName, String a_persistentRepresentation) throws Exception {
        Class geneClass = Class.forName(a_geneClassName);
        Constructor constr = geneClass.getConstructor(new Class[] { Configuration.class });
        Gene gene = (Gene) constr.newInstance(new Object[] { getConfiguration() });
        gene.setValueFromPersistentRepresentation(a_persistentRepresentation);
        return gene;
    }
