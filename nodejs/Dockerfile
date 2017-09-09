FROM node

RUN apt-get update && \
    apt-get install -yy nginx && \
    npm install pm2 -g
    
ADD etc /etc

ADD app /app
WORKDIR /app
RUN npm i

EXPOSE 8080

CMD ["npm","start"]
