        public Object filter(Object object, String args) {
            if (object instanceof String) {
                String stringObj = (String) object;
                String titled = "";
                Pattern pattern = Pattern.compile(("((^\\w)|(\\s+\\w))"));
                Matcher matcher = pattern.matcher(stringObj);
                int lastEnd = -1;
                while (matcher.find()) {
                    if (lastEnd >= 0) titled += stringObj.substring(lastEnd, matcher.start());
                    titled += matcher.group().toUpperCase();
                    lastEnd = matcher.end();
                }
                if (lastEnd < stringObj.length() && lastEnd >= 0) titled += stringObj.substring(lastEnd);
                return titled;
            }
            return null;
        }
