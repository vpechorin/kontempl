INSERT INTO page SET
`body`="Welcome!",
`description`="Home page",
hideTitle=0,
placeholder=FALSE,
publicPage=TRUE,
created=NOW(), 
updated=NOW(), 
`name`='home', 
`parentId`=0, 
sortIndex=1,
tags=NULL,
updatedBy=1,
`title`='Home';
INSERT INTO `role` VALUES (1,'System administrator','admin'),(2,'Editor','editor');
INSERT INTO `user` VALUES (1,1,NULL,0,'Administrator');
INSERT INTO `userrole` VALUES (1,1);
INSERT INTO `credential` SET active=1,authData=SHA1('admin'), authServiceType='password',created=NOW(), email='admin', uid='password:admin', updated=NOW(), userId=1, verified=1;