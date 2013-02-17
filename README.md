termitaria
==========

This repository holds the Termitaria Project

HOW TO INSTALL TERMITARIA
===========================

0. First of all you'll need to setup the software platform on which Termitaria will run, which is:
  JDK 1.7
	Apache Tomcat 7.0 
	MySQL Community Server 5.0

1. Run the MySQL SQL script (TermitariaOM/WebContent/setup/Termitaria.sql). This will create your databases: om, cm, ts and audit.

2. Configure for each module the jdbc parameters
	In JavaSource/config/jdbc.properties, you will have to specify the:
		<module>.jdbc.url=jdbc\:mysql\://localhost\:3306/<module>
		<module>.jdbc.username=root
		<module>.jdbc.password=password
Where <module> is om, cm, ts or audit.

3. Create the following folders 
(or change folder path in config.properties and applicationContext.xml for each of the 3 modules):
	C:\Termitaria\attachmentCacheDirTS
	C:\Termitaria\attachmentCacheDirAUDIT
	C:\Termitaria\attachmentCacheDirCM

4. Add all your projects to Tomcat Server: TermitariaOM, TermitariaCM, TermitariaTS and TermitariaAudit.   

5. It is recomended to increase Tomcat's startup time to more than 45 seconds (try at least 100 seconds).
	(In Eclipse IDE)
	5.1 Open Tomcat's Server Config Editor: double click on Tomcat Server from the Servers View
	5.2 In the Timeouts section, increase the Start (in seconds) value

6. It is also recommend to increase Tomcat's memory (-XX:MaxPermSize=1024m -XX:PermSize=512m).
	(In Eclipse IDE)
	6.1 Open Tomcat's Server Config Editor: double click on Tomcat Server from the Servers View
	6.2 From the General Information Section, click on Open launch configuration
	6.3 Click on the (x)=Arguments tab
	6.4 Add in the VM Arguments box, after the last line the following configuration: -XX:MaxPermSize=1024m -XXermSize=512m 

7. Access the application at the following url: http://localhost:8080/TermitariaOM using the default credentials: adminIT/password.
   AdminIT is the super administrator. Using adminIT you can add as many organizations and users as you please. After adding an 
   organization, you can add an administrator for that organization.
   Logged in as that new created organization's administrator you can build more (adding more people, departments, etc).	

-------------------------------------------------------------------------------------------------------------------

HOW TO USE TERMITARIA
===========================

0. After installing Termitaria, and verifing that everything is up and running by accessing this url http://localhost:8080/TermitariaOM, 
you can log in using the default super admin credentials adminIT/password.

1. Logged in as adminIT create your organization and define the organization's administrator.

2. Log out from Organization Management Module as the adminIT and log in as your organization's administrator, you've just created.



-------------------------------------------------------------------------------------------------------------------
If you need more help, please feel free to contact us!

Tekin Omer-Ali 
Termitaria Main Developer
tekin.omer-ali [at] codesphere.ro

Dan Damian 
Termitaria Community Manager
dan.damian [at] codesphere.ro
