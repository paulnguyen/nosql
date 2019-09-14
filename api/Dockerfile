FROM openjdk
EXPOSE 9090 8888
ADD ./build/libs/nosql-all.jar /srv/nosql-all.jar
ADD ./mdump.sh /srv/mdump.sh
ADD ./ddump.sh /srv/ddump.sh
ADD ./patch1.sh /srv/patch1.sh
ADD ./patch2.sh /srv/patch2.sh
ADD ./patch3.sh /srv/patch3.sh
ADD ./patch4.sh /srv/patch4.sh
ADD ./patch5.sh /srv/patch5.sh
ADD ./hosts-backup.sh /srv/hosts-backup.sh
ADD ./hosts-restore.sh /srv/hosts-restore.sh
RUN chmod +x /srv/*.sh
RUN yum -y install util-linux
RUN yum -y install iputils
RUN yum -y install vim 
WORKDIR /srv
CMD java -cp /srv/nosql-all.jar api.Main > /srv/nosql.log


