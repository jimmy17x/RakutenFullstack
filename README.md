
Started on : November 18 , 9:00PM Indian Standard Time
Completed V1 : Novemeber 19 , 8:10PM Indian Standard Time


Developer comments on current version :

Pagination payload contains all records .Can be brough in chunks by also using server side pagination
The UPDATE logic for an employee is exposed via only a REST API , example request with employee id 3 :

curl -X PUT \
  http://localhost:8080/employee/3 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name":"somename",
	"dept":"tech",
	"designation":"CEO",
	"salary":"12",
	"joingingDate":"2018-08-14"
}'

Getting all records CSV file :
curl -X GET \
  http://localhost:8080/downloadEmployeeRecord/AllEmployeeRecords.csv \
  -H 'cache-control: no-cache' \
  
 Uploading multiple (or single)  file :
 


The "Download All Employees Report " button in UI contains both succesfull records submitted and records having errors during submission with error message

The employee records are being stored in memory as of now in List<>

The joining date in UI table is Time stamp with because of Date object conversion but the date part and reports is of exact requuired format. 

Scope of Improvements : 
Generic Design for CSV writer and services
Code refactoring in both UI and Backend
CSS Styling

Screenshot :
![UI](https://raw.githubusercontent.com/jimmy17x/RakutenFullstack/master/UI%20screensot.png)


