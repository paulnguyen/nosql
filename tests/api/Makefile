all: teardown clean

clean:
	rm -f output/*

network-ls:
	docker network ls

network-inspect:
	docker network inspect api_network

network-create:
	docker network create api_network

network-prune:
	docker network prune

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


startup: network-create
	docker-compose scale node=5
	sleep 5
	docker exec -it api_node_1 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_2 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_3 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_4 cp /etc/hosts /etc/hosts.sav
	docker exec -it api_node_5 cp /etc/hosts /etc/hosts.sav	
	sleep 5
	./config.sh

teardown:
	docker-compose down

partition1:
	docker exec -it api_node_2 bash /srv/patch1.sh
	docker exec -it api_node_3 bash /srv/patch1.sh
	docker exec -it api_node_4 bash /srv/patch1.sh
	docker exec -it api_node_5 bash /srv/patch1.sh
	docker exec -it api_node_1 bash /srv/patch2.sh
	docker exec -it api_node_1 bash /srv/patch3.sh
	docker exec -it api_node_1 bash /srv/patch4.sh
	docker exec -it api_node_1 bash /srv/patch5.sh

ping-test:
	newman run tests/00-nosql-ping-checks.json -r cli

test:
	./runtests.sh

reset:
	docker exec -it api_node_1 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_2 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_3 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_4 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_5 cp /etc/hosts.sav /etc/hosts
	
