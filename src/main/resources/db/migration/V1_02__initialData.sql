INSERT INTO `user` SET id=1, active=1, created=NOW(), locked=0, name='Administrator';
INSERT INTO `user_role` SET user_id=1, roles='admin';
INSERT INTO `user_role` SET user_id=1, roles='editor';
INSERT INTO `user_role` SET user_id=1, roles='user';
INSERT INTO `credential` SET active=1,auth_data='d033e22ae348aeb5660fc2140aec35850c4da997', auth_service_type='password',created=NOW(), email='admin', uid='password:admin', updated=NOW(), user_id=1, verified=1;

INSERT INTO `site` SET id=1, domain='localhost', name='default', title='Default';

INSERT INTO `page` SET id=1, name='home', title='Home', body='Welcome!', created=NOW(), description='',hide_title=1,  parent_id=0, placeholder=0, public_page=1, sortindex=1, auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=2, sortindex=1, parent_id=1, name='products', title='Products', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1, auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=3, sortindex=1, parent_id=2, name='product1', title='Product 1', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1, auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=4, sortindex=2, parent_id=2, name='product2', title='Product 2', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=5, sortindex=3, parent_id=2, name='product3', title='Product 3', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=6, sortindex=4, parent_id=2, name='product4', title='Product 4', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=7, sortindex=5, parent_id=2, name='product5', title='Product 5', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=8, sortindex=6, parent_id=2, name='product6', title='Product 6', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=9, sortindex=7, parent_id=2, name='product7', title='Product 7', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=10, sortindex=8, parent_id=2, name='product8', title='Product 8', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=11, sortindex=9, parent_id=2, name='product9', title='Product 9', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=12, sortindex=2, parent_id=1, name='contacts', title='Contact us', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
INSERT INTO `page` SET id=13, sortindex=3, parent_id=1, name='about', title='About us', body='', created=NOW(), description='',hide_title=0, placeholder=0, public_page=1,auto_name=1, rich_text=1, tags='', updated=NOW(), site_id=1;
