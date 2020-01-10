/*
   Copyright 2018 Wayne Tsui and Jens Krinke

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

package crest.siamese.language.python3;

import com.google.common.collect.ImmutableMap;
import crest.siamese.language.Normalizer;
import crest.siamese.language.NormalizerMode;

import java.util.ArrayList;
import java.util.Map;

/**
 * Normalizer for Python 3 source code.
 */
public class Python3Normalizer implements Normalizer {

    private Map<String, Boolean> normalizeSettings;

    private static final String NORMALIZED_TOKEN_KEYWORD = "K";
    private static final String NORMALIZED_TOKEN_VALUE = "V";
    private static final String NORMALIZED_TOKEN_STRING = "S";
    private static final String NORMALIZED_TOKEN_OPERATOR = "O";
    private static final String NORMALIZED_TOKEN_NAME = "W"; // same symbol as Java clone search

    /**
     * Maps {@link Python3Parser#_SYMBOLIC_NAMES} to a normalised form.
     * Symbols with no corresponding normalised form have the empty string value. Normalization for these
     * symbols will always be false.
     */
    private static final Map<String, String> normalizedSymbols = ImmutableMap.<String, String>builder()
            .put("STRING", NORMALIZED_TOKEN_STRING)
            .put("NUMBER", NORMALIZED_TOKEN_VALUE)
            .put("INTEGER", NORMALIZED_TOKEN_VALUE)
            .put("DEF", NORMALIZED_TOKEN_KEYWORD)
            .put("RETURN", NORMALIZED_TOKEN_KEYWORD)
            .put("RAISE", NORMALIZED_TOKEN_KEYWORD)
            .put("FROM", NORMALIZED_TOKEN_KEYWORD)
            .put("IMPORT", NORMALIZED_TOKEN_KEYWORD)
            .put("AS", NORMALIZED_TOKEN_KEYWORD)
            .put("GLOBAL", NORMALIZED_TOKEN_KEYWORD)
            .put("NONLOCAL", NORMALIZED_TOKEN_KEYWORD)
            .put("ASSERT", NORMALIZED_TOKEN_KEYWORD)
            .put("IF", NORMALIZED_TOKEN_KEYWORD)
            .put("ELIF", NORMALIZED_TOKEN_KEYWORD)
            .put("ELSE", NORMALIZED_TOKEN_KEYWORD)
            .put("WHILE", NORMALIZED_TOKEN_KEYWORD)
            .put("FOR", NORMALIZED_TOKEN_KEYWORD)
            .put("IN", NORMALIZED_TOKEN_KEYWORD)
            .put("TRY", NORMALIZED_TOKEN_KEYWORD)
            .put("FINALLY", NORMALIZED_TOKEN_KEYWORD)
            .put("WITH", NORMALIZED_TOKEN_KEYWORD)
            .put("EXCEPT", NORMALIZED_TOKEN_KEYWORD)
            .put("LAMBDA", NORMALIZED_TOKEN_KEYWORD)
            .put("OR", NORMALIZED_TOKEN_KEYWORD)
            .put("AND", NORMALIZED_TOKEN_KEYWORD)
            .put("NOT", NORMALIZED_TOKEN_KEYWORD)
            .put("IS", NORMALIZED_TOKEN_KEYWORD)
            .put("NONE", NORMALIZED_TOKEN_KEYWORD)
            .put("TRUE", NORMALIZED_TOKEN_KEYWORD)
            .put("FALSE", NORMALIZED_TOKEN_KEYWORD)
            .put("CLASS", NORMALIZED_TOKEN_KEYWORD)
            .put("YIELD", NORMALIZED_TOKEN_KEYWORD)
            .put("DEL", NORMALIZED_TOKEN_KEYWORD)
            .put("PASS", NORMALIZED_TOKEN_KEYWORD)
            .put("CONTINUE", NORMALIZED_TOKEN_KEYWORD)
            .put("BREAK", NORMALIZED_TOKEN_KEYWORD)
            .put("ASYNC", NORMALIZED_TOKEN_KEYWORD)
            .put("AWAIT", NORMALIZED_TOKEN_KEYWORD)
            .put("NEWLINE", "")
            .put("NAME", NORMALIZED_TOKEN_NAME)
            .put("STRING_LITERAL", NORMALIZED_TOKEN_STRING)
            .put("BYTES_LITERAL", NORMALIZED_TOKEN_STRING)
            .put("DECIMAL_INTEGER", NORMALIZED_TOKEN_VALUE)
            .put("OCT_INTEGER", NORMALIZED_TOKEN_VALUE)
            .put("HEX_INTEGER", NORMALIZED_TOKEN_VALUE)
            .put("BIN_INTEGER", NORMALIZED_TOKEN_VALUE)
            .put("FLOAT_NUMBER", NORMALIZED_TOKEN_VALUE)
            .put("IMAG_NUMBER", NORMALIZED_TOKEN_VALUE)
            .put("DOT", "")
            .put("ELLIPSIS", "")
            .put("STAR", NORMALIZED_TOKEN_OPERATOR)
            .put("OPEN_PAREN", "")
            .put("CLOSE_PAREN", "")
            .put("COMMA", "")
            .put("COLON", "")
            .put("SEMI_COLON", "")
            .put("POWER", NORMALIZED_TOKEN_OPERATOR)
            .put("ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("OPEN_BRACK", "")
            .put("CLOSE_BRACK", "")
            .put("OR_OP", NORMALIZED_TOKEN_OPERATOR)
            .put("XOR", NORMALIZED_TOKEN_OPERATOR)
            .put("AND_OP", NORMALIZED_TOKEN_OPERATOR)
            .put("LEFT_SHIFT", NORMALIZED_TOKEN_OPERATOR)
            .put("RIGHT_SHIFT", NORMALIZED_TOKEN_OPERATOR)
            .put("ADD", NORMALIZED_TOKEN_OPERATOR)
            .put("MINUS", NORMALIZED_TOKEN_OPERATOR)
            .put("DIV", NORMALIZED_TOKEN_OPERATOR)
            .put("MOD", NORMALIZED_TOKEN_OPERATOR)
            .put("IDIV", NORMALIZED_TOKEN_OPERATOR)
            .put("NOT_OP", NORMALIZED_TOKEN_OPERATOR)
            .put("OPEN_BRACE", "")
            .put("CLOSE_BRACE", "")
            .put("LESS_THAN", NORMALIZED_TOKEN_OPERATOR)
            .put("GREATER_THAN", NORMALIZED_TOKEN_OPERATOR)
            .put("EQUALS", NORMALIZED_TOKEN_OPERATOR)
            .put("GT_EQ", NORMALIZED_TOKEN_OPERATOR)
            .put("LT_EQ", NORMALIZED_TOKEN_OPERATOR)
            .put("NOT_EQ_1", NORMALIZED_TOKEN_OPERATOR)
            .put("NOT_EQ_2", NORMALIZED_TOKEN_OPERATOR)
            .put("AT", NORMALIZED_TOKEN_OPERATOR)
            .put("ARROW", "")
            .put("ADD_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("SUB_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("MULT_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("AT_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("DIV_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("MOD_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("AND_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("OR_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("XOR_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("LEFT_SHIFT_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("RIGHT_SHIFT_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("POWER_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("IDIV_ASSIGN", NORMALIZED_TOKEN_OPERATOR)
            .put("SKIP_", "")
            .put("UNKNOWN_CHAR", "")
            .put("INDENT", "")
            .put("DEDENT", "")
            .build();

    /**
     * Constructor that refer to parent Object class. Required for calling Class::newInstance.
     */
    public Python3Normalizer() {
        super();
    }

    /**
     * Configures the normalization settings according to the input NormalizerMode.
     * @param normalizerMode Configuration specifications for which category of symbols are to be normalised
     */
    @Override
    public void configure(NormalizerMode normalizerMode) {
        Python3NormalizerMode python3NormalizerMode = (Python3NormalizerMode) normalizerMode;
        normalizeSettings = ImmutableMap.<String, Boolean>builder()
                .put(NORMALIZED_TOKEN_KEYWORD, python3NormalizerMode.isKeywordToBeNormalised())
                .put(NORMALIZED_TOKEN_VALUE, python3NormalizerMode.isValueToBeNormalised())
                .put(NORMALIZED_TOKEN_STRING, python3NormalizerMode.isStringToBeNormalised())
                .put(NORMALIZED_TOKEN_OPERATOR, python3NormalizerMode.isOperatorToBeNormalised())
                .put(NORMALIZED_TOKEN_NAME, python3NormalizerMode.isNameToBeNormalised())
                .put("", false)
                .build();
    }

    /**
     * Normalizes a token depending on the normalization settings.
     * @param token String text of the token
     * @param type String type of the token
     * @return Normalised token
     */
    @Override
    public String normalizeAToken(String token, String type) {
        String normalizedSymbol = normalizedSymbols.get(type);
        return normalizeSettings.get(normalizedSymbol) ? normalizedSymbol : token;
    }

    @Override
    public ArrayList<String> noNormalizeAToken(String token) {
        return null;
    }
}
