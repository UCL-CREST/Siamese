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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Created by Chaiyong on 4/29/17.
 */
public class Parameter {
    private String type;
    private String id;

    public Parameter(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Parameter) {
            Parameter p = (Parameter) o;
            return new EqualsBuilder()
                    .append(type, p.getType())
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return type + " " + id;
    }
}
