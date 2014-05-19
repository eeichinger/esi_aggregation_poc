
# ESI and centralised master template aggregation layouts sample ####

This demo illustrates how ESI and Spring MVC remote view resolution can be combined in a flexible content view aggregation but still allowing central maintenance of those views.

## Features

* the web application fetches all templates from the central server, only falling back to local copies if necessary
* Either serve the template to the web application through varnish Varnish or let the application fetch it from the CMS directly. Varnish allows to serve different master templates to different websites by e.g. url rewriting
* the CMS returns a master template to the application for rendering, potentially containing ESI includes for header and footer etc. The application renders the requested page using that template.
* The HTML returned by the application to varnish contains ESI include tags to be resolved. Varnish resolves those tags and returns the aggregated page to the browser
* Using Varnish's url-rewriting, we can introduce a distinguished namespace for each application.

## Preliminaries

PHP >= 5.4: needed for running a simple php test server to simulate our CMS
mvn == 3.0.5: needed to build & run the java sample web application
varnish >= 4: for ESI and url-rewriting

## Running the demo

```
mvn -f javasamplewebapp/pom.xml jetty:run > jetty.log &
php -S 127.0.0.1:4000 -t ./cmssimulated &
varnishd -a :1234 -i hugo -f varnish-base.vcl -T localhost:2345 -F &
```

first we start the java sample web which simulates the dynamic content application, then a php test server to simulate our CMS (any http server capable of serving static files will do!). Finally we start varnish and access the application through varnish by opening in the browser http://localhost:1234/sampleapp/home. You should see a page displaying

```
this is coming from the master header via ESI!
Hello from REMOTE Master!!!
Hello from Content!
this is coming from the master-footer via ESI
```

## Experiments

1. Override the application's own content template with the CMS'

	In ./cmssimulated/master/ rename home.html.ignore to home.html. The application always checks the CMS first when resolving a template. Refresh the browser and it should now show  
	
	```
Hello from REMOTE content!!!
	```

2. Shut down the CMS
	
	e.g. `pkill php` to shut down the CMS. As long as the web applications fetch their templates directly from the CMS, an unresponsive CMS will cause the application to fall back to using it's baked-in templates. Kill our "CMS" and you should see
	
	```
Hello from Master
Hello from Content!
	```

3. Fetch templates from CMS via Varnish

	fetching the templates via Varnish allows to Varnish to serve templates to applications even if the CMS is down, e.g. for maintenance
