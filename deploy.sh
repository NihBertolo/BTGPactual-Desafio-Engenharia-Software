#Prometheus Deploy
#kubectl create namespace monitoring
#kubectl create -f ./deploy/prometheus/k8s/manifests/clusterRole.yaml
#kubectl create -f ./deploy/prometheus/k8s/manifests/config-map.yaml

#kubectl create  -f ./deploy/prometheus/k8s/manifests/prometheus-deployment.yaml
#kubectl create  -f ./deploy/prometheus/k8s/manifests/prometheus-service.yaml

#printf "\n Prometheus Deployed!"

#Initializing database
docker run -d -p 27017:27017 --name test-mongo -v data-vol:/data/db -e MONGODB_INITDB_ROOT_USERNAME=desafio -e MONGODB_INITDB_ROOT_PASSWORD=engenharia mongo:latest

#RabbitMQ Deploy
kubectl apply -f ./deploy/rabbitmq/k8s/deployment.yaml
kubectl apply -f ./deploy/rabbitmq/k8s/service.yaml

#grafana Deploy
#kubectl apply -f ./deploy/grafana/k8s/deployment.yaml
#kubectl apply -f ./deploy/grafana/k8s/service.yaml

#DeployMS
for dir in $(find -type d); do
  if [ -f "$dir/deployMS.sh" ]; then
    cd "$dir"
    bash deployMS.sh
    cd -
  fi
done

printf 'Microservices Deployed! ;D'