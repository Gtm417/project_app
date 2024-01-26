# project_app

to build images:
1)  mvn install -- to create jar file
2)   docker build  --no-cache -t elastic-app.jar .
3)  /mnt/c/MyFiles/Diploma/project/projects_app docker build  --no-cache -t app.jar .
4)  /mnt/c/MyFiles/Diploma/project/eureka docker build  --no-cache -t eureka.jar .
5)  /mnt/c/MyFiles/Diploma/project/api-gateway docker build  --no-cache -t api-gateway.jar .
