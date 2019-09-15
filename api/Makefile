
all: clean 

clean:
	find . -name "*.class" -exec rm -rf {} \;
	find . -name .DS_Store -exec rm -rf {} \;
	rm -rf .gradle
	rm -rf build/*
	rm -f *.db

compile:
	gradle build

build: compile
	gradle shadowJar

run: build
	echo Starting Service at:  http://localhost:9090
	java -cp build/libs/nosql-all.jar api.Main

docker-build: build
	docker build -t nosql .
	docker images

docker-clean:
	docker stop nosql
	docker rm nosql
	docker rmi nosql

docker-run:
	docker run --name nosql -td -p 9090:9090 -p 8888:8888 nosql
	docker ps

docker-stop:
	docker stop nosql
	docker rm nosql

docker-shell:
	docker exec -it nosql bash 

network-ls:
	docker network ls

network-inspect:
	docker network inspect api_network

network-create:
	docker network create api_network

network-prune:
	docker network prune	

docker-ps:
	 docker ps --all --format "table {{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}\t"

docker-ps-ports:
	 docker ps --all --format "table {{.Names}}\t{{.Ports}}\t"

nosql-up: network-create
	docker-compose scale node=5

nosql-down:
	docker-compose down

shell1:
	docker exec -it api_node_1 bash 

shell2:
	docker exec -it api_node_2 bash 

shell3:
	docker exec -it api_node_3 bash 

shell4:
	docker exec -it api_node_4 bash 

shell5:
	docker exec -it api_node_5 bash 

docker-nodes-save:
	docker exec -it api_node_1 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_2 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_3 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_4 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_5 cp /etc/hosts /etc/hosts.sav

docker-nodes-restore:
	docker exec -it api_node_1 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_2 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_3 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_4 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_5 cp /etc/hosts.sav /etc/hosts

docker-patch-test:
	docker exec -it api_node_2 bash /srv/hosts-backup.sh
	docker exec -it api_node_2 bash /srv/patch1.sh
	docker exec -it api_node_2 bash /srv/patch2.sh
	docker exec -it api_node_2 cat /etc/hosts 

docker-patch-restore:
	docker exec -it api_node_2 bash /srv/hosts-restore.sh
	docker exec -it api_node_2 cat /etc/hosts 






	
