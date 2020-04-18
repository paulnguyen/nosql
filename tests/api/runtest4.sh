
#!/bin/bash
#
# Run a NoSQL Test Sequence
# HTML Output in ./output folder
#
# runtest4.sh <basedir> <config:local|server> <cron:yes|no>
#


echo "#################################################"
echo "# Run Delete/Create Test (Test #4)"
echo "#################################################"

basedir=$1
config=$2
cron=$3

if [ "$basdir" = "" ]
then
	basedir="."
fi

if [ "$config" = "" ]
then
	config="local"
fi

if [ "$config" = "server" ]
then
	newman="/bin/newman"
else
	newman="newman"
fi

if [ "$cron" = "yes" ]
then
	it="-i"
else
	it="-it"
fi

# Run Ping Tests (Post Startup)
rm -f $basedir/output/newman-run*.html
$newman run $basedir/tests/00-nosql-ping-checks.json -r cli,html --reporter-html-export $basedir/output
mv $basedir/output/newman-run-*.html $basedir/output/00-nosql-ping-checks.html

# Backup Host Files
echo "#################################################"
echo "# Backup Host Files for Partition Recovery"
echo "#################################################"
docker exec $it api_node_1 bash /srv/backup.sh
docker exec $it api_node_2 bash /srv/backup.sh
docker exec $it api_node_3 bash /srv/backup.sh
docker exec $it api_node_4 bash /srv/backup.sh
docker exec $it api_node_5 bash /srv/backup.sh


echo "#################################################"
echo "# Run Postman Test Suite"
echo "#################################################"
rm -f $basedir/output/newman-run*.html
$newman run $basedir/tests/4.1-nosql-delete-create-conflicts-setup.json -r cli,html --reporter-html-export $basedir/output
mv $basedir/output/newman-run-*.html $basedir/output/4.1-nosql-delete-create-conflicts-setup.html
sleep 30
# partition 3
docker exec $it api_node_2 bash /srv/patch3.sh
docker exec $it api_node_1 bash /srv/patch3.sh
docker exec $it api_node_4 bash /srv/patch3.sh
docker exec $it api_node_5 bash /srv/patch3.sh
docker exec $it api_node_3 bash /srv/patch2.sh
docker exec $it api_node_3 bash /srv/patch1.sh
docker exec $it api_node_3 bash /srv/patch4.sh
docker exec $it api_node_3 bash /srv/patch5.sh
# partition 4
docker exec $it api_node_2 bash /srv/patch4.sh
docker exec $it api_node_3 bash /srv/patch4.sh
docker exec $it api_node_1 bash /srv/patch4.sh
docker exec $it api_node_5 bash /srv/patch4.sh
docker exec $it api_node_4 bash /srv/patch2.sh
docker exec $it api_node_4 bash /srv/patch3.sh
docker exec $it api_node_4 bash /srv/patch1.sh
docker exec $it api_node_4 bash /srv/patch5.sh
# Pause for 1 minute to alow Network Partition Detections
sleep 60
$newman run $basedir/tests/00-nosql-ping-checks.json -r cli
# Update Documents During Partition 
$newman run $basedir/tests/4.2-nosql-delete-create-conflicts-partitioned.json -r cli,html --reporter-html-export $basedir/output
mv $basedir/output/newman-run-*.html $basedir/output/4.2-nosql-delete-create-conflicts-partitioned.html
# Restore Network
docker exec $it api_node_1 bash /srv/reset.sh
docker exec $it api_node_2 bash /srv/reset.sh
docker exec $it api_node_3 bash /srv/reset.sh
docker exec $it api_node_4 bash /srv/reset.sh
docker exec $it api_node_5 bash /srv/reset.sh
# Pause for 3 minutes to let Conflict Resolution Resolve
sleep 60
$newman run $basedir/tests/00-nosql-ping-checks.json -r cli
sleep 60
$newman run $basedir/tests/00-nosql-ping-checks.json -r cli
sleep 60
$newman run $basedir/tests/00-nosql-ping-checks.json -r cli
# Validate Partition Recovery
$newman run $basedir/tests/4.3-nosql-delete-create-conflicts-recovery.json -r cli,html --reporter-html-export $basedir/output
mv $basedir/output/newman-run-*.html $basedir/output/4.3-nosql-delete-create-conflicts-recovery.html



