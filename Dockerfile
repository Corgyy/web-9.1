# ===== BUILD WAR =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# pom.xml đã chuyển sang javax.servlet-api:4.0.1 (scope provided)
RUN mvn -DskipTests -Dmaven.test.skip=true clean package

# ===== RUN ON TOMCAT 9 (javax) =====
FROM tomcat:9.0-jdk17-temurin

# Xoá webapps mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# (Khuyến nghị) set UTF-8 cho Connector để xử lý query/URL non-ASCII
RUN sed -i 's#<Connector port="8080"#<Connector port="8080" URIEncoding="UTF-8"#' /usr/local/tomcat/conf/server.xml

# Deploy WAR thành ROOT.war (context = "/")
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh","run"]
