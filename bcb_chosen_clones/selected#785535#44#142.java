    @Override
    public void handler(Map<String, Match> result, TargetPage target) {
        List<String> lines = new LinkedList<String>();
        List<String> page = new LinkedList<String>();
        try {
            URL url = new URL(target.getUrl());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                page.add(line);
            }
            reader.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        try {
            result.put("27 svk par fix", MatchEventFactory.getFix27());
            result.put("41 svk ita fix", MatchEventFactory.getFix41());
            result.put("01 rsa mex", MatchEventFactory.get01());
            result.put("02 uru fra", MatchEventFactory.get02());
            result.put("04 kor gre", MatchEventFactory.get04());
            result.put("03 arg ngr", MatchEventFactory.get03());
            result.put("05 eng usa", MatchEventFactory.get05());
            result.put("06 alg slo", MatchEventFactory.get06());
            result.put("08 scg gha", MatchEventFactory.get08());
            result.put("07 ger aus", MatchEventFactory.get07());
            result.put("09 end den", MatchEventFactory.get09());
            result.put("10 jpn cmr", MatchEventFactory.get10());
            result.put("11 ita par", MatchEventFactory.get11());
            result.put("12 nzl svk", MatchEventFactory.get12());
            result.put("13 civ por", MatchEventFactory.get13());
            result.put("14 bra prk", MatchEventFactory.get14());
            result.put("15 hon chi", MatchEventFactory.get15());
            result.put("16 esp sui", MatchEventFactory.get16());
            result.put("17 rsa uru", MatchEventFactory.get17());
            result.put("20 arg kor", MatchEventFactory.get20());
            result.put("19 gre ngr", MatchEventFactory.get19());
            result.put("18 fra mex", MatchEventFactory.get18());
            result.put("21 ger scg", MatchEventFactory.get21());
            result.put("22 slo usa", MatchEventFactory.get22());
            result.put("23 eng alg", MatchEventFactory.get23());
            result.put("25 end jpn", MatchEventFactory.get25());
            result.put("24 gha aus", MatchEventFactory.get24());
            result.put("26 cmr den", MatchEventFactory.get26());
            result.put("27 slo par", MatchEventFactory.get27());
            result.put("28 ita nzl", MatchEventFactory.get28());
            result.put("29 bra civ", MatchEventFactory.get29());
            result.put("30 por prk", MatchEventFactory.get30());
            result.put("31 chi sui", MatchEventFactory.get31());
            result.put("32 esp hon", MatchEventFactory.get32());
            result.put("34 fra rsa", MatchEventFactory.get34());
            result.put("33 mex uru", MatchEventFactory.get33());
            result.put("35 ngr kor", MatchEventFactory.get35());
            result.put("36 gre arg", MatchEventFactory.get36());
            result.put("38 usa alg", MatchEventFactory.get38());
            result.put("37 slo eng", MatchEventFactory.get37());
            result.put("39 gha ger", MatchEventFactory.get39());
            result.put("40 aus scg", MatchEventFactory.get40());
            result.put("42 par nzl", MatchEventFactory.get42());
            result.put("41 slo ita", MatchEventFactory.get41());
            result.put("44 cmr ned", MatchEventFactory.get44());
            result.put("43 den jpn", MatchEventFactory.get43());
            result.put("45 por bra", MatchEventFactory.get45());
            result.put("46 prk civ", MatchEventFactory.get46());
            result.put("47 chi esp", MatchEventFactory.get47());
            result.put("48 sui hon", MatchEventFactory.get48());
            result.put("49 uru kor", MatchEventFactory.get49Team());
            result.put("50 usa gha", MatchEventFactory.get50Team());
            result.put("51 ger eng", MatchEventFactory.get51Team());
            result.put("52 arg mex", MatchEventFactory.get52Team());
            result.put("53 ned svk", MatchEventFactory.get53Team());
            result.put("54 bra chi", MatchEventFactory.get54Team());
            result.put("55 par jpn", MatchEventFactory.get55Team());
            result.put("56 esp por", MatchEventFactory.get56Team());
            result.put("57 ned bra", MatchEventFactory.get57Team());
            result.put("58 uru gha", MatchEventFactory.get58Team());
            result.put("59 arg ger", MatchEventFactory.get59Team());
            result.put("49", MatchEventFactory.get49());
            result.put("50", MatchEventFactory.get50());
            result.put("51", MatchEventFactory.get51());
            result.put("52", MatchEventFactory.get52());
            result.put("53", MatchEventFactory.get53());
            result.put("54", MatchEventFactory.get54());
            this.stage2MatchHandler("318295", "55", "2010-06-29 22:30", result);
            this.stage2MatchHandler("318296", "56", "2010-06-30 02:30", result);
            this.stage2MatchHandler("318297", "57", "2010-07-02 22:00", result);
            this.stage2MatchHandler("318298", "58", "2010-07-03 02:30", result);
            this.stage2MatchHandler("318299", "59", "2010-07-03 22:00", result);
            this.stage2MatchHandler("318300", "60", "2010-07-04 02:30", result);
            this.stage2MatchHandler("318301", "61", "2010-07-07 02:30", result);
            this.stage2MatchHandler("318302", "62", "2010-07-08 02:30", result);
            this.stage2MatchHandler("318303", "63", "2010-07-11 02:30", result);
            this.stage2MatchHandler("318304", "64", "2010-07-12 02:30", result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
