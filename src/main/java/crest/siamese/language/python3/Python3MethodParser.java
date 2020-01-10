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

import crest.siamese.document.Method;
import crest.siamese.document.Parameter;
import crest.siamese.language.MethodParser;
import crest.siamese.settings.Settings;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Method Parser for Python 3. Parses the source code via an input file path to extract a list of methods or the entire
 * source code file (as a single method).
 */
public class Python3MethodParser implements MethodParser {

    private String FILE_PATH;
    private String MODE;
    private static final String WHITESPACE = " ";
    private static final String NEWLINE = "\n";
    private static final String TAB = "\t";

    /**
     * Constructor that refer to parent Object class. Required for calling Class::newInstance.
     */
    public Python3MethodParser() {
        super();
    }

    /**
     * Initialises a Python3MethodParser
     * @param filePath Location of input file
     * @param prefixToRemove Legacy parameter
     * @param mode Method level or Class level parsing
     * @param isPrint Legacy parameter
     */
    public Python3MethodParser(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        this.FILE_PATH = filePath;
        this.MODE = mode;
    }

    /**
     * License extraction is not implemented for Python 3
     * @return null
     */
    @Override
    public String getLicense() {
        return null;
    }

    private static String readFile(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    /**
     * Parses an input file and return the root RuleContext.
     * @param filePath Location of input file
     * @return root RuleContext
     */
    private static Python3Parser.File_inputContext parseFile(String filePath) {
        File file = new File(filePath);
        String sourceCode = readFile(file);

        Python3Lexer lexer = new Python3Lexer(CharStreams.fromString(sourceCode));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokenStream);

        return parser.file_input();
    }

    /**
     * Traverses the class definition RuleContext to find the TerminalNodeImpl containing the class name
     * @param ruleContext Class definition RuleContext
     * @return TerminalNodeImpl of the class name
     */
    private TerminalNodeImpl getClassNode(RuleContext ruleContext) {
        TerminalNodeImpl prev = null;
        TerminalNodeImpl curr = null;
        for (int i = 0; i < ruleContext.getChildCount(); i++) {
            ParseTree parseTree = ruleContext.getChild(i);
            if (parseTree instanceof TerminalNodeImpl) {
                curr = (TerminalNodeImpl) parseTree;
                if (curr.getSymbol().getType() == Python3Parser.NAME &&
                        prev != null && prev.getSymbol().getType() == Python3Parser.CLASS) {
                    break;
                }
                prev = curr;
            }
        }
        return curr;
    }

    /**
     * The method is called when the traverse method encounters a function definition RuleContext. Extracts the list of
     * TerminalNodeImpl of the input RuleContext. If an inner function definition is found, that RuleContext will be
     * traversed and extracted as a separate method. Method extraction excludes the async keyword (for asynchronous
     * function definition) and function decorators.
     * @param ruleContext Starting RuleContext of a function definition
     * @param methods List of Method extracted
     * @param classNode Current TerminalNodeImpl with class name that is the class
     *                  containing all methods traversed onwards from current iteration
     * @param terminalNodes List of TerminalNodeImpl of a method being extracted
     */
    private void extract(RuleContext ruleContext, ArrayList<Method> methods,
                         TerminalNodeImpl classNode, List<TerminalNodeImpl> terminalNodes) {
        for (int i = 0; i < ruleContext.getChildCount(); i++) {
            ParseTree parseTree = ruleContext.getChild(i);
            if (parseTree instanceof RuleContext) {
                RuleContext childRuleContext = (RuleContext) parseTree;
                if (childRuleContext.getRuleIndex() == Python3Parser.RULE_funcdef) {
                    // Inner function definition within a function
                    traverse(childRuleContext, methods, classNode);
                    // Remove async keyword if inner function is asynchronous
                    RuleContext parent = (RuleContext) terminalNodes.get(terminalNodes.size() - 1).getParent();
                    if (parent.getRuleIndex() == Python3Parser.RULE_async_funcdef
                            || parent.getRuleIndex() == Python3Parser.RULE_async_stmt) {
                        terminalNodes.remove(terminalNodes.size() - 1);
                    }
                } else {
                    // Continue extracting terminal nodes from current function
                    // Ignore function decorators of inner function
                    if (childRuleContext.getRuleIndex() != Python3Parser.RULE_decorators) {
                        extract(childRuleContext, methods, classNode, terminalNodes);
                    }
                }
            } else if (parseTree instanceof TerminalNodeImpl) {
                TerminalNodeImpl terminalNode = (TerminalNodeImpl) parseTree;
                terminalNodes.add(terminalNode);
            }
        }
    }

