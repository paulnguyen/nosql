
all: clean 

clean:
	find . -name "*.class" -exec rm -rf {} \;
	rm -rf build/*
	rm *.db

compile:
	gradle build

jar: compile
	gradle shadowJar

run: 
	echo Starting Service at:  http://localhost:9090
	java -cp build/libs/nosql-all.jar api.Main

docker-build: 
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








	
