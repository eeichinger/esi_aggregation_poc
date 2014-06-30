vcl 4.0;

backend contentserver {
   .host = "192.168.20.20";
   .port = "80";
}

backend appserver {
   .host = "127.0.0.1";
   .port = "8080";
}

sub vcl_recv {
  // default requests to be sent to our CMS
  set req.backend_hint=appserver;
  set req.http.X-Url = req.url;
  set req.http.X-Host = req.http.host;

  if (req.http.host ~ "^cms\.") {
	  // this is an app asking for a template. Point it to the default masters
    set req.backend_hint=contentserver;
    set req.http.Host = "pol-cms-cdps-ci-dev-pub-one.192.168.20.20.xip.io";
    set req.url = regsub(req.url, "^/", "/dam/templates/");
	}
	
/*
  if (req.url ~ "^/sampleapp/")  {
		// check for browser client requests to our "sampleapp"
    set req.backend_hint=appserver;		
    set req.url = regsub(req.url, "^/sampleapp/", "/");
  } else if (req.url ~ "^/master/\w+/") {
	  // this is an app asking for a template. Point it to the default masters
    set req.backend_hint=contentserver;
    set req.http.host = "pol-cms-cdps-ci-dev-pub-one.192.168.20.20.xip.io";
    set req.url = regsub(req.url, "^/master/\w+/", "/master/");
	}
*/
}

sub vcl_backend_response {
  // ensure backend response is not cached (for TESTING ONLY ATM!)
  set beresp.ttl = 0s;
	// scan for ESI tags in backend response
	if (bereq.url !~ "^/master/") {
		set beresp.do_esi=true;
	}
}
