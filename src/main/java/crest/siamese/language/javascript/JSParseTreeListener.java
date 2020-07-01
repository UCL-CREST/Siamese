package crest.siamese.language.javascript;

import crest.siamese.language.javascript.JavaScriptParser.*;
import crest.siamese.document.Method;
import crest.siamese.document.Parameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSParseTreeListener extends JavaScriptParserBaseListener {

    static final String FUNCTION_EXPRESSION = "Function Expression";
    static final String ARROW_FUNCTION = "Arrow Function";
    static final String ANONYMOUS_FUNCTION = "Anonymous Function";
    static final String START = "START";
    static final String END = "END";

    ParseTree parseTree;
    List<Method> jsMethods;
    String filePath;

    public JSParseTreeListener(String filePath, ParseTree parseTree) {
        this.parseTree = parseTree;
        this.filePath = filePath;
        jsMethods = new ArrayList<>();

    }

    public List<Method> getJSMethods() {
        return this.jsMethods;
    }

    public Method getFileBlockMethod() {
        String src = getSourceCode(this.parseTree);
        Map<String, Integer> range = getRange(this.parseTree);
        int startLine = range.get(START);
        int endLine = range.get(END);
        List<Parameter> parameters = new ArrayList<>();

        return new Method(this.filePath, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, src, startLine, endLine, parameters, StringUtils.EMPTY);
    }

    private Method buildMethod(ParseTree tree) {
        String className = getClassName(tree);
        String functionName = getFunctionIdentifier(tree);
        List<Parameter> parameters = getParameters(tree);
        String header = getHeader(functionName, parameters);
        Map<String, Integer> range = getRange(tree);
        int startLine = range.get(START);
        int endLine = range.get(END);
        String src = getSourceCode(tree);
        return new Method(filePath, StringUtils.EMPTY, className, functionName, StringUtils.EMPTY,
                src, startLine, endLine, parameters, header);
    }


    @Override
    public void enterFunctionDeclaration(FunctionDeclarationContext ctx) {
        jsMethods.add(buildMethod(ctx));
    }

    @Override
    public void enterFunctionExpression(FunctionExpressionContext ctx) {
        jsMethods.add(buildMethod(ctx));
    }

    @Override
    public void enterFunctionDecl(FunctionDeclContext ctx) {
        jsMethods.add(buildMethod(ctx));
    }

    @Override
    public void enterAnoymousFunctionDecl(AnoymousFunctionDeclContext ctx) {
        jsMethods.add(buildMethod(ctx));
    }

    @Override
    public void enterArrowFunction(ArrowFunctionContext ctx) {
        jsMethods.add(buildMethod(ctx));
    }

    @Override
    public void enterMethodDefinition(MethodDefinitionContext ctx) {
        jsMethods.add(buildMethod(ctx));
    }


    private static String getSourceCode(ParseTree tree) {
        StringBuilder builder = new StringBuilder();
        List<TerminalNodeImpl> terminalNodes = traverseParseTree(tree);
        for (TerminalNodeImpl tm : terminalNodes) {
            builder.append(tm.getText() + " ");
        }
        return builder.toString();
    }

    private static List<TerminalNodeImpl> traverseParseTree(ParseTree tree) {
        List<TerminalNodeImpl> terminalNodes = new ArrayList<>();
        List<ParseTree> firstStack = new ArrayList<ParseTree>();
        firstStack.add(tree);
        List<List<ParseTree>> childListStack = new ArrayList<List<ParseTree>>();
        childListStack.add(firstStack);
        while (!childListStack.isEmpty()) {
            List<ParseTree> childStack = childListStack.get(childListStack.size() - 1);
            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            } else {
                tree = childStack.remove(0);
                if (tree instanceof TerminalNodeImpl) {
                    terminalNodes.add((TerminalNodeImpl) tree);
                }
                if (tree.getChildCount() > 0) {
                    List<ParseTree> children = new ArrayList<ParseTree>();
                    for (int i = 0; i < tree.getChildCount(); i++) {
                        children.add(tree.getChild(i));
                    }
                    childListStack.add(children);
                }
            }
        }
        return terminalNodes;
    }

    private static List<Parameter> getFunctionDeclarationParameters(ParseTree tree) {
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

    private static List<Parameter> getArrowFunctionParameters(ParseTree tree) {
        List<Parameter> parameters = new ArrayList<>();
        if (tree.getChildCount() > 0) {
            ParseTree firstChild = tree.getChild(0);//ArrowFunctionParametersContext
            if (firstChild.getChildCount() > 0) {
                for (int i = 0; i < firstChild.getChildCount(); i++) {
                    if (firstChild.getChild(i) instanceof JavaScriptParser.FormalParameterListContext) {
                        ParseTree secondChild = firstChild.getChild(i);
                        if (secondChild.getChildCount() > 0) {
                            for (int j = 0; j < secondChild.getChildCount(); j++) {
                                if (secondChild.getChild(j) instanceof JavaScriptParser.FormalParameterArgContext) {
                                    String param = secondChild.getChild(j).getText();
                                    parameters.add(new Parameter(StringUtils.EMPTY, param));
                                }
                            }
                            break;
                        }

                    }
                }
            }
        }
        return parameters;
    }

    private static List<Parameter> getParameters(ParseTree tree) {
        List<Parameter> parameters = new ArrayList<>();
        if (tree instanceof JavaScriptParser.AnoymousFunctionContext) {
            parameters.addAll(getArrowFunctionParameters(tree));
        } else {
            parameters.addAll(getFunctionDeclarationParameters(tree));
        }
        return parameters;
    }

    private static String getClassName(ParseTree tree) {
        String className = StringUtils.EMPTY;
        if (tree instanceof JavaScriptParser.MethodDefinitionContext) {
            ParseTree parentClassContext = tree.getParent();
            while (!(parentClassContext instanceof JavaScriptParser.ClassDeclarationContext)) {
                parentClassContext = parentClassContext.getParent();
            }
            ParseTree identifier = parentClassContext.getChild(1);
            className = identifier.getText();
        }
        return className;
    }

    private static String getFunctionIdentifier(ParseTree tree) {
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
        } else if (tree instanceof JavaScriptParser.ArrowFunctionContext) {
            functionName = ARROW_FUNCTION;//Arrow function does not contain any identifier
        } else if (tree instanceof JavaScriptParser.AnoymousFunctionContext) {
            functionName = ANONYMOUS_FUNCTION;// Anonymous Function does not contain any identifier
        } else if (tree instanceof JavaScriptParser.MethodDefinitionContext) {// profess class member method
            while (!(tree instanceof JavaScriptParser.IdentifierContext)) {
                if (tree.getChildCount() > 0) {// check even a member method is a generator function
                    if (tree.getChild(0) instanceof TerminalNodeImpl && tree.getChild(0).getText().equals("*")) {
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

    private static String getHeader(String functionName, List<Parameter> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(functionName + " " + "(");
        for (Parameter p : parameters) {
            sb.append(p.getId() + ",");
        }
        if (sb.charAt(sb.length() - 1) == ',')
            sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    private static Map<String, Integer> getRange(ParseTree tree) {
        Map<String, Integer> range = new HashMap<>();
        range.put(START, 0);
        range.put(END, 0);
        if (tree instanceof ParserRuleContext) {
            ParserRuleContext ctx = (ParserRuleContext) tree;
            range.put(START, ctx.getStart().getLine());
            range.put(END, ctx.getStop().getLine());
        }
        return range;
    }

}
