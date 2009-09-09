Introduction

	DDesk is a ticket, task and incident management system profiled for deskside support companies.

	It has two types of UI: client and internal. The internal UI can be accessed by partner users, with certain limitations.
	Main, non obvious, features include:
		I18N at most levels - The application was designed to be used in trilingual setting. 3 translations exist: English, Lithuanian and Russian.
			Statuses, priorities and data are not internationalized, but change log and UI are.
			Email subject lines are not internationalized.
		Mail tracking - the application includes ability to track statuses of tasks over email (Experimental feature)
			The application is designed as a sidetool for communication, not as a central tool.
			It was not to impose itself on the users, but to help in tracking.
		Automatic login based on client certificate
			This requires configuration on Tomcat side and generation of an approrpiate certificate for DDesk itself.
		Contextual task ID coding
			Task IDs will be generated based on assigned project or by a special ID in form of U{date}[number]
	Limitations:
		The main workflow points are hardcoded: Accept and Close.
		Predefined dashboard
		Heavy, non AJAX GUI
	Problems:
		Seriously heavy internal index and ticket list pages - may reash up to 1MB in HTML


Dependencies

	The application is designed to work within Tomcat. It requires least configuration in a Tomcat container.
	(Though no hardcded dependencies exist on Tomcat)
	Java 5 is required for the code and some libraries are compiled with Java 6 syntax.

	Hibernate 3 and above is used for data access
	Apache Commons DBCP is used for connection pooling, initiated and configured by Tomcat itself
	PostgreSQL is the database used. There are 2 PL/pgSQL prcedures defined in the schema.
	JSF 1.2 on MyFaces and Tomahawk comonents
	JSF Facelets for JSF 1.2 is used


	Tested on Tomcat 5.5 and 6.0, with PostgreSQL 8.1, 8.2, 8.3 and 8.4.

Installation

	See INSTALL.txt

Overview

	The general target of this application is close contact with the client and client support. There are projects and project modules defined, but those are for internal information.
	This application has been developed for a deskside support company to register problems, track tasks, prioritize them and generate reporta data for invoising.
	Projects, project modules and some other features were added to manage internal application dvelopment.

Fist steps

	To login with the base install use the following credentials:
	User: admin
	Password: admin

	The application is either http://localhost:8080/ or http://localhost:8080/ddesk/

	You will be presented with the internal UI after login as admin.
	To see the client UI, go to http://localhost:8080/ddesk/index.jsf or http://localhost:8080/index.jsf

Default configuration

	The database is configured with URL jdbc:postgresql://localhost:5432/ddesk with user postgres and no password
	The configuration is in /web/META-INF/context.xml
	Application will try to get the data source from JNDI by looking up jdbc/ddesk
	That is configure in Hibernate configuration in /src/hibernate.cfg.xml

	By default email sending is not enabled.
	Email server and authentication is configured in the Configuration page.

For more technical documentation see {project page and blog}