    /**
     * Reformats the input list of TerminalNodeImpl to form the source code String, where lexing the String
     * would return the tokens from the list. This includes the indentation structure which is recreated.
     * @param terminalNodes List of TerminalNodeImpl for a method
     * @return Source code of a method formed from the input list
     */
    private String reformat(List<TerminalNodeImpl> terminalNodes) {
        StringBuilder stringBuilder = new StringBuilder();
        int indents = 0;
        int i = 0;
        Token prevToken = null;
        while (i < terminalNodes.size()) {
            TerminalNodeImpl terminalNodeImpl = terminalNodes.get(i);
            Token token = terminalNodeImpl.getSymbol();
            if (token.getType() == Python3Parser.NEWLINE) {
                stringBuilder.append(NEWLINE);
                while (i + 1 < terminalNodes.size()) {
                    Token next = terminalNodes.get(i + 1).getSymbol();
                    if (next.getType() == Python3Parser.INDENT) {
                        indents++;
                    } else if (next.getType() == Python3Parser.DEDENT) {
                        indents--;
                    } else {
                        break;
                    }
                    i++;
                }
                stringBuilder.append(StringUtils.repeat(TAB, indents));
            } else {
                if (prevToken == null ||
                    prevToken.getType() == Python3Parser.NEWLINE ||
                    prevToken.getType() == Python3Parser.INDENT ||
                    prevToken.getType() == Python3Parser.DEDENT) {
                    stringBuilder.append(token.getText());
                } else {
                    stringBuilder.append(WHITESPACE).append(token.getText());
                }
            }
            i++;
            prevToken = token;
        }
        return stringBuilder.toString();
    }

    /**
     * Creates a Method given the class node (null for global methods) and the list of TerminalNodeImpl for that
     * function. Loops through terminalNodes to find function name, function parameters, function headers.
     * @param classNode TerminalNodeImpl of the class name
     * @param terminalNodes List of TerminalNodeImpl of a method
     * @return {@link crest.siamese.document.Method}
     */
    private Method createMethod(TerminalNodeImpl classNode, List<TerminalNodeImpl> terminalNodes) {
        String className = classNode == null ? StringUtils.EMPTY : classNode.getText();
        String functionName = null;
        int startLine = terminalNodes.get(0).getSymbol().getLine();
        int endLine = terminalNodes.get(terminalNodes.size() - 1).getSymbol().getLine();
        List<Parameter> params = new ArrayList<>();
        StringBuilder header = new StringBuilder();
        for (TerminalNodeImpl terminalNodeImpl: terminalNodes) {
            Token token = terminalNodeImpl.getSymbol();
            RuleContext parent = (RuleContext) terminalNodeImpl.getParent();

            // Function name
            if (functionName == null && token.getType() == Python3Parser.NAME) {
                functionName = token.getText();
            }

            // Function parameters
            if (token.getType() == Python3Parser.NAME && parent.getRuleIndex() == Python3Parser.RULE_tfpdef) {
                params.add(new Parameter(StringUtils.EMPTY, token.getText()));
            }

            // Function header, append space after "def" keyword
            header.append(header.length() == 0 ? token.getText() + WHITESPACE : token.getText());

            // Outside of function header, no further iteration required
            if (token.getType() == Python3Parser.COLON && parent.getRuleIndex() == Python3Parser.RULE_funcdef) {
                break;
            }
        }
        String src = reformat(terminalNodes);
        return new Method(FILE_PATH, StringUtils.EMPTY, className, functionName,
                StringUtils.EMPTY, src, startLine, endLine, params, header.toString());
    }

