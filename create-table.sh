#!/bin/zsh

aws dynamodb --endpoint-url http://localhost:8000 --region sa-east-1 create-table --table-name demo-customer-info \
          --attribute-definitions AttributeName=id,AttributeType=S --key-schema AttributeName=id,KeyType=HASH \
          --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5