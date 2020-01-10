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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class XMLFormatter {

    private Document dom;
    private Element root;

    public XMLFormatter() {
        initialise();
    }

    private void initialise() {
        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            this.dom = db.newDocument();
            // create the root element
            this.root = this.dom.createElement("CLONECLASSES");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createDocument() {
        initialise();
    }

    public String getXMLAsString() {
        this.dom.appendChild(this.root);
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(dom), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(String filename) {
        this.dom.appendChild(this.root);
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            // send DOM to file
            tr.transform(new DOMSource(dom),
                    new StreamResult(new FileOutputStream(filename)));
        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void addCloneClass(int id, int sim, ArrayList<crest.siamese.document.Document> results) {
        // create data elements and place them under root
        Element cc = this.dom.createElement("CLONECLASS");
        Element e = this.dom.createElement("ID");
        e.setTextContent(String.valueOf(id));
        Element s = this.dom.createElement("SIMILARITY");
        s.setTextContent(String.valueOf(sim));
        cc.appendChild(e);
        cc.appendChild(s);
        for (crest.siamese.document.Document r: results) {
            cc.appendChild(createClone(r));
        }
        this.root.appendChild(cc);
    }

    public Element createClone(crest.siamese.document.Document d) {
        Element clone = this.dom.createElement("CLONE");
        Element frag = this.dom.createElement("Fragment");
        Element fElem = this.dom.createElement("File");
        fElem.setTextContent(d.getFile().split(".java_")[0] + ".java");
        frag.appendChild(fElem);
        Element startElem = this.dom.createElement("Start");
        startElem.setTextContent(String.valueOf(d.getStartLine()));
        Element endElem = this.dom.createElement("End");
        endElem.setTextContent(String.valueOf(d.getEndLine()));
        frag.appendChild(startElem);
        frag.appendChild(endElem);
        Element licenseElem = this.dom.createElement("License");
        licenseElem.setTextContent(d.getLicense());
        frag.appendChild(licenseElem);
        clone.appendChild(frag);
        return clone;
    }
}
