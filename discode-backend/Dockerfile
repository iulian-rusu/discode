FROM openjdk:15-jdk-alpine
ADD .env .env
ADD target/backend-1.0.0.war target/backend-1.0.0.war
ADD scripts/deploy.sh scripts/deploy.sh
RUN mkdir images
ENTRYPOINT ["scripts/deploy.sh"]