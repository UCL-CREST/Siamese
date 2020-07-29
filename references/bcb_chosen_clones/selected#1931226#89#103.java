        public CharSequence replace(String codeString, int index) {
            StringBuffer buffer = new StringBuffer(codeString);
            Pattern pattern = Pattern.compile("(#)+");
            Matcher matcher = pattern.matcher(buffer);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String group = matcher.group();
                CharSequence decimalFormatString = repeat("0", group.length());
                DecimalFormat format = new DecimalFormat(decimalFormatString.toString());
                String result = format.format(index);
                buffer.replace(start, end, result);
            }
            return buffer;
        }
