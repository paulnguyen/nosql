
INSTANCES=`docker ps --format "table {{.ID}}|{{.Names}}|{{.Ports}}|" | tail -n +2 | tr -d " \t"` 
for i in $INSTANCES
do 
	#echo $i
	id=`echo "$i" | cut -f 1 -d '|'`
	name=`echo "$i" | cut -f 2 -d '|'`
	ports=`echo "$i" | cut -f 3 -d '|'`
	#echo $ports
	admin_port=`echo "$ports" | cut -f 1 -d ',' | cut -f 2 -d':' | cut -f 1 -d'-'`
	api_port=`echo "$ports" | cut -f 2 -d ',' | cut -f 2 -d':' | cut -f 1 -d'-'`
	echo " "
	echo "id:$id name:$name api_port:$api_port admin_port:$admin_port"
	echo " "
	curl -X POST http://localhost:8001/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\", \"admin_port\" : \"$admin_port\", \"api_port\" : \"$api_port\"}"
	curl -X POST http://localhost:8002/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\", \"admin_port\" : \"$admin_port\", \"api_port\" : \"$api_port\"}"
	curl -X POST http://localhost:8003/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\", \"admin_port\" : \"$admin_port\", \"api_port\" : \"$api_port\"}"
	curl -X POST http://localhost:8004/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\", \"admin_port\" : \"$admin_port\", \"api_port\" : \"$api_port\"}"
	curl -X POST http://localhost:8005/node -H 'Content-Type: application/json' -d "{ \"id\" : \"$id\", \"name\" : \"$name\", \"admin_port\" : \"$admin_port\", \"api_port\" : \"$api_port\"}"
	echo " "
done

