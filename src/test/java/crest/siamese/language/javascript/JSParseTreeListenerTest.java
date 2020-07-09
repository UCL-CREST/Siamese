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
import static org.junit.Assert.assertNotNull;


public class JSParseTreeListenerTest {


    String dummyFilePath = "crest/siamese/language/javascript/DemoTest.js";
    String functionalDeclarationSourceCode = "function factorial(n) {\n" +
            "    if (n === 0) {\n" +
            "        return 1;\n" +
            "    }\n" +
            "    return n * factorial(n - 1);\n" +
            "}";
    String classAndMemberMethodDeclarationSourceCode = "class Car {\n" +
            "    present() {\n" +
            "        return \"I have a \" + this.carname;\n" +
            "    }\n" +
            "}";
    String classMemberWithGeneratorMethods = "\"use strict\";\n" +
            "class Car {\n" +
            "    * stopCar() {\n" +
            "        return \"The car is stopped\";\n" +
            "    }\n" +
            "}";

    String functionalExpresionSourceCode = "var messageFunction = function messageFunction(message) {\n" +
            "    return message + ' World!';\n" +
            "}";


    String startWithHashSymbolMethodSourceCode = "class Bar {\n" +
            "    async static #asyncStaticClassMethod() {\n" +
            "    }\n" +
            "}";
    String arrowFunctionSourceCode = "const absValue = (number) => {\n" +
            "    if (number < 0) {\n" +
            "        return -number;\n" +
            "    }\n" +
            "    return number;\n" +
            "}";
    String generationFunctionSourceCode = "\"use strict\";\n" +
            "function* range(start, end, step) {\n" +
            "    while (start < end) {\n" +
            "        yield start\n" +
            "        start += step\n" +
            "    }\n" +
            "}";

    String allFunctionalExpression = "\"use strict\";\n" +
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

    String functionDecSource = "(function messageFunction(message) {\n" +
            "        return message + ' World!';\n" +
            "    })('Hello');";
    ParseTree parseTree;
    JSParseTreeListener jsParseTreeListener;

    public void init(String source) {
        CharStream sourceStream = CharStreams.fromString(source, dummyFilePath);
        JavaScriptParser parser = new Builder.Parser(sourceStream).build();
        parseTree = parser.program();
        jsParseTreeListener = new JSParseTreeListener(dummyFilePath);
    }

    @Test
    public void JSParseTreeListenerConstructorAndOverridenFunctionTest() {
        CharStream sourceStream = CharStreams.fromString(allFunctionalExpression, dummyFilePath);
        JavaScriptParser parser = new Builder.Parser(sourceStream).build();
        parseTree = parser.program();
        jsParseTreeListener = new JSParseTreeListener(dummyFilePath);
        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
        List<Method> methods = jsParseTreeListener.getJSMethods();
        assertEquals(methods.size(), 3);
    }

    @Test
    public void getRangeTest() {
        init(this.functionalDeclarationSourceCode);
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
        init(this.functionalDeclarationSourceCode);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext ctx) {
                assertEquals("factorial", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);


    }

    @Test
    public void functionIdentifierOfFunctionExpressionTest() {
        init(this.functionalExpresionSourceCode);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterFunctionExpression(JavaScriptParser.FunctionExpressionContext ctx) {
                assertEquals("Function Expression", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);


    }

    @Test
    public void functionIdentifierOfClassMethodDeclarationTest() {
        init(this.classAndMemberMethodDeclarationSourceCode);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                assertEquals("present", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void functionIdentifierOfClassGeneratorMethodDeclarationTest() {
        init(this.classMemberWithGeneratorMethods);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                assertEquals("stopCar", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void functionIdentifierOfHashMethodDeclarationTest() {
        init(this.startWithHashSymbolMethodSourceCode);
        ParseTreeWalker.DEFAULT.walk(new JavaScriptParserBaseListener() {
            @Override
            public void enterMethodDefinition(JavaScriptParser.MethodDefinitionContext ctx) {
                assertEquals("asyncStaticClassMethod", jsParseTreeListener.getFunctionIdentifier(ctx));
            }
        }, this.parseTree);
    }

    @Test
    public void getParametersTest() {
        init(this.functionalDeclarationSourceCode);
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
        init(this.functionalDeclarationSourceCode);
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
        init(this.classAndMemberMethodDeclarationSourceCode);
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
        init(this.functionalDeclarationSourceCode);
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
        init(this.functionalDeclarationSourceCode);
        String src = "function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial ( n - 1 ) ; }";
        int startLine = 1;
        int endLine = 6;
        List<Parameter> parameters = new ArrayList<>();
        Method method = new Method(this.dummyFilePath, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, src, startLine, endLine, parameters, StringUtils.EMPTY);
        Method fileBlockMethod = this.jsParseTreeListener.getFileBlockMethod(this.parseTree);
        assertEquals(method, fileBlockMethod);
    }

    @Test
    public void traverseParseTreeTest() {
        init(this.functionalDeclarationSourceCode);
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
        init(this.functionalDeclarationSourceCode);
        String src = "function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial ( n - 1 ) ; }";
        int startLine = 1;
        int endLine = 6;
        String className = StringUtils.EMPTY;
        String functionName = "factorial";
        String header = "function (n)";
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(StringUtils.EMPTY, "n"));
        String filePath = this.dummyFilePath;

        Method method = new Method(filePath, StringUtils.EMPTY, className, functionName, StringUtils.EMPTY,
                src, startLine, endLine, parameters, header);

        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
        assertEquals(1, jsParseTreeListener.getJSMethods().size());
        assertEquals(method, jsParseTreeListener.getJSMethods().get(0));

    }

    @Test
    public void ignoringRepeatedFunctionExtractionTest() {
        init(functionDecSource);
        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
        List<Method> methods = jsParseTreeListener.getJSMethods();
        assertEquals(methods.size(), 1);
    }

}
