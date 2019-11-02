
#!/bin/bash
#
# Run a NoSQL Test Sequence
# HTML Output in ./output folder
#
# runtest1.sh <basedir> <config:local|server> <cron:yes|no>
#

echo "#################################################"
echo "# Run Basic Replication Test (Test #1)"
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
rm -f output/newman-run*.html
$newman run $basedir/tests/1.1-nosql-one-to-many-create.json -r cli,html --reporter-html-export $basedir/output
mv $basedir/output/newman-run-*.html $basedir/output/1.1-nosql-one-to-many-create.html
sleep 30
$newman run $basedir/tests/1.2-nosql-one-to-many-checks.json -r cli,html --reporter-html-export $basedir/output
mv $basedir/output/newman-run-*.html $basedir/output/1.2-nosql-one-to-many-checks.html





