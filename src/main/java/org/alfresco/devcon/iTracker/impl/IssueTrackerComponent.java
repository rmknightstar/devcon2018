package org.alfresco.devcon.iTracker.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.alfresco.devcon.iTracker.IssueTrackerConstants;
import org.alfresco.devcon.iTracker.IssueTrackerService.CaseStatus;
import org.alfresco.devcon.iTracker.policy.CreateCasePolicy;
import org.alfresco.devcon.util.folder_hierarchy.FolderHierarchyHelper;
import org.alfresco.devcon.util.unique_property.UniquePropertyManager;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.rule.Rule;
import org.alfresco.service.cmr.rule.RuleService;
import org.alfresco.service.cmr.search.QueryConsistency;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.GUID;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	RuleService rulesService;
	List<String> projectsHome;
	private static final Log logger = LogFactory.getLog(IssueTrackerComponent.class);

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
		this.nodeLocatorService = this.serviceRegistry.getNodeLocatorService();
		this.searchService = this.serviceRegistry.getSearchService();
		this.contentService = this.serviceRegistry.getContentService();
		this.rulesService = this.serviceRegistry.getRuleService();
		
	}
	
	public void setFolderHierarchyHelper(FolderHierarchyHelper folderHierarchyHelper) {
		this.folderHierarchyHelper = folderHierarchyHelper;
	}
	
	void setProjectsHome(String projectsHome) {
		if (projectsHome.startsWith("/")) {
			projectsHome = projectsHome.substring(1);
		}
		this.projectsHome = Arrays.asList(projectsHome.split("/"));
	}

	NodeRef getProjectsHome() {
		// Get the projects folder and if it is not there, create it (runAsSystem)
		return folderHierarchyHelper.getFolder(nodeLocatorService.getNode(CompanyHomeNodeLocator.NAME, null, null),this.projectsHome,true);
	}
	
	NodeRef getProjectFolder(String projectId) {
		return folderHierarchyHelper.getFolder(getProjectsHome(),projectId,false);
	}
	
    public void setUniquePropertyManager(UniquePropertyManager uniquePropertyManager) {
        this.uniquePropertyManager = uniquePropertyManager;
    }
    
	private String getByCaseIdSearchQuery(String caseId) {
		return String.format("=%s:'%s' AND TYPE:'%s'", IssueTrackerConstants.PROP_CASE_ID,caseId,IssueTrackerConstants.TYPE_CASE);
	}

    public NodeRef getCaseById(String caseId) {
		SearchParameters sp = new SearchParameters();
		sp.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
		sp.setQueryConsistency(QueryConsistency.TRANSACTIONAL);
		sp.setQuery(getByCaseIdSearchQuery(caseId));
		sp.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
		ResultSet rs = searchService.query(sp);
		logger.debug("QUERY: "+sp.getQuery());
		if (rs.length() < 1) {
			return null;
		}
		logger.debug("NODEREF: "+rs.getNodeRef(0));
		return rs.getNodeRef(0);   	
    	
    }

    public void addRuleToCase(NodeRef caseRef) {
		/*NodeRef documentLibrary = nodeService.getChildByName(nodeRef, ContentModel.ASSOC_CONTAINS, SiteService.DOCUMENT_LIBRARY);
		Rule rule = createChangeOwnerActionAndRule(newOwner);
		rule.setRuleType(RuleType.INBOUND);
		String siteId = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
		Map<QName,Serializable> props = new HashMap<QName,Serializable>();
		props.put(ChangeOwnerConstants.PROP_SITE_ID, siteId);
		ruleService.saveRule(documentLibrary, rule);
		// Rule only has a nodeRef after it is saved
		nodeService.addAspect(rule.getNodeRef(), ChangeOwnerConstants.ASPECT_CHANGE_OWNER_RULE, props);	*/
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
		//TODO Add current year to the path hierarchy
		NodeRef caseRef = nodeService.createNode(getProjectFolder(projectId), ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, GUID.generate()), IssueTrackerConstants.TYPE_CASE).getChildRef();
		nodeService.setProperty(caseRef, IssueTrackerConstants.PROP_SUBJECT, subject);
		nodeService.setProperty(caseRef, IssueTrackerConstants.PROP_PROJECT_ID, projectId);
		return caseRef;
	}
	
	private String genName(String prefix) {
		// Use this to generate unique names (where the name is not important)
		return prefix + GUID.generate();
	}
	
	private NodeRef createIssueObject(NodeRef caseRef, String subject, QName type,String name) {
		//The child association needs a QName.  In this case I am using the Name space from the content model.
		NodeRef objRef = nodeService.createNode(caseRef, ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, name), type).getChildRef();

		// The Name shows up in two places
		nodeService.setProperty(objRef, ContentModel.PROP_NAME, name);
		return objRef;
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


	public void  makeCaseAttachment(NodeRef attachmentRef) {
		// Specialize the Type (the assumption being that this could have been dragged and dropped in as a cm:content type
		nodeService.setType(attachmentRef, IssueTrackerConstants.TYPE_CASE_ATTACHMENT);
		
		//Preserve the original name
		nodeService.setProperty(attachmentRef, IssueTrackerConstants.PROP_FILENAME, nodeService.getProperty(attachmentRef, ContentModel.PROP_NAME));
		NodeRef caseRef = nodeService.getPrimaryParent(attachmentRef).getParentRef();
		
		//Randomize the actual cm:name and the association name so that there are no conflicts
		String name=genName("attachment_");
        nodeService.moveNode(attachmentRef, caseRef, ContentModel.ASSOC_CONTAINS, QName.createQName(IssueTrackerConstants.ITRACK_MODEL_1_0_URI, name));
        nodeService.setProperty(attachmentRef, ContentModel.PROP_NAME, name);
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
		//TODO: we should probably cache this in the component but it is most likely cached within Alfresco
		//Because of the side effects of the get
		AuthenticationUtil.runAsSystem(new RunAsWork<NodeRef>() {

			@Override
			public NodeRef doWork() throws Exception {
				return getProjectsHome();
			}
			
		});
	}

	@Override
	protected void onShutdown(ApplicationEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void applyCaseInfo(NodeRef parentRef, NodeRef childRef) {
		nodeService.setProperty(childRef,IssueTrackerConstants.PROP_CASE_ID,nodeService.getProperty(parentRef,IssueTrackerConstants.PROP_CASE_ID));
		nodeService.setProperty(childRef,IssueTrackerConstants.PROP_PROJECT_ID,nodeService.getProperty(parentRef,IssueTrackerConstants.PROP_PROJECT_ID));
	}

}
