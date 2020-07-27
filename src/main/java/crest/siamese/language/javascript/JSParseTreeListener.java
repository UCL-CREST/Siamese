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

import crest.siamese.document.Method;
import crest.siamese.document.Parameter;
import crest.siamese.language.javascript.JavaScriptParser.FunctionDeclarationContext;
import crest.siamese.language.javascript.JavaScriptParser.FunctionExpressionContext;
import crest.siamese.language.javascript.JavaScriptParser.MethodDefinitionContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class works for collecting method blocks by traversing ANTLR4 generated Parse Tree
 * based on the ANTLR4 recommended Listener pattern.
 */

public class JSParseTreeListener extends JavaScriptParserBaseListener {

    private final String FUNCTION_EXPRESSION = "Function Expression";
    private final String START = "START";
    private final String END = "END";

    private List<Method> jsMethods;
    private String filePath;
    private Map<Integer, Integer> sourceStartEndMap;
    private Map<Integer, String> sourceCodeMap;


    /**
     * Constructor to build JSParseTreeListener
     *
     * @param filePath JavaScript source file path
     */
    public JSParseTreeListener(String filePath) {
        this.filePath = filePath;
        this.jsMethods = new ArrayList<>();
        this.sourceStartEndMap = new HashMap<>();
        this.sourceCodeMap = new HashMap<>();

    }


    @Override
    public void enterFunctionDeclaration(FunctionDeclarationContext ctx) {
        buildMethod(ctx);
    }

    @Override
    public void enterFunctionExpression(FunctionExpressionContext ctx) {
        buildMethod(ctx);
    }

    @Override
    public void enterMethodDefinition(MethodDefinitionContext ctx) {
        buildMethod(ctx);
    }


    protected List<Method> getJSMethods() {
        return this.jsMethods;
    }

    /**
     * This method builds a Method object using the complete source code of the file.
     *
     * @param tree ANTLR4 generated Parse Tree
     * @return A Method Object containing the whole source code of the file block.
     */
    protected Method getFileBlockMethod(ParseTree tree) {
        String src = getSourceCode(tree);
        Map<String, Integer> range = getRange(tree);
        int startLine = range.get(START);
        int endLine = range.get(END);
        List<Parameter> parameters = new ArrayList<>();

        return new Method(this.filePath, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, src, startLine, endLine, parameters, StringUtils.EMPTY);
    }


    /**
     * This method builds a Complete Method from the ANTLR4 generated Parse Tree.
     * It collects function name, parameters, headers source, start and end line.
     * Moreover, it helps to detect duplicate extracted methods from the Parse Tree.
     *
     * @param tree ANTLR4 generated Parse Tree
     */
    protected void buildMethod(ParseTree tree) {
        Map<String, Integer> range = getRange(tree);
        int startLine = range.get(START);
        int endLine = range.get(END);
        if ((sourceStartEndMap.containsKey(startLine) && sourceStartEndMap.get(startLine).equals(endLine))) {
            if (sourceCodeMap.get(startLine).equals(getSourceCode(tree))) {
                return;
            }
        }
        String src = getSourceCode(tree);
        List<Parameter> parameters = new ArrayList<>();

        /*
          Method @{@link #getClassName(ParseTree)} and
          @{@link #getFunctionIdentifier(ParseTree)} and
         * @{@link #getHeader(ParseTree)} are implemented according to the requirement
         * but not utilized here.
         */

       /* String className = getClassName(tree);
        String functionName = getFunctionIdentifier(tree);
        String header = getHeader(functionName, parameters);*/

        this.sourceStartEndMap.put(startLine, endLine);
        this.sourceCodeMap.put(startLine, src);


        Method method = new Method(this.filePath, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, src, startLine, endLine, parameters, StringUtils.EMPTY);
        jsMethods.add(method);

    }

    /**
     * This method is responsible for retrieving the source code from the ANTLR4 generated Parse Tree.
     * It travers ANTLR4 generated Parse Tree to get the list of terminal nodes and then concatenating
     * the terminal nodes values as string to produce the tokenize source code.
     *
     * @param tree ANTLR4 generated Parse Tree
     * @return Extract function block source code.
     */
    protected String getSourceCode(ParseTree tree) {
        StringBuilder builder = new StringBuilder();
        List<String> terminalNodes = traverseParseTree(tree);
        for (String tm : terminalNodes) {
            builder.append(tm).append(" ");
        }
        return builder.toString().trim();
    }


