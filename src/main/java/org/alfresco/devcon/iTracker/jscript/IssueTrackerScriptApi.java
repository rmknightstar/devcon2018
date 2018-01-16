package org.alfresco.devcon.iTracker.jscript;

import java.io.InputStream;
import java.io.Reader;

import org.alfresco.devcon.iTracker.IssueTrackerService;
import org.alfresco.devcon.iTracker.IssueTrackerService.CaseStatus;
import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.repo.jscript.ScriptNode;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;

public class IssueTrackerScriptApi extends BaseScopableProcessorExtension {
	ServiceRegistry serviceRegistry;
	IssueTrackerService issueTrackerService;

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setIssueTrackerService(IssueTrackerService issueTrackerService) {
		this.issueTrackerService = issueTrackerService;
	}

	private ScriptNode toScriptNode(NodeRef nodeRef) {
		return new ScriptNode(nodeRef, this.serviceRegistry, getScope());
	}

	public ScriptNode createProject(String projectId, String projectName) {
		return toScriptNode(issueTrackerService.createProject(projectId, projectName));
	}

	public ScriptNode createCase(String projectId, String subject) {
		return toScriptNode(issueTrackerService.createCase(projectId, subject));
	}

	public ScriptNode addCommentToCase(ScriptNode caseRef, String subject, String comment) {
		return toScriptNode(issueTrackerService.addCommentToCase(caseRef.getNodeRef(), subject, comment));
	}

	public ScriptNode linkReferenceToCase(ScriptNode caseRef, String subject, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public ScriptNode addCommentToCase(String caseId, String subject, String comment) {
		return toScriptNode(issueTrackerService.addCommentToCase(issueTrackerService.getCaseFromId(caseId), subject, comment));
	}

	public ScriptNode linkReferenceToCase(String caseId, String subject, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCaseStatus(ScriptNode caseRef) {
		// TODO Auto-generated method stub
		return null;
	}

	public void suspendCase(ScriptNode caseRef) {
		// TODO Auto-generated method stub

	}

	public void activateCase(ScriptNode caseRef) {
		// TODO Auto-generated method stub

	}

	public void closeCase(ScriptNode caseRef) {
		// TODO Auto-generated method stub

	}

	public void reOpenCase(ScriptNode caseRef) {
		// TODO Auto-generated method stub

	}

}
