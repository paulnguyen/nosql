#!/bin/sh

# Container

CONTAINER="nosql"
VERSION="latest"

# Shell Variables

OPT=""
OPT1=""
OPT2=""
DEBUG="TRUE"
XMENU="N"

## Set Echo Command Flavor

PROMPT=""
OS=`uname -a | cut -f1 -d" "`
if [ "$OS" = "Darwin" ] ; then
    PROMPT="echo"
else
    PROMPT="echo -e"
fi ;

#
# Shell Functions 
# for Menu Operations
#

docker_build() {
	make jar
	docker build -t $CONTAINER .
}

docker_run() { 
	docker run --name $CONTAINER -td -p 9090:9090 -p 8888:8888 $CONTAINER
}

docker_up() {
	docker network create api_network
	docker-compose scale nodes=5
}

docker_down() {
	docker-compose down 	
	docker network prune
}

docker_restart () {
	docker restart $CONTAINER
}

docker_images() {
	docker images
}

docker_networks() {
	docker network ls
}

docker_rmi() {
	IMG_ID=`docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}" | grep $CONTAINER | tr -s ' ' | tr ' ' '|' | cut -f 1 -d '|' | head -1`
	while [ "$IMG_ID" != "" ]
	do
		echo "Removing Image: $IMG_ID"
 		docker rmi -f $IMG_ID
		IMG_ID=`docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}" | grep $CONTAINER | tr -s ' ' | tr ' ' '|' | cut -f 1 -d '|' | head -1`
	done
}

docker_rmi_all() {
	IMG_ID=`docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}" | tr -s ' ' | tr ' ' '|' | cut -f 1 -d '|' | tail -n +2 | head -1`
	while [ "$IMG_ID" != "" ]
	do
		echo "Removing Image: $IMG_ID"
 		docker rmi -f $IMG_ID
		IMG_ID=`docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}" | tr -s ' ' | tr ' ' '|' | cut -f 1 -d '|' | tail -n +2 | head -1`
	done
}


docker_ps() {
	echo "Running Containers:"
	echo " "
	docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Ports}}\t{{.Status}}\t"
}

docker_restart() {
	docker restart $CONTAINER
}

docker_stop() {
	docker stop $CONTAINER
	docker rm $CONTAINER
}

docker_stop_all () {
	INST_ID=`docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}\t" | tr -s ' ' | tr ' ' '|' | cut -f 2 -d '|' | tail -n +2 | head -1`
	while [ "$INST_ID" != "" ]
	do
		echo "Stopping Instance: $INST_ID"
 		docker stop $INST_ID  > /dev/null 2>&1
 		docker rm $INST_ID > /dev/null 2>&1
		INST_ID=`docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}\t" | tr -s ' ' | tr ' ' '|' | cut -f 2 -d '|' | tail -n +2 | head -1`
	done	
}


docker_cmd () {
  	$PROMPT "CMD: \c" ; read cmd ;	
  	echo $cmd
	docker exec -it $CONTAINER $cmd
}

set_version() {
  	$PROMPT "Set Container Version: \c" ; read VERSION ;
}

okay_pause() {
	$PROMPT "\n[Okay] \c"; 
	read ans ; 
}


##
## MAIN MENU LOOP
##

while [ "$OPT" != "X" ]  
do
	clear
	echo ""
	echo "============================================" ;
	echo "         NOSQL PROJECT DOCKER MENU          " ;
	echo "============================================" ;
	echo "> $CONTAINER:$VERSION                       " ;
	echo " "
	echo "[i] images     - Show Docker Images         " ;
	echo "[n] networks   - Show Docker Networks       " ;
	echo "[b] build      - Build Container Image      " ;
	echo "[r] run        - Run Single Container       " ;
	echo "[s] stop       - Stop Single Container      " ;
	echo "[u] up         - Start Up Cluster           " ;
	echo "[d] down       - Shut Down Cluster          " ;
	echo "[p] ps         - Show Running Containers    " ;
	echo "[s] shell #    - Enter Container Shell      " ;
	echo "[c] cleanup    - Remove Local Images        " ;
	echo "[v] version    - Set Container Version      " ;
	echo " " 	
	echo "[X] Exit Menu                               " ;
	echo " "
	$PROMPT "Selection: \c"
	read OPT OPT1 OPT2
	case $OPT in
		i|images)	    echo " " ; docker_images ; okay_pause ;;
		n|networks)		echo " " ; docker_networks ; okay_pause ;;
		b|build)		echo " " ; docker_build ; okay_pause ;;
		r|run) 			echo " " ; docker_run ; okay_pause ;;
		s|stop) 		echo " " ; docker_stop; okay_pause ;;
 		u|up)			echo " " ; docker_up ; okay_pause ;;
		d|down)			echo " " ; docker_down ; okay_pause ;;
		p|ps) 			echo " " ; docker_ps ; okay_pause ;;
		c|cleanup) 		echo " " ; docker_stop_all; docker_rmi_all ; okay_pause ;;
		v|version) 		echo " " ; set_version ; okay_pause ;;
		cmd)			echo " " ; docker_cmd ; okay_pause ;;
        debug)          echo " " ; if [ "$OPT1" = "" -o "$OPT1" = "on" ] ; then DEBUG="TRUE" ; echo "Debug ON" ; 
								   else DEBUG="FALSE" ; echo "Debug OFF" ; fi ; okay_pause ;;
        s|shell)        echo " " ; if [ "$OPT1" = "" -o "$OPT1" = "on" ] ; then clear ; docker exec -it $CONTAINER bash ; 
								   else docker exec -it api_nodes_$OPT1 bash  ; fi ; okay_pause ;;
		x|X) 			clear ; OPT="X" ; echo "Exiting " ;; 
	esac
done

