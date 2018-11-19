import React, { Component } from 'react';
import Main from './container/Main';
import './App.css';
import { FilePond,registerPlugin } from 'react-filepond';
import 'filepond/dist/filepond.min.css';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css';
registerPlugin(FilePondPluginImagePreview);

class App extends Component {
  render() {
    return (
    		<div className="app">
    		 <header className="app-header">
    		   <h1 className="app-title">Upload Employee Record(s)</h1>
    		  </header>
    		  <FilePond  allowMultiple={true} server="http://localhost:8080/uploadMultipleEmployee"/>
    			<form method = "get" action="http://localhost:8080/downloadEmployeeRecord/AllEmployeeRecords.csv">
    			<input type="submit" value="Get All Employee"/>
    		  </form>
    		</div>
    		  
    );
  }
}

export default App;
