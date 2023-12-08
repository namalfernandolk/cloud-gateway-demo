# Read Me First
The following was discovered as part of building this project:

* Generate the token : 

curl --location --request GET 'http://localhost:7777/auth/token' \
  --header 'Content-Type: application/json' \
  --data '{
  "userName":"your_user_name",
  "password":"your_password"
  }'
<br><br><br>
* Call the service using the obtained token 

curl --location 'http://localhost:7777/token-management/tokens' \
  --header 'accept: */*' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer < Obtained bearer token >' \
  --data '{
  "data": "4111111111113333",
  "tokenScheme": "NUM_BIN"
  }'