    /**
     * Travers ANTLR4 generated Parse Tree using Depth-First-Search and collect all the terminal nodes.
     *
     * @param tree tree ANTLR4 generated Parse Tree
     * @return A list of String derived from function block terminal nodes.
     */
    protected List<String> traverseParseTree(ParseTree tree) {
        List<String> terminalNodes = new ArrayList<>();
        List<ParseTree> firstStack = new ArrayList<>();
        firstStack.add(tree);
        List<List<ParseTree>> childListStack = new ArrayList<>();
        childListStack.add(firstStack);
        while (!childListStack.isEmpty()) {
            List<ParseTree> childStack = childListStack.get(childListStack.size() - 1);
            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            } else {
                tree = childStack.remove(0);
                if (tree instanceof TerminalNodeImpl) {
                    terminalNodes.add((tree.getText()));
                }
                if (tree.getChildCount() > 0) {
                    List<ParseTree> children = new ArrayList<>();
                    for (int i = 0; i < tree.getChildCount(); i++) {
                        children.add(tree.getChild(i));
                    }
                    childListStack.add(children);
                }
            }
        }
        return terminalNodes;
    }


    /**
     * This method is called to identify the function parameters. It first travers ANTLR4 generated Parse Tree
     * to locate the FormalParameterListContext RuleContext and extract function parameters from
     * the FormalParameterArgContext RuleContext
     *
     * @param tree tree ANTLR4 generated Parse Tree
     * @return A list of Parameters derived from function block and return empty list for Arrow function
     */
    protected List<Parameter> getParameters(ParseTree tree) {
        List<Parameter> parameters = new ArrayList<>();
        if (tree.getChildCount() > 0) {
            for (int i = 0; i < tree.getChildCount(); i++) {
                if (tree.getChild(i) instanceof JavaScriptParser.FormalParameterListContext) {
                    ParseTree parameter_list = tree.getChild(i);
                    for (int j = 0; j < parameter_list.getChildCount(); j++) {
                        if (parameter_list.getChild(j) instanceof JavaScriptParser.FormalParameterArgContext) {
                            String param = parameter_list.getChild(j).getText();
                            parameters.add(new Parameter(StringUtils.EMPTY, param));
                        }
                    }
                    break;
                }
            }
        }
        return parameters;
    }


    /**
     * This method is invoked to identify class name for the methods that are declared inside a class. It travers
     * ANTLR4 generated Parse Tree to locate the MethodDefinitionContext RuleContext and the
     * extract the class name from the parent RuleContext ClassDeclarationContext.
     *
     * @param tree tree ANTLR4 generated Parse Tree
     * @return Class name if the extracted method is under any class declaration otherwise
     * return empty string as StringUtils.EMPTY
     */
    protected String getClassName(ParseTree tree) {
        String className = StringUtils.EMPTY;
        try {
            if (tree instanceof JavaScriptParser.MethodDefinitionContext) {
                ParseTree parentClassContext = tree.getParent();
                while (!(parentClassContext instanceof JavaScriptParser.ClassDeclarationContext)) {
                    parentClassContext = parentClassContext.getParent();
                }
                ParseTree identifier = parentClassContext.getChild(1);
                className = identifier.getText();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return className;
    }


    /**
     * This method helps to extract the function name. It first travers ANTLR4 generated Parse Tree to locate
     * the FunctionDeclarationContext, FunctionExpressionContext and MethodDefinitionContext RuleContext.
     * The it extracts function name/ identifier from the IdentifierContext RuleContext.
     *
     * @param tree tree ANTLR4 generated Parse Tree
     * @return Function name/ Identifier as String
     */
    protected String getFunctionIdentifier(ParseTree tree) {
        String functionName = StringUtils.EMPTY;
        if (tree instanceof JavaScriptParser.FunctionDeclarationContext) {
            if (tree.getChildCount() > 0) {
                for (int i = 0; i < tree.getChildCount(); i++) {
                    ParseTree child = tree.getChild(i);
                    if (child instanceof JavaScriptParser.IdentifierContext) {
                        functionName = child.getText();
                        break;
                    }
                }
            }
        } else if (tree instanceof JavaScriptParser.FunctionExpressionContext) {
            functionName = FUNCTION_EXPRESSION;//Functional expression does not contain any identifier
        } else if (tree instanceof JavaScriptParser.MethodDefinitionContext) {// profess class member method
            while (!(tree instanceof JavaScriptParser.IdentifierContext)) {
                if (tree.getChildCount() > 0) {// check even a member method is a generator function
                    if (tree.getChild(0) instanceof TerminalNodeImpl && tree.getChild(0).getText().equals("*")) {
                        tree = tree.getChild(1);
                        continue;
                    }// check even a member method starts with #
                    if (tree.getChild(0) instanceof TerminalNodeImpl && tree.getChild(0).getText().equals("#")) {
                        tree = tree.getChild(1);
                        continue;
                    }
                    tree = tree.getChild(0);
                }
            }
            functionName = tree.getText();
        }
        return functionName;
    }


    /**
     * Create function signature as function header by concatenating the function name and parameters
     *
     * @param functionName Extracted function name
     * @param parameters   List of parameters
     * @return Function header containing function name and parameters as String
     */
    protected String getHeader(String functionName, List<Parameter> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(functionName).append(" ").append("(");
        for (Parameter p : parameters) {
            sb.append(p.getId()).append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',')
            sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }


    /**
     * Identify the method block range from the Parse tree.
     *
     * @param tree ANTLR4 generated Parse Tree
     * @return A Map containing start line and end line of the method block
     */
    protected Map<String, Integer> getRange(ParseTree tree) {
        Map<String, Integer> range = new HashMap<>();
        range.put(START, 1);
        range.put(END, 1);
        if (tree instanceof ParserRuleContext) {
            ParserRuleContext ctx = (ParserRuleContext) tree;
            range.put(START, ctx.getStart().getLine());
            range.put(END, ctx.getStop().getLine());
        }
        return range;
    }


}