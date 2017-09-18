package org.nuxeo.workshopguide.adapters;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class NWGProductAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if ("NWGProduct".equals(doc.getType()) && doc.hasSchema("dublincore")){
            return new NWGProductAdapter(doc);
        }else{
            return null;
        }
    }
}
