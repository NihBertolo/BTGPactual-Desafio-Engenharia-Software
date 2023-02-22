# Desafio Engenharia Software


### Arquitetura 

Nesse projeto foi adotada a arquitetura baseada em microsserviços, com comunicação feita tanto com gRPC e mensageria com
RabbitMQ, atraves de duas filas entre os domínios de pagamento e pedido.

Existem no total 4 dominios (cliente, pedido, pagamento e produto) e para cada domínio, um banco de dados inserido no 
cluster do Amazon DocumentDB

Segue abaixo o diagrama de arquitetura implantada no Cluster Kubernetes:

![Architecture](ms-backend-for-fronted/src/main/resources/diagrams/architecture.png)


### Modelagem de dados
 
 * Domínio Cliente

![Customer](ms-customer/src/main/resources/docs/datamodeling/customer-model.drawio.png)


 * Domínio Pedido

![Order](ms-order/src/main/resources/docs/datamodeling/order-model.drawio.png)

 * Domínio Pagamento

![Payment](ms-payment/src/main/resources/documentation/datamodeling/payment-model.drawio.png)

* Domínio Produto

![Product](ms-product/src/main/resources/documentation/datamodeling/product-model.drawio.png)