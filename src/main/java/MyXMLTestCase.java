/*
******************************************************************
Copyright (c) 2001, Jeff Martin, Tim Bacon
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of the xmlunit.sourceforge.net nor the names
      of its contributors may be used to endorse or promote products
      derived from this software without specific prior written
      permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.

******************************************************************
*/

import autparsing.ElementNameAndTextAndAttrQualifier;
import javafx.geometry.Insets;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.custommonkey.xmlunit.*;
import org.custommonkey.xmlunit.examples.CountingNodeTester;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xmlunit.builder.DiffBuilder;

import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;


/**
 * Example XMLUnit XMLTestCase code
 * Demonstrates use of:<br />
 * <ul>
 * <li>XMLTestCase: assertXMLEqual(), assertXMLNotEqual(),
 * assertXpathExists(), assertXpathNotExists(), assertXpathEvaluatesTo(),
 * assertXpathsEqual(), assertXpathsNotEqual(), assertNodeTestPasses()</li>
 * <li>Diff: similar(), identical()</li>
 * <li>DetailedDiff: getAllDifferences()</li>
 * <li>DifferenceListener: use with Diff class,
 * IgnoreTextAndAttributeValuesDifferenceListener implementation</li>
 * <li>ElementQualifier: use with Diff class,
 * ElementNameAndTextQualifier implementation</li>
 * <li>Transform: constructors, getResultDocument(), use with Diff class</li>
 * <li>Validator: constructor, isValid()</li>
 * <li>TolerantSaxDocumentBuilder and HTMLDocumentBuilder usage</li>
 * <li>NodeTest: CountingNodeTester and custom implementations</li>
 * <li>XMLUnit static methods: buildDocument(), buildControlDocument(),
 * buildTestDocument(), setIgnoreWhitespace()</li>
 * </ul>
 * <br />Examples and more at <a href="http://xmlunit.sourceforge.net"/>xmlunit.sourceforge.net</a>
 */
public class MyXMLTestCase extends XMLTestCase {
    public MyXMLTestCase(String name) {
        super(name);
    }

    static boolean c = true;

    public void testForEquality() throws Exception {
        String myControlXML = "<msg><uuid>0x00435A8C</uuid></msg>";
        String myTestXML = "<msg><localId>2376</localId></msg>";
        assertXMLEqual("comparing test xml to control xml", myControlXML, myTestXML);

        assertXMLNotEqual("test xml not similar to control xml", myControlXML, myTestXML);
    }

    public void testIdentical() throws Exception {
        String myControlXML = "<struct><int>3</int><boolean>false</boolean></struct>";
        String myTestXML = "<struct><boolean>false</boolean><int>3</int></struct>";
        Diff myDiff = new Diff(myControlXML, myTestXML);
        assertTrue("pieces of XML are similar " + myDiff, myDiff.similar());
        assertTrue("but are they identical? " + myDiff, myDiff.identical());
    }

    @Test
    public void test_similar() throws Exception {

        String myControlXML = "<struct><int>3</int><boolean>false</boolean></struct>";
        String myTestXML = "<struct><boolean>false</boolean><int>3</int></struct>";
        XMLUnit.setIgnoreWhitespace(true);
        Diff myDiff = new Diff(new FileReader("RecurringProduct.xml"), new FileReader("RecurringProductTest.xml"));
        if (myDiff.similar()) {
            System.out.println("Esit");
        } else
            System.out.println(myDiff);

    }

    @Test
    public void testRepeatedChildElements() throws Exception {
        //String myControlXML = "<suite><test status=\"fail\">FirstTestCase</test><test status=\"pass\">SecondTestCase</test></suite>";
        //String myTestXML = "<suite><test status=\"pass\">SecondTestCase</test><test status=\"fail\">FirstTestCase</test></suite>";
        XMLUnit.setIgnoreWhitespace(true);
        //  assertXMLNotEqual("Repeated child elements in different sequence order are not equal by default", myControlXML, myTestXML);

        //Diff myDiff = new Diff(myControlXML, myTestXML);
        Diff myDiff = new Diff(new FileReader("RecurringProduct.xml"), new FileReader("RecurringProductTest.xml"));
        myDiff.overrideElementQualifier(new ElementNameAndTextAndAttrQualifier());
        assertXMLEqual("farkli",
                myDiff, true);
    }

    public void testRepeatedChildElements(String c, String t) throws Exception {
        XMLUnit.setIgnoreWhitespace(true);

        Diff myDiff = new Diff(new FileReader(c), new FileReader(t));
        myDiff.overrideElementQualifier(new ElementNameAndTextAndAttrQualifier());
        //assertXMLEqual("farkli",               myDiff, true);
        if (myDiff.similar()) System.out.println("XML'ler Esit");
        else {
            System.out.println(myDiff);
            StringSelection selection = new StringSelection("\n" + c + ", " + t + " \n" + myDiff.toString());
            java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

        }
    }

