package org.alfresco.devcon.util.attribute_service;

import org.alfresco.service.namespace.QName;

public interface AttributeApplication {
    //This is useful for any applications that leverage the attribute
	// Service
	public QName getApplicationName();

}
