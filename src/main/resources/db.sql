INSERT INTO `site` SET id=1, domain='localhost', name='default', title='Default';
INSERT INTO `page` SET id=1, name='home', title='Home', body='Welcome!', created=NOW(), description='',hideTitle=1,  parentId=0, placeholder=0, publicPage=1, sortindex=1, autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=2, sortindex=1, parentId=1, name='products', title='Products', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1, autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=3, sortindex=1, parentId=2, name='products1', title='Products 1', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1, autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=4, sortindex=2, parentId=2, name='products2', title='Products 2', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=5, sortindex=3, parentId=2, name='products3', title='Products 3', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=6, sortindex=4, parentId=2, name='products4', title='Products 4', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=7, sortindex=5, parentId=2, name='products5', title='Products 5', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=8, sortindex=6, parentId=2, name='products6', title='Products 6', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=9, sortindex=7, parentId=2, name='products7', title='Products 7', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=10, sortindex=8, parentId=2, name='products8', title='Products 8', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=11, sortindex=9, parentId=2, name='products9', title='Products 9', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=12, sortindex=2, parentId=1, name='contacts', title='Contact us', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `page` SET id=13, sortindex=3, parentId=1, name='about', title='About us', body='', created=NOW(), description='',hideTitle=0, placeholder=0, publicPage=1,autoName=1, richText=1, tags='', updated=NOW(), siteId=1;
INSERT INTO `user` SET id=1, active=1, created=NOW(), locked=0, name='Administrator';
INSERT INTO `user_role` SET User_id=1, roles='admin';
INSERT INTO `user_role` SET User_id=1, roles='editor';
INSERT INTO `user_role` SET User_id=1, roles='user';
INSERT INTO `credential` SET active=1,authData='d033e22ae348aeb5660fc2140aec35850c4da997', authServiceType='password',created=NOW(), email='admin', uid='password:admin', updated=NOW(), userId=1, verified=1;