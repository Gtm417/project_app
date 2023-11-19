# project_app

to build images:
1)  mvn install -- to create jar file
2)  docker build  --no-cache -t elastic-app.jar .
3)  docker build  --no-cache -t app.jar .
4) docker build  --no-cache -t eureka.jar .
