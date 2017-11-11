
# NoJava (Simple NoSQL - Key/Value Pair Storage Manager) 
# Based on Shore DB SM (Release 2.0) + Java NIO

    http://research.cs.wisc.edu/shore/      (Archived Site)
    http://research.cs.wisc.edu/shore-mt/   (New Site)

    http://www.eclipse.org/aspectj/                                 (Using AspectJ 1.8)
    https://eclipse.org/aspectj/doc/next/devguide/ajc-ref.html      (AspectJ Compiler)
    https://eclipse.org/aspectj/docs.php                            (AspectJ Documentation)
    https://eclipse.org/aspectj/doc/released/devguide/index.html    (AspectJ Dev Guide/Tools)

    http://www.tutorialspoint.com/java/
    https://docs.oracle.com/javase/tutorial/
    http://www.tutorialspoint.com/java/java_networking.htm 
    https://docs.oracle.com/javase/tutorial/networking/index.html

    http://www.javaworld.com/article/2882984/core-java/nio2-cookbook-part-1.html
    http://www.javaworld.com/article/2899694/core-java/nio-2-cookbook-part-2.html
    http://www.javaworld.com/article/2928805/core-java/nio-2-cookbook-part-3.html
	
    
## Install AspectJ Compiler & Tools

    $ pwd
    /home/ubuntu
    $ ls
    lib/  workspace/
    $ unzip workspace/aspectj/aspectj1.8.zip 
    creating: aspectj1.8/
    creating: aspectj1.8/bin/
    inflating: aspectj1.8/bin/aj 
    ...
    
## Install Java 7 JDK

    sudo apt-get update
    sudo apt-get install openjdk-7-jdk
    
## Setup AspectJ and Java Home

    $ vi /home/ubuntu/.bashrc
    
    # SET UP ASPECTJ and JAVA
    export CLASSPATH=/home/ubuntu/aspectj1.8/lib/aspectjrt.jar:.
    export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
    export ASPECTJ_HOME=/home/ubuntu/aspectj1.8
    export ASPECTJ_RT=/home/ubuntu/aspectj1.8/lib/aspectjrt.jar	
    export PATH=$PATH:$ASPECTJ_HOME/bin:$JAVA_HOME/bin:.
    

    