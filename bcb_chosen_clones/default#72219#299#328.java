    public java.util.Vector testLookUpFromClassNames() {
        java.util.Vector mgedOntClassNames = new java.util.Vector();
        try {
            java.util.Iterator it = entryClassSet.iterator();
            int counter = 0;
            while (it.hasNext()) {
                EntryClassPair pair = (EntryClassPair) it.next();
                counter++;
                java.lang.Class MAGEclass = java.lang.Class.forName(pair.className);
                java.lang.reflect.Constructor constructor = MAGEclass.getConstructor(null);
                Extendable MAGEobj = null;
                try {
                    MAGEobj = (Extendable) constructor.newInstance(null);
                    String mgedOntologyClassName = oh.resolveOntologyClassNameFromModel(MAGEobj, pair.entryName);
                    StringOutputHelpers.writeOutput("Resolve (" + pair.className + "," + pair.entryName + ") ===> ", 3);
                    if (mgedOntologyClassName != null) {
                        StringOutputHelpers.writeOutput("OK : " + mgedOntologyClassName + "\n", 3);
                        mgedOntClassNames.add(mgedOntologyClassName);
                    } else {
                        StringOutputHelpers.writeOutput("ERROR : Couldn't resolve" + "\n", 3);
                    }
                } catch (java.lang.InstantiationException e) {
                    StringOutputHelpers.writeOutput("Couldn't instantiate class " + MAGEclass.getName() + "\n", 3);
                }
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return mgedOntClassNames;
    }
