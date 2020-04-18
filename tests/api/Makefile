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
	docker-compose up -d --scale node=1
	sleep 1
	docker-compose up -d --scale node=2
	sleep 1
	docker-compose up -d --scale node=3
	sleep 1
	docker-compose up -d --scale node=4
	sleep 1
	docker-compose up -d --scale node=5
	sleep 5
	docker exec -it api_node_1 bash /srv/backup.sh
	docker exec -it api_node_2 bash /srv/backup.sh
	docker exec -it api_node_3 bash /srv/backup.sh
	docker exec -it api_node_4 bash /srv/backup.sh
	docker exec -it api_node_5 bash /srv/backup.sh
	sleep 5
	./config.sh

teardown:
	docker-compose down

ping-test:
	newman run tests/00-nosql-ping-checks.json -r cli

test:
	./runtest1.sh

reset:
	docker exec -it api_node_1 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_2 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_3 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_4 cp /etc/hosts.sav /etc/hosts
	docker exec -it api_node_5 cp /etc/hosts.sav /etc/hosts

partition1:
	docker exec -it api_node_2 bash /srv/patch1.sh
	docker exec -it api_node_3 bash /srv/patch1.sh
	docker exec -it api_node_4 bash /srv/patch1.sh
	docker exec -it api_node_5 bash /srv/patch1.sh
	docker exec -it api_node_1 bash /srv/patch2.sh
	docker exec -it api_node_1 bash /srv/patch3.sh
	docker exec -it api_node_1 bash /srv/patch4.sh
	docker exec -it api_node_1 bash /srv/patch5.sh

partition2:
	docker exec -it api_node_1 bash /srv/patch2.sh
	docker exec -it api_node_3 bash /srv/patch2.sh
	docker exec -it api_node_4 bash /srv/patch2.sh
	docker exec -it api_node_5 bash /srv/patch2.sh
	docker exec -it api_node_2 bash /srv/patch1.sh
	docker exec -it api_node_2 bash /srv/patch3.sh
	docker exec -it api_node_2 bash /srv/patch4.sh
	docker exec -it api_node_2 bash /srv/patch5.sh

partition3:
	docker exec -it api_node_2 bash /srv/patch3.sh
	docker exec -it api_node_1 bash /srv/patch3.sh
	docker exec -it api_node_4 bash /srv/patch3.sh
	docker exec -it api_node_5 bash /srv/patch3.sh
	docker exec -it api_node_3 bash /srv/patch2.sh
	docker exec -it api_node_3 bash /srv/patch1.sh
	docker exec -it api_node_3 bash /srv/patch4.sh
	docker exec -it api_node_3 bash /srv/patch5.sh

partition4:
	docker exec -it api_node_2 bash /srv/patch4.sh
	docker exec -it api_node_3 bash /srv/patch4.sh
	docker exec -it api_node_1 bash /srv/patch4.sh
	docker exec -it api_node_5 bash /srv/patch4.sh
	docker exec -it api_node_4 bash /srv/patch2.sh
	docker exec -it api_node_4 bash /srv/patch3.sh
	docker exec -it api_node_4 bash /srv/patch1.sh
	docker exec -it api_node_4 bash /srv/patch5.sh

partition5:
	docker exec -it api_node_2 bash /srv/patch5.sh
	docker exec -it api_node_3 bash /srv/patch5.sh
	docker exec -it api_node_4 bash /srv/patch5.sh
	docker exec -it api_node_1 bash /srv/patch5.sh
	docker exec -it api_node_5 bash /srv/patch2.sh
	docker exec -it api_node_5 bash /srv/patch3.sh
	docker exec -it api_node_5 bash /srv/patch4.sh
	docker exec -it api_node_5 bash /srv/patch1.sh

partition-2-3:
	# 1 and 2 can't reach 3, 4 and 5
	docker exec -it api_node_1 bash /srv/patch3.sh
	docker exec -it api_node_1 bash /srv/patch4.sh
	docker exec -it api_node_1 bash /srv/patch5.sh
	docker exec -it api_node_2 bash /srv/patch3.sh
	docker exec -it api_node_2 bash /srv/patch4.sh
	docker exec -it api_node_2 bash /srv/patch5.sh
	# 3, 4 and 5 can't reach 1 and 2
	docker exec -it api_node_3 bash /srv/patch1.sh
	docker exec -it api_node_3 bash /srv/patch2.sh
	docker exec -it api_node_4 bash /srv/patch1.sh
	docker exec -it api_node_4 bash /srv/patch2.sh
	docker exec -it api_node_5 bash /srv/patch1.sh
	docker exec -it api_node_5 bash /srv/patch2.sh

partition-lunch:
	# 1 (Alice) Isolated
	docker exec -it api_node_2 bash /srv/patch1.sh
	docker exec -it api_node_3 bash /srv/patch1.sh
	docker exec -it api_node_4 bash /srv/patch1.sh
	docker exec -it api_node_5 bash /srv/patch1.sh
	docker exec -it api_node_1 bash /srv/patch2.sh
	docker exec -it api_node_1 bash /srv/patch3.sh
	docker exec -it api_node_1 bash /srv/patch4.sh
	docker exec -it api_node_1 bash /srv/patch5.sh	
	# 2 (Ben) can only reach 4 (Dave)
	docker exec -it api_node_2 bash /srv/patch1.sh
	docker exec -it api_node_2 bash /srv/patch2.sh
	docker exec -it api_node_2 bash /srv/patch3.sh
	docker exec -it api_node_2 bash /srv/patch5.sh
	# 3 (Cathy) can only reach 4 (Dave)
	docker exec -it api_node_3 bash /srv/patch1.sh
	docker exec -it api_node_3 bash /srv/patch2.sh
	docker exec -it api_node_3 bash /srv/patch3.sh
	docker exec -it api_node_3 bash /srv/patch5.sh
	# 4 (Dave) can only reach 2 (Ben) and 3 (Cathy)
	docker exec -it api_node_4 bash /srv/patch1.sh
	docker exec -it api_node_4 bash /srv/patch4.sh
	docker exec -it api_node_4 bash /srv/patch5.sh	
	# 5 (+1 Guest) Isolated
	docker exec -it api_node_2 bash /srv/patch5.sh
	docker exec -it api_node_3 bash /srv/patch5.sh
	docker exec -it api_node_4 bash /srv/patch5.sh
	docker exec -it api_node_1 bash /srv/patch5.sh
	docker exec -it api_node_5 bash /srv/patch2.sh
	docker exec -it api_node_5 bash /srv/patch3.sh
	docker exec -it api_node_5 bash /srv/patch4.sh
	docker exec -it api_node_5 bash /srv/patch1.sh	