    public void testRepeatedChildElements(String a[]) throws Exception {
        if (a[2].contentEquals("1"))
            XMLUnit.setIgnoreWhitespace(true);
        else
            XMLUnit.setIgnoreWhitespace(false);
        if (a[3].contentEquals("1"))
            XMLUnit.setIgnoreComments(true);
        else
            XMLUnit.setIgnoreComments(false);
        if (a[4].contentEquals("1"))
            XMLUnit.setIgnoreAttributeOrder(true);
        else
            XMLUnit.setIgnoreAttributeOrder(false);

        Diff myDiff = new Diff(new FileReader(a[0]), new FileReader(a[1]));
        if (a[5].contentEquals("1")) {
            myDiff.overrideElementQualifier(new ElementNameAndTextAndAttrQualifier());
            //assertXMLEqual("farkli",               myDiff, true);
            if (myDiff.similar()) System.out.println("XML'ler Esit");
            else {
                System.out.println(myDiff);
                StringSelection selection = new StringSelection("\n" + a[0] + ", " + a[1] + " \n" + myDiff.toString());
                java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        }
        else{
            if (myDiff.identical()) System.out.println("XML'ler Esit. (identical)");
            else {
                System.out.println(myDiff);
                StringSelection selection = new StringSelection("\n" + a[0] + ", " + a[1] + " \n" + myDiff.toString());
                java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        }
    }

    public String testRepeatedChildElements(File C, File T, VBoxExample a) throws Exception {
        if (a.ignoreWhiteSpace.isSelected())
            XMLUnit.setIgnoreWhitespace(true);
        else
            XMLUnit.setIgnoreWhitespace(false);
        if (a.ignoreComment.isSelected())
            XMLUnit.setIgnoreComments(true);
        else
            XMLUnit.setIgnoreComments(false);
        if (a.ignoreAtributeOrder.isSelected())
            XMLUnit.setIgnoreAttributeOrder(true);
        else
            XMLUnit.setIgnoreAttributeOrder(false);

        Diff myDiff = new Diff(new FileReader(C), new FileReader(T));


        if (a.ignoreElementOrder.isSelected()) {
            myDiff.overrideElementQualifier(new ElementNameAndTextAndAttrQualifier());
            if (myDiff.similar()) a.ll.setText(C.getPath() + ",      " + T.getPath() + "\nXML'ler Eşit");
            else {
                a.ll.setText(C.getPath() + ",      " + T.getPath() + "\n" + myDiff.toString());
                a.ll.setManaged(true);
                if (c)
                    a.ll.setBackground((new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY))));
                else
                    a.ll.setBackground((new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY))));
                c = !(c);
                return myDiff.toString();
            }
        } else {
            if (myDiff.identical()) a.ll.setText(C.getPath() + ",      " + T.getPath() + "\nXML'ler Eşit (Identical)");
            else {
                a.ll.setText(C.getPath() + ",      " + T.getPath() + "\n" + myDiff.toString());
                a.ll.setManaged(true);
                if (c)
                    a.ll.setBackground((new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY))));
                else
                    a.ll.setBackground((new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY))));
                c = !(c);
                return myDiff.toString();
            }
        }
        if (c)
            a.ll.setBackground((new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY))));
        else
            a.ll.setBackground((new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY))));
        c = !(c);
        return "XML'ler Eşit";

        //assertXMLEqual("farkli", myDiff, true);
    }

    @Test
    public void test_given2XMLS_whenGeneratesDifferences_thenCorrect() {
        String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
        String testXml = "<struct><boolean>false</boolean><int>3</int></struct>";
        org.xmlunit.diff.Diff myDiff = DiffBuilder.compare(controlXml).withTest(testXml).build();

        Iterator<org.xmlunit.diff.Difference> iter = myDiff.getDifferences().iterator();
        int size = 0;
        while (iter.hasNext()) {
            System.out.println(iter.next().toString());
            size++;
        }
        if (size == 0) {
            System.out.println("Esit");
        } else
            System.out.println("Fark " + size + " tane");
    }

    public void testAllDifferences() throws Exception {
        String myControlXML = "<news><item id=\"1\">War</item>"
                + "<item id=\"2\">Plague</item><item id=\"3\">Famine</item></news>";
        String myTestXML = "<news><item id=\"1\">Peace</item>"
                + "<item id=\"2\">Health</item><item id=\"3\">Plenty</item></news>";
        DetailedDiff myDiff = new DetailedDiff(compareXML(myControlXML, myTestXML));
        List allDifferences = myDiff.getAllDifferences();
        assertEquals(myDiff.toString(), 0, allDifferences.size());
    }

    public void testCompareToSkeletonXML() throws Exception {
        String myControlXML = "<location><street-address>22 any street</street-address><postcode>XY00 99Z</postcode></location>";
        String myTestXML = "<location><street-address>20 east cheap</street-address><postcode>EC3M 1EB</postcode></location>";
        DifferenceListener myDifferenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        Diff myDiff = new Diff(myControlXML, myTestXML);
        myDiff.overrideDifferenceListener(myDifferenceListener);
        assertTrue("test XML matches control skeleton XML " + myDiff, myDiff.similar());
    }

    public void testXSLTransformation() throws Exception {
        String myInputXML = "...";
        File myStylesheetFile = new File("...");
        Transform myTransform = new Transform(myInputXML, myStylesheetFile);
        String myExpectedOutputXML = "...";
        Diff myDiff = new Diff(myExpectedOutputXML, myTransform);
        assertTrue("XSL transformation worked as expected " + myDiff, myDiff.similar());
    }

    public void testAnotherXSLTransformation() throws Exception {
        File myInputXMLFile = new File("...");
        File myStylesheetFile = new File("...");
        Transform myTransform = new Transform(new StreamSource(myInputXMLFile), new StreamSource(myStylesheetFile));
        // Document myExpectedOutputXML = XMLUnit.buildDocument(XMLUnit.getControlParser(), new FileReader("..."));
        //Diff myDiff = new Diff(myExpectedOutputXML, myTransform.getResultDocument());
        //  assertTrue("XSL transformation worked as expected " + myDiff, myDiff.similar());
    }

    public void testValidation() throws Exception {
        XMLUnit.getTestDocumentBuilderFactory().setValidating(true);
        // As the document is parsed it is validated against its referenced DTD
        Document myTestDocument = XMLUnit.buildTestDocument("...");
        String mySystemId = "...";
        String myDTDUrl = new File("...").toURL().toExternalForm();
        Validator myValidator = new Validator(myTestDocument, mySystemId, myDTDUrl);
        assertTrue("test document validates against unreferenced DTD", myValidator.isValid());
    }

    public void testXPaths() throws Exception {
        String mySolarSystemXML = "<solar-system><planet name='Earth' position='3' supportsLife='yes'/>"
                + "<planet name='Venus' position='4'/></solar-system>";
        assertXpathExists("//planet[@name='Earth']", mySolarSystemXML);
        assertXpathNotExists("//star[@name='alpha centauri']", mySolarSystemXML);
        assertXpathsEqual("//planet[@name='Earth']", "//planet[@position='3']", mySolarSystemXML);
        assertXpathsNotEqual("//planet[@name='Venus']", "//planet[@supportsLife='yes']", mySolarSystemXML);
    }

    public void testXPathValues() throws Exception {
        String myJavaFlavours = "<java-flavours><jvm current='some platforms'>1.1.x</jvm>"
                + "<jvm current='no'>1.2.x</jvm><jvm current='yes'>1.3.x</jvm>"
                + "<jvm current='yes' latest='yes'>1.4.x</jvm></java-flavours>";
        assertXpathEvaluatesTo("1.4.x", "//jvm[@latest='yes']", myJavaFlavours);
        assertXpathEvaluatesTo("2", "count(//jvm[@current='yes'])", myJavaFlavours);
        assertXpathValuesEqual("//jvm[4]/@latest", "//jvm[4]/@current", myJavaFlavours);
        assertXpathValuesNotEqual("//jvm[2]/@current", "//jvm[3]/@current", myJavaFlavours);
    }

    public void testXpathsInHTML() throws Exception {
        String someBadlyFormedHTML = "<html><title>Ugh</title><body><h1>Heading<ul><li id='1'>Item One<li id='2'>Item Two";
        // TolerantSaxDocumentBuilder tolerantSaxDocumentBuilder = new TolerantSaxDocumentBuilder(XMLUnit.getTestParser());
        // HTMLDocumentBuilder htmlDocumentBuilder = new HTMLDocumentBuilder(tolerantSaxDocumentBuilder);
        // Document wellFormedDocument = htmlDocumentBuilder.parse(someBadlyFormedHTML);
        //    assertXpathEvaluatesTo("Item One", "/html/body//li[@id='1']", wellFormedDocument);
    }

    public void testCountingNodeTester() throws Exception {
        String testXML = "<fibonacci><val>1</val><val>2</val><val>3</val>"
                + "<val>5</val><val>9</val></fibonacci>";
        CountingNodeTester countingNodeTester = new CountingNodeTester(4);
        assertNodeTestPasses(testXML, countingNodeTester, Node.TEXT_NODE);
    }

    public void testCustomNodeTester() throws Exception {
        String testXML = "<fibonacci><val>1</val><val>2</val><val>3</val>"
                + "<val>5</val><val>9</val></fibonacci>";
        NodeTest nodeTest = new NodeTest(testXML);
        assertNodeTestPasses(nodeTest, new FibonacciNodeTester(),
                new short[]{Node.TEXT_NODE, Node.ELEMENT_NODE}, true);
    }

    private class FibonacciNodeTester extends AbstractNodeTester {
        private int nextVal = 1, lastVal = 1, priorVal = 0;

        public void testText(Text text) throws NodeTestException {
            int val = Integer.parseInt(text.getData());
            if (nextVal != val) {
                throw new NodeTestException("Incorrect sequence value", text);
            }
            nextVal = val + lastVal;
            priorVal = lastVal;
            lastVal = val;
        }

        public void testElement(Element element) throws NodeTestException {
            String name = element.getLocalName();
            if ("fibonacci".equals(name) || "val".equals(name)) {
                return;
            }
            throw new NodeTestException("Unexpected element", element);
        }

        public void noMoreNodes(NodeTest nodeTest) throws NodeTestException {
        }
    }
}
