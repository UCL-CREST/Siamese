    @Override
    public Object map(CharSequence valStr, Field field, Class type, CommandOpt anno, CommandLine line) throws IllegalAccessException {
        CommandLineAdvanced adv = line.getAdvancedKnobs();
        Class compType = type.getComponentType();
        if (compType.isArray()) {
            throw new IllegalArgumentException("Multi-dimensional array fields not supported");
        }
        Object curArray = field.get(line.getHolder());
        Pattern p;
        String pat = anno.mode();
        if (pat.length() > 0) p = Pattern.compile(pat); else p = adv.getValueSplitter();
        String[] vals = p.split(valStr);
        if (vals.length == 1 && vals[0].equals("")) vals = new String[0];
        int curLength;
        if (curArray == null) {
            curLength = 0;
        } else {
            curLength = Array.getLength(curArray);
        }
        Object newArray = Array.newInstance(compType, curLength + vals.length);
        if (curLength > 0) {
            System.arraycopy(curArray, 0, newArray, 0, curLength);
        }
        for (int i = 0; i < vals.length; i++) {
            String val = vals[i];
            Object valObj = adv.getValueFor(val, compType, anno);
            Array.set(newArray, curLength + i, valObj);
        }
        return newArray;
    }
