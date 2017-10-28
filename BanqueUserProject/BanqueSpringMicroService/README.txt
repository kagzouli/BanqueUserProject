In the banqueSpring project, we must call a REST Service user which is in https (security).

In this case, to avoid Java + HTTPS: Unable to Find Valid Certification Path to Requested Target
during RestTemplate call

do

Create the certificate for user service JKS
keytool -keystore userservice.jks -genkey -alias userservice 

Generate the public key of the JKS for the banque project
openssl s_client -connect localhost:12090 < /dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > userservice.crt


Export the public key on the banque project java (java of banque project)
$JAVA_HOME/keytool -import -alias userservice -keystore $JAVA_HOME/lib/security/cacerts -file userservice.crt

keytool -import -file userservice.cert -keypass password -keystore keystore.jks -storepass password
