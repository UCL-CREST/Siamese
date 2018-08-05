            public List<Code> getProperties() {
                if (value == null) {
                    return Collections.emptyList();
                }
                String value = org.apache.commons.lang.StringUtils.strip(this.value, "\" \n\r\f\t");
                Pattern p = Pattern.compile("(memo|date|desc|summary|milestone|def|value|vdef|quote|ref|title|cause)\\s*:\\s*");
                Matcher m = p.matcher(value);
                List<Code> result = new LinkedList<Code>();
                String lastKey = "desc";
                StringBuilder currentValue = new StringBuilder();
                int pos = 0;
                while (m.find()) {
                    int start = m.start();
                    String key = m.group(1);
                    if (pos < start) {
                        currentValue.append(value.substring(pos, start));
                    }
                    pos = m.end();
                    if (currentValue.length() > 0) {
                        result.add(new TagCodedCode(lastKey + "=\"" + org.apache.commons.lang.StringUtils.strip(currentValue.toString(), ", \n\r\f\t") + "\""));
                        currentValue = new StringBuilder();
                    }
                    lastKey = key;
                }
                if (pos < value.length()) {
                    currentValue.append(value.substring(pos));
                }
                if (currentValue.length() > 0) {
                    result.add(new TagCodedCode(lastKey + "=\"" + org.apache.commons.lang.StringUtils.strip(currentValue.toString(), ", \n\r\f\t") + "\""));
                }
                return result;
            }
