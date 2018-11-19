import React, { Component } from 'react';
import Main from './container/Main';
import './App.css';
import { FilePond,registerPlugin } from 'react-filepond';
import 'filepond/dist/filepond.min.css';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css';
import Reactable from 'reactable';  
import axios from 'axios';

registerPlugin(FilePondPluginImagePreview);


var Table = Reactable.Table,  
Thead = Reactable.Thead,
Th = Reactable.Th;

class App extends Component { 

	constructor(props){
	   super(props);
	      this.state = {
	    		  allEmpl: []
	      }
	}
	      
	 renderTable() {
		   return (
				   <Table className="table"
					   noDataText="No matching records found"
				        itemsPerPage={5}
				        currentPage={0}
				        sortable={true}
				        data={this.state.allEmpl}>
				        <Thead>
				          <Th column="emplId">ID</Th>
				          <Th column="name">NAME</Th>
				          <Th column="dept">DEPARTMENT</Th>
				          <Th column="designation">DESIGNATION</Th>
				          <Th column="salary">SALARY</Th>
				          <Th column="joingingDate">JOINING DATE</Th>
				        </Thead>
				      </Table>
		   )
		 }
	
  render() {
    return (
    		<div className="app">
    		 <header className="app-header">
    		   <h1 className="app-title">Upload Employee Record(s)</h1>
    		  </header>
    		  <FilePond  allowMultiple={true} server="http://localhost:8080/uploadMultipleEmployee"/>
    			<form method = "get" action="http://localhost:8080/downloadEmployeeRecord/AllEmployeeRecords.csv">
    			<input className = "get-report" type="submit" value="Download All Employees report"/>
    		  </form>
    		   {this.renderTable()}
    		</div>
    		
    		  
    );
  }
  
  componentDidMount() {
	 
		  axios.get(` http://localhost:8080/allemployees`)
		    .then((response) => this.setState({allEmpl: response.data}));
	}
}

export default App;
