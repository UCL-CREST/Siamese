    public PairsGlobalConstraint(FieldNames field, FieldType type, int startIndex, int endIndex, Class<?> constraintType, HashSet<Object> values) {
        super(field, type, startIndex, endIndex, 0, 0, values);
        this.setStartIndex(startIndex);
        this.setEndIndex(endIndex);
        this.setType(constraintType.getCanonicalName());
        this.setDomainValues(values);
        ArrayList<Integer> set = new ArrayList<Integer>();
        for (int a = startIndex; a <= endIndex; a++) {
            set.add(a);
        }
        CombinationGenerator<Integer> combinationGenerator = new CombinationGenerator<Integer>(set, 2);
        binaryConstraints = new ArrayList<BinaryConstraint>();
        try {
            Constructor<?> constraintConstruct = constraintType.getConstructor(FieldNames.class, FieldType.class, Integer.class, Integer.class, HashSet.class);
            while (combinationGenerator.hasNext()) {
                List<Integer> comb = combinationGenerator.next();
                binaryConstraints.add((BinaryConstraint) constraintConstruct.newInstance(field, type, comb.get(0), comb.get(1), values));
            }
            binaryConstraints.trimToSize();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
