package dependencyviewer.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import dependencyviewer.model.PackageElement;

/**
 *
 * @author gbeine
 */
public class SaxConverter {

    private static final Logger logger = LoggerFactory.getLogger(SaxConverter.class);
    private static final SAXParserFactory spf = SAXParserFactory.newInstance();

    private File file;
    private XMLReader reader;
    private InputSource is;
    private SaxHandler saxHandler = new SaxHandler();

    /**
     *
     */
    public SaxConverter(File file) {
        if (SaxConverter.logger.isDebugEnabled()) {
            SaxConverter.logger.debug("New SaxConverter for file " + file.getName());
        }
        this.file = file;
        this.initComponents();
        SaxConverter.spf.setValidating(true);
    }

    public Set<PackageElement> getPackages() {
        this.parse();
        return this.saxHandler.getPackages();
    }

    private void initComponents() {
        try {
            this.is = new InputSource(new FileInputStream(this.file));
            this.reader = SaxConverter.spf.newSAXParser().getXMLReader();
            this.reader.setContentHandler(this.saxHandler);
        } catch (FileNotFoundException fnfe) {
            SaxConverter.logger.error(fnfe.getMessage());
        } catch (ParserConfigurationException pce) {
            SaxConverter.logger.error(pce.getMessage());
        } catch (SAXException se) {
            SaxConverter.logger.error(se.getMessage());
        }
    }

    private void parse() {
        try {
            this.reader.parse(is);
        } catch (IOException ioe) {
            SaxConverter.logger.error(ioe.getMessage());
        } catch (SAXException se) {
            SaxConverter.logger.error(se.getMessage());
        }
    }
}
