    public static int searchStringCharOffset(String text, Font font, int xPixelOffset) {
        int first = 0;
        int upto = text.length();
        int mid = 0;
        while (first < upto) {
            mid = (first + upto) / 2;
            int charPos1 = font.getWidth(text.substring(0, mid));
            int charPos2 = charPos1 + font.getWidth(text.substring(mid, mid + 1));
            if (xPixelOffset < charPos1) {
                upto = mid;
            } else if (xPixelOffset > charPos2) {
                first = mid + 1;
            } else {
                break;
            }
        }
        return mid;
    }
