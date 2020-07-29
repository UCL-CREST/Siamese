    private static String sort(final String item) {
        final char[] chars = item.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            for (int j = 0; j < chars.length - 1; j++) {
                if (chars[j] > chars[j + 1]) {
                    final char temp = chars[j];
                    chars[j] = chars[j + 1];
                    chars[j + 1] = temp;
                }
            }
        }
        return String.valueOf(chars);
    }
