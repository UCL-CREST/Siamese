/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

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

package crest.siamese.language.java;

import crest.siamese.language.Normalizer;
import crest.siamese.language.Tokenizer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JavaTokenizer implements Tokenizer {
	private ArrayList<String> tokens = new ArrayList<String>();
	private Normalizer normalizer;

	public JavaTokenizer() {
		super();
	}

	public JavaTokenizer(Normalizer normalizer) {
	    super();
		this.normalizer = normalizer;
	}

	@Override
	public void configure(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public ArrayList<String> tokenize(String s) throws Exception {
        tokens = new ArrayList<>();
        InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        JavaLexer lexer = new JavaLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        List<? extends Token> tokenList = lexer.getAllTokens();
        for (Token token : tokenList) {
            String symbolicName = JavaLexer.VOCABULARY.getSymbolicName(token.getType());
            // normalize the token except white space (skip)
            if (!symbolicName.equals("WS")
                    && !symbolicName.equals("COMMENT")
                    && !symbolicName.equals("LINE_COMMENT")) {
                tokens.add(normalizer.normalizeAToken(token.getText().trim(), symbolicName));
            }
        }

        return tokens;
    }

    @Override
    public ArrayList<String> tokenize(Reader reader) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while((line =bufferedReader.readLine())!=null){
            stringBuffer.append(line).append("\n");
        }
        reader.close();
        bufferedReader.close();
        String code = stringBuffer.toString();
        return tokenize(code);
    }

    @Override
    public ArrayList<String> tokenizeLine(Reader reader) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(reader);
        ArrayList<String> tLines = new ArrayList<>();
        String line;
        while((line = bufferedReader.readLine())!=null){
            ArrayList<String> l = tokenize(line);
            String[] tline = l.toArray(new String[l.size()]);
            String str = String.join("", tline);
            tLines.add(str);
        }

        return tLines;
    }

    @Override
    public ArrayList<String> tokenize(File f) throws Exception {
        String code = FileUtils.readFileToString(f);
        return tokenize(code);
	}

    @Override
	public ArrayList<String> getTokensFromFile(String file) throws Exception {
        File f = new File(file);
		return tokenize(f);
	}

    @Override
	public ArrayList<String> getTokensFromString(String input) throws Exception {
		return tokenize(new StringReader(input));
	}
}