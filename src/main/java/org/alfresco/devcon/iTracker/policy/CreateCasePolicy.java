package org.alfresco.devcon.iTracker.policy;

import org.alfresco.devcon.iTracker.IssueTrackerConstants;
import org.alfresco.devcon.iTracker.impl.IssueTrackerComponent;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies.OnCreateChildAssociationPolicy;
import org.alfresco.repo.node.NodeServicePolicies.OnCreateNodePolicy;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CreateCasePolicy implements OnCreateNodePolicy, OnCreateChildAssociationPolicy {

	private PolicyComponent policyComponent;
	IssueTrackerComponent issueTrackerComponent;
	private static final Log logger = LogFactory.getLog(CreateCasePolicy.class);

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}

	public void setIssueTrackerComponent(IssueTrackerComponent issueTrackerComponent) {
		this.issueTrackerComponent = issueTrackerComponent;
	}

	public void initialise() {

		policyComponent.bindClassBehaviour(
				OnCreateNodePolicy.QNAME,
				IssueTrackerConstants.TYPE_CASE,
				new JavaBehaviour(this, OnCreateNodePolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT));
		policyComponent.bindAssociationBehaviour(
				OnCreateChildAssociationPolicy.QNAME,
				IssueTrackerConstants.TYPE_CASE,
				ContentModel.ASSOC_CONTAINS,
				new JavaBehaviour(this, OnCreateChildAssociationPolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT));
		logger.debug("INITIALISED");
	}	


	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		issueTrackerComponent.registerCaseId(childAssocRef.getChildRef());
	}

	@Override
	public void onCreateChildAssociation(ChildAssociationRef childAssocRef, boolean isNewNode) {
		issueTrackerComponent.applyCaseInfo(childAssocRef.getParentRef(),childAssocRef.getChildRef());
	}

}