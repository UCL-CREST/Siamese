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

package crest.siamese.document;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private String file;
    private String methodPackage;
    private String className;
    private String name;
    private String header;
    private String comment;
    private String src;
    private int startLine;
    private int endLine;
    private List<Parameter> params;

    public Method() {
        /* create a blank method */
        this.methodPackage = "";
        this.name = "";
        this.className = "";
        this.params = new ArrayList<>();
    }

    public Method(String file,
                  String methodPackage,
                  String className,
                  String name,
                  String comment,
                  String src,
                  int startLine,
                  int endLine,
                  List<Parameter> params,
                  String header) {
        this.file = file;
        this.methodPackage = methodPackage;
        this.className = className;
        this.name = name;
        this.comment = comment;
        this.src = src;
        this.startLine = startLine;
        this.endLine = endLine;
        this.params = params;
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getFullyQualifiedMethodName() {

        String paramsStr = "";

        for (int i=0; i<params.size(); i++) {
            Parameter p = params.get(i);
            if (i == 0)
                paramsStr = p.getType() + " " + p.getId();
            else
                paramsStr += ", " + p.getType() + " " + p.getId();
        }

        return this.getMethodPackage() + "."
                + this.getClassName() + "#"
                + this.getName() + "("
                + paramsStr + ")";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public String getMethodPackage() {
        return methodPackage;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setMethodPackage(String methodPackage) {
        this.methodPackage = methodPackage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return methodPackage + "." + className + "." + name + "," + params;
    }

    @Override
    public int hashCode(){

        // adapted from https://www.mkyong.com/java/java-how-to-overrides-equals-and-hashcode/

        int result = 17;
        result = 31 * result + methodPackage.hashCode();
        result = 31 * result + className.hashCode();
        result = 31 * result + name.hashCode();

        for (Parameter p: params) {
            result = 31 * result + p.hashCode();
        }

        return result;
    }

    public boolean equals(Object o) {
        Method m = (Method) o;

        int countParamsMatches = 0;

        // if the no. of params is different, they're not equal
        if (params.size() != m.getParams().size())
            return false;
        else {
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i).equals(m.getParams().get(i))) {
                    countParamsMatches++;
                }
            }

            return methodPackage.equals(m.getMethodPackage())
                    && className.equals(m.getClassName())
                    && name.equals(m.getName())
                    && countParamsMatches == params.size();
        }
    }
}