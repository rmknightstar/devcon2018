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

	public NodeRef addCommentToCase(NodeRef caseRef, String subject, InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef addCommentToCase(NodeRef caseRef, String subject, Reader rdr) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef addAttachmentToCase(NodeRef caseRef, String subject, InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef addAttachmentToCase(NodeRef caseRef, String subject, Reader rdr) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef linkReferenceToCase(NodeRef caseRef, String subject, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCaseStatus(NodeRef caseRef) {
		// TODO Auto-generated method stub
		return null;
	}

	public void suspendCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

	public void activateCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

	public void closeCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

	public void reOpenCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

}
