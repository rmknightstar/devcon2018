package org.alfresco.devcon.iTracker.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;

import org.alfresco.devcon.iTracker.IssueTrackerConstants;
import org.alfresco.devcon.iTracker.IssueTrackerService.CaseStatus;
import org.alfresco.devcon.util.folder_hierarchy.FolderHierarchyHelper;
import org.alfresco.devcon.util.unique_property.UniquePropertyManager;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.GUID;
import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.context.ApplicationEvent;
import org.springframework.extensions.surf.util.AbstractLifecycleBean;

public class IssueTrackerComponent extends AbstractLifecycleBean {
	
	static final String PROJECTS_FOLDER = "Projects";
	
	ContentService contentService;
	NodeService nodeService;
	ServiceRegistry serviceRegistry;
	NodeLocatorService nodeLocatorService;
	SearchService searchService;
	FolderHierarchyHelper folderHierarchyHelper;
	UniquePropertyManager uniquePropertyManager;
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
		this.nodeLocatorService = this.serviceRegistry.getNodeLocatorService();
		this.searchService = this.serviceRegistry.getSearchService();
		this.contentService = this.serviceRegistry.getContentService();
	
		
	}
	
	public void setFolderHierarchyHelper(FolderHierarchyHelper folderHierarchyHelper) {
		this.folderHierarchyHelper = folderHierarchyHelper;
	}

	NodeRef getProjectsHome() {
		return folderHierarchyHelper.getFolder(nodeLocatorService.getNode(CompanyHomeNodeLocator.NAME, null, null),PROJECTS_FOLDER,true,true);
	}
	
	NodeRef getProjectFolder(String projectId) {
		return folderHierarchyHelper.getFolder(getProjectsHome(),projectId,false);
	}
	
    public void setUniquePropertyManager(UniquePropertyManager uniquePropertyManager) {
        this.uniquePropertyManager = uniquePropertyManager;
    }


	public void registerCaseId(NodeRef caseRef) {
	        long id=uniquePropertyManager.getNextGlobalSequenceId(IssueTrackerConstants.PROP_CASE_ID);
	        String caseId = String.format("Case_%09d", id);
	        uniquePropertyManager.storeGlobalUniqueId(IssueTrackerConstants.PROP_CASE_ID, caseId, caseRef);
	        nodeService.setProperty(caseRef, IssueTrackerConstants.PROP_CASE_ID, caseId);
	        nodeService.setProperty(caseRef, ContentModel.PROP_NAME, caseId);
	        //TODO Check for blank ProjectId
	        NodeRef projectRef = nodeService.getPrimaryParent(caseRef).getParentRef();
	        nodeService.moveNode(caseRef, projectRef, ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, caseId));
	}
		
	public NodeRef createProject(String projectId, String projectName) {
		NodeRef projectRef = nodeService.createNode(getProjectsHome(), ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, projectId), IssueTrackerConstants.TYPE_PROJECT).getChildRef();
		nodeService.setProperty(projectRef, ContentModel.PROP_NAME, projectId);
		nodeService.setProperty(projectRef, IssueTrackerConstants.PROP_PROJECT_ID, projectId);
		nodeService.setProperty(projectRef, IssueTrackerConstants.PROP_PROJECT_NAME, projectName);
		return projectRef;
	}

	public NodeRef createCase(String projectId, String subject) {
		NodeRef caseRef = nodeService.createNode(getProjectsHome(), ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, GUID.generate()), IssueTrackerConstants.TYPE_CASE).getChildRef();
		nodeService.setProperty(caseRef, IssueTrackerConstants.PROP_SUBJECT, subject);
		return caseRef;
	}
	
	private String genName(String prefix) {
		return prefix + GUID.generate();
	}
	
	private NodeRef createIssueObject(NodeRef caseRef, String subject, QName type,String name) {
		return nodeService.createNode(caseRef, ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, name), type).getChildRef();
	}
	
	private NodeRef addObjectToCase(NodeRef caseRef, String subject, String content,QName type,String name) {
		NodeRef objectRef = createIssueObject(caseRef,subject,type,name);
		ContentWriter cw = contentService.getWriter(objectRef, ContentModel.PROP_CONTENT, true);
		cw.putContent(content);
		return objectRef;
	}

	private NodeRef addObjectToCase(NodeRef caseRef, String subject, Reader content,QName type,String name) {
		return addObjectToCase(caseRef,subject,new ReaderInputStream(content),type,name);
	}

	private NodeRef addObjectToCase(NodeRef caseRef, String subject, InputStream content,QName type,String name) {
		NodeRef objectRef = createIssueObject(caseRef,subject,type,name);
		ContentWriter cw = contentService.getWriter(objectRef, ContentModel.PROP_CONTENT, true);
		cw.putContent(content);
		return objectRef;
	}

	public NodeRef addCommentToCase(NodeRef caseRef, String subject, String comment) {
		return addObjectToCase(caseRef,subject,comment,IssueTrackerConstants.TYPE_CASE_COMMENT,genName("comment_"));
	}

	public NodeRef addCommentToCase(NodeRef caseRef, String subject, InputStream is) {
		return addObjectToCase(caseRef,subject,is,IssueTrackerConstants.TYPE_CASE_COMMENT,genName("comment_"));
	}

	public NodeRef addCommentToCase(NodeRef caseRef, String subject, Reader rdr) {
		return addObjectToCase(caseRef,subject,rdr,IssueTrackerConstants.TYPE_CASE_COMMENT,genName("comment_"));
	}


	public NodeRef addAttachmentToCase(NodeRef caseRef, String subject, String filename,InputStream is) {
		NodeRef objectRef = addObjectToCase(caseRef,subject,is,IssueTrackerConstants.TYPE_CASE_ATTACHMENT,genName("attachment_"));
		nodeService.setProperty(objectRef, IssueTrackerConstants.PROP_FILENAME, filename);
		return objectRef;
	}


	public NodeRef addAttachmentToCase(NodeRef caseRef, String subject, String filename,Reader rdr) {
		NodeRef objectRef = addObjectToCase(caseRef,subject,rdr,IssueTrackerConstants.TYPE_CASE_ATTACHMENT,genName("attachment_"));
		nodeService.setProperty(objectRef, IssueTrackerConstants.PROP_FILENAME, filename);
		return objectRef;
	}


	public NodeRef linkReferenceToCase(NodeRef caseRef, String subject, String url) {
		NodeRef objectRef = createIssueObject(caseRef,subject,IssueTrackerConstants.TYPE_CASE_REFERENCE,genName("link_"));
		nodeService.setProperty(objectRef, IssueTrackerConstants.PROP_LINK_URL, url);
		return objectRef;
	}


	public CaseStatus getCaseStatus(NodeRef caseRef) {
		return  CaseStatus.valueOf((String) nodeService.getProperty(caseRef, IssueTrackerConstants.PROP_CASE_STATUS));
	}


	public void suspendCase(NodeRef caseRef) {
		//TODO check for invalid state transition
		nodeService.setProperty(caseRef, IssueTrackerConstants.PROP_CASE_STATUS, CaseStatus.Suspended.toString());
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

	@Override
	protected void onBootstrap(ApplicationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onShutdown(ApplicationEvent event) {
		// TODO Auto-generated method stub
		
	}

}
