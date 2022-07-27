package oscarblancarte.ipd.templetemethod.impl;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscarblancarte.ipd.templetemethod.util.OnMemoryDataBase;

public class PaymentsMadeFileProcess extends AbstractFileProcessTemplete {

    private Document doc;
    private String log = "";

    public PaymentsMadeFileProcess(File file, String logPath, String movePath) {
        super(file, logPath, movePath);
    }

    @Override
    protected void validateName() throws Exception {
        String fileName = file.getName();
        if (!fileName.endsWith(".xml")) {
            throw new Exception("Invalid file name, must end with .xml ");
        }

        if (fileName.length() != 12) {
            throw new Exception("Invalid document format " + fileName.length());
        }
    }

    @Override
    protected void processFile() throws Exception {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList pMade = doc.getElementsByTagName("payment");

            for (int temp = 0; temp < pMade.getLength(); temp++) {
                Node node = pMade.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element paymentElement = (Element) node;
                    String id = paymentElement.getElementsByTagName("id").item(0).getTextContent();
                    String customer = paymentElement.getElementsByTagName("customer").item(0).getTextContent();
                    double amount = Double.parseDouble(paymentElement.getElementsByTagName("amount").item(0).getTextContent());
                    String date = paymentElement.getElementsByTagName("date").item(0).getTextContent();
                    String wayPay = paymentElement.getElementsByTagName("waypay").item(0).getTextContent();
                    String card = paymentElement.getElementsByTagName("card").item(0).getTextContent();
                    
                    boolean exist = OnMemoryDataBase.customerExist(Integer.parseInt(customer));
                    if (!exist) {
                    	log = id + " E" + customer + "\t" + date + " Customer not exist";

                    } else if (amount > 200) {

                    	log = id + " E" + customer + "\t" + date + " The amount exceeds the maximum";
                    } else {

                    	log = id + " E" + customer + "\t" + date + " \t"+wayPay +"\t"+ card + " Successfully payment";
                    }
                   /* Element resultAction = doc.createElement("log");
                    resultAction.appendChild(doc.createTextNode(log));
                    node.appendChild(resultAction);		*/
                }
            }
        } finally
        {
            try {

            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void createLog() throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            File outFile = new File(logPath + "/" + file.getName());
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(outFile);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
