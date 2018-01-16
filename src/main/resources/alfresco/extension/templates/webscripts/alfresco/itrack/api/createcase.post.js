var obj=jsonUtils.toObject(requestbody.content);
var node = issueTracker.createCase(obj.projectId,obj.subject);
model.id=node.id;