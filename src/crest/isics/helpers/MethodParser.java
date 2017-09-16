package crest.isics.helpers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import crest.isics.document.Method;
import crest.isics.main.Experiment;
import crest.isics.settings.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MethodParser {
    private ArrayList<Method> methodList = new ArrayList<Method>();
    private String FILE_PATH = "";
    private String PREFIX_TO_REMOVE = "";
    private String MODE = Settings.MethodParserType.METHOD;
    public static String JAVA_PACKAGE = "";
    public static String JAVA_CLASS = "";

    public MethodParser(String filePath, String prefixToRemove, String mode) {
        FILE_PATH = filePath;
        PREFIX_TO_REMOVE = prefixToRemove;
        MODE = mode;
    }

    /***
     * Extract both methods and constructors
     * @return a list of methods & constructors
     */
    public ArrayList<Method> parseMethods() {
        try {
            FileInputStream in = new FileInputStream(FILE_PATH);
            CompilationUnit cu;
            // method-level parser
            if (MODE == Settings.MethodParserType.METHOD) {
                try {
                    cu = JavaParser.parse(in);

                    List<TypeDeclaration> types = cu.getTypes();
                    for (TypeDeclaration type : types) {
                        if (type instanceof ClassOrInterfaceDeclaration) {
                            // getting class name
                            ClassOrInterfaceDeclaration classDec = (ClassOrInterfaceDeclaration) type;
                            JAVA_CLASS = classDec.getName();
                        }
                    }

                    new MethodVisitor().visit(cu, null);
                    new ConstructorVisitor().visit(cu, null);

                } catch (Throwable e) {
                    if (Experiment.isPrint)
                        System.out.println("Unparseable method (use whole fragment)");
                    Method m = getWholeFragment();
                    if (m != null)
                        methodList.add(m);
                } finally {
                    in.close();
                }
            } else {
                // file-level parser
                Method m = getWholeFragment();
                if (m != null)
                    methodList.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodList;
    }

    private Method getWholeFragment() throws FileNotFoundException {
        try {
            String content = new Scanner(new File(FILE_PATH)).useDelimiter("\\Z").next();
            Method m = new Method(
                    FILE_PATH.replace(PREFIX_TO_REMOVE, ""),
                    "package",
                    "ClassName",
                    "method",
                    content,
                    -1,
                    -1,
                    new LinkedList<crest.isics.document.Parameter>(),
                    "");
            return m;
        } catch (NoSuchElementException e) {
            System.out.println("ERROR: can't parse + get whole fragment from file " + FILE_PATH);
            return null;
        }
    }

    private String getOnlyMethodName(String methodHeader) {
        String[] methodNames = methodHeader.split(" ");
        String methodName = "no_name";
        for (String mName : methodNames) {
            if (mName.contains("(")) {
                methodName = mName.substring(0, mName.indexOf("("));
            }
        }
        return methodName;
    }

    /***
     * Extract methods
     */
    private class MethodVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(MethodDeclaration n, Object arg) {

            List<Parameter> parameterArrayList = n.getParameters();
            ArrayList<crest.isics.document.Parameter> paramsList = new ArrayList<>();
            for (Parameter p: parameterArrayList) {
                paramsList.add(
                        new crest.isics.document.Parameter(
                                p.getType().toString(),
                                p.getId().toString()));
            }

            Method m = new Method(
                    FILE_PATH.replace(PREFIX_TO_REMOVE, "")
                    , JAVA_PACKAGE
                    , JAVA_CLASS
                    , n.getName()
                    // copied the regex from
                    // http://stackoverflow.com/questions/9205988/writing-a-java-program-to-remove-the-comments-in-same-java-program
                    , n.toStringWithoutComments()
                    .replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","")
                    , n.getBeginLine()
                    , n.getEndLine()
                    , paramsList
                    , n.getDeclarationAsString());
            methodList.add(m);
            super.visit(n, arg);
        }
    }

    /***
     * Extract constructors
     */
    private class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration c, Object arg) {

            List<Parameter> parameterArrayList = c.getParameters();
            ArrayList<crest.isics.document.Parameter> paramsList = new ArrayList<>();
            for (Parameter p: parameterArrayList) {
                paramsList.add(
                        new crest.isics.document.Parameter(
                                p.getType().toString(),
                                p.getId().toString()));
            }

            Method m = new Method(
                    FILE_PATH.replace(PREFIX_TO_REMOVE, "")
                    , JAVA_PACKAGE
                    , JAVA_CLASS
                    , c.getName()
                    // copied the regex from
                    // http://stackoverflow.com/questions/9205988/writing-a-java-program-to-remove-the-comments-in-same-java-program
                    , c.toStringWithoutComments()
                    .replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "")
                    , c.getBeginLine()
                    , c.getEndLine()
                    , paramsList
                    , c.getDeclarationAsString());
            methodList.add(m);
            super.visit(c, arg);
        }
    }

    public void printMethods(String javaFile) throws IOException {
        FileInputStream in = new FileInputStream(javaFile);
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
            new MethodVisitor().visit(cu, null);
            new ConstructorVisitor().visit(cu, null);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
    }
}
