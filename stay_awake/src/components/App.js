import '../style/App.css';
import Navbar from '../components/Navbar';
import Taskbar from './Taskbar';

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

         

      
      <Taskbar></Taskbar>
      
      



      </div>

      </div>


    </div>
  );
}

export default App;
