/*
   Copyright 2020 Md Rakib Hossain and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */


package crest.siamese.language.javascript;

import crest.siamese.language.NormalizerMode;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Normalizer Mode for JavaScript source code.
 */
public class JSNormalizerMode implements NormalizerMode {

    private static final char KEYWORD_OPTION = 'k';
    private static final char VALUE_OPTION = 'v';
    private static final char STRING_OPTION = 's';
    private static final char OPERATOR_OPTION = 'o';
    private static final char NAME_OPTION = 'w'; // same option character as Java clone search

    private Map<Character, Boolean> map = Stream.of(new Object[][]{
            {KEYWORD_OPTION, false},
            {VALUE_OPTION, false},
            {STRING_OPTION, false},
            {OPERATOR_OPTION, false},
            {NAME_OPTION, false},
    }).collect(Collectors.toMap(data -> (char) data[0], data -> (boolean) data[1]));


    /**
     * Constructor that refer to parent Object class. Required for calling Class::newInstance.
     */
    public JSNormalizerMode() {
        super();
    }


    /**
     * Configures the normalizer mode according to configuration char array.
     *
     * @param normOptions Normalization options
     */
    @Override
    public void configure(char[] normOptions) {
        for (char normOption : normOptions) {
            if (map.containsKey(normOption)) {
                map.put(normOption, true);
            }
        }
    }

    /**
     * Resets existing normalization configuration.
     */
    @Override
    public void reset() {
        for (Map.Entry<Character, Boolean> entry : map.entrySet()) {
            map.put(entry.getKey(), false);
        }
    }

    public boolean isKeywordToBeNormalised() {
        return map.get(KEYWORD_OPTION);
    }

    public boolean isValueToBeNormalised() {
        return map.get(VALUE_OPTION);
    }

    public boolean isStringToBeNormalised() {
        return map.get(STRING_OPTION);
    }

    public boolean isOperatorToBeNormalised() {
        return map.get(OPERATOR_OPTION);
    }

    public boolean isNameToBeNormalised() {
        return map.get(NAME_OPTION);
    }
}
