    private static void readData() {
        StringTokenizer sTok;
        topSet = new HashSet();
        sTok = new StringTokenizer(tops, ",");
        while (sTok.hasMoreTokens()) topSet.add(sTok.nextToken());
        leftSet = new HashSet();
        sTok = new StringTokenizer(lefts, ",");
        while (sTok.hasMoreTokens()) leftSet.add(sTok.nextToken());
        rightSet = new HashSet();
        sTok = new StringTokenizer(rights, ",");
        while (sTok.hasMoreTokens()) rightSet.add(sTok.nextToken());
        farRightSet = new HashSet();
        sTok = new StringTokenizer(farrights, ",");
        while (sTok.hasMoreTokens()) farRightSet.add(sTok.nextToken());
        vowelSet = new HashSet();
        sTok = new StringTokenizer(vowels, ",");
        while (sTok.hasMoreTokens()) {
            String ntk;
            vowelSet.add(ntk = sTok.nextToken());
            if (maxEwtsVowelLength < ntk.length()) maxEwtsVowelLength = ntk.length();
            validInputSequences.put(ntk, anyOldObjectWillDo);
        }
        puncSet = new HashSet();
        sTok = new StringTokenizer(others, ",");
        while (sTok.hasMoreTokens()) {
            String ntk;
            puncSet.add(ntk = sTok.nextToken());
            validInputSequences.put(ntk, anyOldObjectWillDo);
        }
        charSet = new HashSet();
        tibSet = new HashSet();
        sTok = new StringTokenizer(tibetanConsonants, ",");
        while (sTok.hasMoreTokens()) {
            String ntk;
            charSet.add(ntk = sTok.nextToken());
            tibSet.add(ntk);
            validInputSequences.put(ntk, anyOldObjectWillDo);
        }
        sanskritStackSet = new HashSet();
        sTok = new StringTokenizer(otherConsonants, ",");
        while (sTok.hasMoreTokens()) {
            String ntk;
            charSet.add(ntk = sTok.nextToken());
            sanskritStackSet.add(ntk);
            validInputSequences.put(ntk, anyOldObjectWillDo);
        }
        numberSet = new HashSet();
        sTok = new StringTokenizer(numbers, ",");
        while (sTok.hasMoreTokens()) {
            String ntk;
            charSet.add(ntk = sTok.nextToken());
            numberSet.add(ntk);
            validInputSequences.put(ntk, anyOldObjectWillDo);
        }
        charSet.add("Y");
        charSet.add("R");
        charSet.add("W");
        validInputSequences.put("Y", anyOldObjectWillDo);
        validInputSequences.put("R", anyOldObjectWillDo);
        validInputSequences.put("W", anyOldObjectWillDo);
        sTok = null;
        top_vowels = new HashSet();
        top_vowels.add(i_VOWEL);
        top_vowels.add(e_VOWEL);
        top_vowels.add(o_VOWEL);
        top_vowels.add(ai_VOWEL);
        top_vowels.add(au_VOWEL);
        top_vowels.add(reverse_i_VOWEL);
        try {
            URL url = TibetanMachineWeb.class.getResource(fileName);
            if (url == null) {
                System.err.println("Cannot find " + fileName + "; aborting.");
                System.exit(1);
            }
            InputStreamReader isr = new InputStreamReader(url.openStream());
            BufferedReader in = new BufferedReader(isr);
            System.out.println("Reading Tibetan Machine Web code table " + fileName);
            String line;
            boolean hashOn = false;
            boolean isTibetan = false;
            boolean isSanskrit = false;
            boolean ignore = false;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("<?")) {
                    if (line.equalsIgnoreCase("<?Consonants?>")) {
                        isSanskrit = false;
                        isTibetan = true;
                        hashOn = false;
                        ignore = false;
                        do {
                            line = in.readLine();
                        } while (line.startsWith("//") || line.equals(""));
                    } else if (line.equalsIgnoreCase("<?Numbers?>")) {
                        isSanskrit = false;
                        isTibetan = false;
                        hashOn = false;
                        ignore = false;
                        do {
                            line = in.readLine();
                        } while (line.startsWith("//") || line.equals(""));
                    } else if (line.equalsIgnoreCase("<?Vowels?>")) {
                        isSanskrit = false;
                        isTibetan = false;
                        hashOn = false;
                        ignore = false;
                        do {
                            line = in.readLine();
                        } while (line.startsWith("//") || line.equals(""));
                    } else if (line.equalsIgnoreCase("<?Other?>")) {
                        isSanskrit = false;
                        isTibetan = false;
                        hashOn = false;
                        ignore = false;
                        do {
                            line = in.readLine();
                        } while (line.startsWith("//") || line.equals(""));
                    } else if (line.equalsIgnoreCase("<?Input:Punctuation?>") || line.equalsIgnoreCase("<?Input:Vowels?>")) {
                        isSanskrit = false;
                        isTibetan = false;
                        hashOn = true;
                        ignore = false;
                    } else if (line.equalsIgnoreCase("<?Input:Tibetan?>")) {
                        isSanskrit = false;
                        isTibetan = true;
                        hashOn = true;
                        ignore = false;
                    } else if (line.equalsIgnoreCase("<?Input:Numbers?>")) {
                        isSanskrit = false;
                        isTibetan = false;
                        hashOn = true;
                        ignore = false;
                    } else if (line.equalsIgnoreCase("<?Input:Sanskrit?>")) {
                        isSanskrit = true;
                        isTibetan = false;
                        hashOn = true;
                        ignore = false;
                    } else if (line.equalsIgnoreCase("<?ToWylie?>")) {
                        isSanskrit = false;
                        isTibetan = false;
                        hashOn = false;
                        ignore = false;
                    } else if (line.equalsIgnoreCase("<?Ignore?>")) {
                        isSanskrit = false;
                        ignore = true;
                    }
                } else if (line.startsWith("//")) {
                    ;
                } else if (line.equals("")) {
                    ;
                } else {
                    StringTokenizer st = new StringTokenizer(line, DELIMITER, true);
                    String wylie = null;
                    DuffCode[] duffCodes;
                    duffCodes = new DuffCode[11];
                    int k = 0;
                    StringBuffer escapedToken = new StringBuffer("");
                    ThdlDebug.verify(escapedToken.length() == 0);
                    while (st.hasMoreTokens()) {
                        String val = getEscapedToken(st, escapedToken);
                        if (val.equals(DELIMITER) && escapedToken.length() == 0) {
                            k++;
                        } else if (!val.equals("")) {
                            if (escapedToken.length() != 0) {
                                escapedToken = new StringBuffer("");
                                ThdlDebug.verify(escapedToken.length() == 0);
                            }
                            switch(k) {
                                case 0:
                                    wylie = val;
                                    break;
                                case 1:
                                    duffCodes[TM] = new DuffCode(val, false);
                                    break;
                                case 2:
                                    if (!ignore) {
                                        duffCodes[REDUCED_C] = new DuffCode(val, true);
                                    }
                                    break;
                                case 3:
                                    duffCodes[TMW] = new DuffCode(val, true);
                                    if (null != duffCodes[TM]) {
                                        TMtoTMW[duffCodes[TM].getFontNum() - 1][duffCodes[TM].getCharNum() - 32] = duffCodes[TMW];
                                    }
                                    if (null != TMWtoTM[duffCodes[TMW].getFontNum() - 1][duffCodes[TMW].getCharNum() - 32]) throw new Error("tibwn.ini is supposed to use the TibetanMachineWeb glyph as the unique key, but " + val + " appears two or more times.");
                                    TMWtoTM[duffCodes[TMW].getFontNum() - 1][duffCodes[TMW].getCharNum() - 32] = duffCodes[TM];
                                    if (wylie.toLowerCase().startsWith("\\uf0")) {
                                        int x = Integer.parseInt(wylie.substring("\\u".length()), 16);
                                        ThdlDebug.verify((x >= 0xF000 && x <= 0xF0FF));
                                        NonUnicodeToTMW[x - ''] = new DuffCode[] { duffCodes[TMW] };
                                    }
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                    if (!ignore) {
                                        try {
                                            duffCodes[k - 1] = new DuffCode(val, true);
                                        } catch (Exception e) {
                                            System.err.println("Couldn't make a DuffCode out of " + val + "; line is " + line + "; k is " + k);
                                        }
                                    }
                                    break;
                                case 10:
                                    if (!val.equals("none")) {
                                        StringBuffer unicodeBuffer = new StringBuffer();
                                        StringTokenizer uTok = new StringTokenizer(val, ",");
                                        while (uTok.hasMoreTokens()) {
                                            String subval = uTok.nextToken();
                                            ThdlDebug.verify(subval.length() == 4 || subval.length() == 3);
                                            try {
                                                int x = Integer.parseInt(subval, 16);
                                                ThdlDebug.verify((x >= 0x0F00 && x <= 0x0FFF) || x == 0x5350 || x == 0x534D || x == 0x0020 || x == 0x00A0 || x == 0x2003);
                                                unicodeBuffer.append((char) x);
                                            } catch (NumberFormatException e) {
                                                ThdlDebug.verify(false);
                                            }
                                        }
                                        TMWtoUnicode[duffCodes[TMW].getFontNum() - 1][duffCodes[TMW].getCharNum() - 32] = unicodeBuffer.toString();
                                        char ch;
                                        if (unicodeBuffer.length() == 1 && UnicodeUtils.isInTibetanRange(ch = unicodeBuffer.charAt(0))) {
                                            if (null != UnicodeToTMW[ch - 'ༀ'][0] && 'ༀ' != ch && '༂' != ch && '༃' != ch && '་' != ch && '༎' != ch && 'ཀ' != ch && 'ག' != ch && 'ཉ' != ch && 'ཏ' != ch && 'ད' != ch && 'ན' != ch && 'ཞ' != ch && 'ར' != ch && 'ཤ' != ch && 'ཧ' != ch && 'ཪ' != ch && 'ཱ' != ch && 'ི' != ch && 'ཱི' != ch && 'ུ' != ch && 'ཱུ' != ch && 'ྲྀ' != ch && 'ཷ' != ch && 'ླྀ' != ch && 'ཹ' != ch && 'ེ' != ch && 'ོ' != ch && 'ཾ' != ch && 'ཱྀ' != ch) {
                                                throw new Error("tibwn.ini has more than one TMW fellow listed that has the Unicode " + val + ", but it's not on the list of specially handled glyphs");
                                            }
                                            UnicodeToTMW[ch - 'ༀ'][0] = duffCodes[TMW];
                                        }
                                    }
                                    break;
                                case 11:
                                    if (!ignore) {
                                        duffCodes[HALF_C] = new DuffCode(val, true);
                                    }
                                    break;
                                case 12:
                                    if (!ignore) {
                                        DuffCode binduCode = new DuffCode(val, true);
                                        binduMap.put(duffCodes[TMW], binduCode);
                                    }
                                    break;
                                case 13:
                                    throw new Error("tibwn.ini has only 13 columns, you tried to use a 14th column.");
                            }
                        } else {
                            if (k == 10) {
                                throw new Error("needed none or some unicode; line is " + line);
                            }
                        }
                    }
                    if (k < 10) {
                        throw new Error("needed none or some unicode; line is " + line);
                    }
                    if (!ignore) {
                        if (null == wylie) throw new Error(fileName + " has a line ^" + DELIMITER + " which means that no Wylie is assigned.  That isn't supported.");
                        if (hashOn) {
                            tibHash.put(Manipulate.unescape(wylie), duffCodes);
                        }
                        if (isTibetan) {
                            StringBuffer wylieWithoutDashes = new StringBuffer(wylie);
                            for (int wl = 0; wl < wylieWithoutDashes.length(); wl++) {
                                if (wylieWithoutDashes.charAt(wl) == '-') {
                                    wylieWithoutDashes.deleteCharAt(wl);
                                    --wl;
                                }
                            }
                            tibSet.add(wylieWithoutDashes.toString());
                        }
                        if (isSanskrit) {
                            sanskritStackSet.add(wylie);
                        }
                        if (null == duffCodes[TMW]) throw new Error(fileName + " has a line with wylie " + wylie + " but no TMW; that's not allowed");
                        int font = duffCodes[TMW].getFontNum();
                        int code = duffCodes[TMW].getCharNum() - 32;
                        toHashKey[font][code] = Manipulate.unescape(wylie);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("file Disappeared");
            ThdlDebug.noteIffyCode();
        }
    }
