    @Override
    public void execute() throws ExecutionException {
        interrupted = false;
        if (document == null) throw new ExecutionException("No Document provided!");
        AnnotationSet outputAS = document.getAnnotations(outputAnnotationSetName);
        long startTime = System.currentTimeMillis();
        fireStatusChanged("Tagging Roman Numerals in " + document.getName());
        fireProgressChanged(0);
        Pattern pattern;
        if (allowLowerCase) {
            if (maxTailLength > 0) {
                pattern = Pattern.compile("\\b((?:[mdclxvi]+)|(?:[MDCLCVI]+))(\\w{0," + maxTailLength + "})\\b");
            } else {
                pattern = Pattern.compile("\\b((?:[mdclxvi]+)|(?:[MDCLCVI]+))\\b");
            }
        } else {
            if (maxTailLength > 0) {
                pattern = Pattern.compile("\\b([MDCLCVI]+)(\\w{0," + maxTailLength + "})\\b");
            } else {
                pattern = Pattern.compile("\\b([MDCLCVI]+)\\b");
            }
        }
        String content = document.getContent().toString();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            if (isInterrupted()) {
                throw new ExecutionInterruptedException("The execution of the \"" + getName() + "\" Roman Numerals Tagger has been abruptly interrupted!");
            }
            int numStart = matcher.start(1);
            int numEnd = matcher.end(1);
            if (numStart >= 0 && numEnd > numStart) {
                String romanNumeral = content.substring(numStart, numEnd);
                int value = romanToInt(romanNumeral);
                if (value > 0) {
                    String tail = null;
                    if (maxTailLength > 0) {
                        int tailStart = matcher.start(2);
                        int tailEnd = matcher.end(2);
                        if (tailStart < tailEnd) {
                            tail = content.substring(tailStart, tailEnd);
                            numEnd = tailEnd;
                        }
                    }
                    FeatureMap fm = Factory.newFeatureMap();
                    fm.put(VALUE_FEATURE_NAME, Integer.valueOf(value).doubleValue());
                    fm.put(TYPE_FEATURE_NAME, "roman");
                    if (tail != null) fm.put("tail", tail);
                    try {
                        outputAS.add((long) numStart, (long) numEnd, NUMBER_ANNOTATION_NAME, fm);
                    } catch (InvalidOffsetException e) {
                    }
                }
            }
        }
        fireProcessFinished();
        fireStatusChanged(document.getName() + " tagged with Roman Numerals in " + NumberFormat.getInstance().format((double) (System.currentTimeMillis() - startTime) / 1000) + " seconds!");
    }
