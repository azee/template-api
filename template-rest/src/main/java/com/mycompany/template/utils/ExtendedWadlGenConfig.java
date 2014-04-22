package com.mycompany.template.utils;

import com.sun.jersey.api.wadl.config.WadlGeneratorConfig;
import com.sun.jersey.api.wadl.config.WadlGeneratorDescription;
import com.sun.jersey.server.wadl.generators.WadlGeneratorApplicationDoc;
import com.sun.jersey.server.wadl.generators.WadlGeneratorGrammarsSupport;
import com.sun.jersey.server.wadl.generators.resourcedoc.WadlGeneratorResourceDocSupport;

import java.util.List;

/**
 * Created by azee on 4/22/14.
 */
public class ExtendedWadlGenConfig extends WadlGeneratorConfig {

    @Override
    public List<WadlGeneratorDescription> configure() {
        return generator( WadlGeneratorApplicationDoc.class )
                .prop( "applicationDocsStream", "wadl/application-doc.xml")
                .generator( WadlGeneratorGrammarsSupport.class )
                .prop( "grammarsStream", "wadl/application-grammars.xml")
                .generator( WadlGeneratorResourceDocSupport.class )
                .prop( "resourceDocStream", "resourcedoc.xml" )
                .descriptions();
    }
}
