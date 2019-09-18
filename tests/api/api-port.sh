
node=`docker ps --format "table {{.ID}}|{{.Names}}|{{.Ports}}|" | grep "api_node_$1" | cut -f 3 -d '|' | cut -f 2 -d ',' | cut -f 2 -d':' | cut -f 1 -d'-'`
echo $node