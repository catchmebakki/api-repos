# Example cURL commands

## Running the service

Set the spring profile using vm argument 
````bash
-Dspring.profiles.active=sit
````

## Login

This will help us to get the JDT token. This token is valid for 20 minuted. 

````bash
curl --location --request GET 'http://localhost:9001/auth/login'
````
Add the token from this response in all the below request. Token should go in t

## Add Profile
````bash
curl --location --request POST 'http://localhost:9001/profile/' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZWZhdWx0Iiwic2NvcGVzIjoicHJvZmlsZSIsImlhdCI6MTY2MzYzNjEwNiwiZXhwIjoxNjYzNjM3MzA2fQ.yBEVAlyPDceDdl8K99pE2glfsGVIr-Uwb0vMsgqUxZo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 7,
    "name": "Steven",
    "email": "dfasdfsadf@email.com"
}'
````

### Add request with bean level validation error

cURL Command:
````
curl --location --request POST 'http://localhost:9001/profile/' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZWZhdWx0Iiwic2NvcGVzIjoicHJvZmlsZSIsImlhdCI6MTY2MzYzNjEwNiwiZXhwIjoxNjYzNjM3MzA2fQ.yBEVAlyPDceDdl8K99pE2glfsGVIr-Uwb0vMsgqUxZo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 7,git 
    "name": "St",
    "email": "dfasdfsadf"
}'
````
Example Response:
````
{
    "timestamp": "2022-09-19T21:09:43.772656700",
    "status": 400 BAD_REQUEST,
    "reason": "Validation Error",
    {
        "profile.email": "must be a well-formed email address",
        "profile.name": "The name 'St' must be between 3 and 14 characters long"
    }
}
````
## Update profile
````bash
curl --location --request PUT 'http://localhost:9001/profile/' \
--header 'Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZWZhdWx0Iiwic2NvcGVzIjoicHJvZmlsZSIsImlhdCI6MTY2MzYzNjEwNiwiZXhwIjoxNjYzNjM3MzA2fQ.yBEVAlyPDceDdl8K99pE2glfsGVIr-Uwb0vMsgqUxZo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 2,
    "name": "stive_update_2"
}'
````

## Get profile
````bash
curl --location --request GET 'http://localhost:9001/profile/1' \
--header 'Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZWZhdWx0Iiwic2NvcGVzIjoicHJvZmlsZSIsImlhdCI6MTY2MzYzNjEwNiwiZXhwIjoxNjYzNjM3MzA2fQ.yBEVAlyPDceDdl8K99pE2glfsGVIr-Uwb0vMsgqUxZo' \
--header 'Content-Type: application/json'
````
### Path variable validation Example
cURL command
````bash
curl --location --request DELETE 'http://localhost:9001/profile/-1' \
--header 'Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZWZhdWx0Iiwic2NvcGVzIjoicHJvZmlsZSIsImlhdCI6MTY2MzYzNjEwNiwiZXhwIjoxNjYzNjM3MzA2fQ.yBEVAlyPDceDdl8K99pE2glfsGVIr-Uwb0vMsgqUxZo' \
--header 'Content-Type: application/json'
````
Example Response:
````
{
    "timestamp": "2022-09-19T21:15:03.153341400",
    "status": 400 BAD_REQUEST,
    "reason": "Validation Error",
    {
        "updateProfiles.id": "Id cannot be in Negative"
    }
}
````
## Delete profile
````bash
curl --location --request DELETE 'http://localhost:9001/profile/2' \
--header 'Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZWZhdWx0Iiwic2NvcGVzIjoicHJvZmlsZSIsImlhdCI6MTY2MzYzNjEwNiwiZXhwIjoxNjYzNjM3MzA2fQ.yBEVAlyPDceDdl8K99pE2glfsGVIr-Uwb0vMsgqUxZo' \
--header 'Content-Type: application/json'
````
