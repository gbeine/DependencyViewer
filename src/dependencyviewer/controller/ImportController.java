/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.controller;

import dependencyviewer.converter.DomConverter;
import dependencyviewer.converter.ModelConverter;
import dependencyviewer.converter.ContentProvider;
import dependencyviewer.converter.SaxConverter;
import java.io.File;

/**
 *
 * @author gbeine
 */
public class ImportController {

    // The file to read contents from
    private File file;

    /**
     * 
     * @param file
     */
    public ImportController(final File file) {
        this.file = file;
    }

    /**
     * 
     * @return
     */
    public ContentProvider getContentProvider() {
        final SaxConverter sax = new SaxConverter(this.file);
//        final DomConverter dc = new DomConverter(this.file);
//        final ModelConverter mc = new ModelConverter(dc.getDomDocument());
        return new ContentProvider(sax.getPackages());
    }
}
