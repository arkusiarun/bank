docker network create bank-mysql

docker container run --name mysqldb --network bank-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bank -d mysql:8

docker image build -t bank-jdbc .

docker container run --network bank-mysql --name bank-jdbc-container -p 8080:8080 -d bank-jdbc