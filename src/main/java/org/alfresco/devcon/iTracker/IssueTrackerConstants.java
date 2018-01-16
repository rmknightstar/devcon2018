package org.alfresco.devcon.iTracker;

import org.alfresco.service.namespace.QName;

public interface IssueTrackerConstants {
	public static final String ITRACK_MODEL_PREFIX = "itrack";
	public static final String ITRACK_MODEL_1_0_URI = "http://www.alfresco.com/model/issueTracker/1.0";
	
	public static final QName TYPE_ISSUE_CONTAINER = QName.createQName(ITRACK_MODEL_1_0_URI, "issueContainer");
	public static final QName TYPE_CASE = QName.createQName(ITRACK_MODEL_1_0_URI, "case");
	public static final QName TYPE_PROJECT = QName.createQName(ITRACK_MODEL_1_0_URI, "project");
	public static final QName TYPE_CASE_COMMENT = QName.createQName(ITRACK_MODEL_1_0_URI, "caseComment");
	public static final QName TYPE_CASE_ATTACHMENT = QName.createQName(ITRACK_MODEL_1_0_URI, "caseAttachment");
	public static final QName TYPE_CASE_REFERENCE = QName.createQName(ITRACK_MODEL_1_0_URI, "caseReference");

	public static final QName ASPECT_CASE_OBJECT = QName.createQName(ITRACK_MODEL_1_0_URI, "caseObject");
	public static final QName ASPECT_PROJECT_INFO = QName.createQName(ITRACK_MODEL_1_0_URI, "projectInfo");
	public static final QName ASPECT_CASE_INFO = QName.createQName(ITRACK_MODEL_1_0_URI, "caseInfo");
	

	public static final QName PROP_LINK_URL = QName.createQName(ITRACK_MODEL_1_0_URI, "linkUrl");
	public static final QName PROP_FILENAME = QName.createQName(ITRACK_MODEL_1_0_URI, "filename");
	public static final QName PROP_CASE_STATUS = QName.createQName(ITRACK_MODEL_1_0_URI, "caseStatus");
	public static final QName PROP_PROJECT_ID = QName.createQName(ITRACK_MODEL_1_0_URI, "projectId");
	public static final QName PROP_PROJECT_NAME = QName.createQName(ITRACK_MODEL_1_0_URI, "projectName");
	public static final QName PROP_CASE_ID = QName.createQName(ITRACK_MODEL_1_0_URI, "caseId");
	public static final QName PROP_SUBJECT = QName.createQName(ITRACK_MODEL_1_0_URI, "subject");

	public static final QName PROP_DUE_DATE = QName.createQName(ITRACK_MODEL_1_0_URI, "dueDate");
	public static final QName PROP_AUDIT = QName.createQName(ITRACK_MODEL_1_0_URI, "audit");
	public static final QName PROP_INVOICED_PERSONS = QName.createQName(ITRACK_MODEL_1_0_URI, "involvedPersons");
	public static final QName PROP_TASK_RFI_DATE = QName.createQName(ITRACK_MODEL_1_0_URI, "rfiDate");
	public static final QName PROP_LEGAL_NAME = QName.createQName(ITRACK_MODEL_1_0_URI, "legalName");
	public static final QName PROP_CONTRACT_DATE = QName.createQName(ITRACK_MODEL_1_0_URI, "contractDate");
	public static final QName PROP_APPROVED = QName.createQName(ITRACK_MODEL_1_0_URI, "approved");
	public static final QName PROP_REJECTED = QName.createQName(ITRACK_MODEL_1_0_URI, "rejected");
	public static final QName PROP_ACTION_TYPE = QName.createQName(ITRACK_MODEL_1_0_URI, "actionType");

}
