package crest.siamese.language.javascript;

import crest.siamese.document.Method;
import crest.siamese.document.Parameter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class JSParseTreeListenerTest {


    final String DUMMY_FILE_PATH = "crest/siamese/language/javascript/DemoTest.js";

    final String FUNCTIONAL_DECLARATION = "function factorial(n) {\n" +
            "    if (n === 0) {\n" +
            "        return 1;\n" +
            "    }\n" +
            "    return n * factorial(n - 1);\n" +
            "}";
    final String FUNCTIONAL_EXPRESION_SOURCE = "var messageFunction = function messageFunction(message) {\n" +
            "    return message + ' World!';\n" +
            "}";

    final String CLASS_MEMBER_METHOD_DECLARATION = "class Car {\n" +
            "    present() {\n" +
            "        return \"I have a \" + this.carname;\n" +
            "    }\n" +
            "}";
    final String CLASS_MEMBER_WITH_GENERATOR_METHOD = "\"use strict\";\n" +
            "class Car {\n" +
            "    * stopCar() {\n" +
            "        return \"The car is stopped\";\n" +
            "    }\n" +
            "}";


    final String METHOD_START_WITH_HASH_SYMBOL = "class Bar {\n" +
            "    async static #asyncStaticClassMethod() {\n" +
            "    }\n" +
            "}";

    final String ALL_FUNCTIONAL_EXPRESSION = "\"use strict\";\n" +
            "\n" +
            "function add(a, b) {\n" +
            "    return a + b;\n" +
            "}\n" +
            "\n" +
            "var mul = function (x, y) {\n" +
            "    return x * y;\n" +
            "}\n" +
            "\n" +
            "class Car {\n" +
            "    present() {\n" +
            "        \n" +
            "    }\n" +
            "}";

    final String FUNCTION_DEC_SOURCE = "(function messageFunction(message) {\n" +
            "        return message + ' World!';\n" +
            "    })('Hello');";


    final String ARROW_FUNCTION = "const absValue = (number) => {\n" +
            "    if (number < 0) {\n" +
            "        return -number;\n" +
            "    }\n" +
            "    return number;\n" +
            "}";
    final String GENERATOR_FUNCTION = "\"use strict\";\n" +
            "function* range(start, end, step) {\n" +
            "    while (start < end) {\n" +
            "        yield start\n" +
            "        start += step\n" +
            "    }\n" +
            "}";


    ParseTree parseTree;
    JSParseTreeListener jsParseTreeListener;

    public void init(String source) {
        CharStream sourceStream = CharStreams.fromString(source, DUMMY_FILE_PATH);
        JavaScriptParser parser = new Builder.Parser(sourceStream).build();
        parseTree = parser.program();
        jsParseTreeListener = new JSParseTreeListener(DUMMY_FILE_PATH);
    }

    @Test
    public void JSParseTreeListenerConstructorAndOverriddenFunctionTest() {
        CharStream sourceStream = CharStreams.fromString(ALL_FUNCTIONAL_EXPRESSION, DUMMY_FILE_PATH);
        JavaScriptParser parser = new Builder.Parser(sourceStream).build();
        parseTree = parser.program();
        jsParseTreeListener = new JSParseTreeListener(DUMMY_FILE_PATH);
        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
        List<Method> methods = jsParseTreeListener.getJSMethods();
        assertEquals(methods.size(), 3);
    }

    @Test
    public void getRangeTest() {
        init(this.FUNCTIONAL_DECLARATION);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
                assertEquals(1, (int) jsParseTreeListener.getRange(ctx).get("START"));
                assertEquals(6, (int) jsParseTreeListener.getRange(ctx).get("END"));
            }
        }, this.parseTree);
    }

    @Test
    public void functionIdentifierOfFunctionDeclarationTest() {
        init(this.FUNCTIONAL_DECLARATION);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
                assertEquals("factorial", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);


    }

    @Test
    public void functionIdentifierOfFunctionExpressionTest() {
        init(this.FUNCTIONAL_EXPRESION_SOURCE);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionExpression(JavaScriptParser.FunctionExpressionContext ctx) {
                assertEquals("Function Expression", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);


    }

    @Test
    public void functionIdentifierOfClassMethodDeclarationTest() {
        init(this.CLASS_MEMBER_METHOD_DECLARATION);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                assertEquals("present", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void functionIdentifierOfClassGeneratorMethodDeclarationTest() {
        init(this.CLASS_MEMBER_WITH_GENERATOR_METHOD);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                assertEquals("stopCar", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void functionIdentifierOfHashMethodDeclarationTest() {
        init(this.METHOD_START_WITH_HASH_SYMBOL);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                assertEquals("asyncStaticClassMethod", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void getParametersTest() {
        init(this.FUNCTIONAL_DECLARATION);
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(StringUtils.EMPTY, "n"));
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
                assertEquals(params.size(), jsParseTreeListener.getParameters(ctx).size());
                assertEquals(params, jsParseTreeListener.getParameters(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void getHeaderTest() {
        init(this.FUNCTIONAL_DECLARATION);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {

                String functionName = jsParseTreeListener.getFunctionIdentifier(ctx);
                List<Parameter> parameters = jsParseTreeListener.getParameters(ctx);
                String header = jsParseTreeListener.getHeader(functionName, parameters);
                assertEquals("factorial (n)", header);
            }
        }, this.parseTree);
    }

    @Test
    public void getClassNameTest() {
        init(this.CLASS_MEMBER_METHOD_DECLARATION);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                String functionName = jsParseTreeListener.getClassName(ctx);
                assertEquals("Car", functionName);
            }
        }, this.parseTree);
    }

    @Test
    public void getSourceCodeTest() {
        init(this.FUNCTIONAL_DECLARATION);
        String source = "function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial ( n - 1 ) ; }";
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
                String extractedSource = jsParseTreeListener.getSourceCode(ctx);
                assertEquals(source, extractedSource);
            }
        }, this.parseTree);
    }

    @Test
    public void getFileBlockMethodTest() {
        init(this.FUNCTIONAL_DECLARATION);
        String src = "function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial ( n - 1 ) ; }";
        int startLine = 1;
        int endLine = 6;
        List<Parameter> parameters = new ArrayList<>();
        Method method = new Method(this.DUMMY_FILE_PATH, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, src, startLine, endLine, parameters, StringUtils.EMPTY);
        Method fileBlockMethod = this.jsParseTreeListener.getFileBlockMethod(this.parseTree);
        assertEquals(method, fileBlockMethod);
    }

    @Test
    public void traverseParseTreeTest() {
        init(this.FUNCTIONAL_DECLARATION);
        String source = "function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial ( n - 1 ) ; }";
        List<String> terminalNodes = Arrays.asList(source.split("\\s"));
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
                List<String> nodes = jsParseTreeListener.traverseParseTree(ctx);
                assertEquals(terminalNodes, nodes);
            }
        }, this.parseTree);
    }

    @Test
    public void buildMethodTest() {
        init(this.FUNCTIONAL_DECLARATION);
        String src = "function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial ( n - 1 ) ; }";
        int startLine = 1;
        int endLine = 6;
        String className = StringUtils.EMPTY;
        String functionName = StringUtils.EMPTY;
        String header = StringUtils.EMPTY;
        List<Parameter> parameters = new ArrayList<>();

        Method method = new Method(this.DUMMY_FILE_PATH, StringUtils.EMPTY, className, functionName, StringUtils.EMPTY,
                src, startLine, endLine, parameters, header);

        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
        assertEquals(1, jsParseTreeListener.getJSMethods().size());
        assertEquals(method, jsParseTreeListener.getJSMethods().get(0));

    }

    @Test
    public void ignoringRepeatedFunctionExtractionTest() {
        init(FUNCTION_DEC_SOURCE);
        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
        List<Method> methods = jsParseTreeListener.getJSMethods();
        assertEquals(methods.size(), 1);
    }

}
