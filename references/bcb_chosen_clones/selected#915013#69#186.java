    private final long test(final boolean applyFilter, final int executionCount) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, RuleLoadingException {
        final boolean stripHtmlEnabled = true;
        final boolean injectSecretTokensEnabled = true;
        final boolean encryptQueryStringsEnabled = true;
        final boolean protectParamsAndFormsEnabled = true;
        final boolean applyExtraProtectionForDisabledFormFields = true;
        final boolean applyExtraProtectionForReadonlyFormFields = false;
        final boolean applyExtraProtectionForRequestParamValueCount = false;
        final ContentInjectionHelper helper = new ContentInjectionHelper();
        final RuleFileLoader ruleFileLoaderModificationExcludes = new ClasspathZipRuleFileLoader();
        ruleFileLoaderModificationExcludes.setPath(WebCastellumFilter.MODIFICATION_EXCLUDES_DEFAULT);
        final ContentModificationExcludeDefinitionContainer containerModExcludes = new ContentModificationExcludeDefinitionContainer(ruleFileLoaderModificationExcludes);
        containerModExcludes.parseDefinitions();
        helper.setContentModificationExcludeDefinitions(containerModExcludes);
        final AttackHandler attackHandler = new AttackHandler(null, 123, 600000, 100000, 300000, 300000, null, "MOCK", false, false, 0, false, false, Pattern.compile("sjghggfakgfjagfgajgfjasgfs"), Pattern.compile("sjghggfakgfjagfgajgfjasgfs"), true);
        final SessionCreationTracker sessionCreationTracker = new SessionCreationTracker(attackHandler, 0, 600000, 300000, 0, "", "", "", "");
        final RequestWrapper request = new RequestWrapper(new RequestMock(), helper, sessionCreationTracker, "123.456.789.000", false, true, true);
        final RuleFileLoader ruleFileLoaderResponseModifications = new ClasspathZipRuleFileLoader();
        ruleFileLoaderResponseModifications.setPath(WebCastellumFilter.RESPONSE_MODIFICATIONS_DEFAULT);
        final ResponseModificationDefinitionContainer container = new ResponseModificationDefinitionContainer(ruleFileLoaderResponseModifications);
        container.parseDefinitions();
        final ResponseModificationDefinition[] responseModificationDefinitions = downCast(container.getAllEnabledRequestDefinitions());
        final List tmpPatternsToExcludeCompleteTag = new ArrayList(responseModificationDefinitions.length);
        final List tmpPatternsToExcludeCompleteScript = new ArrayList(responseModificationDefinitions.length);
        final List tmpPatternsToExcludeLinksWithinScripts = new ArrayList(responseModificationDefinitions.length);
        final List tmpPatternsToExcludeLinksWithinTags = new ArrayList(responseModificationDefinitions.length);
        final List tmpPatternsToCaptureLinksWithinScripts = new ArrayList(responseModificationDefinitions.length);
        final List tmpPatternsToCaptureLinksWithinTags = new ArrayList(responseModificationDefinitions.length);
        final List tmpPrefiltersToExcludeCompleteTag = new ArrayList(responseModificationDefinitions.length);
        final List tmpPrefiltersToExcludeCompleteScript = new ArrayList(responseModificationDefinitions.length);
        final List tmpPrefiltersToExcludeLinksWithinScripts = new ArrayList(responseModificationDefinitions.length);
        final List tmpPrefiltersToExcludeLinksWithinTags = new ArrayList(responseModificationDefinitions.length);
        final List tmpPrefiltersToCaptureLinksWithinScripts = new ArrayList(responseModificationDefinitions.length);
        final List tmpPrefiltersToCaptureLinksWithinTags = new ArrayList(responseModificationDefinitions.length);
        final List tmpGroupNumbersToCaptureLinksWithinScripts = new ArrayList(responseModificationDefinitions.length);
        final List tmpGroupNumbersToCaptureLinksWithinTags = new ArrayList(responseModificationDefinitions.length);
        for (int i = 0; i < responseModificationDefinitions.length; i++) {
            final ResponseModificationDefinition responseModificationDefinition = responseModificationDefinitions[i];
            if (responseModificationDefinition.isMatchesScripts()) {
                tmpPatternsToExcludeCompleteScript.add(responseModificationDefinition.getScriptExclusionPattern());
                tmpPrefiltersToExcludeCompleteScript.add(responseModificationDefinition.getScriptExclusionPrefilter());
                tmpPatternsToExcludeLinksWithinScripts.add(responseModificationDefinition.getUrlExclusionPattern());
                tmpPrefiltersToExcludeLinksWithinScripts.add(responseModificationDefinition.getUrlExclusionPrefilter());
                tmpPatternsToCaptureLinksWithinScripts.add(responseModificationDefinition.getUrlCapturingPattern());
                tmpPrefiltersToCaptureLinksWithinScripts.add(responseModificationDefinition.getUrlCapturingPrefilter());
                tmpGroupNumbersToCaptureLinksWithinScripts.add(ServerUtils.convertSimpleToObjectArray(responseModificationDefinition.getCapturingGroupNumbers()));
            }
            if (responseModificationDefinition.isMatchesTags()) {
                tmpPatternsToExcludeCompleteTag.add(responseModificationDefinition.getTagExclusionPattern());
                tmpPrefiltersToExcludeCompleteTag.add(responseModificationDefinition.getTagExclusionPrefilter());
                tmpPatternsToExcludeLinksWithinTags.add(responseModificationDefinition.getUrlExclusionPattern());
                tmpPrefiltersToExcludeLinksWithinTags.add(responseModificationDefinition.getUrlExclusionPrefilter());
                tmpPatternsToCaptureLinksWithinTags.add(responseModificationDefinition.getUrlCapturingPattern());
                tmpPrefiltersToCaptureLinksWithinTags.add(responseModificationDefinition.getUrlCapturingPrefilter());
                tmpGroupNumbersToCaptureLinksWithinTags.add(ServerUtils.convertSimpleToObjectArray(responseModificationDefinition.getCapturingGroupNumbers()));
            }
        }
        final Matcher[] matchersToExcludeCompleteTag = ServerUtils.convertListOfPatternToArrayOfMatcher(tmpPatternsToExcludeCompleteTag);
        final Matcher[] matchersToExcludeCompleteScript = ServerUtils.convertListOfPatternToArrayOfMatcher(tmpPatternsToExcludeCompleteScript);
        final Matcher[] matchersToExcludeLinksWithinScripts = ServerUtils.convertListOfPatternToArrayOfMatcher(tmpPatternsToExcludeLinksWithinScripts);
        final Matcher[] matchersToExcludeLinksWithinTags = ServerUtils.convertListOfPatternToArrayOfMatcher(tmpPatternsToExcludeLinksWithinTags);
        final Matcher[] matchersToCaptureLinksWithinScripts = ServerUtils.convertListOfPatternToArrayOfMatcher(tmpPatternsToCaptureLinksWithinScripts);
        final Matcher[] matchersToCaptureLinksWithinTags = ServerUtils.convertListOfPatternToArrayOfMatcher(tmpPatternsToCaptureLinksWithinTags);
        final WordDictionary[] prefiltersToExcludeCompleteTag = (WordDictionary[]) tmpPrefiltersToExcludeCompleteTag.toArray(new WordDictionary[0]);
        final WordDictionary[] prefiltersToExcludeCompleteScript = (WordDictionary[]) tmpPrefiltersToExcludeCompleteScript.toArray(new WordDictionary[0]);
        final WordDictionary[] prefiltersToExcludeLinksWithinScripts = (WordDictionary[]) tmpPrefiltersToExcludeLinksWithinScripts.toArray(new WordDictionary[0]);
        final WordDictionary[] prefiltersToExcludeLinksWithinTags = (WordDictionary[]) tmpPrefiltersToExcludeLinksWithinTags.toArray(new WordDictionary[0]);
        final WordDictionary[] prefiltersToCaptureLinksWithinScripts = (WordDictionary[]) tmpPrefiltersToCaptureLinksWithinScripts.toArray(new WordDictionary[0]);
        final WordDictionary[] prefiltersToCaptureLinksWithinTags = (WordDictionary[]) tmpPrefiltersToCaptureLinksWithinTags.toArray(new WordDictionary[0]);
        final int[][] groupNumbersToCaptureLinksWithinScripts = ServerUtils.convertArrayIntegerListTo2DimIntArray(tmpGroupNumbersToCaptureLinksWithinScripts);
        final int[][] groupNumbersToCaptureLinksWithinTags = ServerUtils.convertArrayIntegerListTo2DimIntArray(tmpGroupNumbersToCaptureLinksWithinTags);
        final Cipher cipher = CryptoUtils.getCipher();
        final CryptoKeyAndSalt key = CryptoUtils.generateRandomCryptoKeyAndSalt(false);
        Cipher.getInstance("AES");
        MessageDigest.getInstance("SHA-1");
        final ResponseWrapper response = new ResponseWrapper(new ResponseMock(), request, attackHandler, helper, false, "___ENCRYPTED___", cipher, key, "___SEC-KEY___", "___SEC-VALUE___", "___PROT-KEY___", false, false, false, false, "123.456.789.000", new HashSet(), prefiltersToExcludeCompleteScript, matchersToExcludeCompleteScript, prefiltersToExcludeCompleteTag, matchersToExcludeCompleteTag, prefiltersToExcludeLinksWithinScripts, matchersToExcludeLinksWithinScripts, prefiltersToExcludeLinksWithinTags, matchersToExcludeLinksWithinTags, prefiltersToCaptureLinksWithinScripts, matchersToCaptureLinksWithinScripts, prefiltersToCaptureLinksWithinTags, matchersToCaptureLinksWithinTags, groupNumbersToCaptureLinksWithinScripts, groupNumbersToCaptureLinksWithinTags, true, false, true, true, true, true, true, true, true, true, true, false, false, true, "", "", (short) 3, true, false, false);
        final List durations = new ArrayList();
        for (int i = 0; i < executionCount; i++) {
            final long start = System.currentTimeMillis();
            Reader reader = null;
            Writer writer = null;
            try {
                reader = new BufferedReader(new FileReader(this.htmlFile));
                writer = new FileWriter(this.outputFile);
                if (applyFilter) {
                    writer = new ResponseFilterWriter(writer, true, "http://127.0.0.1/test/sample", "/test", "/test", "___SEC-KEY___", "___SEC-VALUE___", "___PROT-KEY___", cipher, key, helper, "___ENCRYPTED___", request, response, stripHtmlEnabled, injectSecretTokensEnabled, protectParamsAndFormsEnabled, encryptQueryStringsEnabled, applyExtraProtectionForDisabledFormFields, applyExtraProtectionForReadonlyFormFields, applyExtraProtectionForRequestParamValueCount, prefiltersToExcludeCompleteScript, matchersToExcludeCompleteScript, prefiltersToExcludeCompleteTag, matchersToExcludeCompleteTag, prefiltersToExcludeLinksWithinScripts, matchersToExcludeLinksWithinScripts, prefiltersToExcludeLinksWithinTags, matchersToExcludeLinksWithinTags, prefiltersToCaptureLinksWithinScripts, matchersToCaptureLinksWithinScripts, prefiltersToCaptureLinksWithinTags, matchersToCaptureLinksWithinTags, groupNumbersToCaptureLinksWithinScripts, groupNumbersToCaptureLinksWithinTags, true, true, false, true, true, true, true, true, true, true, true, false, false, true, "", "", (short) 3, true, false);
                    writer = new BufferedWriter(writer);
                }
                char[] chars = new char[16 * 1024];
                int read;
                while ((read = reader.read(chars)) != -1) {
                    if (read > 0) {
                        writer.write(chars, 0, read);
                    }
                }
                durations.add(new Long(System.currentTimeMillis() - start));
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        long sum = 0;
        for (final Iterator iter = durations.iterator(); iter.hasNext(); ) {
            Long value = (Long) iter.next();
            sum += value.longValue();
        }
        return sum / durations.size();
    }
