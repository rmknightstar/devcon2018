package org.alfresco.devcon.iTracker.action;

import java.util.List;

import org.alfresco.devcon.iTracker.impl.IssueTrackerComponent;
import org.alfresco.devcon.iTracker.policy.CreateCasePolicy;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddAttachmentAction extends ActionExecuterAbstractBase {
	IssueTrackerComponent issueTrackerComponent;
	private static final Log logger = LogFactory.getLog(CreateCasePolicy.class);

	
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		issueTrackerComponent.makeCaseAttachment(actionedUponNodeRef);
	}
	
	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
	// TODO Add an optional parameter to set the subject
	}

}
