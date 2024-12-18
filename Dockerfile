# Etapa 1: Gerar o Jar
FROM gradle:8.11-jdk-21-and-23-alpine AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src
RUN gradle clean build --console=plain
RUN rm build/libs/*plain.jar && mv build/libs/*.jar sellers-api.jar

#Etapa 2: Gerar JRE customizado
FROM eclipse-temurin:22-alpine AS jre-slim
ARG JAR_NAME=sellers-api.jar
RUN apk update && apk add binutils
WORKDIR /app
COPY --from=build /app/$JAR_NAME .

RUN  jar -xvf $JAR_NAME

RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 22  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    $JAR_NAME > deps.info

RUN jlink \
    --add-modules $(cat deps.info),jdk.jdwp.agent \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=zip-9 \
    --output /app/jre-slim

#Etapa 3: Imagem final
FROM alpine:latest

LABEL maintainer="Denis Schimidt <denao@gmail.com>"
LABEL description="Microserviço para cadastro de vendedores"

ENV JAVA_HOME=/opt/jre-slim
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV JVM_OPTIONS="--enable-preview -XX:NativeMemoryTracking=summary -XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0"
ENV JAVA_REMOTE_DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

WORKDIR /app

COPY --from=jre-slim /app/jre-slim $JAVA_HOME
COPY --from=build /app/sellers-api.jar sellers-api.jar
COPY --from=build /app/build/resources/main/scripts/start.sh start.sh

RUN addgroup -S appgroup && adduser -S app-user -G appgroup
USER app-user

EXPOSE 8080
ENTRYPOINT ["sh","start.sh"]
