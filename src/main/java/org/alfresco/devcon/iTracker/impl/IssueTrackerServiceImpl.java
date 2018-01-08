package org.alfresco.devcon.iTracker.impl;

import java.io.InputStream;
import java.io.Reader;

import org.alfresco.devcon.iTracker.IssueTrackerService;
import org.alfresco.service.cmr.repository.NodeRef;

public class IssueTrackerServiceImpl implements IssueTrackerService {

	IssueTrackerComponent issueTrackerComponent;

	public void setIssueTrackerComponent(IssueTrackerComponent issueTrackerComponent) {
		this.issueTrackerComponent = issueTrackerComponent;
	}

	@Override
	public NodeRef createProject(String projectId, String projectName) {
		return issueTrackerComponent.createProject(projectId, projectName);
	}

	@Override
	public NodeRef createCase(String projectId, String subject) {
		return issueTrackerComponent.createCase(projectId, subject);
	}

	@Override
	public NodeRef addCommentToCase(NodeRef caseRef, String subject,
			String comment) {
		return issueTrackerComponent.addCommentToCase(caseRef, subject, comment);
	}

	@Override
	public NodeRef addCommentToCase(NodeRef caseRef, String subject,
			InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeRef addCommentToCase(NodeRef caseRef, String subject, Reader rdr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeRef addAttachmentToCase(NodeRef caseRef, String subject,
			InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeRef addAttachmentToCase(NodeRef caseRef, String subject,
			Reader rdr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeRef linkReferenceToCase(NodeRef caseRef, String subject,
			String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaseStatus getCaseStatus(NodeRef caseRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void suspendCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reOpenCase(NodeRef caseRef) {
		// TODO Auto-generated method stub

	}

}
