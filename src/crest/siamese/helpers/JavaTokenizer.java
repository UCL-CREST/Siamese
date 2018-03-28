package crest.siamese.helpers;

import crest.siamese.settings.NormalizerMode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;

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
        String[] symbols = JavaLexer._SYMBOLIC_NAMES;
        for(Token token : tokenList){
            // normalize the token except white space (skip)
            if (!symbols[token.getType()].equals("WS")) {
                tokens.add(normalizer.normalizeAToken(token.getText().trim(), symbols[token.getType()]));
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