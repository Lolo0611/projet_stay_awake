import '../style/App.css';
import Navbar from '../components/Navbar';
import Task from './Task';

function App() {
  
  return (
    <div className="App">

      <div className="App-header">
           <h1> Stay Awake </h1> 
      </div>

      <div className="content">
      
      <div className="NavBar">
      <Navbar/>
      </div>

      <div className="content-right">

      <p> content </p>

         

      
      <Task></Task>
      
      



      </div>

      </div>


    </div>
  );
}

export default App;
