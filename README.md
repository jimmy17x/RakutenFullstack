
Started on : November 18 , 9:00PM Indian Standard Time
Completed V1 : Novemeber 19 , 8:10PM Indian Standard Time


Developer comments on current version :

Pagination payload contains all records .Can be brough in chunks by also using server side pagination
The UPDATE logic for an employee is exposed via only a REST API , example request with employee id 3 :

curl -X PUT \
  http://localhost:8080/employee/3 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: b07fa148-e34e-2966-c705-27dd3e69561d' \
  -d '{
	"name":"somename",
	"dept":"tech",
	"designation":"CEO",
	"salary":"12",
	"joingingDate":"2018-08-14"
}	'


The "Download All EMployees Report " button in UI contains both succesfull records submitted and records having errors during submission with error message

The employee records are being stored in memory as of now in List<>



Scope of Improvements : 
Generic Design
Code refactoring
CSS Styling

Screenshot :

