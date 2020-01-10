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

import java.util.List;

/**
 * Created by chaiyong on 14/03/2017.
 */
public class MethodClone extends Method {
    private String cluster;

    public MethodClone(String file, String methodPackage, String name, String className, String src, int startLine, int endLine, List<Parameter> params, String fullHeader, String cluster) {
        super(file, methodPackage, className, name, "", src, startLine, endLine, params, fullHeader);
        this.cluster = cluster;
    }

    public MethodClone(String file, String cluster, String header) {
        this.setFile(file);
        this.cluster = cluster;
        this.setHeader(header);
    }

    public String getCluster() {
        return cluster;
    }
}
