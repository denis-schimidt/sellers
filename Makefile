# Definição de variáveis
PROJECT_NAME := sellers-api
VERSION := $(shell grep -oP 'version = "\K[0-9.]+(?=-SNAPSHOT")' build.gradle.kts)
DOCKER_COMPOSE_FILE := docker-compose.yml
DOCKER_IMAGE := $(PROJECT_NAME):$(VERSION)
JAVA_FLAGS := --enable-preview -Xms1024m -Xmx1024m

.PHONY: test build run ps logs restart stop clean

version:
	@echo $(VERSION)

test:
	@echo "Executando testes com Gradle..."
	./gradlew test

build:
	@echo "Construindo imagem Docker..."
	@docker build -t $(DOCKER_IMAGE) .

run: clean build
	@echo "Iniciando os contêineres com Docker Compose..."
	@APP_VERSION=$(DOCKER_IMAGE) docker-compose -f $(DOCKER_COMPOSE_FILE) up -d

ps:
	@docker ps

logs:
	@docker logs -f $(PROJECT_NAME) || true

restart:
	@echo "Reiniciando os contêineres..."
	@docker-compose -f $(DOCKER_COMPOSE_FILE) start || true

stop:
	@echo "Parando os contêineres..."
	@docker-compose -f $(DOCKER_COMPOSE_FILE) stop

clean:
	@echo "Parando e removendo os contêineres..."
	@docker-compose -f $(DOCKER_COMPOSE_FILE) down
	@if [ -n "$$(docker ps -a -q)" ]; then docker ps -a -q | xargs docker rm || true; fi
