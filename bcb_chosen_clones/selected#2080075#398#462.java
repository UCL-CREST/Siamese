        public void eval(MonotonicTextLabels labels, TextBase textBase) {
            long start = System.currentTimeMillis();
            if ("defDict".equals(keyword)) {
                labels.defineDictionary(type, wordSet);
            } else if ("declareSpanType".equals(keyword)) {
                labels.declareType(type);
            } else if (statementType == PROVIDE) {
                labels.setAnnotatedBy(annotationType);
            } else if (statementType == REQUIRE) {
                labels.require(annotationType, fileToLoad);
            } else {
                Span.Looper input = null;
                if ("top".equals(startType)) {
                    input = textBase.documentSpanIterator();
                } else if (labels.isType(startType)) {
                    input = labels.instanceIterator(startType);
                } else {
                    throw new IllegalStateException("no type '" + startType + "' defined");
                }
                if (statementType == MIXUP) {
                    for (Span.Looper i = mixupExpr.extract(labels, input); i.hasNext(); ) {
                        Span span = i.nextSpan();
                        extendLabels(labels, span);
                    }
                    if ("defSpanType".equals(keyword)) {
                        labels.declareType(type);
                    }
                } else if (statementType == FILTER) {
                    TreeSet accum = new TreeSet();
                    for (Span.Looper i = input; i.hasNext(); ) {
                        Span span = i.nextSpan();
                        if (!hasExtraction(mixupExpr, labels, span)) {
                            accum.add(span);
                        }
                    }
                    for (Iterator i = accum.iterator(); i.hasNext(); ) {
                        extendLabels(labels, ((Span) i.next()));
                    }
                } else if (statementType == TRIE) {
                    while (input.hasNext()) {
                        Span span = input.nextSpan();
                        Span.Looper output = trie.lookup(span);
                        while (output.hasNext()) {
                            extendLabels(labels, output.nextSpan());
                        }
                    }
                } else if (statementType == REGEX) {
                    Pattern pattern = Pattern.compile(regex);
                    while (input.hasNext()) {
                        Span span = input.nextSpan();
                        Matcher matcher = pattern.matcher(span.asString());
                        while (matcher.find()) {
                            try {
                                Span subspan = span.charIndexProperSubSpan(matcher.start(regexGroup), matcher.end(regexGroup));
                                extendLabels(labels, subspan);
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                    }
                } else {
                    throw new IllegalStateException("illegal statement type " + statementType);
                }
            }
            long end = System.currentTimeMillis();
        }
