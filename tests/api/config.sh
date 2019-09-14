
INSTANCES=`docker ps --format "table {{.ID}}|{{.Names}}|{{.Ports}}|" | tail -n +2 | tr -d " \t"` 
for i in $INSTANCES
do 
	#echo $i
	id=`echo "$i" | cut -f 1 -d '|'`
	name=`echo "$i" | cut -f 2 -d '|'`
	ports=`echo "$i" | cut -f 3 -d '|'`
	port1=`echo "$ports" | cut -f 1 -d ',' | cut -f 2 -d':' | cut -f 1 -d'-'`
	echo "id:$id name:$name port:$port1"
	curl -X POST http://localhost:8001/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\"}"
	curl -X POST http://localhost:8002/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\"}"
	curl -X POST http://localhost:8003/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\"}"
	curl -X POST http://localhost:8004/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\"}"
	curl -X POST http://localhost:8005/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\"}"
done

