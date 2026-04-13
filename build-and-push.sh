#!/bin/bash
set -e

DOCKER_USERNAME=andreyjava404

echo "Building jar..."
mvn clean package -DskipTests

VERSION=$(grep -m1 '<version>' pom.xml | tr -d ' <>/version')
GIT_HASH=$(git rev-parse --short HEAD)
TAG="$VERSION-$GIT_HASH"

echo "Building Docker image..."
docker build -t bff-notes .

echo "Tagging image: $TAG"
docker tag bff-notes $DOCKER_USERNAME/bff-notes:$TAG
docker tag bff-notes $DOCKER_USERNAME/bff-notes:latest

echo "Pushing image..."
docker push $DOCKER_USERNAME/bff-notes:$TAG
docker push $DOCKER_USERNAME/bff-notes:latest

echo "Done! Image: $DOCKER_USERNAME/bff-notes:$TAG"