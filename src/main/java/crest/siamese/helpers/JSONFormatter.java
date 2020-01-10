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

package crest.siamese.helpers;

import crest.siamese.document.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class JSONFormatter {
    JSONObject jobj;
    JSONArray jclones;

    public JSONFormatter() {
        jobj = new JSONObject();
        jclones = new JSONArray();
    }

    public void addCloneClass(int id, int sim, ArrayList<Document> results) {
        JSONArray jarray = new JSONArray();
        int count = 0;
        String type = "query";
        for (crest.siamese.document.Document r: results) {
            if (count > 0)
                type = "clone";
            jarray.add(createClone(r, type));
            count++;
        }
        jclones.add(jarray);
    }

    private JSONObject createClone(crest.siamese.document.Document d, String type) {
        String file = d.getFile().split(".java_")[0] + ".java";
        JSONObject item = new JSONObject();
        item.put("type", type);
        item.put("file", file);
        item.put("start", String.valueOf(d.getStartLine()));
        item.put("end", String.valueOf(d.getEndLine()));
        item.put("license", d.getLicense());
        return item;
    }

    public String getJSONString() {
        jobj.put("clones", jclones);
        return jobj.toString();
    }
}
