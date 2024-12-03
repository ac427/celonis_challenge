# Celonis Platform Engineering Challenge

Dear Ananta,
congratulations! You made it to the Celonis Platform Engineering Challenge.

The challenge can be completed in less than 3 hours, but feel free to work at your own pace.

Why do we ask you to complete this challenge? First of all, we need to have some way of comparing different applicants, and we try to answer certain questions which we can not out-right ask in an interview - also we don't want to ask too many technical questions in a face-to-face interview to not be personally biased in a potentially stressful situation. To be as transparent as possible, we want to give you some insights into what we look at and how we evaluate. This challenge gives you the possibility to shine :)

The challenge is divided into two modules: the _Deploy_, and _Design_ module. Both modules are independent of each other, so you can always move on if you get stuck.

## Module 1: Deploy
In `module1` you can find a simple Spring Boot application for uploading and downloading files.

You can start the application using the Gradle wrapper:

```
./gradlew bootRun
```

To upload files you need pass a secret API key via the `Celonis-Auth` header, e.g. using `curl`:

```bash
curl -X POST \
  http://localhost:8080/files \
  -H 'Celonis-Auth: dev' \
  -F file=@/path/to/some/image.jpg
```

The value of this API key for local development is configured in `src/main/resources/application.yaml`.

You can download files from the application using e.g.:
```
curl -LO http://localhost:8080/files/image.jpg
```

The goal of this module is to deploy this application to Kubernetes by completing the following three tasks:

### Task 1: Dockerize
Build the application into a Docker image so it can be run in Kubernetes. Make sure to not bake any secrets into the image ;-)

### Task 2: Manifests
Write the manifests to deploy the application to a Kubernetes cluster. Try to fulfill all of the following three requirements:

* Zero-downtime deployments should be possible
* Data is persisted across application restarts
* The application should be exposed via Ingress

### Task 3: Improvements
What would you add or change to make this a fully production ready application?

# Module 2: Pipeline design
At Celonis we operate multiple instances (which we call _realms_) of our SaaS product _Execution Management System (EMS)_ all around the world across multiple cloud providers. Their configuration (e.g. database hosts, connectivity to external services) is of course slightly different for each realm. What challenges can you imagine us facing in such a setup? Try to sketch a solution for maintaining and deploying into these environments as efficiently as possible.
