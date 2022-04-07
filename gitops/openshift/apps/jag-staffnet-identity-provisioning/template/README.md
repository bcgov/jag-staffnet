## Templates to create openshift components related to jag-staffnet-identity-provisioning api deployment

### Command to execute template
1) Login to OC using login command
2) Run below command in each env. namespace dev/test/prod/tools
   ``oc process -f jag-staffnet-identity-provisioning.yaml --param-file=jag-staffnet-identity-provisioning.env | oc apply -f -``
