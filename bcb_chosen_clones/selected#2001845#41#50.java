        @Override
        public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
            ArrayList<SectionFinderResult> result = new ArrayList<SectionFinderResult>();
            Pattern p = Pattern.compile(PATTERN);
            Matcher m = p.matcher(text);
            while (m.find()) {
                result.add(new SectionFinderResult(m.start(), m.end()));
            }
            return result;
        }
