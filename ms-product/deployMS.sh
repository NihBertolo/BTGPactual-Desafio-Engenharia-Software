#Deploy Microservice
kubectl create secret generic redis-secret --from-literal=password=desafio_engenharia
printf "Redis initialized!"
kubectl apply -f ./k8s/deployment.yaml
kubectl apply -f ./k8s/service.yaml
