FROM openjdk
EXPOSE 9090 8888
ADD ./build/libs/nosql-all.jar /srv/nosql-all.jar
ADD ./mdump.sh /srv/mdump.sh
ADD ./ddump.sh /srv/ddump.sh
RUN chmod +x /srv/mdump.sh
RUN chmod +x /srv/ddump.sh
RUN yum -y install util-linux
WORKDIR /srv
CMD java -cp /srv/nosql-all.jar api.Main


