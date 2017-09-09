FROM hseeberger/scala-sbt

RUN apt-get update && \
    apt-get install -yy nginx
ADD etc /etc

ADD app /app
WORKDIR /app
RUN sbt compile

EXPOSE 8081

CMD ["./run"]