    /**
     * Recursive depth first traversal that updates the current class node and extracts methods.
     * @param ruleContext RuleContext
     * @param methods List of Method extracted
     * @param classNode Current TerminalNodeImpl with class name that is the class
     *                  containing all methods traversed onwards from current iteration
     */
    private void traverse(RuleContext ruleContext, ArrayList<Method> methods, TerminalNodeImpl classNode) {
        if (ruleContext.getRuleIndex() == Python3Parser.RULE_classdef) {
            classNode = getClassNode(ruleContext);
        }

        if (ruleContext.getRuleIndex() == Python3Parser.RULE_funcdef) {
            List<TerminalNodeImpl> terminalNodes = new ArrayList<>();
            // Add terminal nodes to list in place
            extract(ruleContext, methods, classNode, terminalNodes);
            Method method = createMethod(classNode, terminalNodes);
            methods.add(method);
        } else {
            for (int i = 0; i < ruleContext.getChildCount(); i++) {
                ParseTree parseTree = ruleContext.getChild(i);
                if (parseTree instanceof RuleContext) {
                    traverse((RuleContext) parseTree, methods, classNode);
                }
            }
        }
    }

    /**
     * Iterative depth first traversal of the root RuleContext to find all TerminalNodeImpl
     * (in the order of ascending file and token position as in the original source code)
     * which are then used to create a file-level method.
     * @param ruleContext Root RuleContext of the parsed file
     * @return {@link crest.siamese.document.Method}
     */
    private Method createFileMethod(RuleContext ruleContext) {
        List<TerminalNodeImpl> terminalNodes = new ArrayList<>();

        Stack<ParseTree> stack = new Stack<>();
        stack.push(ruleContext);

        while (!stack.isEmpty()) {
            ParseTree curr = stack.pop();
            if (curr instanceof RuleContext) {
                // First child would be at top of stack
                for (int i = curr.getChildCount() - 1; i >= 0; i--) {
                    stack.push(curr.getChild(i));
                }
            } else if (curr instanceof TerminalNodeImpl) {
                terminalNodes.add((TerminalNodeImpl) curr);
            }
        }

        int startLine = terminalNodes.get(0).getSymbol().getLine();
        int endLine = terminalNodes.get(terminalNodes.size() - 1).getSymbol().getLine();
        List<Parameter> params = new ArrayList<>();
        String src = reformat(terminalNodes);

        return new Method(FILE_PATH, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, src, startLine, endLine, params, StringUtils.EMPTY);
    }

    /**
     * Uses ANTLR4 generated Parser and Lexer to extract methods from Python3 source code file
     * @return ArrayList of methods extracted from FILE_PATH attribute
     */
    @Override
    public ArrayList<Method> parseMethods() {
        RuleContext ruleContext = parseFile(FILE_PATH);
        ArrayList<Method> methods = new ArrayList<>();
        if (MODE.equals(Settings.MethodParserType.METHOD)) {
            // Add methods to list in place
            traverse(ruleContext, methods, null);
        } else {
            // Use the entire file as a single method
            methods.add(createFileMethod(ruleContext));
        }
        return methods;
    }

    /**
     * Configures constructor attributes when initialised with Class.newInstance() in Siamese.java
     * @param filePath Location of input file
     * @param prefixToRemove Legacy parameter
     * @param mode Method level or Class level parsing
     * @param isPrint Legacy parameter
     */
    @Override
    public void configure(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        this.FILE_PATH = filePath;
        this.MODE = mode;
    }
}
