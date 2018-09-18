# EvaluatingSystem
综合素质测评管理系统

<<<<<<<<<<<<<<<<综合素质测评系统>>>>>>>>>>>>>>>>

系统安装目前需要以下步骤：（手动安装）

1.导入sql文件到服务器的数据库（mysql)

2.将项目文件使用Eclipse等IDE打开

3.配置WEB-INF目录中的dbconfig.properties文件（需要配置服务器上的数据库信息）


创建数据库
CREATE DATABASE IF NOT EXISTS evaluating DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
grant all privileges on evaluating.* to evaluating@localhost identified by 'evaluating123';
grant all privileges on evaluating.* to evaluating@'%' identified by 'evaluating123';
grant create routine on evaluating.* to evaluating@'%';
grant alter  routine on evaluating.* to evaluating@'%';
grant execute on evaluating.* to evaluating@'%';
