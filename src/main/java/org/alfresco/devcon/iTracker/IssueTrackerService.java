package org.alfresco.devcon.iTracker;

import java.io.InputStream;
import java.io.Reader;

import org.alfresco.service.cmr.repository.NodeRef;

public interface IssueTrackerService {
	enum CaseStatus { Open, Suspended, Closed };
	NodeRef createProject(String projectId,String projectName);
	NodeRef createCase(String projectId,String subject);
	NodeRef addCommentToCase(NodeRef caseRef,String subject,String comment);
	NodeRef addCommentToCase(NodeRef caseRef,String subject,InputStream is);
	NodeRef addCommentToCase(NodeRef caseRef,String subject,Reader rdr);
	NodeRef addAttachmentToCase(NodeRef caseRef, String subject,InputStream is);
	NodeRef addAttachmentToCase(NodeRef caseRef, String subject,Reader rdr);
	NodeRef linkReferenceToCase(NodeRef caseRef, String subject,String url);
	CaseStatus getCaseStatus(NodeRef caseRef);
	void suspendCase(NodeRef caseRef);
	void activateCase(NodeRef caseRef);
	void closeCase(NodeRef caseRef);
	void reOpenCase(NodeRef caseRef);
	NodeRef getCaseFromId(String caseId);
}
