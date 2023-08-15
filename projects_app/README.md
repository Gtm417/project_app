# project_app

to build images:
1) run mvn install -- to create jar file
2) run docker build  --no-cache -t elastic-app.jar .
3) run docker build  --no-cache -t app.jar .
