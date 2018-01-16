var obj=jsonUtils.toObject(requestbody.content);
var node = issueTracker.addCommentToCase(obj.caseId,obj.subject,obj.comment);
model.id=node.id;