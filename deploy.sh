#!/bin/bash
docker build --platform linux/amd64 -t codenames.azurecr.io/postgres:latest ./database
docker build --platform linux/amd64 -t codenames.azurecr.io/service:latest ./service
docker build --platform linux/amd64 -t codenames.azurecr.io/frontend:latest ./frontend
docker push codenames.azurecr.io/postgres:latest
docker push codenames.azurecr.io/service:latest
docker push codenames.azurecr.io/frontend:latest
az container restart --resource-group codenamesResourceGroup --name fullStackContainerGroup