import './App.css';
import TaskPanel from './components/TaskPanel';
import Navbar from './components/Navbar';

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

       

    
    <TaskPanel/>

    
    



    </div>

    </div>


  </div>
  );
}

export default App;
