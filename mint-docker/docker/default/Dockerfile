FROM tomcat:8
RUN sed -i 's/debian testing main/debian testing main contrib non-free/' /etc/apt/sources.list

# Install general tools
RUN apt-get update && apt-get -y install --no-install-recommends \
        graphviz \
        libcurl4-openssl-dev \
        libxml2-dev \
        python-pip \
        git \
        cgroupfs-mount \
        maven \
        tcl \
        tk \
        apt-transport-https \
        software-properties-common \
        gnupg2

# Install Docker
RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | apt-key add -
RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
   $(lsb_release -cs) \
   stable"
RUN apt-get update && apt-get -y install docker-ce

# Add the tomcat server configuration file
COPY ./mint-docker/config/tomcat/server.xml /usr/local/tomcat/conf/

# Install mint
RUN apt-get update && apt-get install -y git default-jdk
RUN mkdir -p /mint-src/mint/
WORKDIR /mint-src/mint
COPY . .

RUN _JAVA_OPTIONS=-Djdk.net.URLClassPath.disableClassPathURLCheck=true mvn package

# Add mint properties
RUN mkdir -p /opt/mint/storage/default && mkdir -p /etc/mint/

# Add mint build to tomcat (Deploy)
RUN cp -R /mint-src/mint/target/mint-server-2.0-SNAPSHOT  /usr/local/tomcat/webapps/mint-server

# Add mint context file to the mint portal webapp
COPY ./mint-docker/config/dev/context.xml /usr/local/tomcat/conf/context.xml
COPY ./mint-docker/config/dev/portal.properties /etc/mint/portal.properties

WORKDIR /usr/local/tomcat