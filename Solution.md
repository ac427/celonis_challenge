I want to make it clear that my springboot expereince is zero, but it it should be no different than running any modern application

Here are the few things I noticed
1. The sample application is using gradle-5.6.4-bin which is very old and doesn't run on any latest docker images. It looks like it is also dependent on outdated java11 
2. Downloading binary during run kind of beats the purpose of using containers. I would put any kind of downloads/installs part of the Docker build,
since I have no experience writing the wrapper/build files I followed the instructions giving to run the application.

Improvements:
1. Use latest versions everywhere. 
2. Don't do any kind of build steps during application start. When a container runs, it should just able to run the binarary. 
3. Run distroless image

# Task 1

### To build container image ( I'm using podman in this example, should be similar with docker) 
There is .dockeringore file to ignore secret during build.

 `podman build --platform linux/amd64,linux/arm64  --manifest  anantac/celonis_challenge:1 .`

### To run the image locally

`podman run -v ./module1/src/main/resources/application.properties:/app/src/main/resources/application.properties -p 8080:8080 anantac/celonis_challenge:1`

# Task 2
I am using kind locally to test the deployment. please check deploy.yaml file

### Zero-downtime deployments should be possible
I am running 3 replicas. We can distribute it across regions/availability zones in a real world cases to have minimal impact when something goes wrong in a az or region.

```
~ $ curl -X POST http://ac.ac.svc.cluster.local:8080/files -H Celonis-Auth: dev -F file=@/tmp/foo.txt
curl: (6) Could not resolve host: dev
~ $ curl -X POST http://ac.ac.svc.cluster.local:8080/files -H 'Celonis-Auth: dev' -F file=@/tmp/foo.txt
~ $ echo foo1 > /tmp/foo1.txt
~ $ curl -X POST http://ac.ac.svc.cluster.local:8080/files -H 'Celonis-Auth: dev' -F file=@/tmp/foo1.txt -v
Note: Unnecessary use of -X or --request, POST is already inferred.
* Host ac.ac.svc.cluster.local:8080 was resolved.
* IPv6: (none)
* IPv4: 10.96.211.112
*   Trying 10.96.211.112:8080...
* Connected to ac.ac.svc.cluster.local (10.96.211.112) port 8080
* using HTTP/1.x
> POST /files HTTP/1.1
> Host: ac.ac.svc.cluster.local:8080
> User-Agent: curl/8.11.0
> Accept: */*
> Celonis-Auth: dev
> Content-Length: 203
> Content-Type: multipart/form-data; boundary=------------------------tVbEgZo92LvIDXELLClOMM
>
* upload completely sent off: 203 bytes
< HTTP/1.1 201
< Content-Length: 0
< Date: Tue, 03 Dec 2024 22:14:59 GMT
<
* Connection #0 to host ac.ac.svc.cluster.local left intact
```


### Data is persisted across application restarts
I am skipping this step, as I don't have a fancy laptop to spin a local cluster with perstant volumes to mount. 
In a real world, it will be very similar to the secret mount in deploy.yaml. Here is an example for AWS to have persistant storage
across all the pods
https://aws.amazon.com/blogs/storage/persistent-storage-for-kubernetes/
It look likes the uploaded files are getting stored in /root. I didn't spent time looking at the code on how to change that, but in a real world case you probably store it somewhere other than /root and have it as a mount point using persistant volume


### The application should be exposed via Ingress
* There are many ways you can do this. You can just have service type has `loadbalancer` which will create classic load balancer in aws
* You can deploy aws lb controller and have that create loadbalancers of type alb or nlb https://kubernetes-sigs.github.io/aws-load-balancer-controller/latest/
* You can deploy service mesh like istio to expose service via shared or dedicated gateways




# Improvements 
* First thing is use https and certs using some well known CA
* get a proper dns record from well known providers like ns1, aws and use external-dns to manage records
* I don't know much about the application, so I can't recommend munch interms of code optimization, but I have some recommendations 
at the start of this page on building image
* I used secret resource, but it will be vaultsecret with path defined instead of kubernetes secret in deploy.yaml
* Use some CDN depending on the budget or value of the service
* rate limiting


# Module 2: Pipeline design

Helm is a popular tool in managing deployments. You can have helm values and templates defined with conditons depending on whatever the condition that differs from realm to realm. Configuration management is kind of easy and the purpose is to write once and run many times. 
The only situation I can think of is someone writng a bad code in managing resources. If the remote state is stateless it should be easy to 
maintante or migrate. 
