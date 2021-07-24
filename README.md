# bank

How to run the application

-> After cloning the project import them into sts and build(maven build) the common-api.
-> Get the location of .jar file of common-api and update the location in other services pom.xml.
-> build the remaining services
-> start the services in the below order
	*registry-service
	*user-registration-service
	*config-service
	*loan-service
	*customer-update-service
-> open the swagger url  http://localhost:7071/swagger-ui.html#!/ and regiter a user with the below JSON input.
{
  "accountType": "Personal Loan",
  "address": "D-No, street, ward, city, district",
  "contactNumber": "9666123456",
  "country": "country",
  "dateOfBirth": "20-1-2021",
  "emailAddress": "abcd@gmail.com",
  "panCard": "ABCDE1234F",
  "password": "selvi00",
  "state": "state",
  "userName": "selvi"
}
-> username will be sent back on successful registration-service.
-> on successful registration login with the below JSON input.
{
	"userName": "selvi",
	"password": "selvi00"
}
-> On successful logging in a JWT token will be generated, copy that token for further use
-> Now to apply loan open post man and the URL http://localhost:7071/loan-service/loan/apply with POST http method and provide the Authorization header with value as Bearer <generated JWT token>
and apply loan with below JSON input.
{
    "username": "selvi",
	"loanType":"home Loan",
    "appliedDate": "2021-06-17",
    "loanAmount": 100000,
    "interestRate": 0.5,
    "duration": 365
}
-> Now to get the loan details hit the URL http://localhost:7071/loan-service/loan/details/selvi with GET http method and Authorization header, you will get the details
-> Now to update the customer details hit URL http://localhost:7071/customer-update-service/update/user with POST method , Authorization header and below JSON input
{
  "accountType": "Personal Account",
  "address": "D-No1, street1, ward1, city1, district1",
  "contactNumber": "9666123456",
  "country": "country1",
  "dateOfBirth": "20-1-2021",
  "emailAddress": "abcd@gmail.com",
  "panCard": "ABCDE1234F",
  "password": "selvi00",
  "state": "state1",
  "userName": "selvi"
}
