package dependencyviewer.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Read and valiate an xml file exported from DependencyFinder
 *
 * @author gbeine
 */
public class DomConverter {

    private static final Logger logger = LoggerFactory.getLogger(DomConverter.class);
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    private File file;
    private InputStream inputStream;
    private DocumentBuilder db;
    private Document document;

    // Set to true after parsing the document
    private boolean parsed = false;

    /**
     *
     */
    public DomConverter(File file) {
        if (DomConverter.logger.isDebugEnabled()) {
            DomConverter.logger.debug("New DomConverter for file " + file.getName());
        }
        this.file = file;
        this.initComponents();
    }

    public DomConverter(InputStream inputStream) {
        if (DomConverter.logger.isDebugEnabled()) {
            DomConverter.logger.debug("New DomConverter for file " + inputStream.toString());
        }
        this.inputStream = inputStream;
        this.initComponents();
    }

    /**
     *
     */
    public Document getDomDocument() {
        if (!this.parsed) {
            if (DomConverter.logger.isDebugEnabled()) {
                DomConverter.logger.debug("File not parsed yet");
            }
            this.parse();
        }
        return this.document;
    }

    /**
     *
     */
    private void parse() {
        if (DomConverter.logger.isDebugEnabled()) {
            DomConverter.logger.debug("Start parsing file");
        }
        try {
            this.document = this.db.parse(this.inputStream);
//            this.document = this.db.parse(this.file);
        } catch (SAXException ex) {
            DomConverter.logger.error(ex.getMessage());
        } catch (IOException ex) {
            DomConverter.logger.error(ex.getMessage());
        }
        if (DomConverter.logger.isDebugEnabled()) {
            DomConverter.logger.debug("Parsing successful");
        }
        this.parsed = true;
    }

    /**
     * 
     */
    private void initComponents() {
        if (DomConverter.logger.isDebugEnabled()) {
            DomConverter.logger.debug("Initialize DomConverter");
        }
        DomConverter.dbf.setValidating(true);
        try {
            this.db = DomConverter.dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            DomConverter.logger.error(ex.getMessage());
        }
        if (DomConverter.logger.isDebugEnabled()) {
            DomConverter.logger.debug("Initialization successful");
        }
    }
}
