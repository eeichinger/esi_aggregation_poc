mvn -f javasamplewebapp/pom.xml jetty:run > jetty.log &
php -S 127.0.0.1:4000 -t ./cmssimulated &
varnishd -a :1234 -i hugo -f varnish-base.vcl -T localhost:2345 -F &
# varnishlog &
jobs
