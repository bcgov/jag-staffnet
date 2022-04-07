## Templates to create openshift components related to jag-staffnet-tests api deployment

### Command to execute template
1) Login to OC using login command.
2) Run below command in each env. namespace dev/test/prod
   ``oc process -f jag-staffnet-tests.yaml --param-file=jag-staffnet-tests.env | oc apply -f -``
