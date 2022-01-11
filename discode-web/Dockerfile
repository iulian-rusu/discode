FROM nginx:1.17.8-alpine

COPY ng.conf /etc/nginx/conf.d/
RUN rm /etc/nginx/conf.d/default.conf
COPY dist/Discode /usr/share/nginx/html/